package com.customhcf.hcf.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by nyang on 4/7/2017.
 */
public class PanicCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("panic")) {
            Bukkit.getServer().broadcast(" ", "panic.notify");
            Bukkit.getServer().broadcast(" ", "panic.notify");
            Bukkit.getServer().broadcast("§4§l" + player.getName() + " needs support now! (/panic)", "panic.notify");
            Bukkit.getServer().broadcast(" ", "panic.notify");
            Bukkit.getServer().broadcast(" ", "panic.notify");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "freeze " + player.getName());
            return true;
        }
        return false;
    }
}
