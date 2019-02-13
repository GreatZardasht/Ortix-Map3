/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 */
package com.customhcf.hcf.visualise;

import com.customhcf.hcf.visualise.VisualBlockData;
import com.customhcf.hcf.visualise.VisualType;
import org.bukkit.Location;

public class VisualBlock {
    private final VisualType visualType;
    private final VisualBlockData blockData;
    private final Location location;

    public VisualBlock(VisualType visualType, VisualBlockData blockData, Location location) {
        this.visualType = visualType;
        this.blockData = blockData;
        this.location = location;
    }

    public VisualType getVisualType() {
        return this.visualType;
    }

    public VisualBlockData getBlockData() {
        return this.blockData;
    }

    public Location getLocation() {
        return this.location;
    }
}

