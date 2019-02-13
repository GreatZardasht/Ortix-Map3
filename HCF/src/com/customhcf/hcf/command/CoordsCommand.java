package com.customhcf.hcf.command;

import com.customhcf.hcf.HCF;
import com.sun.org.apache.xerces.internal.xs.StringList;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by Nathan'PC on 12/19/2016.
 */
public class CoordsCommand implements CommandExecutor {


    public boolean onCommand(CommandSender sender, Command cmd, String label ,String[] args) {
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("coords")) {
            List<String> list = HCF.getPlugin().getConfig().getStringList("Server.Coords");
            for (String coordsMessage : list) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', coordsMessage));
            }
            return true;
        }
        return false;
    }
}
