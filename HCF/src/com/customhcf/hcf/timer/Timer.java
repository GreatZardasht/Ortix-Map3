/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.util.Config
 */
package com.customhcf.hcf.timer;

import com.customhcf.util.Config;

public abstract class Timer {
    protected final String name;
    protected final long defaultCooldown;

    public Timer(String name, long defaultCooldown) {
        this.name = name;
        this.defaultCooldown = defaultCooldown;
    }

    public abstract String getScoreboardPrefix();

    public String getName() {
        return this.name;
    }

    public final String getDisplayName() {
        return this.getScoreboardPrefix() + this.name;
    }

    public void load(Config config) {
    }

    public void onDisable(Config config) {
    }
}

