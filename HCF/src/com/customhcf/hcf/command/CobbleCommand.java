package com.customhcf.hcf.command;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.Utils.Lang;
import com.customhcf.hcf.classes.PvpClass;
import com.customhcf.hcf.classes.type.MinerClass;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

public class CobbleCommand implements CommandExecutor {

    public static ArrayList<String> cobbletoggle = new ArrayList();


    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if ((sender instanceof Player))
        {
            Player p = (Player)sender;
            if ((command.getName().equalsIgnoreCase("cobble")))
            {
                if (args.length == 0) {
                    if ((!cobbletoggle.contains(p.getName())))
                    {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.COBBLE_PICKUP_ON.toString()));
                        cobbletoggle.add(p.getName());
                    }
                    else if ((cobbletoggle.contains(p.getName())))
                    {
                        cobbletoggle.remove(p.getName());
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.COBBLE_PICKUP_OFF.toString()));
                    }
                }
            }
        }
        return false;
    }
}

