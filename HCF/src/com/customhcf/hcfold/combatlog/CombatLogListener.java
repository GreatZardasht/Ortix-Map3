/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.util.InventoryUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.craftbukkit.v1_7_R4.entity.CraftLivingEntity
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityInteractEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.inventory.EntityEquipment
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 *  org.spigotmc.event.player.PlayerSpawnLocationEvent
 */
package com.customhcf.hcfold.combatlog;

import com.customhcf.hcf.HCF;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

//import com.gmail.xd.zwander.istaff.data.PlayerHackerMode;

/**
 * Listener that prevents {@link Player}s from combat-logging.
 */
public class CombatLogListener implements Listener {

    private static final int NEARBY_SPAWN_RADIUS = 64;

    private final Set<UUID> safelyDisconnected = new HashSet<>();
    private final Map<UUID, LoggerEntity> loggers = new HashMap<>();

    private final HCF plugin;

    public CombatLogListener(HCF plugin) {
        this.plugin = plugin;
    }

    /**
     * Disconnects a {@link Player} without a {@link LoggerEntity} spawning.
     *
     * @param player the {@link Player} to disconnect
     * @param reason the reason for disconnecting
     */
    public void safelyDisconnect(Player player, String reason) {
        if (this.safelyDisconnected.add(player.getUniqueId())) {
            player.kickPlayer(reason);
        }
    }

    public boolean removeCombatLogger(UUID uuid) {
        LoggerEntity entity = loggers.remove(uuid);
        if (entity != null) {
            entity.getBukkitEntity().setHealth(0);
            entity.destroy();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes all the {@link LoggerEntity} instances from the server.
     */
    public void removeCombatLoggers() {
        Iterator<LoggerEntity> iterator = loggers.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().destroy();
            iterator.remove();
        }

        safelyDisconnected.clear();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onLoggerRemoved(LoggerRemovedEvent event) {
        loggers.remove(event.getLoggerEntity().getUniqueID());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerLogin(PlayerLoginEvent event) {
        LoggerEntity currentLogger = loggers.remove(event.getPlayer().getUniqueId());
        if (currentLogger != null) {
            currentLogger.destroy();
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        boolean result = safelyDisconnected.remove(uuid);
        if (!result && player.getGameMode() != GameMode.CREATIVE && !player.isDead() && true) {
            // Make sure the player hasn't already spawned a logger.
            if (loggers.containsKey(player.getUniqueId())) {
                return;
            }

            // If the player has PVP protection, don't spawn a logger
            if (plugin.getTimerManager().pvpProtectionTimer.getRemaining(uuid) > 0L) {
                return;
            }

            // Make sure the player is not in a safe-zone.
            Location location = player.getLocation();
            if (plugin.getFactionManager().getFactionAt(location).isSafezone()) {
                return;
            }

            // There is no enemies near the player, so don't spawn a logger.
            if (plugin.getSotwTimer().getSotwRunnable() != null || plugin.getTimerManager().teleportTimer.getNearbyEnemies(player, NEARBY_SPAWN_RADIUS) <= 0) {
                return;
            }

            LoggerEntity loggerEntity = new LoggerEntityHuman(player, location.getWorld());

            LoggerSpawnEvent calledEvent = new LoggerSpawnEvent(loggerEntity);
            Bukkit.getPluginManager().callEvent(calledEvent);
            if (!calledEvent.isCancelled()) {
                loggers.put(player.getUniqueId(), loggerEntity);

                // Call a tick later allowing for the NBT to save, the reason why
                // it is saved after the PlayerQuitEvent is so the plugins can modify
                // the inventory during this event.
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        // Just double check the player hasn't
                        // logged off during this tick.
                        if (!player.isOnline()) {
                            loggerEntity.postSpawn(plugin);
                        }
                    }
                }.runTaskLater(plugin, 1L);
            }
        }
    }
}
