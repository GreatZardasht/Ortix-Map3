/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  net.minecraft.util.org.apache.commons.lang3.StringUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.customhcf.base.command.module.essential;

import com.customhcf.base.command.BaseCommand;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RequestCommand
extends BaseCommand {
    private final String RECIEVE_MESSAGE;

    public RequestCommand() {
        super("request", "Gets the staffs attention");
        this.setUsage("/(command) <Message>");
        this.setAliases(new String[]{"helpop"});
        this.RECIEVE_MESSAGE = "command.request.recieve";
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String s, String[] args) {
        if (args.length == 0) {
            cs.sendMessage(this.getUsage(s));
            return true;
        }
        String message = StringUtils.join((Object[])args, (char)' ');
        for (Player on : Bukkit.getServer().getOnlinePlayers()) {
            if (!on.hasPermission(this.RECIEVE_MESSAGE)) continue;
            on.sendMessage((Object)ChatColor.GRAY + "[Request] " + (Object)ChatColor.YELLOW + cs.getName() + (Object)ChatColor.GRAY + ": " + (Object)ChatColor.DARK_AQUA + message);
            return true;
        }
        return true;
    }
}

