/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.util.command.CommandArgument
 *  com.google.common.primitives.Ints
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 */
package com.customhcf.hcf.kothgame.conquest;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.faction.FactionManager;
import com.customhcf.hcf.faction.type.Faction;
import com.customhcf.hcf.faction.type.PlayerFaction;
import com.customhcf.hcf.kothgame.EventType;
import com.customhcf.hcf.kothgame.tracker.ConquestTracker;
import com.customhcf.hcf.kothgame.tracker.EventTracker;
import com.customhcf.util.command.CommandArgument;
import com.google.common.primitives.Ints;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ConquestSetpointsArgument
extends CommandArgument {
    private final HCF plugin;

    public ConquestSetpointsArgument(HCF plugin) {
        super("setpoints", "Sets the points of a faction in the Conquest event", "hcf.command.conquest.argument.setpoints");
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName() + " <factionName> <amount>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage((Object)ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        Faction faction = this.plugin.getFactionManager().getFaction(args[1]);
        if (!(faction instanceof PlayerFaction)) {
            sender.sendMessage((Object)ChatColor.RED + "Faction " + args[1] + " is either not found or is not a player faction.");
            return true;
        }
        Integer amount = Ints.tryParse((String)args[2]);
        if (amount == null) {
            sender.sendMessage((Object)ChatColor.RED + "'" + args[2] + "' is not a number.");
            return true;
        }
        if (amount > 300) {
            sender.sendMessage((Object)ChatColor.RED + "Maximum points for Conquest is " + 300 + '.');
            return true;
        }
        PlayerFaction playerFaction = (PlayerFaction)faction;
        ((ConquestTracker)EventType.CONQUEST.getEventTracker()).setPoints(playerFaction, amount);
        Command.broadcastCommandMessage((CommandSender)sender, (String)((Object)ChatColor.YELLOW + "Set the points of faction " + playerFaction.getName() + " to " + amount + '.'));
        return true;
    }
}

