/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockState
 *  org.bukkit.block.Sign
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.util.Vector
 */
package com.customhcf.hcf.fixes;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.util.Vector;

public class BlockJumpGlitchFixListener
implements Listener {
    @EventHandler(ignoreCancelled=false, priority=EventPriority.MONITOR)
    public void onBlockBreak(BlockPlaceEvent event) {
        if (event.isCancelled()) {
            int playerY;
            int blockY;
            Player player = event.getPlayer();
            if (player.getGameMode() == GameMode.CREATIVE || player.getAllowFlight()) {
                return;
            }
            Block block = event.getBlockPlaced();
            if (block.getType().isSolid() && !(block.getState() instanceof Sign) && (playerY = player.getLocation().getBlockY()) > (blockY = block.getLocation().getBlockY())) {
                Vector vector = player.getVelocity();
                vector.setX(-0.3);
                vector.setZ(-0.3);
                player.setVelocity(vector.setY(vector.getY() - 0.41999998688697815));
            }
        }
    }
}

