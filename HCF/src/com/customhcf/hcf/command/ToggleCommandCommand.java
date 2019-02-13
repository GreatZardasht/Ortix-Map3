package com.customhcf.hcf.command;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Created by nyang on 3/17/2017.
 */
public class ToggleCommandCommand implements CommandExecutor {

    public static ArrayList<Player> commandToggle = new ArrayList<>();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("commandtoggle")) {
            if (commandToggle.contains(player)) {
                commandToggle.remove(player);
                player.sendMessage(ChatColor.RED + "You are now UN-able to see all commands done by staffs!");
                return true;
            } else {
                commandToggle.add(player);
                player.sendMessage(ChatColor.GREEN + "You are now able to see all commands done by staffs!");
                return true;
            }
        }
        return false;
    }
}
