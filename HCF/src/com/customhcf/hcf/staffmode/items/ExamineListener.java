package com.customhcf.hcf.staffmode.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by nyang on 3/25/2017.
 */
public class ExamineListener implements Listener {

    @EventHandler
    public void onExamine(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Player target = (Player) event.getRightClicked();
        ItemStack item = player.getItemInHand();
        if (item != null) {
            if (item.getType() == Material.BOOK) {
                if (item.getItemMeta().getDisplayName().contains("Examine Book")) {
                    player.openInventory(target.getInventory());
                }
            }
        }
    }
}
