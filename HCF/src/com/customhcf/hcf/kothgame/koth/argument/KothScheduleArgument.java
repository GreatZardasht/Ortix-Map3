/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.util.BukkitUtils
 *  com.customhcf.util.command.CommandArgument
 *  org.apache.commons.lang3.text.WordUtils
 *  org.apache.commons.lang3.time.DurationFormatUtils
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 */
package com.customhcf.hcf.kothgame.koth.argument;

import com.customhcf.hcf.Utils.DateTimeFormats;
import com.customhcf.hcf.HCF;
import com.customhcf.util.BukkitUtils;
import com.customhcf.util.command.CommandArgument;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class KothScheduleArgument
extends CommandArgument {
    private static final String TIME_UNTIL_PATTERN = "d'd' H'h' mm'm'";
    public static final DateTimeFormatter HHMMA;
    private static List<String> shownEvents;
    private final HCF plugin;

    public KothScheduleArgument(HCF plugin) {
        super("schedule", "View the schedule for KOTH arenas");
        this.plugin = plugin;
        this.aliases = new String[]{"info", "i", "time"};
        this.permission = "hcf.command.koth.argument." + this.getName();
    }

    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        LocalDateTime now = LocalDateTime.now(DateTimeFormats.SERVER_ZONE_ID);
        int currentDay = now.getDayOfYear();
        Map<LocalDateTime, String> scheduleMap = this.plugin.eventScheduler.getScheduleMap();
        ArrayList<String> shownEvents = new ArrayList<String>();
        for (Map.Entry<LocalDateTime, String> entry : scheduleMap.entrySet()) {
            LocalDateTime scheduleDateTime = entry.getKey();
            if (!scheduleDateTime.isAfter(now)) continue;
            int dayDifference = scheduleDateTime.getDayOfYear() - currentDay;
            String eventName = entry.getValue();
            String monthName = scheduleDateTime.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            String weekName = scheduleDateTime.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            ChatColor colour = dayDifference == 0 ? ChatColor.GREEN : ChatColor.RED;
            shownEvents.add("  " + (Object)colour + WordUtils.capitalizeFully((String)eventName) + ": " + (Object)ChatColor.YELLOW + weekName + ' ' + scheduleDateTime.getDayOfMonth() + ' ' + monthName + (Object)ChatColor.RED + " (" + HHMMA.format(scheduleDateTime) + ')' + (Object)ChatColor.GRAY + " - " + (Object)ChatColor.GOLD + DurationFormatUtils.formatDuration((long)now.until(scheduleDateTime, ChronoUnit.MILLIS), (String)"d'd' H'h' mm'm'"));
        }
        if (shownEvents.isEmpty()) {
            sender.sendMessage((Object)ChatColor.RED + "There are no event schedules defined.");
            return true;
        }
        String monthName2 = WordUtils.capitalizeFully((String)now.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
        String weekName2 = WordUtils.capitalizeFully((String)now.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
        sender.sendMessage((Object)ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        sender.sendMessage((Object)ChatColor.YELLOW + "Current Server time " + (Object)ChatColor.GREEN + weekName2 + ' ' + now.getDayOfMonth() + ' ' + monthName2 + ' ' + HHMMA.format(now) + (Object)ChatColor.YELLOW + '.');
        sender.sendMessage(shownEvents.toArray(new String[shownEvents.size()]));
        sender.sendMessage((Object)ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        return true;
    }

    static {
        shownEvents = new ArrayList<String>();
        HHMMA = DateTimeFormatter.ofPattern("h:mma");
    }
}

