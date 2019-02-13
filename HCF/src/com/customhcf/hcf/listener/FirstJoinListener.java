package com.customhcf.hcf.listener;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.command.ReclaimCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Created by Nathan'PC on 12/20/2016.
 */
public class FirstJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPlayedBefore()) {

            ItemStack rod = new ItemStack(Material.FISHING_ROD);
            ItemMeta rodMeta = rod.getItemMeta();
            rod.addEnchantment(Enchantment.DURABILITY, 1);
            rod.setItemMeta(rodMeta);
            player.getInventory().addItem(rod, new ItemStack(Material.COOKED_BEEF, 32));

        }

    }

    @EventHandler
    public void onStaffJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("staff.join")) {
            for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
                if (staff.hasPermission("staff.join")) {
                    staff.sendMessage(ChatColor.AQUA.toString() + "[STAFF] Staff has connected : " + ChatColor.WHITE +  player.getName());
                }
            }
        }
    }

    @EventHandler
    public void onStaffQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("staff.join")) {
            for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
                if (staff.hasPermission("staff.join")) {
                    staff.sendMessage(ChatColor.AQUA.toString()  + "[STAFF] Staff has disconnected : " + ChatColor.WHITE +  player.getName());
                }
            }
        }
    }
}
