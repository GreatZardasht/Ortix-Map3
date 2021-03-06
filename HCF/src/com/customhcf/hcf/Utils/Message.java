/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.util.chat.Text
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.customhcf.hcf.Utils;

import com.customhcf.hcf.HCF;
import com.customhcf.util.chat.Text;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class Message {
    HCF plugin;
    private final HashMap<UUID, Long> messageDelay = new HashMap();

    public Message(HCF plugin) {
        this.plugin = plugin;
    }

    public void sendMessage(Player player, String message) {
        if (this.messageDelay.containsKey(player.getUniqueId())) {
            if (this.messageDelay.get(player.getUniqueId()) - System.currentTimeMillis() > 0) {
                return;
            }
            this.messageDelay.remove(player.getUniqueId());
        }
        this.messageDelay.putIfAbsent(player.getUniqueId(), System.currentTimeMillis() + 3000);
        player.sendMessage(message);
    }

    public void sendMessage(Player player, Text text) {
        if (this.messageDelay.containsKey(player.getUniqueId())) {
            if (this.messageDelay.get(player.getUniqueId()) - System.currentTimeMillis() > 0) {
                return;
            }
            this.messageDelay.remove(player.getUniqueId());
        }
        this.messageDelay.putIfAbsent(player.getUniqueId(), System.currentTimeMillis() + 3000);
        text.send((CommandSender)player);
    }
}

