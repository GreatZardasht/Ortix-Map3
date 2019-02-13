/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 */
package com.customhcf.base.command.module.essential;

import com.customhcf.base.command.BaseCommand;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class DonateCommand
extends BaseCommand {
    public DonateCommand() {
        super("donate", "Donates");
        this.setAliases(new String[]{"buy"});
        this.setUsage("/(command)]");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(ChatColor.GREEN.toString() + (Object)ChatColor.BOLD + "You can donate by " + (Object)ChatColor.GREEN + "using the command /shop" + (Object)ChatColor.YELLOW + ".");
        return true;
    }
}

