/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.scheduler.BukkitTask
 */
package com.customhcf.util.bossbar;

import com.customhcf.util.bossbar.BossBar;
import org.bukkit.scheduler.BukkitTask;

public class BossBarEntry {
    private final BossBar bossBar;
    private final BukkitTask cancelTask;

    public BossBarEntry(BossBar bossBar, BukkitTask cancelTask) {
        this.bossBar = bossBar;
        this.cancelTask = cancelTask;
    }

    public BossBar getBossBar() {
        return this.bossBar;
    }

    public BukkitTask getCancelTask() {
        return this.cancelTask;
    }
}

