/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.util.Config
 *  com.google.common.base.Preconditions
 *  com.google.common.cache.Cache
 *  com.google.common.cache.CacheBuilder
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.EquipmentSetEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package com.customhcf.hcf.timer.type;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.classes.PvpClass;
import com.customhcf.hcf.classes.PvpClassManager;
import com.customhcf.hcf.timer.PlayerTimer;
import com.customhcf.hcf.timer.TimerRunnable;
import com.customhcf.util.Config;
import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.EquipmentSetEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class PvpClassWarmupTimer
extends PlayerTimer
implements Listener {
    protected final ConcurrentMap<Object, Object> classWarmups;
    private final HCF plugin;

    public PvpClassWarmupTimer(HCF plugin) {
        super("WarmUp", TimeUnit.SECONDS.toMillis(10), false);
        this.plugin = plugin;
        this.classWarmups = CacheBuilder.newBuilder().expireAfterWrite(this.defaultCooldown + 5000, TimeUnit.MILLISECONDS).build().asMap();
        new BukkitRunnable(){

            public void run() {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    PvpClassWarmupTimer.this.attemptEquip(player);
                }
            }
        }.runTaskLater((Plugin)plugin, 10);
    }

    @Override
    public void onDisable(Config config) {
        super.onDisable(config);
        this.classWarmups.clear();
    }

    @Override
    public String getScoreboardPrefix() {
        return (Object)ChatColor.AQUA + ChatColor.BOLD.toString();
    }

    @Override
    public TimerRunnable clearCooldown(UUID playerUUID) {
        TimerRunnable runnable = super.clearCooldown(playerUUID);
        if (runnable != null) {
            this.classWarmups.remove(playerUUID);
            return runnable;
        }
        return null;
    }

    @Override
    public void onExpire(UUID userUUID) {
        Player player = Bukkit.getPlayer((UUID)userUUID);
        if (player == null) {
            return;
        }
        String className = (String)this.classWarmups.remove(userUUID);
        Preconditions.checkNotNull((Object)className, (String)"Attempted to equip a class for %s, but nothing was added", (Object[])new Object[]{player.getName()});
        this.plugin.getPvpClassManager().setEquippedClass(player, this.plugin.getPvpClassManager().getPvpClass(className));
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPlayerKick(PlayerQuitEvent event) {
        this.plugin.getPvpClassManager().setEquippedClass(event.getPlayer(), null);
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.attemptEquip(event.getPlayer());
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onEquipmentSet(EquipmentSetEvent event) {
        HumanEntity humanEntity = event.getHumanEntity();
        if (humanEntity instanceof Player) {
            this.attemptEquip((Player)humanEntity);
        }
    }

    private void attemptEquip(Player player) {
        PvpClass equipped = this.plugin.getPvpClassManager().getEquippedClass(player);
        if (equipped != null) {
            if (equipped.isApplicableFor(player)) {
                return;
            }
            this.plugin.getPvpClassManager().setEquippedClass(player, null);
        }
        PvpClass warmupClass = null;
        String warmup = (String)this.classWarmups.get(player.getUniqueId());
        if (warmup != null && !(warmupClass = this.plugin.getPvpClassManager().getPvpClass(warmup)).isApplicableFor(player)) {
            this.clearCooldown(player.getUniqueId());
        }
        Collection<PvpClass> pvpClasses = this.plugin.getPvpClassManager().getPvpClasses();
        for (PvpClass pvpClass : pvpClasses) {
            if (warmupClass == pvpClass || !pvpClass.isApplicableFor(player)) continue;
            this.classWarmups.put(player.getUniqueId(), pvpClass.getName());
            this.setCooldown(player, player.getUniqueId(), pvpClass.getWarmupDelay(), false);
            break;
        }
    }

}

