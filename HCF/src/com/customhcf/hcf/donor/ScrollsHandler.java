package com.customhcf.hcf.donor;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ScrollsHandler {

    public void giveSpawnScroll(Player player, String msg) {
        ItemStack scroll = new ItemStack(Material.PAPER);
        ItemMeta scrollMeta = scroll.getItemMeta();
        scrollMeta.setDisplayName(ChatColor.GREEN + "» Spawn Scroll «");
        scrollMeta.setLore(Arrays.asList(ChatColor.WHITE + "Right-Click to be sent to Spawn!"));
        scroll.setItemMeta(scrollMeta);
        player.getInventory().addItem(scroll);

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

    public void giveBonusXPScroll(Player player, String msg) {
        ItemStack scroll = new ItemStack(Material.PAPER);
        ItemMeta scrollMeta = scroll.getItemMeta();
        scrollMeta.setDisplayName(ChatColor.GOLD + "» BonusXP Scroll «");
        scrollMeta.setLore(Arrays.asList(ChatColor.WHITE + "Right-Click to receive BonusXP (Only usable on SOTW)!"));
        scroll.setItemMeta(scrollMeta);
        player.getInventory().addItem(scroll);

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

}
