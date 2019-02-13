/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  org.apache.commons.lang.time.DurationFormatUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.AsyncPlayerChatEvent
 */
package com.customhcf.base.listener;

import com.customhcf.base.BasePlugin;
import com.customhcf.base.ServerHandler;
import com.customhcf.base.event.PlayerMessageEvent;
import com.customhcf.base.user.BaseUser;
import com.customhcf.base.user.ServerParticipator;
import com.customhcf.base.user.UserManager;
import com.customhcf.util.BukkitUtils;
import com.google.common.collect.Sets;

import java.util.*;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.permissions.Permissible;

public class ChatListener
implements Listener {
    private static final String MESSAGE_SPY_FORMAT = (Object)ChatColor.GRAY + "[" + (Object)ChatColor.AQUA + "SS: " + (Object)ChatColor.YELLOW + "%1$s" + (Object)ChatColor.GRAY + " -> " + (Object)ChatColor.YELLOW + "%2$s" + (Object)ChatColor.GRAY + "] %3$s";
    private static final String STAFF_CHAT_NOTIFY = "command.staffchat";
    private static final String SLOWED_CHAT_BYPASS = "slowchat.bypass";
    private static final String TOGGLED_CHAT_BYPASS = "disablechat.bypass";
    private static final long AUTO_IDLE_TIME = TimeUnit.MINUTES.toMillis(5);
    private final BasePlugin plugin;

    public ChatListener(BasePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        long remainingChatDisabled;
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        String name = player.getName();
        BaseUser baseUser = this.plugin.getUserManager().getUser(uuid);
        Iterator iterator = event.getRecipients().iterator();
        while (iterator.hasNext()) {
            Player target = (Player)iterator.next();
            BaseUser targetUser = this.plugin.getUserManager().getUser(target.getUniqueId());
            if (baseUser.isInStaffChat() && !targetUser.isStaffChatVisible()) {
                iterator.remove();
                continue;
            }
            if (targetUser.getIgnoring().contains(name)) {
                iterator.remove();
                continue;
            }
            if (targetUser.isGlobalChatVisible()) continue;
            iterator.remove();
        }
        if (baseUser.isInStaffChat()) {
            final Set<CommandSender> staffChattable = Sets.newHashSet();
            for (final Permissible permissible : Bukkit.getServer().getOnlinePlayers()) {
                if (permissible.hasPermission("command.staffchat") && permissible instanceof CommandSender) {
                    staffChattable.add((CommandSender) permissible);
                }
            }
            if (staffChattable.contains(player) && baseUser.isInStaffChat()) {
                final String format = ChatColor.AQUA + String.format(Locale.ENGLISH, "%1$s" + ChatColor.GRAY + ": %2$s", player.getName(), event.getMessage());
                for (final CommandSender target2 : staffChattable) {
                    if (target2 instanceof Player) {
                        final Player targetPlayer = (Player)target2;
                        final BaseUser targetUser2 = this.plugin.getUserManager().getUser(targetPlayer.getUniqueId());
                        if (targetUser2.isStaffChatVisible()) {
                            target2.sendMessage(format);
                        }
                        else {
                            if (!target2.equals(player)) {
                                continue;
                            }
                            target2.sendMessage(ChatColor.RED + "Your message was sent, but you cannot see staff chat messages as your notifications are disabled: Use /togglesc.");
                        }
                    }
                }
                event.setCancelled(true);
                return;
            }
        }
        if ((remainingChatDisabled = this.plugin.getServerHandler().getRemainingChatDisabledMillis()) > 0 && !player.hasPermission("disablechat.bypass")) {
            event.setCancelled(true);
            player.sendMessage((Object)ChatColor.RED + "Global chat is currently disabled for another " + (Object)ChatColor.RED + DurationFormatUtils.formatDurationWords((long)remainingChatDisabled, (boolean)true, (boolean)true) + (Object)ChatColor.RED + '.');
            return;
        }
        long remainingChatSlowed = this.plugin.getServerHandler().getRemainingChatSlowedMillis();
        if (remainingChatSlowed > 0 && !player.hasPermission("slowchat.bypass")) {
            long speakTimeRemaining = baseUser.getLastSpeakTimeRemaining();
            if (speakTimeRemaining <= 0) {
                baseUser.updateLastSpeakTime();
                return;
            }
            event.setCancelled(true);
            long delayMillis = (long)this.plugin.getServerHandler().getChatSlowedDelay() * 1000;
            player.sendMessage((Object)ChatColor.RED + "Global chat is currently in slow mode with a " + (Object)ChatColor.GRAY + DurationFormatUtils.formatDurationWords((long)delayMillis, (boolean)true, (boolean)true) + (Object)ChatColor.RED + " delay for another " + (Object)ChatColor.GRAY + DurationFormatUtils.formatDurationWords((long)remainingChatSlowed, (boolean)true, (boolean)true) + (Object)ChatColor.RED + ". You spoke " + (Object)ChatColor.GRAY + DurationFormatUtils.formatDurationWords((long)(delayMillis - speakTimeRemaining), (boolean)true, (boolean)true) + (Object)ChatColor.RED + " ago, so you gotta wait another " + (Object)ChatColor.GRAY + DurationFormatUtils.formatDurationWords((long)speakTimeRemaining, (boolean)true, (boolean)true) + (Object)ChatColor.RED + '.');
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onPlayerPreMessage(PlayerMessageEvent event) {
        Player sender = event.getSender();
        Player recipient = event.getRecipient();
        UUID recipientUUID = recipient.getUniqueId();
        if (!sender.hasPermission("base.messaging.bypass")) {
            BaseUser recipientUser = this.plugin.getUserManager().getUser(recipientUUID);
            if (!recipientUser.isMessagesVisible() || recipientUser.getIgnoring().contains(sender.getName())) {
                event.setCancelled(true);
                sender.sendMessage((Object)ChatColor.RED + recipient.getName() + " has private messaging toggled.");
            }
            return;
        }
        ServerParticipator senderParticipator = this.plugin.getUserManager().getParticipator((CommandSender)sender);
        if (!senderParticipator.isMessagesVisible()) {
            event.setCancelled(true);
            sender.sendMessage((Object)ChatColor.RED + "You have private messages toggled.");
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPlayerMessage(PlayerMessageEvent event) {
        Player sender = event.getSender();
        Player recipient = event.getRecipient();
        String message = event.getMessage();
        if (BukkitUtils.getIdleTime(recipient) > AUTO_IDLE_TIME) {
            sender.sendMessage((Object)ChatColor.RED + recipient.getName() + " may not respond as their idle time is over " + DurationFormatUtils.formatDurationWords((long)AUTO_IDLE_TIME, (boolean)true, (boolean)true) + '.');
        }
        final UUID senderUUID = sender.getUniqueId();
        final String senderId = senderUUID.toString();
        final String recipientId = recipient.getUniqueId().toString();
        final Collection<CommandSender> recipients = new HashSet<CommandSender>(Arrays.asList(Bukkit.getOnlinePlayers()));
        recipients.remove(sender);
        recipients.remove(recipient);
        recipients.add(Bukkit.getConsoleSender());
        for (CommandSender target : recipients) {
            ServerParticipator participator = this.plugin.getUserManager().getParticipator(target);
            Set<String> messageSpying = participator.getMessageSpying();
            if (!messageSpying.contains("all") && !messageSpying.contains(recipientId) && !messageSpying.contains(senderId)) continue;
            target.sendMessage(String.format(Locale.ENGLISH, MESSAGE_SPY_FORMAT, sender.getName(), recipient.getName(), message));
        }
    }
}

