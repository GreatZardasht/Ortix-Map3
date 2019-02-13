package com.customhcf.hcf.command;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.Utils.ChatUtils;
import com.customhcf.hcf.Utils.Cooldowns;
import com.customhcf.hcf.Utils.PUtils;
import com.customhcf.hcf.Utils.ReviveSetup;
import com.customhcf.hcf.deathban.Deathban;
import com.customhcf.hcf.user.FactionUser;
import com.customhcf.util.BukkitUtils;
import com.customhcf.util.command.CommandArgument;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ReviveCommand implements CommandExecutor {
    private final HCF plugin;

    public ReviveCommand(HCF plugin) {
        this.plugin = plugin;
    }


    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <player>");
            return true;
        }

            if (Cooldowns.isOnCooldown("revive_cooldown", p)) {
                p.sendMessage("§cYou cannot do this for another §l" + Cooldowns.getCooldownForPlayerInt("revive_cooldown", p) / 60 + " §cminutes.");
                return true;
            }
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
            if ((!target.hasPlayedBefore())) {
                sender.sendMessage(ChatColor.GOLD + "Player '" + ChatColor.WHITE + args[1] + ChatColor.GOLD + "' not found.");
                return true;
            }
            UUID targetUUID = target.getUniqueId();
            FactionUser factionTarget = HCF.getPlugin().getUserManager().getUser(targetUUID);
            Deathban deathban = factionTarget.getDeathban();
            if ((deathban == null) || (!deathban.isActive())) {
                sender.sendMessage(ChatColor.RED + target.getName() + " is not deathban! Try Again Later");
                return true;
            }

            factionTarget.removeDeathban();
            Bukkit.broadcastMessage(ChatColor.GOLD.toString() + ChatColor.STRIKETHROUGH + "--------------------------------");
            Bukkit.broadcastMessage(ChatColor.YELLOW.toString() + "Medic: " + ChatColor.WHITE + sender.getName());
            Bukkit.broadcastMessage(ChatColor.YELLOW.toString() + "Reived: " + ChatColor.WHITE + target.getName());
            Bukkit.broadcastMessage(ChatColor.GOLD.toString() + ChatColor.STRIKETHROUGH + "--------------------------------");
            Cooldowns.addCooldown("revive_cooldown", p, 3600);
        return false;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length != 1) {
            return Collections.emptyList();
        }
        List<String> results = new ArrayList();
        for (FactionUser factionUser : this.plugin.getUserManager().getUsers().values())
        {
            Deathban deathban = factionUser.getDeathban();
            if ((deathban != null) && (deathban.isActive()))
            {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(factionUser.getUserUUID());
                String name = offlinePlayer.getName();
                if (name != null) {
                    results.add(name);
                }
            }
        }
        return BukkitUtils.getCompletions(args, results);
    }
}


