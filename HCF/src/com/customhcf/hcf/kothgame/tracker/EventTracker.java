/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package com.customhcf.hcf.kothgame.tracker;

import com.customhcf.hcf.kothgame.CaptureZone;
import com.customhcf.hcf.kothgame.EventTimer;
import com.customhcf.hcf.kothgame.EventType;
import com.customhcf.hcf.kothgame.faction.EventFaction;
import org.bukkit.entity.Player;

@Deprecated
public interface EventTracker {
    public EventType getEventType();

    public void tick(EventTimer var1, EventFaction var2);

    public void onContest(EventFaction var1, EventTimer var2);

    public boolean onControlTake(Player var1, CaptureZone var2);

    public boolean onControlLoss(Player var1, CaptureZone var2, EventFaction var3);

    public void stopTiming();
}

