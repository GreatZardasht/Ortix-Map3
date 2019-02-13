package com.customhcf.hcf.listener;

import com.customhcf.util.chat.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * Created by nyang on 2/6/2017.
 */
public class CrashFixListener implements Listener {

    @EventHandler
    public void onCrash(PlayerCommandPreprocessEvent event) {

        String lc = event.getMessage().toLowerCase();
        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
            if (online.hasPermission("crashblock.notify")) {
            if (lc.startsWith("//calc")
                    || (lc.startsWith("//eval")
                    || (lc.startsWith("//evaluate")
                    || (lc.startsWith("//solve")
                    || (lc.startsWith("//calculate")
                    || (lc.startsWith("/worldedit:/"))))))) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "Nice Try!");
                new Text(ChatColor.DARK_RED + "[ALERT] " + event.getPlayer().getName() + " is trying to crash the server!").setHoverText(ChatColor.GOLD.toString() + ChatColor.BOLD + "Command Ran: " + lc.toString()).send(online);
            }
            }
        }
    }
}