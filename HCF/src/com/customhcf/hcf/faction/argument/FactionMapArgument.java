/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.util.command.CommandArgument
 *  com.google.common.base.Enums
 *  com.google.common.base.Predicate
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.customhcf.hcf.faction.argument;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.faction.LandMap;
import com.customhcf.hcf.user.FactionUser;
import com.customhcf.hcf.user.UserManager;
import com.customhcf.hcf.visualise.VisualBlock;
import com.customhcf.hcf.visualise.VisualType;
import com.customhcf.hcf.visualise.VisualiseHandler;
import com.customhcf.util.command.CommandArgument;
import com.google.common.base.Enums;
import com.google.common.base.Predicate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionMapArgument
extends CommandArgument {
    private final HCF plugin;

    public FactionMapArgument(HCF plugin) {
        super("map", "View all claims around your chunk.");
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName() + " [factionName]";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean newShowingMap;
        VisualType visualType;
        if (!(sender instanceof Player)) {
            sender.sendMessage((Object)ChatColor.RED + "This command is only executable by players.");
            return true;
        }
        Player player = (Player)sender;
        UUID uuid = player.getUniqueId();
        FactionUser factionUser = this.plugin.getUserManager().getUser(uuid);
        if (args.length <= 1) {
            visualType = VisualType.CLAIM_MAP;
        } else {
            visualType = (VisualType)((Object)Enums.getIfPresent((Class)VisualType.class, (String)args[1]).orNull());
            if (visualType == null) {
                player.sendMessage((Object)ChatColor.RED + "Visual type " + args[1] + " not found.");
                return true;
            }
        }
        boolean bl = newShowingMap = !factionUser.isShowClaimMap();
        if (newShowingMap) {
            if (!LandMap.updateMap(player, this.plugin, visualType, true)) {
                return true;
            }
        } else {
            this.plugin.getVisualiseHandler().clearVisualBlocks(player, visualType, null);
            sender.sendMessage((Object)ChatColor.RED + "Claim pillars are no longer shown.");
        }
        factionUser.setShowClaimMap(newShowingMap);
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2 || !(sender instanceof Player)) {
            return Collections.emptyList();
        }
        VisualType[] values = VisualType.values();
        ArrayList<String> results = new ArrayList<String>(values.length);
        for (VisualType visualType : values) {
            results.add(visualType.name());
        }
        return results;
    }
}

