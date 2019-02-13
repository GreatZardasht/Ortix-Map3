package com.customhcf.hcfold.combatlog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import com.customhcf.hcfold.combatlog.LoggerEntity;

@Getter
public class LoggerDeathEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final LoggerEntity loggerEntity;

    public LoggerDeathEvent(LoggerEntity loggerEntity) {
        this.loggerEntity = loggerEntity;
    }

    public LoggerEntity getLoggerEntity() {
        return this.loggerEntity;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

