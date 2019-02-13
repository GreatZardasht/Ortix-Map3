/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.google.common.cache.Cache
 *  com.google.common.cache.CacheBuilder
 *  com.google.common.collect.ImmutableSet
 *  com.google.common.collect.ImmutableSet$Builder
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.AsyncPlayerChatEvent
 *  ru.tehkode.permissions.bukkit.PermissionsEx
 */
package com.customhcf.hcf.listener;

import com.customhcf.base.BasePlugin;
import com.customhcf.base.command.module.essential.FreezeCommand;
import com.customhcf.base.user.BaseUser;
import com.customhcf.hcf.HCF;
import com.customhcf.hcf.chat.ChatHandler;
import com.customhcf.hcf.command.MuteChatCommand;
import com.customhcf.hcf.command.ToggleCommandCommand;
import com.customhcf.hcf.faction.FactionManager;
import com.customhcf.hcf.faction.FactionMember;
import com.customhcf.hcf.faction.event.FactionChatEvent;
import com.customhcf.hcf.faction.struct.ChatChannel;
import com.customhcf.hcf.faction.type.PlayerFaction;
import com.customhcf.hcfold.crate.Key;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class  ChatListener
implements Listener {
    private static final String DOUBLE_POST_BYPASS_PERMISSION = "hcf.doublepost.bypass";
    private static final Pattern PATTERN;
    private final ConcurrentMap<Object, Object> messageHistory;
    private final HCF plugin;

    public ChatListener(HCF plugin) {
        this.plugin = plugin;
        this.messageHistory = CacheBuilder.newBuilder().expireAfterWrite(2, TimeUnit.MINUTES).build().asMap();
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        Player player = event.getPlayer();
        String lastMessage = (String)this.messageHistory.get(player.getUniqueId());
        String cleanedMessage = PATTERN.matcher(message).replaceAll("");
        if (lastMessage != null && (message.equals(lastMessage) || StringUtils.getLevenshteinDistance((String)cleanedMessage, (String)lastMessage) <= 1) && !player.hasPermission("hcf.doublepost.bypass")) {
            player.sendMessage((Object)ChatColor.RED + "Double posting is prohibited.");
            event.setCancelled(true);
            return;
        }
        this.messageHistory.put(player.getUniqueId(), cleanedMessage);
        PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
        ChatChannel chatChannel = playerFaction == null ? ChatChannel.PUBLIC : playerFaction.getMember(player).getChatChannel();
        Set recipients = event.getRecipients();
        if (chatChannel == ChatChannel.FACTION || chatChannel == ChatChannel.ALLIANCE) {
            if (!this.isGlobalChannel(message)) {
                Set online = playerFaction.getOnlinePlayers();
                if (chatChannel == ChatChannel.ALLIANCE) {
                    List<PlayerFaction> allies = playerFaction.getAlliedFactions();
                    for (PlayerFaction ally : allies) {
                        online.addAll(ally.getOnlinePlayers());
                    }
                }
                recipients.retainAll(online);
                event.setFormat(chatChannel.getRawFormat(player));
                Bukkit.getPluginManager().callEvent((Event)new FactionChatEvent(true, playerFaction, player, chatChannel, recipients, event.getMessage()));
                return;
            }
            message = message.substring(1, message.length()).trim();
            event.setMessage(message);
        }
        boolean usingRecipientVersion = false;
        event.setCancelled(true);
        Boolean isTag = true;
        if (player.hasPermission("faction.removetag")) {
            isTag = true;
        }
        String rank = ChatColor.translateAlternateColorCodes((char)'&', (String)("&e" + PermissionsEx.getUser((Player)player).getPrefix())).replace("_", " ");
        String displayName = player.getDisplayName();
        displayName = rank + displayName;
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        if (message.toLowerCase().contains("nigger") || message.toLowerCase().contains("steal plugins") || message.toLowerCase().contains("take plugins") || message.toLowerCase().contains("kill yourself") || message.toLowerCase().contains("shit staff") || message.toLowerCase().contains("kys")) {
            for (Player on : Bukkit.getServer().getOnlinePlayers()) {
                if (!on.hasPermission("base.command.staffchat")) continue;
                on.sendMessage(ChatColor.RED + "[Filter] " + ChatColor.YELLOW + player.getName() + "'s message has been denied! \"" + event.getMessage() + "\"");
            }
            event.setCancelled(true);
            return;
        }
        String tag = playerFaction == null ? (Object)ChatColor.DARK_RED + "-" : playerFaction.getDisplayName((CommandSender)console);
        console.sendMessage((Object)ChatColor.DARK_GRAY + "[" + tag + (Object)ChatColor.DARK_GRAY + "] " + displayName + " " + (Object)ChatColor.GRAY + message);
        for (Player recipient : event.getRecipients()) {
            tag = playerFaction == null ? (Object)ChatColor.RED + "-" : playerFaction.getDisplayName((CommandSender)recipient);
            recipient.sendMessage((Object)ChatColor.DARK_GRAY + "[" + tag + (Object)ChatColor.DARK_GRAY + "] " + displayName + " " + (Object)ChatColor.GRAY + message);
        }
    }

    private boolean isGlobalChannel(String input) {
        int length = input.length();
        if (length <= 1 || !input.startsWith("!")) {
            return false;
        }
        for (int i = 1; i < length; ++i) {
            char character = input.charAt(i);
            if (character == ' ') {
                continue;
            }
            if (character != '/') break;
            return false;
        }
        return true;
    }

    static {
        ImmutableSet.Builder builder = ImmutableSet.builder();
        PATTERN = Pattern.compile("\\W");
    }



    @EventHandler
    public void onPluginCheck(PlayerCommandPreprocessEvent event) {

        String lc = event.getMessage().toLowerCase();

        if (lc.equalsIgnoreCase("/pl")
                || lc.contains("/plugins")
                || lc.contains("/about")
                || lc.contains("/bukkit:pl")
                || lc.contains("/bukkit:me")
                || lc.contains("/bukkit:?")
                || lc.contains("/bukkit:about")
                || lc.contains("/minecraft:me")
                || lc.contains("/minecraft:")
                || lc.contains("/me")
                || lc.contains("/MINECRAFT:")
                || lc.contains("/bukkit:")
                || lc.contains("/bukkit")
                || lc.contains("/ver")) {
            if (!event.getPlayer().isOp()) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "Not enough permissions!!");
            } else {
                return;
            }
        }
    }


    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (ChatHandler.sc.contains(player)) {
            event.setCancelled(true);
            for (Player staffs : Bukkit.getServer().getOnlinePlayers()) {
                if (staffs.hasPermission("messages.staffchat")) {
                    String staffChatFormat = "&b(StaffChat) %player%: &7%message%";
                    staffChatFormat = staffChatFormat.replaceAll("%player%", player.getName());
                    staffChatFormat = staffChatFormat.replaceAll("%message%", event.getMessage());
                    staffs.sendMessage(ChatColor.translateAlternateColorCodes('&', staffChatFormat));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerChat1(AsyncPlayerChatEvent e)
    {
        Player player = e.getPlayer();
        PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
        ChatChannel chatChannel = playerFaction == null ? ChatChannel.PUBLIC : playerFaction.getMember(player).getChatChannel();

        if (MuteChatCommand.isChatMuted() && chatChannel == ChatChannel.PUBLIC)
        {
            if (e.getPlayer().hasPermission("mutechat.bypass") || chatChannel == ChatChannel.FACTION || chatChannel == ChatChannel.ALLIANCE) {
                return;
            }
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "Chat is currently muted!");
        }
        else {}
    }

    @EventHandler
    public void onFrozen(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        BaseUser user = BasePlugin.getPlugin().getUserManager().getUser(player.getUniqueId());
        if (FreezeCommand.frozen.contains(player.getName())) {
            for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
                if (staff.hasPermission("freezechannel.see")) {
                    staff.sendMessage(ChatColor.GREEN + "[FROZEN] " + ChatColor.RED + player.getName() + ": "+ ChatColor.GRAY + e.getMessage());
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onCommandsNotify(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
            if (staff.hasPermission("commands.alert")) {
                if (event.getMessage().contains("/") || event.getMessage().contains("//")) {
                    if (ToggleCommandCommand.commandToggle.contains(staff)) {
                        staff.sendMessage(ChatColor.RED + "[Command] " + ChatColor.GREEN + player.getName() + ": " + ChatColor.GRAY + event.getMessage());
                    }
                }
            }
        }
    }
}

