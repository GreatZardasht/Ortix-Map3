/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.util.JavaUtils
 *  com.customhcf.util.command.CommandArgument
 *  com.google.common.collect.ImmutableList
 *  com.google.common.primitives.Ints
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.customhcf.hcf.faction.argument;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.balance.EconomyManager;
import com.customhcf.hcf.faction.FactionManager;
import com.customhcf.hcf.faction.FactionMember;
import com.customhcf.hcf.faction.struct.Relation;
import com.customhcf.hcf.faction.struct.Role;
import com.customhcf.hcf.faction.type.PlayerFaction;
import com.customhcf.util.JavaUtils;
import com.customhcf.util.command.CommandArgument;
import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionDepositArgument
extends CommandArgument {
    private static final ImmutableList<String> COMPLETIONS = ImmutableList.of("all");
    private final HCF plugin;

    public FactionDepositArgument(HCF plugin) {
        super("deposit", "Deposits money to the faction balance.", new String[]{"d"});
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName() + " <all|amount>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Integer amount;
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
        UUID uuid = player.getUniqueId();
        int playerBalance = this.plugin.getEconomyManager().getBalance(uuid);
        if (args[1].equalsIgnoreCase("all")) {
            amount = playerBalance;
        } else {
            amount = Ints.tryParse((String)args[1]);
            if (amount == null) {
                sender.sendMessage((Object)ChatColor.RED + "'" + args[1] + "' is not a valid number.");
                return true;
            }
        }
        if (amount <= 0) {
            sender.sendMessage((Object)ChatColor.RED + "Amount must be positive.");
            return true;
        }
        if (playerBalance < amount) {
            sender.sendMessage((Object)ChatColor.RED + "You need at least " + '$' + JavaUtils.format((Number)amount) + " to do this, you only have " + '$' + JavaUtils.format((Number)playerBalance) + '.');
            return true;
        }
        this.plugin.getEconomyManager().subtractBalance(uuid, amount);
        playerFaction.setBalance(playerFaction.getBalance() + amount);
        playerFaction.broadcast((Object)Relation.MEMBER.toChatColour() + playerFaction.getMember(player).getRole().getAstrix() + sender.getName() + (Object)ChatColor.YELLOW + " has deposited " + (Object)ChatColor.GREEN + '$' + JavaUtils.format((Number)amount) + (Object)ChatColor.YELLOW + " into the faction balance.");
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 2 ? COMPLETIONS : Collections.emptyList();
    }
}

