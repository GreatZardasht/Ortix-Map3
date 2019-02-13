/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.inventory.InventoryOpenEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.Recipe
 *  org.bukkit.material.EnderChest
 */
package com.customhcf.hcf.fixes;

import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.material.EnderChest;

public class EnderChestRemovalListener
implements Listener {
    public EnderChestRemovalListener() {
        this.removeRecipe();
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public void onEnderChestOpen(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.ENDER_CHEST) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory() instanceof EnderChest) {
            event.setCancelled(true);
        }
    }

    private void removeRecipe() {
        Iterator iterator = Bukkit.recipeIterator();
        while (iterator.hasNext()) {
            if (((Recipe)iterator.next()).getResult().getType() != Material.ENDER_CHEST) continue;
            iterator.remove();
        }
    }
}
