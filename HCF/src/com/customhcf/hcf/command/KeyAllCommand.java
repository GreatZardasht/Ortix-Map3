package com.customhcf.hcf.command;

import com.customhcf.hcf.HCF;
import com.customhcf.hcfold.crate.Key;
import com.google.common.primitives.Ints;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.io.File;
import java.util.Map;

/**
 * Created by nyang on 2/19/2017.
 */
public class KeyAllCommand implements CommandExecutor {


    public boolean onCommand(CommandSender sender, Command cmd, String label, String [] args) {
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("keyall")) {
            if (args.length == 0) {
                sender.sendMessage("/keyall <keyname>");
                return true;
            }

            player.sendMessage(ChatColor.GREEN + "Key " + args[0] + " has been given to all online players!");
            for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "key give " + online.getName() + " " + args[0]);
            }
        }
        return false;
    }
}
