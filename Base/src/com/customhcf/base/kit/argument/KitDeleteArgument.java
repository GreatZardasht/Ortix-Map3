/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.event.Event
 */
package com.customhcf.base.kit.argument;

import com.customhcf.base.BasePlugin;
import com.customhcf.base.kit.Kit;
import com.customhcf.base.kit.KitManager;
import com.customhcf.base.kit.event.KitRemoveEvent;
import com.customhcf.util.command.CommandArgument;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;

public class KitDeleteArgument
extends CommandArgument {
    private final BasePlugin plugin;

    public KitDeleteArgument(BasePlugin plugin) {
        super("delete", "Deletes a kit");
        this.plugin = plugin;
        this.aliases = new String[]{"del", "remove"};
        this.permission = "command.kit.argument." + this.getName();
    }

    @Override
    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName() + " <kitName>";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage((Object)ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        Kit kit = this.plugin.getKitManager().getKit(args[1]);
        if (kit == null) {
            sender.sendMessage((Object)ChatColor.RED + "There is not a kit named " + args[1] + '.');
            return true;
        }
        KitRemoveEvent event = new KitRemoveEvent(kit);
        Bukkit.getPluginManager().callEvent((Event)event);
        if (event.isCancelled()) {
            return true;
        }
        this.plugin.getKitManager().removeKit(kit);
        sender.sendMessage((Object)ChatColor.GRAY + "Removed kit '" + args[1] + "'.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            return Collections.emptyList();
        }
        List<Kit> kits = this.plugin.getKitManager().getKits();
        ArrayList<String> results = new ArrayList<String>(kits.size());
        for (Kit kit : kits) {
            results.add(kit.getName());
        }
        return results;
    }
}

