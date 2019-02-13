package com.customhcf.hcf.lives.argument.commands;

import com.customhcf.hcf.HCF;
import com.customhcf.util.command.CommandArgument;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * Created by Nathan'PC on 12/28/2016.
 */
public class LivesCheckCommand extends CommandArgument
{
    private final HCF plugin;

    public LivesCheckCommand(final HCF plugin) {
        super("check", "Check Lives");
        this.plugin = plugin;
        this.permission = "hcf.command.lives.argument." + this.getName();
    }

    public String getUsage(final String label) {
        return '/' + label + ' ' + this.getName() + " <player>";
    }

    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        OfflinePlayer target;
        if (args.length > 1) {
            target = Bukkit.getOfflinePlayer(args[1]);
        }
        else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
                return true;
            }
            target = (OfflinePlayer)sender;
        }
        if (!target.hasPlayedBefore() && !target.isOnline()) {
            sender.sendMessage(ChatColor.GOLD + "Player '" + ChatColor.WHITE + args[1] + ChatColor.GOLD + "' not found.");
            return true;
        }
        final int targetLives = this.plugin.getDeathbanManager().getLives(target.getUniqueId());
        sender.sendMessage(ChatColor.YELLOW + target.getName() + ChatColor.YELLOW + " has " + ChatColor.LIGHT_PURPLE + targetLives + ChatColor.YELLOW + ' ' + ((targetLives == 1) ? "life" : "lives") + '.');
        return true;
    }

    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        return (args.length == 2) ? null : Collections.emptyList();
    }
}