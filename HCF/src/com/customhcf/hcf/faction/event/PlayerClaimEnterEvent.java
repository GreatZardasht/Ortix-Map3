/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Cancellable
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package com.customhcf.hcf.faction.event;

import com.customhcf.hcf.faction.type.Faction;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerClaimEnterEvent
extends Event
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Faction fromFaction;
    private final Faction toFaction;
    private final Location from;
    private final Location to;
    private final EnterCause enterCause;
    private boolean cancelled;

    public PlayerClaimEnterEvent(Player player, Location from, Location to, Faction fromFaction, Faction toFaction, EnterCause enterCause) {
        this.player = player;
        this.from = from;
        this.to = to;
        this.fromFaction = fromFaction;
        this.toFaction = toFaction;
        this.enterCause = enterCause;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Faction getFromFaction() {
        return this.fromFaction;
    }

    public Faction getToFaction() {
        return this.toFaction;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Location getFrom() {
        return this.from;
    }

    public Location getTo() {
        return this.to;
    }

    public EnterCause getEnterCause() {
        return this.enterCause;
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

    public static enum EnterCause {
        TELEPORT,
        MOVEMENT;
        

        private EnterCause() {
        }
    }

}

