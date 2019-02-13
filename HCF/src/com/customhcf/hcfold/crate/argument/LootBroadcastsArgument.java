/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.util.command.CommandArgument
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 */
package com.customhcf.hcfold.crate.argument;

import com.customhcf.hcf.Utils.ConfigurationService;
import com.customhcf.util.command.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class LootBroadcastsArgument
extends CommandArgument {
    public LootBroadcastsArgument() {
        super("broadcasts", "Toggle broadcasts for key announcements", new String[]{"togglealerts", "togglebroadcasts"});
        this.permission = "hcf.command.loot.argument." + this.getName();
    }

    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ConfigurationService.CRATE_BROADCASTS = !ConfigurationService.CRATE_BROADCASTS;
        boolean newBroadcasts = ConfigurationService.CRATE_BROADCASTS;
        sender.sendMessage((Object)ChatColor.GOLD + "Crate keys " + (newBroadcasts ? "now" : "no longer") + " broadcasts reward messages.");
        return true;
    }
}

