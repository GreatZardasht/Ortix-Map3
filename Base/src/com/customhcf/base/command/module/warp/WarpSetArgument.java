/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.customhcf.base.command.module.warp;

import com.customhcf.base.BasePlugin;
import com.customhcf.base.warp.Warp;
import com.customhcf.base.warp.WarpManager;
import com.customhcf.util.command.CommandArgument;
import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpSetArgument
extends CommandArgument {
    private final BasePlugin plugin;

    public WarpSetArgument(BasePlugin plugin) {
        super("set", "Sets a new server warps");
        this.plugin = plugin;
        this.aliases = new String[]{"create", "make"};
        this.permission = "command.warp.argument." + this.getName();
    }

    @Override
    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage((Object)ChatColor.RED + "This command is only executable by players.");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage((Object)ChatColor.RED + "Usage: /" + label + ' ' + this.getName() + " <warpName>");
            return true;
        }
        if (this.plugin.getWarpManager().getWarp(args[1]) != null) {
            sender.sendMessage((Object)ChatColor.RED + "There is already a warp named " + args[1] + '.');
            return true;
        }
        Player player = (Player)sender;
        Location location = player.getLocation();
        Warp warp = new Warp(args[1], location);
        this.plugin.getWarpManager().createWarp(warp);
        sender.sendMessage((Object)ChatColor.GRAY + "Created a global warp named " + (Object)ChatColor.BLUE + warp.getName() + (Object)ChatColor.GRAY + " with permission " + (Object)ChatColor.BLUE + warp.getPermission() + (Object)ChatColor.GRAY + '.');
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}

