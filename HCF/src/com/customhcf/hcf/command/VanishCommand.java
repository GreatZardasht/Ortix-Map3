package com.customhcf.hcf.command;

import com.customhcf.hcf.Utils.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class VanishCommand implements CommandExecutor {

    public static ArrayList<Player> v = new ArrayList<>();

    public boolean onCommand(CommandSender sender, Command cmd , String label, String[] args) {
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("vanish")) {
            if (v.contains(player)) {
                v.remove(player);
                for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                    online.showPlayer(player);
                }
                sender.sendMessage(Lang.VANISHOFF.toString());
                return true;
            } else {
                v.add(player);
                for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                    online.hidePlayer(player);
                }
                sender.sendMessage(Lang.VANISHON.toString());
                return true;
            }
        }
        return false;
    }
}
