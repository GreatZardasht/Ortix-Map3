/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.customhcf.base.command.module.warp;

import com.customhcf.base.BasePlugin;
import com.customhcf.base.warp.Warp;
import com.customhcf.base.warp.WarpManager;
import com.customhcf.util.BukkitUtils;
import com.customhcf.util.command.CommandArgument;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpRemoveArgument
extends CommandArgument {
    private final BasePlugin plugin;

    public WarpRemoveArgument(BasePlugin plugin) {
        super("del", "Deletes a new server warp");
        this.plugin = plugin;
        this.aliases = new String[]{"delete", "remove", "unset"};
        this.permission = "command.warp.argument." + this.getName();
    }

    @Override
    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage((Object)ChatColor.RED + "Only players can delete warps.");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage((Object)ChatColor.RED + "Usage: /" + label + ' ' + this.getName() + " <warpName>");
            return true;
        }
        Warp warp = this.plugin.getWarpManager().getWarp(args[1]);
        if (warp == null) {
            sender.sendMessage((Object)ChatColor.RED + "There is not a warp named " + args[1] + '.');
            return true;
        }
        this.plugin.getWarpManager().removeWarp(warp);
        sender.sendMessage((Object)ChatColor.GRAY + "Removed global warp named " + warp.getName() + '.');
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            return Collections.emptyList();
        }
        Collection<Warp> warps = this.plugin.getWarpManager().getWarps();
        ArrayList<String> warpNames = new ArrayList<String>(warps.size());
        for (Warp warp : warps) {
            warpNames.add(warp.getName());
        }
        return BukkitUtils.getCompletions(args, warpNames);
    }
}

