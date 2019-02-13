/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 */
package com.customhcf.base.listener;

import com.customhcf.base.BasePlugin;
import com.customhcf.base.user.BaseUser;
import com.customhcf.base.user.UserManager;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener
implements Listener {
    private final BasePlugin plugin;

    public JoinListener(BasePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        BaseUser baseUser = this.plugin.getUserManager().getUser(uuid);
        for (Player player1 : Bukkit.getServer().getOnlinePlayers()) {
            if (!player1.hasPermission("command.staffchat")) continue;
            if (!baseUser.getNotes().isEmpty()) {
                player1.sendMessage((Object)ChatColor.YELLOW + player.getName() + (Object)ChatColor.BOLD + " has the following notes" + (Object)ChatColor.RED + '\u2193');
            }
            for (String notes : baseUser.getNotes()) {
                player1.sendMessage(notes);
            }
        }
        Integer value = baseUser.getAddressHistories().size();
        baseUser.tryLoggingName(player);
        baseUser.setMessagingSounds(true);
        baseUser.setMessagesVisible(true);
        baseUser.tryLoggingAddress(player.getAddress().getAddress().getHostAddress());
    }


}

