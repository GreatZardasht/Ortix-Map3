/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.util.BukkitUtils
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 *  com.google.common.collect.ImmutableSet
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.World$Environment
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.serialization.ConfigurationSerializable
 */
package com.customhcf.hcf.kothgame.faction;

import com.customhcf.hcf.faction.claim.Claim;
import com.customhcf.hcf.kothgame.CaptureZone;
import com.customhcf.hcf.kothgame.EventType;
import com.customhcf.hcf.kothgame.faction.CapturableFaction;
import com.customhcf.util.BukkitUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class ConquestFaction
        extends CapturableFaction
        implements ConfigurationSerializable {
    private final EnumMap<ConquestZone, CaptureZone> captureZones = new EnumMap(ConquestZone.class);


    public FactionType getFactionType() {
        return FactionType.CONQUEST;
    }

    public ConquestFaction(String name) {
        super(name);
    }

    public ConquestFaction(Map<String, Object> map) {
        super(map);
        Object object = map.get("red");
        if (object instanceof CaptureZone) {
            this.captureZones.put(ConquestZone.RED, (CaptureZone)object);
        }
        if ((object = map.get("green")) instanceof CaptureZone) {
            this.captureZones.put(ConquestZone.GREEN, (CaptureZone)object);
        }
        if ((object = map.get("blue")) instanceof CaptureZone) {
            this.captureZones.put(ConquestZone.BLUE, (CaptureZone)object);
        }
        if ((object = map.get("yellow")) instanceof CaptureZone) {
            this.captureZones.put(ConquestZone.YELLOW, (CaptureZone)object);
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        for (Map.Entry<ConquestZone, CaptureZone> entry : this.captureZones.entrySet()) {
            map.put(entry.getKey().name().toLowerCase(), entry.getValue());
        }
        return map;
    }

    @Override
    public EventType getEventType() {
        return EventType.CONQUEST;
    }

    @Override
    public void printDetails(CommandSender sender) {
        sender.sendMessage(this.getDisplayName(sender));
        for (Claim claim : this.claims) {
            Location location = claim.getCenter();
            sender.sendMessage((Object)ChatColor.YELLOW + "  Location: " + (Object)ChatColor.RED + '(' + (String)ENVIRONMENT_MAPPINGS.get((Object)location.getWorld().getEnvironment()) + ", " + location.getBlockX() + " | " + location.getBlockZ() + ')');
        }
    }

    public void setZone(ConquestZone conquestZone, CaptureZone captureZone) {
        switch (conquestZone) {
            case RED: {
                this.captureZones.put(ConquestZone.RED, captureZone);
                break;
            }
            case BLUE: {
                this.captureZones.put(ConquestZone.BLUE, captureZone);
                break;
            }
            case GREEN: {
                this.captureZones.put(ConquestZone.GREEN, captureZone);
                break;
            }
            case YELLOW: {
                this.captureZones.put(ConquestZone.YELLOW, captureZone);
                break;
            }
            default: {
                throw new AssertionError((Object)"Unsupported operation");
            }
        }
    }

    public CaptureZone getRed() {
        return this.captureZones.get((Object)ConquestZone.RED);
    }

    public CaptureZone getGreen() {
        return this.captureZones.get((Object)ConquestZone.GREEN);
    }

    public CaptureZone getBlue() {
        return this.captureZones.get((Object)ConquestZone.BLUE);
    }

    public CaptureZone getYellow() {
        return this.captureZones.get((Object)ConquestZone.YELLOW);
    }

    public Collection<ConquestZone> getConquestZones() {
        return ImmutableSet.copyOf(this.captureZones.keySet());
    }

    @Override
    public List<CaptureZone> getCaptureZones() {
        return ImmutableList.copyOf(this.captureZones.values());
    }

    public static enum ConquestZone {
        RED(ChatColor.RED, "Red"),
        BLUE(ChatColor.AQUA, "Blue"),
        YELLOW(ChatColor.YELLOW, "Yellow"),
        GREEN(ChatColor.GREEN, "Green");

        private final String name;
        private final ChatColor color;
        private static final Map<String, ConquestZone> BY_NAME;

        private ConquestZone(ChatColor color, String name) {
            this.color = color;
            this.name = name;
        }

        public ChatColor getColor() {
            return this.color;
        }

        public String getName() {
            return this.name;
        }

        public String getDisplayName() {
            return this.color.toString() + this.name;
        }

        public static ConquestZone getByName(String name) {
            return BY_NAME.get(name.toUpperCase());
        }

        public static Collection<String> getNames() {
            return new ArrayList<String>(BY_NAME.keySet());
        }

        static {
            ImmutableMap.Builder builder = ImmutableMap.builder();
            for (ConquestZone zone : ConquestZone.values()) {
                builder.put((Object)zone.name().toUpperCase(), (Object)zone);
            }
            BY_NAME = builder.build();
        }
    }

}

