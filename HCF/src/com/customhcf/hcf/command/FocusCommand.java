package com.customhcf.hcf.command;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.Utils.Cooldowns;
import com.customhcf.hcf.faction.event.FactionFocusEvent;
import com.customhcf.hcf.faction.struct.Role;
import com.customhcf.hcf.faction.type.PlayerFaction;
import com.customhcf.util.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 * Created by Nathan'PC on 2/1/2017.
 */
public class FocusCommand
        implements CommandExecutor {
    public String getUsage(String label) {
        return (Object)ChatColor.RED + "Usage: /" + label + " <playerName>";
    }

    public boolean onCommand(CommandSender cs, Command command, String label, String[] args) {
        Player player = (Player)cs;
        if (args.length != 1) {
            cs.sendMessage(this.getUsage(label));
            return true;
        }

        if (Cooldowns.isOnCooldown("focus_cooldown", player)) {
            player.sendMessage(ChatColor.RED+ "You can re-focus in " + Cooldowns.getCooldownForPlayerInt("focus_cooldown", player) + "m");
            return true;
        }

        PlayerFaction playerFaction = HCF.getPlugin().getFactionManager().getPlayerFaction(player.getUniqueId());
        if (playerFaction == null) {
            player.sendMessage((Object)ChatColor.RED + "You must be in a faction!");
            return true;
        }
        if (playerFaction.getMember(player.getUniqueId()).getRole() == Role.MEMBER) {
            player.sendMessage((Object)ChatColor.RED + "You cannot focus a member of your faction!");
            return true;
        }
        if (Bukkit.getPlayer((String)args[0]) == null && !args[0].equalsIgnoreCase("none")) {
            player.sendMessage((Object)ChatColor.RED + this.getUsage(label));
            player.sendMessage((Object)ChatColor.RED + "Expected 'playerName' but got " + args[0]);
            return true;
        }
        if (args[0].equalsIgnoreCase("none")) {
            playerFaction.broadcast((Object)ChatColor.LIGHT_PURPLE + cs.getName() + (Object)ChatColor.YELLOW + " has removed the current focus!");
            Bukkit.getPluginManager().callEvent((Event)new FactionFocusEvent(playerFaction, null, playerFaction.getFocus()));
            return true;
        }
        Bukkit.getPluginManager().callEvent((Event)new FactionFocusEvent(playerFaction, Bukkit.getPlayer((String)args[0]), playerFaction.getFocus()));
        if (playerFaction.getFocus() == null) {
            playerFaction.broadcast((Object)net.md_5.bungee.api.ChatColor.LIGHT_PURPLE + cs.getName() + (Object)net.md_5.bungee.api.ChatColor.YELLOW + " has focused " + (Object)net.md_5.bungee.api.ChatColor.LIGHT_PURPLE + args[0]);
            Cooldowns.addCooldown("focus_cooldown", player, 300);
        } else {
            playerFaction.broadcast((Object)net.md_5.bungee.api.ChatColor.LIGHT_PURPLE + cs.getName() + (Object)net.md_5.bungee.api.ChatColor.YELLOW + " has removed the focus on " + (Object)net.md_5.bungee.api.ChatColor.LIGHT_PURPLE + BukkitUtils.offlinePlayerWithNameOrUUID((String)playerFaction.getFocus().toString()).getName() + (Object)net.md_5.bungee.api.ChatColor.YELLOW + " and has focused " + (Object)net.md_5.bungee.api.ChatColor.DARK_PURPLE + args[0]);
        }
        playerFaction.setFocus(Bukkit.getPlayer((String)args[0]).getUniqueId());
        return false;
    }
}
