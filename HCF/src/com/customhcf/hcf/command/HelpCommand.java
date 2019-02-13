package com.customhcf.hcf.command;

import com.customhcf.hcf.HCF;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

/**
 * Created by Nathan'PC on 12/19/2016.
 */
public class HelpCommand implements CommandExecutor, Listener {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String [] args) {
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("help") || cmd.getName().equalsIgnoreCase("?")) {
            helpMessage(player);
            return true;
        }
        return false;
    }
    public void helpMessage(Player player)
    {
        List<String> helpStrings = HCF.getPlugin().getConfig().getStringList("Server.Help");
        for (String list : helpStrings) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', list));
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (event.getPlayer().hasPlayedBefore()) {
            helpMessage(event.getPlayer());
        } else {
            helpMessage(event.getPlayer());
        }
    }


    /*
    Inventory Holder
    ---------------------

     */
}
