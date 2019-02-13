/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.util.command.CommandArgument
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 */
package com.customhcf.hcf.faction.argument.staff;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.faction.FactionManager;
import com.customhcf.hcf.faction.type.Faction;
import com.customhcf.hcf.faction.type.PlayerFaction;
import com.customhcf.util.command.CommandArgument;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class FactionLockArgument
        extends CommandArgument {
    private final HCF plugin;

    public FactionLockArgument(HCF plugin) {
        super("lock", "Lock all factions");
        this.plugin = plugin;
        this.permission = "command.faction." + this.getName();
    }

    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName() + " [on|off]";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage((Object)ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        if (args[1].equalsIgnoreCase("on")) {
            Bukkit.broadcastMessage((String)((Object)ChatColor.RED + "All Factions are being locked!"));
            for (Faction faction : this.plugin.getFactionManager().getFactions()) {
                if (!(faction instanceof PlayerFaction)) continue;
                faction.setLocked(true);
            }
        }
        if (args[1].equalsIgnoreCase("off")) {
            Bukkit.broadcastMessage((String)((Object)ChatColor.GREEN + "All Factions are being un-locked!"));
            for (Faction faction : this.plugin.getFactionManager().getFactions()) {
                if (!(faction instanceof PlayerFaction)) continue;
                faction.setLocked(false);
            }
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 2 ? null : Collections.emptyList();
    }
}

