/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.util.JavaUtils
 *  com.customhcf.util.command.CommandArgument
 *  org.apache.commons.lang3.StringUtils
 *  org.apache.commons.lang3.time.DurationFormatUtils
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 */
package com.customhcf.hcf.kothgame.koth.argument;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.faction.FactionManager;
import com.customhcf.hcf.faction.type.Faction;
import com.customhcf.hcf.kothgame.CaptureZone;
import com.customhcf.hcf.kothgame.faction.KothFaction;
import com.customhcf.util.JavaUtils;
import com.customhcf.util.command.CommandArgument;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class KothSetCapDelayArgument
extends CommandArgument {
    private final HCF plugin;

    public KothSetCapDelayArgument(HCF plugin) {
        super("setcapdelay", "Sets the cap delay of a KOTH");
        this.plugin = plugin;
        this.aliases = new String[]{"setcapturedelay"};
        this.permission = "hcf.command.koth.argument." + this.getName();
    }

    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName() + " <kothName> <capDelay>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage((Object)ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        Faction faction = this.plugin.getFactionManager().getFaction(args[1]);
        if (faction == null || !(faction instanceof KothFaction)) {
            sender.sendMessage((Object)ChatColor.RED + "There is not a KOTH arena named '" + args[1] + "'.");
            return true;
        }
        long duration = JavaUtils.parse((String)StringUtils.join((Object[])args, (char)' ', (int)2, (int)args.length));
        if (duration == -1) {
            sender.sendMessage((Object)ChatColor.RED + "Invalid duration, use the correct format: 10m 1s");
            return true;
        }
        KothFaction kothFaction = (KothFaction)faction;
        CaptureZone captureZone = kothFaction.getCaptureZone();
        if (captureZone == null) {
            sender.sendMessage((Object)ChatColor.RED + kothFaction.getDisplayName(sender) + (Object)ChatColor.RED + " does not have a capture zone.");
            return true;
        }
        if (captureZone.isActive() && duration < captureZone.getRemainingCaptureMillis()) {
            captureZone.setRemainingCaptureMillis(duration);
        }
        captureZone.setDefaultCaptureMillis(duration);
        sender.sendMessage((Object)ChatColor.YELLOW + "Set the capture delay of KOTH arena " + (Object)ChatColor.WHITE + kothFaction.getDisplayName(sender) + (Object)ChatColor.YELLOW + " to " + (Object)ChatColor.WHITE + DurationFormatUtils.formatDurationWords((long)duration, (boolean)true, (boolean)true) + (Object)ChatColor.WHITE + '.');
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            return Collections.emptyList();
        }
        return this.plugin.getFactionManager().getFactions().stream().filter(faction -> faction instanceof KothFaction).map(Faction::getName).collect(Collectors.toList());
    }
}

