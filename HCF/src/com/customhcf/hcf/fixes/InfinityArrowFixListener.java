/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_7_R4.EntityArrow
 *  org.bukkit.craftbukkit.v1_7_R4.entity.CraftArrow
 *  org.bukkit.entity.Arrow
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.ProjectileHitEvent
 *  org.bukkit.projectiles.ProjectileSource
 */
package com.customhcf.hcf.fixes;

import net.minecraft.server.v1_7_R4.EntityArrow;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.projectiles.ProjectileSource;

public class InfinityArrowFixListener
implements Listener {
    @EventHandler(priority=EventPriority.NORMAL)
    public void onProjectileHit(ProjectileHitEvent event) {
        Arrow arrow;
        Projectile entity = event.getEntity();
        if (entity instanceof Arrow && (!((arrow = (Arrow)entity).getShooter() instanceof Player) || ((CraftArrow)arrow).getHandle().fromPlayer == 2)) {
            arrow.remove();
        }
    }
}

