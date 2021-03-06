/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.google.common.cache.Cache
 *  com.google.common.cache.CacheBuilder
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Chunk
 *  org.bukkit.Effect
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 */
package com.customhcf.hcf.timer.type;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.faction.FactionManager;
import com.customhcf.hcf.faction.type.Faction;
import com.customhcf.hcf.faction.type.PlayerFaction;
import com.customhcf.hcf.timer.PlayerTimer;
import com.customhcf.hcf.timer.TimerRunnable;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportTimer
        extends PlayerTimer
        implements Listener {
    private final ConcurrentMap<Object, Object> destinationMap;
    private final HCF plugin;

    public TeleportTimer(HCF plugin) {
        super("Home", TimeUnit.SECONDS.toMillis(10), false);
        this.plugin = plugin;
        this.destinationMap = CacheBuilder.newBuilder().expireAfterWrite(60000, TimeUnit.MILLISECONDS).build().asMap();
    }

    public Object getDestination(Player player) {
        return this.destinationMap.get(player.getUniqueId());
    }

    @Override
    public String getScoreboardPrefix() {
        return ChatColor.DARK_AQUA.toString() + (Object)ChatColor.BOLD;
    }

    @Override
    public TimerRunnable clearCooldown(UUID uuid) {
        TimerRunnable runnable = super.clearCooldown(uuid);
        if (runnable != null) {
            this.destinationMap.remove(uuid);
            return runnable;
        }
        return null;
    }

    public int getNearbyEnemies(Player player, int distance) {
        FactionManager factionManager = this.plugin.getFactionManager();
        PlayerFaction playerFaction = factionManager.getPlayerFaction(player.getUniqueId());
        int count = 0;
        final Collection<Entity> nearby = (Collection<Entity>)player.getNearbyEntities((double)distance, (double)distance, (double)distance);
        for (Entity entity : nearby) {
            Player target;
            PlayerFaction targetFaction;
            if (!(entity instanceof Player) || !(target = (Player)entity).canSee(player) || !player.canSee(target) || playerFaction != null && (targetFaction = factionManager.getPlayerFaction(target.getUniqueId())) != null && targetFaction.equals(playerFaction)) continue;
            ++count;
        }
        return count;
    }

    public boolean teleport(Player player, Location location, long millis, String warmupMessage, PlayerTeleportEvent.TeleportCause cause) {
        boolean result;
        this.cancelTeleport(player, null);
        if (millis <= 0) {
            result = player.teleport(location, cause);
            this.clearCooldown(player.getUniqueId());
        } else {
            UUID uuid = player.getUniqueId();
            player.sendMessage(warmupMessage);
            this.destinationMap.put(uuid, (Object)location.clone());
            this.setCooldown(player, uuid, millis, true);
            result = true;
        }
        return result;
    }

    public void cancelTeleport(Player player, String reason) {
        UUID uuid = player.getUniqueId();
        if (this.getRemaining(uuid) > 0) {
            this.clearCooldown(uuid);
            if (reason != null && !reason.isEmpty()) {
                player.sendMessage(reason);
            }
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        if (from.getBlockX() == to.getBlockX() && from.getBlockY() == to.getBlockY() && from.getBlockZ() == to.getBlockZ()) {
            return;
        }
        this.cancelTeleport(event.getPlayer(), (Object)ChatColor.YELLOW + "You moved a block, therefore cancelling your teleport.");
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPlayerDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            this.cancelTeleport((Player)entity, (Object)ChatColor.YELLOW + "You took damage, therefore cancelling your teleport.");
        }
    }

    @Override
    public void onExpire(UUID userUUID) {
        Player player = Bukkit.getPlayer((UUID)userUUID);
        if (player == null) {
            return;
        }
        Location destination = (Location)this.destinationMap.remove(userUUID);
        if (destination != null) {
            destination.getChunk();
            player.playEffect(player.getLocation().clone().add(0.5, 1.0, 0.5), Effect.ENDER_SIGNAL, 3);
            player.teleport(destination, PlayerTeleportEvent.TeleportCause.COMMAND);
        }
    }
}


