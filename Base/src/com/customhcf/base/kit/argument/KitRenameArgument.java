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
import com.customhcf.base.kit.event.KitRenameEvent;
import com.customhcf.util.command.CommandArgument;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;

public class KitRenameArgument
extends CommandArgument {
    private final BasePlugin plugin;

    public KitRenameArgument(BasePlugin plugin) {
        super("rename", "Renames a kit");
        this.plugin = plugin;
        this.permission = "base.command.kit.argument." + this.getName();
    }

    @Override
    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName() + " <kitName> <newKitName>";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage((Object)ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        Kit kit = this.plugin.getKitManager().getKit(args[2]);
        if (kit != null) {
            sender.sendMessage((Object)ChatColor.RED + "There is already a kit named " + kit.getName() + '.');
            return true;
        }
        kit = this.plugin.getKitManager().getKit(args[1]);
        if (kit == null) {
            sender.sendMessage((Object)ChatColor.RED + "There is not a kit named " + args[1] + '.');
            return true;
        }
        KitRenameEvent event = new KitRenameEvent(kit, kit.getName(), args[2]);
        Bukkit.getPluginManager().callEvent((Event)event);
        if (event.isCancelled()) {
            return true;
        }
        if (event.getOldName().equals(event.getNewName())) {
            sender.sendMessage((Object)ChatColor.RED + "This kit is already called " + event.getNewName() + '.');
            return true;
        }
        kit.setName(event.getNewName());
        sender.sendMessage((Object)ChatColor.AQUA + "Renamed kit " + event.getOldName() + " to " + event.getNewName() + '.');
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

