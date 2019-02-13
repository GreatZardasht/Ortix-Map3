package com.customhcf.hcf.staffmode.items;

import com.customhcf.hcf.HCF;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Created by nyang on 3/25/2017.
 */
public class StaffOnlineListener implements Listener {

    @EventHandler
    public void onStaffOnline(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK && (HCF.toggle.contains(player))) {
            if (item != null) {
                if (item.getType() == Material.NETHER_STAR) {
                    if (item.getItemMeta().getDisplayName().contains("Staff Online")) {
                        event.setCancelled(true);
                        showStaffs(player);
                    }
                }
            } else {
                return;
            }
        }
    }


    public void showStaffs(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, "Online Staffs");
        int slot = 0;
        int lowPlayers = 0;
        for (Player all : Bukkit.getServer().getOnlinePlayers()) {
            if (all.hasPermission("command.staffmode")) {
                ItemStack xrayplayer = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
                ItemMeta xraymeta = xrayplayer.getItemMeta();
                xraymeta.setDisplayName(net.md_5.bungee.api.ChatColor.WHITE + all.getName());
                xraymeta.setLore(Arrays.asList(net.md_5.bungee.api.ChatColor.GOLD + "Name: " + net.md_5.bungee.api.ChatColor.RED + all.getName()));
                xrayplayer.setItemMeta(xraymeta);
                inv.setItem(slot, xrayplayer);
                lowPlayers = lowPlayers + 1;
                slot++;
            }
            player.openInventory(inv);
        }

    }
}
