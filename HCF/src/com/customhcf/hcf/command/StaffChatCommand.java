package com.customhcf.hcf.command;

import com.customhcf.hcf.Utils.Lang;
import com.customhcf.hcf.chat.ChatHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffChatCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label , String[] args) {
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("staffchat")) {
            if (ChatHandler.sc.contains(player)) {
                ChatHandler.sc.remove(player);
                player.sendMessage(Lang.OUTSTAFFCHAT.toString());
                return true;
            } else {
                ChatHandler.sc.add(player);
                player.sendMessage(Lang.INSTAFFCHAT.toString());
                return true;
            }
        }
        return false;
    }
}