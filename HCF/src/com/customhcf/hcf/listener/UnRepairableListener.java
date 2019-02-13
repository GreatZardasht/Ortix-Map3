/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.PrepareAnvilRepairEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package com.customhcf.hcf.listener;

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilRepairEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class UnRepairableListener
implements Listener {
    @EventHandler
    public void onRepair(PrepareAnvilRepairEvent e) {
        for (ItemStack itemStack : e.getInventory().getContents()) {
            for (String lore : itemStack.getItemMeta().getLore()) {
                String fixedLore = ChatColor.stripColor((String)lore.toLowerCase());
                if (!fixedLore.contains("no repair") && !fixedLore.contains("unrepairable") && !fixedLore.contains("norepair") && !fixedLore.contains("nofix") && !fixedLore.contains("no fix")) continue;
                e.setCancelled(true);
                e.setResult(new ItemStack(Material.AIR));
                e.getRepairer().closeInventory();
                ((Player)e.getRepairer()).sendMessage((Object)ChatColor.RED + "This item cannot be repaired.");
            }
        }
    }
}

