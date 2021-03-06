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

import com.customhcf.hcf.Utils.ConfigurationService;
import com.customhcf.hcf.HCF;
import com.customhcf.hcf.faction.FactionMember;
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

public class FactionWithdrawArgument
extends CommandArgument {
    private static final ImmutableList<String> COMPLETIONS = ImmutableList.of("all");
    private final HCF plugin;

    public FactionWithdrawArgument(HCF plugin) {
        super("withdraw", "Withdraws money from the faction balance.", new String[]{"w"});
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName() + " <all|amount>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Integer amount;
        if (!(sender instanceof Player)) {
            sender.sendMessage((Object)ChatColor.RED + "Only players can update the faction balance.");
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
        FactionMember factionMember = playerFaction.getMember(uuid);
        if (factionMember.getRole() == Role.MEMBER) {
            sender.sendMessage((Object)ChatColor.RED + "You must be a faction officer to withdraw money.");
            return true;
        }
        int factionBalance = playerFaction.getBalance();
        if (args[1].equalsIgnoreCase("all")) {
            amount = factionBalance;
        } else {
            amount = Ints.tryParse((String)args[1]);
            if (amount == null) {
                sender.sendMessage((Object)ChatColor.RED + "Error: '" + args[1] + "' is not a valid number.");
                return true;
            }
        }
        if (amount <= 0) {
            sender.sendMessage((Object)ChatColor.RED + "Amount must be positive.");
            return true;
        }
        if (amount > factionBalance) {
            sender.sendMessage((Object)ChatColor.RED + "Your faction need at least " + '$' + JavaUtils.format((Number)amount) + " to do this, whilst it only has " + '$' + JavaUtils.format((Number)factionBalance) + '.');
            return true;
        }
        this.plugin.getEconomyManager().addBalance(uuid, amount);
        playerFaction.setBalance(factionBalance - amount);
        playerFaction.broadcast((Object)ConfigurationService.TEAMMATE_COLOUR + factionMember.getRole().getAstrix() + sender.getName() + (Object)ChatColor.YELLOW + " has withdrew " + (Object)ChatColor.BOLD + '$' + JavaUtils.format((Number)amount) + (Object)ChatColor.YELLOW + " from the faction balance.");
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 2 ? COMPLETIONS : Collections.emptyList();
    }
}

