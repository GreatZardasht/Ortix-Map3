/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.util.command.CommandArgument
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 */
package com.customhcf.hcf.kothgame.koth.argument;

import com.customhcf.util.command.CommandArgument;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class KothShowArgument
extends CommandArgument {
    public KothShowArgument() {
        super("show", "View the information on a koth");
    }

    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args[1].isEmpty()) {
            sender.sendMessage((Object)ChatColor.RED + "FAIL: No koth");
            return true;
        }
        Bukkit.dispatchCommand((CommandSender)sender, (String)("f who " + args[1]));
        return true;
    }
}

