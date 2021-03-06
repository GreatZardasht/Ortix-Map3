/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 */
package com.customhcf.hcf.command;

import com.customhcf.hcf.HCF;

import java.util.Collections;
import java.util.List;

import com.customhcf.hcf.timer.type.SotwTimer;
import com.customhcf.util.BukkitUtils;
import com.customhcf.util.JavaUtils;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.util.com.google.common.collect.ImmutableList;
import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class SotwCommand implements CommandExecutor, TabCompleter
{
    private static final List<String> COMPLETIONS;
    private final HCF plugin;


    public SotwCommand(final HCF plugin) {
        super();
        this.plugin = plugin;
    }

    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("start")) {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.YELLOW + "Usage: /" + label + " " +  " start <time> - Make sure to use HH:MM:SS format.");
                    return true;
                }
                final long duration = JavaUtils.parse(args[1]);
                if (duration == -1L) {
                    sender.sendMessage(ChatColor.RED + "'" + args[0] + "' is an invalid duration.");
                    return true;
                }
                if (duration < 1000L) {
                    sender.sendMessage(ChatColor.YELLOW + "Sotw Protection must be atleast 20 ticks or the timer will not run.");
                    return true;
                }
                final SotwTimer.SotwRunnable sotwRunnable = this.plugin.getSotwTimer().getSotwRunnable();
                if (sotwRunnable != null) {
                    sender.sendMessage(ChatColor.YELLOW + "Sotw Protection is already running! To cancel use /sotw cancel");
                    return true;
                }
                this.plugin.getSotwTimer().start(duration);
                sender.sendMessage(ChatColor.YELLOW + "Sotw Protection is now starting at " + DurationFormatUtils.formatDurationWords(duration, true, true));
                return true;
            }
            else if (args[0].equalsIgnoreCase("end") || args[0].equalsIgnoreCase("cancel")) {
                if (this.plugin.getSotwTimer().cancel()) {
                    sender.sendMessage(ChatColor.YELLOW + "Sotw Protection has been canceled.");
                    return true;
                }
                sender.sendMessage(ChatColor.YELLOW + "There is no active Sotw Protection Timer. To start one use /sotw start");
                return true;
            }
        }
        sender.sendMessage(ChatColor.YELLOW + "Usage: /" + label + " " +  " start <time> - Make sure to use HH:MM:SS format.");
        return true;
    }

    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        return (args.length == 1) ? BukkitUtils.getCompletions(args, SotwCommand.COMPLETIONS) : Collections.emptyList();
    }

    static {
        COMPLETIONS = ImmutableList.of("start", "end");
    }
}