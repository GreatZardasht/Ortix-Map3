/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 */
package com.customhcf.hcf.faction.struct;

import org.bukkit.ChatColor;

public enum RegenStatus {
    FULL(ChatColor.GREEN.toString() + '\u25b6'),
    REGENERATING(ChatColor.GOLD.toString() + '\u25b2'),
    PAUSED(ChatColor.RED.toString() + '\u25a0');
    
    private final String symbol;

    private RegenStatus(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return this.symbol;
    }
}

