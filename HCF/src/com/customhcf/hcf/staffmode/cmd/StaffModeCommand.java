package com.customhcf.hcf.staffmode.cmd;

import com.customhcf.base.BasePlugin;
import com.customhcf.hcf.HCF;
import com.customhcf.hcf.command.VanishCommand;
import com.customhcf.hcf.vanish.VanishHandler;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by nyang on 3/25/2017.
 */
public class StaffModeCommand implements CommandExecutor {

    VanishHandler handler = new VanishHandler();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("staffmode") && (sender.hasPermission("command.staffmode"))) {
            if (HCF.toggle.contains(player)) {
                HCF.toggle.remove(player);
                disableStaff(player);
                return true;
            } else {
                HCF.toggle.add(player);
                player.getInventory().clear();
                enableStaff(player);
                return true;
            }
        }
        return false;
    }

    public void enableStaff(Player player) {


        Inventory inv = player.getInventory();
        inv.clear();
        player.sendMessage(ChatColor.GOLD + "Mod Mode: " + ChatColor.DARK_GREEN + "ENABLED");
        ItemStack compass = new ItemStack(Material.WATCH);
        ItemStack book = new ItemStack(Material.BOOK);
        ItemStack worledit = new ItemStack(Material.WOOD_AXE);
        ItemStack carpet = new ItemStack(Material.ICE, 1);
        ItemStack staff = new ItemStack(Material.NETHER_STAR, 1);
        ItemStack vanish = new ItemStack(Material.INK_SACK, 1, (short) 10);

        ItemMeta compassMeta = compass.getItemMeta();
        ItemMeta bookMeta = book.getItemMeta();
        ItemMeta worldMeta = worledit.getItemMeta();
        ItemMeta carpetMeta = carpet.getItemMeta();
        ItemMeta staffMeta = staff.getItemMeta();

        compassMeta.setDisplayName(ChatColor.BLUE + "Random Teleporter");
        bookMeta.setDisplayName(ChatColor.BLUE + "Examine Book");
        worldMeta.setDisplayName(ChatColor.BLUE + "World edit");
        carpetMeta.setDisplayName(ChatColor.BLUE + "Freeze");
        staffMeta.setDisplayName(ChatColor.BLUE + "Staff Online:");

        compass.setItemMeta(compassMeta);
        book.setItemMeta(bookMeta);
        worledit.setItemMeta(worldMeta);
        carpet.setItemMeta(carpetMeta);
        staff.setItemMeta(staffMeta);


        ItemStack vanishon = new ItemStack(Material.COMPASS);
        ItemMeta v1Meta = vanishon.getItemMeta();
        v1Meta.setDisplayName(ChatColor.BLUE + "Compass");
        vanishon.setItemMeta(v1Meta);

        inv.setItem(0, compass);
        inv.setItem(1, book);
        inv.setItem(4, carpet);
        inv.setItem(7, staff);
        inv.setItem(8, vanishon);

        player.setGameMode(GameMode.CREATIVE);
        BasePlugin.getPlugin().getUserManager().getUser(player.getUniqueId()).setStaffUtil(true);

        if (VanishCommand.v.contains(player) == false) {
            VanishCommand.v.add(player);
            handler.setVanish(player);
        }
    }

    public void disableStaff(Player player) {
        player.sendMessage(ChatColor.GOLD + "Mod Mode: " + ChatColor.RED.toString() + ChatColor.BOLD + "DISABLED");
        player.getInventory().clear();
        player.setGameMode(GameMode.SURVIVAL);
        BasePlugin.getPlugin().getUserManager().getUser(player.getUniqueId()).setStaffUtil(false);
        player.getInventory().setLeggings(null);

        if (VanishCommand.v.contains(player) == true) {
            VanishCommand.v.remove(player);
            handler.unVanish(player);
        }
    }

}
