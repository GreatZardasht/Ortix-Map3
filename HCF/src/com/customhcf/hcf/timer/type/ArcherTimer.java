/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Optional
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Arrow
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.projectiles.ProjectileSource
 */
package com.customhcf.hcf.timer.type;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.classes.archer.ArcherClass;
import com.customhcf.hcf.scoreboard.PlayerBoard;
import com.customhcf.hcf.scoreboard.ScoreboardHandler;
import com.customhcf.hcf.timer.PlayerTimer;
import com.customhcf.hcf.timer.Timer;
import com.customhcf.hcf.timer.event.TimerExpireEvent;
import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

public class ArcherTimer
        extends PlayerTimer
        implements Listener {
    private final HCF plugin;
    private Double ARCHER_DAMAGE;

    @Override
    public String getScoreboardPrefix() {
        return ChatColor.GOLD.toString() + (Object)ChatColor.BOLD;
    }

    public ArcherTimer(HCF plugin) {
        super("Archer Tag", TimeUnit.SECONDS.toMillis(10L));
        this.plugin = plugin;
        this.ARCHER_DAMAGE = 0.25;
    }

    @EventHandler
    public void onExpire(TimerExpireEvent e) {
        if (e.getUserUUID().isPresent() && e.getTimer().equals(this)) {
            UUID userUUID = (UUID)e.getUserUUID().get();
            Player player = Bukkit.getPlayer((UUID)userUUID);
            if (player == null) {
                return;
            }
            ArcherClass.TAGGED.remove(player.getUniqueId());

            for (Player players : Bukkit.getServer().getOnlinePlayers()) {
                Collection<? extends Player> c = Arrays.asList(Bukkit.getOnlinePlayers());
                this.plugin.getScoreboardHandler().getPlayerBoard(players.getUniqueId()).addUpdates(c);
            }
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        Double damage;
        Player entity;
        Player damager;
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            entity = (Player)e.getEntity();
            damager = (Player)e.getDamager();
            if (this.getRemaining(entity) > 0) {
                damage = e.getDamage() * this.ARCHER_DAMAGE;
                e.setDamage(e.getDamage() + damage);
            }
        }
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Arrow) {
            entity = (Player)e.getEntity();
            damager = (Player)((Arrow)e.getDamager()).getShooter();
            if (damager instanceof Player && this.getRemaining(entity) > 0) {
                if (ArcherClass.TAGGED.get(entity.getUniqueId()).equals(damager.getUniqueId())) {
                    this.setCooldown(entity, entity.getUniqueId());
                }
                damage = e.getDamage() * this.ARCHER_DAMAGE;
                e.setDamage(e.getDamage() + damage);
            }
        }
    }
}


