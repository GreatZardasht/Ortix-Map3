package com.customhcf.hcf.lives.argument.commands;

import com.customhcf.base.BaseConstants;
import com.customhcf.hcf.HCF;
import com.customhcf.util.command.CommandArgument;
import com.google.common.primitives.Ints;
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
public class LivesGiveCommand extends CommandArgument {
    private final HCF plugin;
    private static final String PERMISSION = "hcf.command.lives.argument.give.bypass";

    public LivesGiveCommand(HCF plugin) {
        super("give", "Help someone out by giving them live(s)");
        this.plugin = plugin;
        this.aliases = new String[]{"transfer", "send", "pay", "add"};
        this.permission = "hcf.command.lives.argument." + this.getName();
    }

    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName() + " <player> <amount>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage((Object) ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        Integer amount = Ints.tryParse((String)args[2]);
        if (amount == null) {
            sender.sendMessage((Object)ChatColor.RED + "'" + args[2] + "' is not a number.");
            return true;
        }
        if (amount <= 0) {
            sender.sendMessage((Object)ChatColor.RED + "The amount of lives must be positive.");
            return true;
        }
        OfflinePlayer target = Bukkit.getOfflinePlayer((String)args[1]);
        if (!target.hasPlayedBefore() && !target.isOnline()) {
            sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[1]));
            return true;
        }
        Player onlineTarget = target.getPlayer();
        if (!sender.hasPermission(PERMISSION)) {
            Player player = (Player)sender;
            int ownedLives = this.plugin.getDeathbanManager().getLives(player.getUniqueId());
            if (amount > ownedLives) {
                sender.sendMessage((Object)ChatColor.RED + "You tried to give " + target.getName() + ' ' + amount + " lives, but you only have " + ownedLives + '.');
                return true;
            }
            this.plugin.getDeathbanManager().takeLives(player.getUniqueId(), amount);
        }
        final int targetLives = this.plugin.getDeathbanManager().getLives(target.getUniqueId());
        this.plugin.getDeathbanManager().addLives(target.getUniqueId(), amount);
        sender.sendMessage((Object)ChatColor.YELLOW + "You have sent " + (Object)ChatColor.GOLD + target.getName() + (Object)ChatColor.YELLOW + ' ' + amount + ' ' + (amount > 1 ? "life" : "lives") + '.');
        if (onlineTarget != null) {
            onlineTarget.sendMessage((Object)ChatColor.GOLD + sender.getName() + (Object)ChatColor.YELLOW + " has sent you " + (Object)ChatColor.GOLD + amount + ' ' + (amount > 1 ? "life" : "lives") + '.');
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 2 ? null : Collections.emptyList();
    }
}