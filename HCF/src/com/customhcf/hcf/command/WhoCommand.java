package com.customhcf.hcf.command;

import com.customhcf.hcf.HCF;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

import java.util.List;

/**
 * Created by nyang on 3/17/2017.
 */
public class WhoCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String [] args) {
        if (cmd.getName().equalsIgnoreCase("who")) {
            List<String> whoList = HCF.getPlugin().getConfig().getStringList("Server.List.Format");
            for(String list : whoList) {
                int playerCount = Bukkit.getServer().getOnlinePlayers().length;
                int maxPlayer = Bukkit.getServer().getMaxPlayers();
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', list.replaceAll("%playerCount%", String.valueOf(playerCount)).replaceAll("%maxPlayers%", String.valueOf(maxPlayer))));
            }
            return true;
        }
        return false;
    }
}
