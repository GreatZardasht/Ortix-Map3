/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.World$Environment
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 */
package com.customhcf.hcf.fixes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class VoidGlitchFixListener
implements Listener {
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onPlayerDamage(EntityDamageEvent event) {
        Entity entity;
        if (event.getCause() == EntityDamageEvent.DamageCause.VOID && (entity = event.getEntity()) instanceof Player) {
            if (entity.getWorld().getEnvironment() == World.Environment.THE_END) {
                return;
            }
            Location destination = Bukkit.getWorld((String)"world").getSpawnLocation();
            if (destination == null) {
                return;
            }
            if (entity.teleport(destination, PlayerTeleportEvent.TeleportCause.PLUGIN)) {
                event.setCancelled(true);
                ((Player)entity).sendMessage((Object)ChatColor.YELLOW + "You were saved from the void.");
            }
        }
    }
}

