package com.customhcf.hcf.staffmode.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by nyang on 3/25/2017.
 */
public class FreezeListener implements Listener {

    @EventHandler
    public void onPlayerFreezeInteract(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();
        Player target = (Player) e.getRightClicked();
        ItemStack item = player.getItemInHand();
        // SHOULD GET RID OF ERROR
        if (item != null) {
            if (item.getType() == Material.ICE) {
                if (item.getItemMeta().getDisplayName().contains("Freeze")) {
                    if (target instanceof Player) {
                        player.performCommand("freeze " + target.getName());
                        e.setCancelled(true);
                    }
                }
            }
        }  else {
            return;
        }
    }
}
