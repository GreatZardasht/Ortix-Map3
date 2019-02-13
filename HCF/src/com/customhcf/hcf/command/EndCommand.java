package com.customhcf.hcf.command;

import com.customhcf.hcf.HCF;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by nyang on 3/25/2017.
 */
public class EndCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        Player p = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("setendexit")) {
            HCF.getPlugin().getConfig().set("endexit.world", p.getLocation().getWorld().getName());
            HCF.getPlugin().getConfig().set("endexit.x", p.getLocation().getX());
            HCF.getPlugin().getConfig().set("endexit.y", p.getLocation().getY());
            HCF.getPlugin().getConfig().set("endexit.z", p.getLocation().getZ());
            HCF.getPlugin().saveConfig();
            p.sendMessage(ChatColor.GREEN + "End Exit set!");
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("setendentrance")) {
            HCF.getPlugin().getConfig().set("endentrance.world", p.getLocation().getWorld().getName());
            HCF.getPlugin().getConfig().set("endentrance.x", p.getLocation().getX());
            HCF.getPlugin().getConfig().set("endentrance.y", p.getLocation().getY());
            HCF.getPlugin().getConfig().set("endentrance.z", p.getLocation().getZ());
            HCF.getPlugin().saveConfig();
            p.sendMessage(ChatColor.GREEN + "End Entrance set!");
            return true;
        }

        return true;
    }
}
