/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Optional
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.HandlerList
 */
package com.customhcf.hcf.faction.event;

import com.customhcf.hcf.faction.event.FactionEvent;
import com.customhcf.hcf.faction.type.Faction;
import com.customhcf.hcf.faction.type.PlayerFaction;
import com.google.common.base.Optional;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PlayerJoinedFactionEvent
extends FactionEvent {
    private static final HandlerList handlers = new HandlerList();
    private final UUID uniqueID;
    private Optional<Player> player;

    public PlayerJoinedFactionEvent(Player player, PlayerFaction playerFaction) {
        super(playerFaction);
        this.player = Optional.of(player);
        this.uniqueID = player.getUniqueId();
    }

    public PlayerJoinedFactionEvent(UUID playerUUID, PlayerFaction playerFaction) {
        super(playerFaction);
        this.uniqueID = playerUUID;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public PlayerFaction getFaction() {
        return (PlayerFaction)this.faction;
    }

    public Optional<Player> getPlayer() {
        if (this.player == null) {
            this.player = Optional.fromNullable(Bukkit.getPlayer((UUID)this.uniqueID));
        }
        return this.player;
    }

    public UUID getUniqueID() {
        return this.uniqueID;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}

