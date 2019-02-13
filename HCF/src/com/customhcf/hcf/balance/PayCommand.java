/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.base.BaseConstants
 *  com.google.common.primitives.Ints
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.TabCompleter
 *  org.bukkit.entity.Player
 */
package com.customhcf.hcf.balance;

import com.customhcf.base.BaseConstants;
import com.customhcf.hcf.HCF;
import com.customhcf.hcf.Utils.Lang;
import com.customhcf.hcf.balance.EconomyManager;
import com.google.common.primitives.Ints;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class PayCommand
implements CommandExecutor,
TabCompleter {
    private final HCF plugin;

    public PayCommand(HCF plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        int senderBalance;
        if (args.length < 2) {
            sender.sendMessage((Object)ChatColor.RED + "Usage: /" + label + " <playerName> <amount>");
            return true;
        }
        Integer amount = Ints.tryParse((String)args[1]);
        if (amount == null) {
            sender.sendMessage((Object)ChatColor.RED + "'" + args[1] + "' is not a valid number.");
            return true;
        }
        if (amount <= 0) {
            sender.sendMessage((Object)ChatColor.RED + "You must send money in positive quantities.");
            return true;
        }
        Player senderPlayer = (Player)sender;
        int n = senderBalance = senderPlayer != null ? this.plugin.getEconomyManager().getBalance(senderPlayer.getUniqueId()) : 1024;
        if (senderBalance < amount) {
            sender.sendMessage((Object)ChatColor.RED + "You do not have that much money, you have: " + (Object)ChatColor.GREEN + senderBalance);
            return true;
        }
        OfflinePlayer target = Bukkit.getOfflinePlayer((String)args[0]);
        if (sender.equals((Object)target)) {
            sender.sendMessage((Object)ChatColor.RED + "You cannot send money to yourself.");
            return true;
        }
        Player targetPlayer = target.getPlayer();
        if (!target.hasPlayedBefore() && targetPlayer == null) {
            sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
            return true;
        }
        if (targetPlayer == null) {
            return false;
        }
        if (senderPlayer != null) {
            this.plugin.getEconomyManager().subtractBalance(senderPlayer.getUniqueId(), amount);
        }
        this.plugin.getEconomyManager().addBalance(targetPlayer.getUniqueId(), amount);
        targetPlayer.sendMessage(Lang.ECONOMY_PAY_TARGET.toString().replaceAll("%player%", sender.getName()).replaceAll("%amount%", amount.toString()));
        sender.sendMessage(Lang.ECONOMY_PAY_SENDER.toString().replaceAll("%player%", targetPlayer.getName()).replaceAll("%amount%", amount.toString()));
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? null : Collections.emptyList();
    }
}

