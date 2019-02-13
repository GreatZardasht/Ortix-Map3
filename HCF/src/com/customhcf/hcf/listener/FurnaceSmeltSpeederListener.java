/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockState
 *  org.bukkit.block.Furnace
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 */
package com.customhcf.hcf.listener;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class FurnaceSmeltSpeederListener
implements Listener {
    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block block;
        BlockState state;
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && (state = (block = event.getClickedBlock()).getState()) instanceof Furnace) {
            ((Furnace)state).setCookSpeedMultiplier(5.0);
        }
    }
}

