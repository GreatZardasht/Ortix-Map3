package com.customhcf.hcf.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by nyang on 2/18/2017.
 */
public class FeedAllCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("feedall")) {
            for (Player all : Bukkit.getServer().getOnlinePlayers()) {
                all.setFoodLevel(20);
                player.sendMessage(ChatColor.YELLOW + "Everyone in the server had been fed!");
                return true;
            }
        }
        return false;

    }
}
