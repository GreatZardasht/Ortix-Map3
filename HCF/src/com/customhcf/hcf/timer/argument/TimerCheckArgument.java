/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.util.command.CommandArgument
 *  org.apache.commons.lang3.time.DurationFormatUtils
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package com.customhcf.hcf.timer.argument;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.Utils.UUIDFetcher;
import com.customhcf.hcf.timer.PlayerTimer;
import com.customhcf.hcf.timer.Timer;
import com.customhcf.util.command.CommandArgument;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TimerCheckArgument
extends CommandArgument {
    private final HCF plugin;

    public TimerCheckArgument(HCF plugin) {
        super("check", "Check remaining timer time");
        this.plugin = plugin;
        this.permission = "hcf.command.timer.argument" + this.getName();
    }

    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName() + " <timerName> <playerName>";
    }

    public boolean onCommand(final CommandSender sender, Command command, String label, final String[] args) {
        if (args.length < 3) {
            sender.sendMessage((Object)ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        PlayerTimer temporaryTimer = null;
        for (Timer timer : this.plugin.getTimerManager().getTimers()) {
            if (!(timer instanceof PlayerTimer) || !timer.getName().equalsIgnoreCase(args[1])) continue;
            temporaryTimer = (PlayerTimer)timer;
            break;
        }
        if (temporaryTimer == null) {
            sender.sendMessage((Object)ChatColor.RED + "Timer '" + args[1] + "' not found.");
            return true;
        }
        final PlayerTimer playerTimer = temporaryTimer;
        new BukkitRunnable(){

            public void run() {
                UUID uuid;
                try {
                    uuid = UUIDFetcher.getUUIDOf(args[2]);
                }
                catch (Exception ex) {
                    sender.sendMessage((Object)ChatColor.GOLD + "Player '" + (Object)ChatColor.WHITE + args[2] + (Object)ChatColor.GOLD + "' not found.");
                    return;
                }
                long remaining = playerTimer.getRemaining(uuid);
                sender.sendMessage((Object)ChatColor.YELLOW + args[2] + " has timer " + playerTimer.getName() + " for another " + DurationFormatUtils.formatDurationWords((long)remaining, (boolean)true, (boolean)true));
            }
        }.runTaskAsynchronously((Plugin)this.plugin);
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 2 ? null : Collections.emptyList();
    }

}

