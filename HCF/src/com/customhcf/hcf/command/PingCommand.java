package com.customhcf.hcf.command;

import com.customhcf.hcf.HCF;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;

import java.util.List;

/**
 * Created by nyang on 3/17/2017.
 */
public class PingCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String [] args) {
        if (cmd.getName().equalsIgnoreCase("ping")) {
            List<String> pingList = HCF.getPlugin().getConfig().getStringList("Server.Ping.Format");
            int ping = ((CraftPlayer)sender).getPing();
            for (String list : pingList) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', list.replaceAll("%ping%", String.valueOf(ping))));
            }
            return true;
        }
        return false;
    }
}
