/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.util.command.CommandArgument
 *  com.google.common.collect.ImmutableList
 *  org.apache.commons.lang3.StringUtils
 *  org.bukkit.ChatColor
 *  org.bukkit.Server
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 */
package com.customhcf.hcf.faction.argument.staff;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.faction.FactionManager;
import com.customhcf.hcf.faction.FactionMember;
import com.customhcf.hcf.faction.event.FactionChatEvent;
import com.customhcf.hcf.faction.event.FactionRemoveEvent;
import com.customhcf.hcf.faction.struct.ChatChannel;
import com.customhcf.hcf.faction.struct.Role;
import com.customhcf.hcf.faction.type.Faction;
import com.customhcf.hcf.faction.type.PlayerFaction;
import com.customhcf.hcf.user.FactionUser;
import com.customhcf.hcf.user.UserManager;
import com.customhcf.util.command.CommandArgument;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class FactionChatSpyArgument
extends CommandArgument
implements Listener {
    private static final UUID ALL_UUID = UUID.fromString("5a3ed6d1-0239-4e24-b4a9-8cd5b3e5fc72");
    private static final ImmutableList<String> COMPLETIONS = ImmutableList.of("list", "add", "del", "clear");
    private final HCF plugin;

    public FactionChatSpyArgument(HCF plugin) {
        super("chatspy", "Spy on the chat of a faction.");
        this.plugin = plugin;
        this.aliases = new String[]{"cs"};
        this.permission = "hcf.command.faction.argument." + this.getName();
        plugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
    }

    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName() + " <" + StringUtils.join(COMPLETIONS, (char)'|') + "> [factionName]";
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onFactionRemove(FactionRemoveEvent event) {
        if (event.getFaction() instanceof PlayerFaction) {
            UUID factionUUID = event.getFaction().getUniqueID();
            for (FactionUser user : this.plugin.getUserManager().getUsers().values()) {
                user.getFactionChatSpying().remove(factionUUID);
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onFactionChat(final FactionChatEvent event) {
        final Player player = event.getPlayer();
        final Faction faction = event.getFaction();
        final String format = ChatColor.GOLD + "[" + ChatColor.RED + event.getChatChannel().getDisplayName() + ": " + ChatColor.YELLOW + faction.getName() + ChatColor.GOLD + "] " + ChatColor.GRAY + event.getFactionMember().getRole().getAstrix() + player.getName() + ": " + ChatColor.YELLOW + event.getMessage();
        final HashSet<Player> recipients = new HashSet<Player>();
        recipients.removeAll(event.getRecipients());
        for (final CommandSender recipient : recipients) {
            if (!(recipient instanceof Player)) {
                continue;
            }
            final Player target = (Player)recipient;
            final FactionUser user = event.isAsynchronous() ? this.plugin.getUserManager().getUserAsync(target.getUniqueId()) : this.plugin.getUserManager().getUser(player.getUniqueId());
            final Collection<UUID> spying = user.getFactionChatSpying();
            if (!spying.contains(FactionChatSpyArgument.ALL_UUID) && !spying.contains(faction.getUniqueID())) {
                continue;
            }
            recipient.sendMessage(format);
        }
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage((Object)ChatColor.RED + "This command is only executable by players.");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage((Object)ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        Player player = (Player)sender;
        Set<UUID> currentSpies = this.plugin.getUserManager().getUser(player.getUniqueId()).getFactionChatSpying();
        if (args[1].equalsIgnoreCase("list")) {
            if (currentSpies.isEmpty()) {
                sender.sendMessage((Object)ChatColor.RED + "You are not spying on the chat of any factions.");
                return true;
            }
            sender.sendMessage((Object)ChatColor.GRAY + "You are currently spying on the chat of (" + currentSpies.size() + " factions): " + (Object)ChatColor.RED + StringUtils.join(currentSpies, (String)new StringBuilder().append((Object)ChatColor.GRAY).append(", ").append((Object)ChatColor.RED).toString()) + (Object)ChatColor.GRAY + '.');
            return true;
        }
        if (args[1].equalsIgnoreCase("add")) {
            if (args.length < 3) {
                sender.sendMessage((Object)ChatColor.RED + "Usage: /" + label + ' ' + args[1].toLowerCase() + " <all|factionName|playerName>");
                return true;
            }
            Faction faction = this.plugin.getFactionManager().getFaction(args[2]);
            if (!(faction instanceof PlayerFaction)) {
                sender.sendMessage((Object)ChatColor.RED + "Player based faction named or containing member with IGN or UUID " + args[2] + " not found.");
                return true;
            }
            if (currentSpies.contains(ALL_UUID) || currentSpies.contains(faction.getUniqueID())) {
                sender.sendMessage((Object)ChatColor.RED + "You are already spying on the chat of " + (args[2].equalsIgnoreCase("all") ? "all factions" : args[2]) + '.');
                return true;
            }
            if (args[2].equalsIgnoreCase("all")) {
                currentSpies.clear();
                currentSpies.add(ALL_UUID);
                sender.sendMessage((Object)ChatColor.GREEN + "You are now spying on the chat of all factions.");
                return true;
            }
            if (currentSpies.add(faction.getUniqueID())) {
                sender.sendMessage((Object)ChatColor.GREEN + "You are now spying on the chat of " + faction.getDisplayName(sender) + (Object)ChatColor.GREEN + '.');
            } else {
                sender.sendMessage((Object)ChatColor.RED + "You are already spying on the chat of " + faction.getDisplayName(sender) + (Object)ChatColor.RED + '.');
            }
            return true;
        }
        if (args[1].equalsIgnoreCase("del") || args[1].equalsIgnoreCase("delete") || args[1].equalsIgnoreCase("remove")) {
            if (args.length < 3) {
                sender.sendMessage((Object)ChatColor.RED + "Usage: /" + label + ' ' + args[1].toLowerCase() + " <playerName>");
                return true;
            }
            if (args[2].equalsIgnoreCase("all")) {
                currentSpies.remove(ALL_UUID);
                sender.sendMessage((Object)ChatColor.RED + "No longer spying on the chat of all factions.");
                return true;
            }
            Faction faction = this.plugin.getFactionManager().getContainingFaction(args[2]);
            if (faction == null) {
                sender.sendMessage((Object)ChatColor.GOLD + "Faction '" + (Object)ChatColor.WHITE + args[2] + (Object)ChatColor.GOLD + "' not found.");
                return true;
            }
            if (currentSpies.remove(faction.getUniqueID())) {
                sender.sendMessage((Object)ChatColor.RED + "You are no longer spying on the chat of " + faction.getDisplayName(sender) + (Object)ChatColor.RED + '.');
            } else {
                sender.sendMessage((Object)ChatColor.RED + "You will still not be spying on the chat of " + faction.getDisplayName(sender) + (Object)ChatColor.RED + '.');
            }
            return true;
        }
        if (args[1].equalsIgnoreCase("clear")) {
            currentSpies.clear();
            sender.sendMessage((Object)ChatColor.YELLOW + "You are no longer spying the chat of any faction.");
            return true;
        }
        sender.sendMessage((Object)ChatColor.RED + "Usage: " + this.getUsage(label));
        return true;
    }
}

