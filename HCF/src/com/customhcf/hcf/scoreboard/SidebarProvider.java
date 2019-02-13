/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package com.customhcf.hcf.scoreboard;

import com.customhcf.hcf.scoreboard.SidebarEntry;
import java.util.List;
import org.bukkit.entity.Player;

public interface SidebarProvider {
    public String getTitle();

    public List<SidebarEntry> getLines(Player var1);
}

