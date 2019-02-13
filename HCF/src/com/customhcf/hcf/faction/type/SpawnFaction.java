/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.World$Environment
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.serialization.ConfigurationSerializable
 */
package com.customhcf.hcf.faction.type;

import com.customhcf.hcf.Utils.ConfigurationService;
import com.customhcf.hcf.faction.claim.Claim;

import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class SpawnFaction
extends ClaimableFaction
implements ConfigurationSerializable {
    public SpawnFaction() {
        super("Spawn");
        this.safezone = true;
        for (World world : Bukkit.getWorlds()) {
            World.Environment environment = world.getEnvironment();
            if (environment == World.Environment.THE_END) continue;
            double radius = ConfigurationService.SPAWN_RADIUS_MAP.get((Object)world.getEnvironment());
            this.addClaim(new Claim(this, new Location(world, radius, 0.0, radius), new Location(world, - radius, (double)world.getMaxHeight(), - radius)), null);
        }
    }

    public SpawnFaction(Map<String, Object> map) {
        super(map);
    }

    @Override
    public boolean isDeathban() {
        return false;
    }
}

