package com.customhcf.hcf.faction.argument;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.faction.FactionMember;
import com.customhcf.hcf.faction.struct.Relation;
import com.customhcf.hcf.faction.struct.Role;
import com.customhcf.hcf.faction.type.PlayerFaction;
import com.customhcf.util.command.CommandArgument;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by Nathan'PC on 1/16/2017.
 */
public class FactionCoLeaderArgument extends CommandArgument {
    private final HCF plugin;

    public FactionCoLeaderArgument(HCF plugin) {
        super("co", "Promotes a player to a coleader.");
        this.plugin = plugin;
        this.aliases = new String[]{ "coleader", "cl"};
    }

    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName() + " <playerName>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage((Object) ChatColor.RED + "Only players can set faction co leaders.");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage((Object)ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        Player player = (Player)sender;
        UUID uuid = player.getUniqueId();
        PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(uuid);
        if (playerFaction == null) {
            sender.sendMessage((Object)ChatColor.RED + "You are not in a faction.");
            return true;
        }
        if (playerFaction.getMember(uuid).getRole() != Role.LEADER) {
            sender.sendMessage((Object)ChatColor.RED + "You must be a faction leader to assign members or captains as a coleader.");
            return true;
        }
        FactionMember targetMember = playerFaction.getMember(args[1]);
        if (targetMember == null) {
            sender.sendMessage((Object)ChatColor.RED + "That player is not in your faction.");
            return true;
        }
        if (targetMember.getRole() != Role.CAPTAIN || targetMember.getRole() != Role.CO) {
            sender.sendMessage((Object)ChatColor.RED + "You can only assign coleader to members and captains, " + targetMember.getName() + " is a " + targetMember.getRole().getName() + '.');
            return true;
        }
        Role role = Role.CO;
        targetMember.setRole(role);
        playerFaction.broadcast((Object) Relation.MEMBER.toChatColour() + role.getAstrix() + targetMember.getName() + (Object)ChatColor.YELLOW + " has been assigned as a faction coleader.");
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2 || !(sender instanceof Player)) {
            return Collections.emptyList();
        }
        Player player = (Player)sender;
        PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
        if (playerFaction == null || playerFaction.getMember(player.getUniqueId()).getRole() != Role.LEADER || playerFaction.getMember(player.getUniqueId()).getRole() != Role.CO) {
            return Collections.emptyList();
        }
        ArrayList<String> results = new ArrayList<String>();
        for (Map.Entry<UUID, FactionMember> entry : playerFaction.getMembers().entrySet()) {
            OfflinePlayer target;
            String targetName;
            if (entry.getValue().getRole() != Role.MEMBER || (targetName = (target = Bukkit.getOfflinePlayer((UUID)entry.getKey())).getName()) == null) continue;
            results.add(targetName);
        }
        return results;
    }

}

