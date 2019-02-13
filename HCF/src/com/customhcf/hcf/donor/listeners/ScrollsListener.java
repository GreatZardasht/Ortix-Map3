package com.customhcf.hcf.donor.listeners;

import com.customhcf.hcf.Utils.Cooldowns;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by nyang on 4/6/2017.
 */
public class ScrollsListener implements Listener {

    @EventHandler
    public void onScrollListener(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (item != null) {
                if (item.getType() == Material.PAPER) {
                    if (item.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "» Spawn Scroll «")) {
                        if (Cooldowns.isOnCooldown("spawnscroll", player)) {
                            player.sendMessage(ChatColor.RED + "You must wait 3hrs before using this again!");
                        } else {
                            sendLocation(player);
                            player.getInventory().remove(Material.PAPER);
                            Cooldowns.addCooldown("spawnscroll", player, 10800);
                        }
                    }
                }
            }
        }
    }

    public boolean sendLocation(Player player) {
        if ((player.getLocation().getX() == 0) && (player.getLocation().getBlockZ() == 0)) {
            player.sendMessage(org.bukkit.ChatColor.RED+  "You are already at the spawnpoint!");
            return true;
        } else {
            World world = Bukkit.getWorld("world");
            player.teleport(new Location(world, 0, 75, 0));
        }
        return false;
    }
}
