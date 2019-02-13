package com.customhcf.hcf.command;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.user.FactionUser;
import com.customhcf.hcfold.crate.Key;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import com.google.common.base.Preconditions;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReclaimCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player p = (Player) sender;
        if (commandLabel.equalsIgnoreCase("reclaim")) {
            String groupName = getVaultPermission().getPlayerGroups(p)[0];
            if (HCF.getPlugin().getConfig().getString("Reclaim.groups." + groupName) == null) {
                p.sendMessage(ChatColor.RED + "You dont have anything to reclaim!");
            } else {
                List<String> used = new ArrayList();
                if (HCF.getPlugin().getConfig().getStringList("Reclaim.redeemedPlayers." + groupName) != null) {
                    used.addAll(HCF.getPlugin().getConfig().getStringList("Reclaim.redeemedPlayers." + groupName));
                }
                if (used.contains(p.getName().toLowerCase())) {
                    p.sendMessage(ChatColor.RED + "You have already reclaimed your rewards.");
                } else {
                    used.add(p.getName().toLowerCase());
                    HCF.getPlugin().getConfig().set("Reclaim.redeemedPlayers." + groupName, used);
                    HCF.getPlugin().saveConfig();
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', HCF.getPlugin().getConfig().getString("Message").replace("{PLAYER}", p.getName()).replace("{GROUP}", groupName)));
                    for (String reclaimCommand : HCF.getPlugin().getConfig().getStringList("Reclaim.groups." + groupName)) {
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), reclaimCommand.replace("{PLAYER}", p.getName()));
                    }
                }
            }
        }
        return false;
    }

    private static Permission permission;

    public static void checkVault() {
        if(Bukkit.getPluginManager().getPlugin("Vault").isEnabled()) {
            setupVault();
        } else {
            Bukkit.getConsoleSender().sendMessage("Vault is not enabled or installed. Aborting...");
            Bukkit.getPluginManager().disablePlugin(HCF.getPlugin());
        }
    }

    public static void setupVault() {
        RegisteredServiceProvider<Permission> rsp = Bukkit.getServicesManager().getRegistration(Permission.class);
        if(rsp != null) {
            permission = rsp.getProvider();
        } else {
            Bukkit.getConsoleSender().sendMessage("An error occured whilst registering Permissions with Vault...");
            checkVault();
            return;
        }
        Bukkit.getConsoleSender().sendMessage("Successfully hooked into Vault.");
    }

    public static void unregisterVault() {
        if(permission != null) {
            permission = null;
        }
    }

    public static Permission getVaultPermission() {
        if(permission != null) {
            return permission;
        } else {
            checkVault();
        }
        return permission;
    }

}