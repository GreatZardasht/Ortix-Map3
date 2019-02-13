/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.customhcf.base.command.module.chat;

import com.customhcf.base.BasePlugin;
import com.customhcf.base.command.BaseCommand;
import com.customhcf.base.user.BaseUser;
import com.customhcf.base.user.UserManager;

import java.util.ArrayList;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleMessagesCommand
extends BaseCommand {
    private final BasePlugin plugin;

    public static ArrayList<String> toggleMSG = new ArrayList<>();

    public  ToggleMessagesCommand(BasePlugin plugin) {
        super("togglepm", "Toggles private messages.");
        this.setAliases(new String[]{"stoppm", "pmoff"});
        this.setUsage("/(command)");
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage((Object)ChatColor.RED + "This command is only executable for players.");
            return true;
        }
        Player player = (Player)sender;
        BaseUser baseUser = this.plugin.getUserManager().getUser(player.getUniqueId());
        boolean newToggled = !baseUser.isMessagesVisible();
        baseUser.setMessagesVisible(newToggled);
        sender.sendMessage((Object)ChatColor.YELLOW + "You have turned private messages " + (newToggled ? new StringBuilder().append((Object)ChatColor.GREEN).append("on").toString() : new StringBuilder().append((Object)ChatColor.RED).append("off").toString()) + (Object)ChatColor.YELLOW + '.');
        return true;
    }
}

