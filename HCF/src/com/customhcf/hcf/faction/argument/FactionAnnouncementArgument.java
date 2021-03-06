/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.util.command.CommandArgument
 *  com.google.common.collect.ImmutableList
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.customhcf.hcf.faction.argument;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.faction.FactionManager;
import com.customhcf.hcf.faction.FactionMember;
import com.customhcf.hcf.faction.struct.Role;
import com.customhcf.hcf.faction.type.PlayerFaction;
import com.customhcf.util.command.CommandArgument;
import com.google.common.collect.ImmutableList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionAnnouncementArgument
extends CommandArgument {
    private final HCF plugin;
    private static final ImmutableList<String> CLEAR_LIST = ImmutableList.of("clear");

    public FactionAnnouncementArgument(HCF plugin) {
        super("announcement", "Set your faction announcement.", new String[]{"announce"});
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName() + " <newAnnouncement>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage((Object)ChatColor.RED + "This command is only executable by players.");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage((Object)ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        Player player = (Player)sender;
        PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
        if (playerFaction == null) {
            sender.sendMessage((Object)ChatColor.RED + "You are not in a faction.");
            return true;
        }
        if (playerFaction.getMember(player.getUniqueId()).getRole() == Role.MEMBER) {
            sender.sendMessage((Object)ChatColor.RED + "You must be a officer to edit the faction announcement.");
            return true;
        }
        String oldAnnouncement = playerFaction.getAnnouncement();
        String newAnnouncement = args[1].equalsIgnoreCase("clear") || args[1].equalsIgnoreCase("none") || args[1].equalsIgnoreCase("remove") ? null : StringUtils.join((Object[])args, (char)' ', (int)1, (int)args.length);
        if (oldAnnouncement == null && newAnnouncement == null) {
            sender.sendMessage((Object)ChatColor.RED + "Your factions' announcement is already unset.");
            return true;
        }
        if (oldAnnouncement != null && newAnnouncement != null && oldAnnouncement.equals(newAnnouncement)) {
            sender.sendMessage((Object)ChatColor.RED + "Your factions' announcement is already " + newAnnouncement + '.');
            return true;
        }
        playerFaction.setAnnouncement(newAnnouncement);
        if (newAnnouncement == null) {
            playerFaction.broadcast((Object)ChatColor.AQUA + sender.getName() + (Object)ChatColor.YELLOW + " has cleared the factions' announcement.");
            return true;
        }
        playerFaction.broadcast((Object)ChatColor.GREEN + player.getName() + (Object)ChatColor.YELLOW + " has updated the factions' announcement from " + (Object)ChatColor.GREEN + (oldAnnouncement != null ? oldAnnouncement : "none") + (Object)ChatColor.YELLOW + " to " + (Object)ChatColor.GREEN + newAnnouncement + (Object)ChatColor.YELLOW + '.');
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return Collections.emptyList();
        }
        if (args.length == 2) {
            return CLEAR_LIST;
        }
        return Collections.emptyList();
    }
}

