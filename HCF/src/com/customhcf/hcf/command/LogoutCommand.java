/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.TabCompleter
 *  org.bukkit.entity.Player
 */
package com.customhcf.hcf.command;

import com.customhcf.hcf.HCF;
import java.util.Collections;
import java.util.List;

import com.customhcf.hcf.timer.type.LogoutTimer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class LogoutCommand
implements CommandExecutor,
TabCompleter {
    private final HCF plugin;

    public LogoutCommand(HCF plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
            return true;
        }
        Player player = (Player)sender;
        LogoutTimer logoutTimer = this.plugin.getTimerManager().logoutTimer;
        if (!logoutTimer.setCooldown(player, player.getUniqueId()))
        {
            sender.sendMessage(ChatColor.RED + "Your " + logoutTimer.getDisplayName() + ChatColor.RED + " timer is already active.");
            return true;
        }
        sender.sendMessage(ChatColor.RED + "Your " + logoutTimer.getDisplayName() + ChatColor.RED + " timer has started.");
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        return Collections.emptyList();
    }
}