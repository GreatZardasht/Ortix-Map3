package com.customhcf.hcf.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Nathan'PC on 11/11/2016.
 */
public class OresCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player p = (Player) sender;
        if (command.getName().equalsIgnoreCase("ores")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.LIGHT_PURPLE.toString() + "Your Ore Stats:");
                sender.sendMessage(ChatColor.GREEN + "Emerald Mined: " + ChatColor.GRAY + ((Player) sender).getStatistic(Statistic.MINE_BLOCK, Material.EMERALD_ORE));
                sender.sendMessage(ChatColor.AQUA + "Diamonds Mined: " + ChatColor.GRAY + ((Player) sender).getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE));
                sender.sendMessage(ChatColor.GOLD + "Gold Mined: " + ChatColor.GRAY + ((Player) sender).getStatistic(Statistic.MINE_BLOCK, Material.GOLD_ORE));
                sender.sendMessage(ChatColor.RED + "Redstone Mined: " + ChatColor.GRAY + ((Player) sender).getStatistic(Statistic.MINE_BLOCK, Material.REDSTONE_ORE));
                sender.sendMessage(ChatColor.BLUE + "Lapis Mined: " + ChatColor.GRAY + ((Player) sender).getStatistic(Statistic.MINE_BLOCK, Material.LAPIS_ORE));
                sender.sendMessage(ChatColor.GRAY + "Iron Mined: " + ChatColor.GRAY + ((Player) sender).getStatistic(Statistic.MINE_BLOCK, Material.IRON_ORE));
                sender.sendMessage(ChatColor.DARK_GRAY + "Coal Mined: " + ChatColor.GRAY + ((Player) sender).getStatistic(Statistic.MINE_BLOCK, Material.COAL_ORE));
                return true;
            }
        }
        Player target = Bukkit.getServer().getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "That target is not online right now! Try again later.");
            return true;
        }
        sender.sendMessage(ChatColor.LIGHT_PURPLE.toString() + target.getName() + "'s Ore Stats:");
        sender.sendMessage(ChatColor.GREEN + "Emerald Mined: " + ChatColor.GRAY + ((Player) target).getStatistic(Statistic.MINE_BLOCK, Material.EMERALD_ORE));
        sender.sendMessage(ChatColor.AQUA + "Diamonds Mined: " + ChatColor.GRAY + ((Player) target).getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE));
        sender.sendMessage(ChatColor.GOLD + "Gold Mined: " + ChatColor.GRAY + ((Player) target).getStatistic(Statistic.MINE_BLOCK, Material.GOLD_ORE));
        sender.sendMessage(ChatColor.RED + "Redstone Mined: " + ChatColor.GRAY + ((Player) target).getStatistic(Statistic.MINE_BLOCK, Material.REDSTONE_ORE));
        sender.sendMessage(ChatColor.BLUE + "Lapis Mined: " + ChatColor.GRAY + ((Player) target).getStatistic(Statistic.MINE_BLOCK, Material.LAPIS_ORE));
        sender.sendMessage(ChatColor.GRAY + "Iron Mined: " + ChatColor.GRAY + ((Player) target).getStatistic(Statistic.MINE_BLOCK, Material.IRON_ORE));
        sender.sendMessage(ChatColor.DARK_GRAY + "Coal Mined: " + ChatColor.GRAY + ((Player) target).getStatistic(Statistic.MINE_BLOCK, Material.COAL_ORE));
        return true;
    }
}
