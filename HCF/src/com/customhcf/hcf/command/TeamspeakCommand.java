package com.customhcf.hcf.command;

import com.customhcf.hcf.Utils.Lang;
import com.customhcf.util.chat.ClickAction;
import com.customhcf.util.chat.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamspeakCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd , String label, String[] args) {
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("teamspeak")) {
            new Text(ChatColor.translateAlternateColorCodes('&', Lang.TEAMSPEAK.toString())).setClick(ClickAction.OPEN_URL, "http://www.teamspeak.com/downloads").send(player);
            return true;
        }
        return false;
    }
}
