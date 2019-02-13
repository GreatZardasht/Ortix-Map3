/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.time.FastDateFormat
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 */
package com.customhcf.hcf.kothgame.tracker;

import com.customhcf.hcf.Utils.DateTimeFormats;
import com.customhcf.hcf.HCF;
import com.customhcf.hcf.kothgame.CaptureZone;
import com.customhcf.hcf.kothgame.EventTimer;
import com.customhcf.hcf.kothgame.EventType;
import com.customhcf.hcf.kothgame.faction.EventFaction;
import com.customhcf.hcf.kothgame.faction.KothFaction;

import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@Deprecated
public class KothTracker
implements EventTracker {
    public static final long DEFAULT_CAP_MILLIS;
    private static final long MINIMUM_CONTROL_TIME_ANNOUNCE;
    private final HCF plugin;

    public KothTracker(HCF plugin) {
        this.plugin = plugin;
    }

    @Override
    public EventType getEventType() {
        return EventType.KOTH;
    }

    @Override
    public void tick(EventTimer eventTimer, EventFaction eventFaction) {
        CaptureZone captureZone = ((KothFaction)eventFaction).getCaptureZone();
        long remainingMillis = captureZone.getRemainingCaptureMillis();
        if (remainingMillis <= 0) {
            this.plugin.getTimerManager().eventTimer.handleWinner(captureZone.getCappingPlayer());
            eventTimer.clearCooldown();
            return;
        }
        if (remainingMillis == captureZone.getDefaultCaptureMillis()) {
            return;
        }
        int remainingSeconds = (int)(remainingMillis / 1000);
        if (remainingSeconds > 0 && remainingSeconds % 30 == 0) {
            Bukkit.broadcastMessage((String)((Object)ChatColor.YELLOW + "[" + eventFaction.getEventType().getDisplayName() + "] " + (Object)ChatColor.GOLD + "Someone is controlling " + (Object)ChatColor.LIGHT_PURPLE + captureZone.getDisplayName() + (Object)ChatColor.GOLD + ". " + (Object)ChatColor.RED + '(' + DateTimeFormats.KOTH_FORMAT.format(remainingMillis) + ')'));
        }
    }

    @Override
    public void onContest(EventFaction eventFaction, EventTimer eventTimer) {
        Bukkit.broadcastMessage((String)((Object)ChatColor.YELLOW + "[" + eventFaction.getEventType().getDisplayName() + "] " + (Object)ChatColor.LIGHT_PURPLE + eventFaction.getName() + (Object)ChatColor.GOLD + " can now be contested. " + (Object)ChatColor.RED + '(' + DateTimeFormats.KOTH_FORMAT.format(eventTimer.getRemaining()) + ')'));
    }

    @Override
    public boolean onControlTake(Player player, CaptureZone captureZone) {
        player.sendMessage((Object)ChatColor.GOLD + "You are now in control of " + (Object)ChatColor.LIGHT_PURPLE + captureZone.getDisplayName() + (Object)ChatColor.GOLD + '.');
        return true;
    }

    @Override
    public boolean onControlLoss(Player player, CaptureZone captureZone, EventFaction eventFaction) {
        player.sendMessage((Object)ChatColor.GOLD + "You are no longer in control of " + (Object)ChatColor.LIGHT_PURPLE + captureZone.getDisplayName() + (Object)ChatColor.GOLD + '.');
        long remainingMillis = captureZone.getRemainingCaptureMillis();
        if (remainingMillis > 0 && captureZone.getDefaultCaptureMillis() - remainingMillis > MINIMUM_CONTROL_TIME_ANNOUNCE) {
            Bukkit.broadcastMessage((String)((Object)ChatColor.YELLOW + "[" + eventFaction.getEventType().getDisplayName() + "] " + (Object)ChatColor.LIGHT_PURPLE + player.getName() + (Object)ChatColor.GOLD + " has lost control of " + (Object)ChatColor.LIGHT_PURPLE + captureZone.getDisplayName() + (Object)ChatColor.GOLD + '.' + (Object)ChatColor.RED + " (" + DateTimeFormats.KOTH_FORMAT.format(captureZone.getRemainingCaptureMillis()) + ')'));
        }
        return true;
    }

    @Override
    public void stopTiming() {
    }

    static {
        MINIMUM_CONTROL_TIME_ANNOUNCE = TimeUnit.SECONDS.toMillis(25);
        DEFAULT_CAP_MILLIS = TimeUnit.MINUTES.toMillis(15);
    }
}

