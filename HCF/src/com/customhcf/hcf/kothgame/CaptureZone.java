/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.util.cuboid.Cuboid
 *  com.google.common.collect.Maps
 *  javax.annotation.Nullable
 *  org.apache.commons.lang3.time.DurationFormatUtils
 *  org.bukkit.configuration.serialization.ConfigurationSerializable
 *  org.bukkit.entity.Player
 */
package com.customhcf.hcf.kothgame;

import com.customhcf.hcf.Utils.DateTimeFormats;
import com.customhcf.util.cuboid.Cuboid;
import com.google.common.collect.Maps;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

public class CaptureZone
        implements ConfigurationSerializable {
    public static final int MINIMUM_SIZE_AREA = 2;
    private final Object lock = new Object();
    private String scoreboardRemaining;
    private String name;
    private String prefix;
    private Cuboid cuboid;
    private Player cappingPlayer;
    private long defaultCaptureMillis;
    private String defaultCaptureWords;
    private long endMillis;

    public CaptureZone(String name, Cuboid cuboid, long defaultCaptureMillis) {
        this(name, "", cuboid, defaultCaptureMillis);
    }

    public CaptureZone(String name, String prefix, Cuboid cuboid, long defaultCaptureMillis) {
        this.name = name;
        this.prefix = prefix;
        this.cuboid = cuboid;
        this.setDefaultCaptureMillis(defaultCaptureMillis);
    }

    public CaptureZone(Map<String, Object> map) {
        this.name = (String)map.get("name");
        Object obj = map.get("prefix");
        if (obj instanceof String) {
            this.prefix = (String)obj;
        }
        if ((obj = map.get("cuboid")) instanceof Cuboid) {
            this.cuboid = (Cuboid)obj;
        }
        this.setDefaultCaptureMillis(Long.parseLong((String)map.get("captureMillis")));
    }

    public Map<String, Object> serialize() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("name", this.name);
        if (this.prefix != null) {
            map.put("prefix", this.prefix);
        }
        if (this.cuboid != null) {
            map.put("cuboid", this.cuboid);
        }
        map.put("captureMillis", Long.toString(this.defaultCaptureMillis));
        return map;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public String getScoreboardRemaining() {
        Object object = this.lock;
        synchronized (object) {
            return this.scoreboardRemaining;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void updateScoreboardRemaining() {
        Object object = this.lock;
        synchronized (object) {
            this.scoreboardRemaining = DateTimeFormats.KOTH_FORMAT.format(this.getRemainingCaptureMillis());
        }
    }

    public boolean isActive() {
        return this.getRemainingCaptureMillis() > 0;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        if (this.prefix == null) {
            this.prefix = "";
        }
        return this.prefix;
    }

    public String getDisplayName() {
        return this.getPrefix() + this.name;
    }

    public Cuboid getCuboid() {
        return this.cuboid;
    }

    public long getRemainingCaptureMillis() {
        if (this.endMillis == Long.MIN_VALUE) {
            return -1;
        }
        if (this.cappingPlayer == null) {
            return this.defaultCaptureMillis;
        }
        return this.endMillis - System.currentTimeMillis();
    }

    public void setRemainingCaptureMillis(long millis) {
        this.endMillis = System.currentTimeMillis() + millis;
    }

    public long getDefaultCaptureMillis() {
        return this.defaultCaptureMillis;
    }

    public String getDefaultCaptureWords() {
        return this.defaultCaptureWords;
    }

    public void setDefaultCaptureMillis(long millis) {
        if (this.defaultCaptureMillis != millis) {
            this.defaultCaptureMillis = millis;
            this.defaultCaptureWords = org.apache.commons.lang.time.DurationFormatUtils.formatDurationWords((long)millis, (boolean)true, (boolean)true);
        }
    }

    public Player getCappingPlayer() {
        return this.cappingPlayer;
    }

    public void setCappingPlayer(@Nullable Player player) {
        this.cappingPlayer = player;
        this.endMillis = player == null ? this.defaultCaptureMillis : System.currentTimeMillis() + this.defaultCaptureMillis;
    }
}
