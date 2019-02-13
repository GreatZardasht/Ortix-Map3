/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 */
package com.customhcf.base.kit.argument;

import com.customhcf.base.BasePlugin;
import com.customhcf.base.kit.Kit;
import com.customhcf.base.kit.KitManager;
import com.customhcf.util.command.CommandArgument;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class KitSetDescriptionArgument
extends CommandArgument {
    private final BasePlugin plugin;

    public KitSetDescriptionArgument(BasePlugin plugin) {
        super("setdescription", "Sets the description of a kit");
        this.plugin = plugin;
        this.aliases = new String[]{"setdesc"};
        this.permission = "base.command.kit.argument." + this.getName();
    }

    @Override
    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName() + " <kitName> <none|description>";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage((Object)ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        Kit kit = this.plugin.getKitManager().getKit(args[1]);
        if (kit == null) {
            sender.sendMessage((Object)ChatColor.RED + "There is not a kit named " + args[1] + '.');
            return true;
        }
        if (args[2].equalsIgnoreCase("none") || args[2].equalsIgnoreCase("null")) {
            kit.setDescription(null);
            sender.sendMessage((Object)ChatColor.YELLOW + "Removed description of kit " + kit.getDisplayName() + '.');
            return true;
        }
        String description = ChatColor.translateAlternateColorCodes((char)'&', (String)StringUtils.join((Object[])args, (char)' ', (int)2, (int)args.length));
        kit.setDescription(description);
        sender.sendMessage((Object)ChatColor.YELLOW + "Set description of kit " + kit.getDisplayName() + " to " + description + '.');
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
