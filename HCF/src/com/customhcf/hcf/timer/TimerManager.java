/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.customhcf.hcf.timer;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.kothgame.EventTimer;
import com.customhcf.hcf.timer.type.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class TimerManager
implements Listener {
    public final LogoutTimer logoutTimer;
    public final EnderPearlTimer enderPearlTimer;
    public final NotchAppleTimer notchAppleTimer;
    public final PvpProtectionTimer pvpProtectionTimer;
    public final PvpClassWarmupTimer pvpClassWarmupTimer;
    public final StuckTimer stuckTimer;
    public final SpawnTagTimer spawnTagTimer;
    public final TeleportTimer teleportTimer;
    public final EventTimer eventTimer;
    public final ArcherTimer archerTimer;
    public final SOTWTimer sotwTimer;
    private final Set<Timer> timers = new HashSet<Timer>();
    private final JavaPlugin plugin;

    public TimerManager(HCF plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
        this.archerTimer = new ArcherTimer(plugin);
        this.registerTimer(this.archerTimer);
        this.sotwTimer = new SOTWTimer();
        this.registerTimer(this.sotwTimer);
        this.enderPearlTimer = new EnderPearlTimer(plugin);
        this.registerTimer(this.enderPearlTimer);
        this.logoutTimer = new LogoutTimer();
        this.registerTimer(this.logoutTimer);
        this.notchAppleTimer = new NotchAppleTimer(plugin);
        this.registerTimer(this.notchAppleTimer);
        this.stuckTimer = new StuckTimer();
        this.registerTimer(this.stuckTimer);
        this.pvpProtectionTimer = new PvpProtectionTimer(plugin);
        this.registerTimer(this.pvpProtectionTimer);
        this.spawnTagTimer = new SpawnTagTimer(plugin);
        this.registerTimer(this.spawnTagTimer);
        this.teleportTimer = new TeleportTimer(plugin);
        this.registerTimer(this.teleportTimer);
        this.eventTimer = new EventTimer(plugin);
        this.registerTimer(this.eventTimer);
        this.pvpClassWarmupTimer = new PvpClassWarmupTimer(plugin);
        this.registerTimer(this.pvpClassWarmupTimer);
    }

    public Collection<Timer> getTimers() {
        return this.timers;
    }

    public void registerTimer(Timer timer) {
        this.timers.add(timer);
        if (timer instanceof Listener) {
            this.plugin.getServer().getPluginManager().registerEvents((Listener)timer, (Plugin)this.plugin);
        }
    }

    public void unregisterTimer(Timer timer) {
        this.timers.remove(timer);
    }
}

