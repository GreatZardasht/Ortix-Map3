/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.event.Cancellable
 *  org.bukkit.event.HandlerList
 */
package com.customhcf.hcf.faction.event;

import com.customhcf.hcf.faction.event.FactionEvent;
import com.customhcf.hcf.faction.type.Faction;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class FactionRemoveEvent
extends FactionEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final CommandSender sender;
    private boolean cancelled;

    public FactionRemoveEvent(Faction faction, CommandSender sender) {
        super(faction);
        this.sender = sender;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public CommandSender getSender() {
        return this.sender;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}

