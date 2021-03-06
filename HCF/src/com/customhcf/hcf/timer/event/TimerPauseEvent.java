/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Optional
 *  org.bukkit.event.Cancellable
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package com.customhcf.hcf.timer.event;

import com.customhcf.hcf.timer.PlayerTimer;
import com.customhcf.hcf.timer.Timer;
import com.google.common.base.Optional;
import java.util.UUID;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TimerPauseEvent
extends Event
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final boolean paused;
    private final Optional<UUID> userUUID;
    private final Timer timer;
    private boolean cancelled;

    public TimerPauseEvent(Timer timer, boolean paused) {
        this.userUUID = Optional.absent();
        this.timer = timer;
        this.paused = paused;
    }

    public TimerPauseEvent(UUID userUUID, PlayerTimer timer, boolean paused) {
        this.userUUID = Optional.fromNullable(userUUID);
        this.timer = timer;
        this.paused = paused;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Optional<UUID> getUserUUID() {
        return this.userUUID;
    }

    public Timer getTimer() {
        return this.timer;
    }

    public boolean isPaused() {
        return this.paused;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}

