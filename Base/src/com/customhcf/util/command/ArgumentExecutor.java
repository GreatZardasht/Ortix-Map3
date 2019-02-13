/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  org.apache.commons.lang.WordUtils
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.TabCompleter
 */
package com.customhcf.util.command;

import com.customhcf.util.BukkitUtils;
import com.customhcf.util.chat.ClickAction;
import com.customhcf.util.chat.Text;
import com.customhcf.util.command.CommandArgument;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public abstract class ArgumentExecutor
implements CommandExecutor,
TabCompleter {
    protected final List<CommandArgument> arguments = new ArrayList<CommandArgument>();
    protected final String label;

    public ArgumentExecutor(String label) {
        this.label = label;
    }

    public boolean containsArgument(CommandArgument argument) {
        return this.arguments.contains(argument);
    }

    public void addArgument(CommandArgument argument) {
        this.arguments.add(argument);
    }

    public void removeArgument(CommandArgument argument) {
        this.arguments.remove(argument);
    }

    public CommandArgument getArgument(String id) {
        for (CommandArgument argument : this.arguments) {
            String name = argument.getName();
            if (!name.equalsIgnoreCase(id) && !Arrays.asList(argument.getAliases()).contains(id.toLowerCase())) continue;
            return argument;
        }
        return null;
    }

    public String getLabel() {
        return this.label;
    }

    public List<CommandArgument> getArguments() {
        return ImmutableList.copyOf(this.arguments);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String permission2;
        if (args.length < 1) {
            sender.sendMessage(ChatColor.AQUA.toString() + ChatColor.STRIKETHROUGH + "--------- [ "+ ChatColor.AQUA +WordUtils.capitalizeFully(label + " Help " + ChatColor.AQUA.toString() + ChatColor.STRIKETHROUGH + "] ---------"));
            for (CommandArgument argument : this.arguments) {
                String permission = argument.getPermission();
                if (permission != null && !sender.hasPermission(permission)) continue;
                new Text(ChatColor.GOLD + argument.getUsage(label) + (Object)ChatColor.GRAY ).send(sender);
            }
            return true;
        }
        CommandArgument argument2 = this.getArgument(args[0]);
        String string = permission2 = argument2 == null ? null : argument2.getPermission();
        if (argument2 == null || permission2 != null && !sender.hasPermission(permission2)) {
            sender.sendMessage((Object)ChatColor.RED + WordUtils.capitalizeFully((String)this.label) + " sub-command " + args[0] + " not found.");
            return true;
        }
        argument2.onCommand(sender, command, label, args);
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List results = new ArrayList<String>();
        if (args.length < 2) {
            for (CommandArgument argument : this.arguments) {
                String permission = argument.getPermission();
                if (permission != null && !sender.hasPermission(permission)) continue;
                results.add(argument.getName());
            }
        } else {
            CommandArgument argument2 = this.getArgument(args[0]);
            if (argument2 == null) {
                return results;
            }
            String permission2 = argument2.getPermission();
            if ((permission2 == null || sender.hasPermission(permission2)) && (results = argument2.onTabComplete(sender, command, label, args)) == null) {
                return null;
            }
        }
        return BukkitUtils.getCompletions(args, results);
    }
}

