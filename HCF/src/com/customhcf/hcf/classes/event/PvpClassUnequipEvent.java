/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.player.PlayerEvent
 */
package com.customhcf.hcf.classes.event;

import com.customhcf.hcf.classes.PvpClass;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PvpClassUnequipEvent
extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final PvpClass pvpClass;

    public PvpClassUnequipEvent(Player player, PvpClass pvpClass) {
        super(player);
        this.pvpClass = pvpClass;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public PvpClass getPvpClass() {
        return this.pvpClass;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}

