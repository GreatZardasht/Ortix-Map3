/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Iterables
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 */
package com.customhcf.hcf.visualise;

import com.customhcf.hcf.visualise.VisualBlockData;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.entity.Player;

abstract class BlockFiller {
    BlockFiller() {
    }

    abstract VisualBlockData generate(Player var1, Location var2);

    ArrayList<VisualBlockData> bulkGenerate(Player player, Iterable<Location> locations) {
        ArrayList<VisualBlockData> data = new ArrayList<VisualBlockData>(Iterables.size(locations));
        for (Location location : locations) {
            data.add(this.generate(player, location));
        }
        return data;
    }
}

