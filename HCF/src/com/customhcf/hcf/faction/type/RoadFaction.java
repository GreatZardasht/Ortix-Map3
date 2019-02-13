/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.base.ServerHandler
 *  com.customhcf.util.BukkitUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.World$Environment
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.serialization.ConfigurationSerializable
 */
package com.customhcf.hcf.faction.type;

import com.customhcf.hcf.Utils.ConfigurationService;
import com.customhcf.hcf.faction.claim.Claim;
import com.customhcf.util.BukkitUtils;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class RoadFaction extends ClaimableFaction implements ConfigurationSerializable
{
    public static final int ROAD_EDGE_DIFF = 1000;
    public static final double ROAD_WIDTH_LEFT = 4;
    public static final double ROAD_WIDTH_RIGHT = 4;
    public static final int ROAD_MIN_HEIGHT = 0;
    public static final int ROAD_MAX_HEIGHT = 256;

    public RoadFaction(final String name) {
        super(name);
    }

    public RoadFaction(final Map<String, Object> map) {
        super(map);
    }

    public String getDisplayName(final CommandSender sender) {
        return ConfigurationService.ROAD_COLOUR + this.getName().replace("st", "st ").replace("th", "th ");
    }

    @Override
    public void printDetails(final CommandSender sender) {
        sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        sender.sendMessage(' ' + this.getDisplayName(sender));
        sender.sendMessage(ChatColor.GRAY + "  Location: " + ChatColor.GRAY + "None");
        sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
    }

    public static class NorthRoadFaction extends RoadFaction implements ConfigurationSerializable
    {
        public NorthRoadFaction() {
            super("NorthRoad");
            for (final World world : Bukkit.getWorlds()) {
                final World.Environment environment = world.getEnvironment();
                if (environment != World.Environment.THE_END) {
                    final int borderSize = ConfigurationService.BORDER_SIZES.get(environment);
                    final double offset = ConfigurationService.SPAWN_RADIUS_MAP.get(environment) + 1.0;
                    this.addClaim(new Claim(this, new Location(world, -ROAD_WIDTH_LEFT, ROAD_MIN_HEIGHT, -offset), new Location(world, ROAD_WIDTH_RIGHT, ROAD_MAX_HEIGHT, (double)(-(borderSize - 1)))), null);
                }
            }
        }

        public NorthRoadFaction(final Map<String, Object> map) {
            super(map);
        }
    }

    public static class EastRoadFaction extends RoadFaction implements ConfigurationSerializable
    {
        public EastRoadFaction() {
            super("EastRoad");
            for (final World world : Bukkit.getWorlds()) {
                final World.Environment environment = world.getEnvironment();
                if (environment != World.Environment.THE_END) {
                    final int borderSize = ConfigurationService.BORDER_SIZES.get(environment);
                    final double offset = ConfigurationService.SPAWN_RADIUS_MAP.get(environment) + 1.0;
                    this.addClaim(new Claim(this, new Location(world, offset, ROAD_WIDTH_RIGHT, -ROAD_WIDTH_LEFT), new Location(world, (double)(borderSize - 1), ROAD_MAX_HEIGHT, ROAD_WIDTH_RIGHT)), null);
                }
            }
        }

        public EastRoadFaction(final Map<String, Object> map) {
            super(map);
        }
    }

    public static class SouthRoadFaction extends RoadFaction implements ConfigurationSerializable
    {
        public SouthRoadFaction() {
            super("SouthRoad");
            for (final World world : Bukkit.getWorlds()) {
                final World.Environment environment = world.getEnvironment();
                if (environment != World.Environment.THE_END) {
                    final int borderSize = ConfigurationService.BORDER_SIZES.get(environment);
                    final double offset = ConfigurationService.SPAWN_RADIUS_MAP.get(environment) + 1.0;
                    this.addClaim(new Claim(this, new Location(world, -ROAD_WIDTH_LEFT, ROAD_WIDTH_RIGHT, offset), new Location(world, ROAD_WIDTH_RIGHT, ROAD_MAX_HEIGHT, (double)(borderSize - 1))), null);
                }
            }
        }

        public SouthRoadFaction(final Map<String, Object> map) {
            super(map);
        }
    }

    public static class WestRoadFaction extends RoadFaction implements ConfigurationSerializable
    {
        public WestRoadFaction() {
            super("WestRoad");
            for (final World world : Bukkit.getWorlds()) {
                final World.Environment environment = world.getEnvironment();
                if (environment != World.Environment.THE_END) {
                    final int borderSize = ConfigurationService.BORDER_SIZES.get(environment);
                    final double offset = ConfigurationService.SPAWN_RADIUS_MAP.get(environment) + 1.0;
                    this.addClaim(new Claim(this, new Location(world, -offset, ROAD_WIDTH_RIGHT, ROAD_WIDTH_RIGHT), new Location(world, (double)(-(borderSize - 1)), ROAD_MAX_HEIGHT, -ROAD_WIDTH_LEFT)), null);
                }
            }
        }

        public WestRoadFaction(final Map<String, Object> map) {
            super(map);
        }
    }
}

