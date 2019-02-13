/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Optional
 *  net.md_5.bungee.api.ChatColor
 *  org.apache.commons.lang3.time.DurationFormatUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.SignChangeEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package com.customhcf.hcf.listener;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.faction.FactionManager;
import com.customhcf.hcf.faction.FactionMember;
import com.customhcf.hcf.faction.claim.Claim;
import com.customhcf.hcf.faction.event.CaptureZoneEnterEvent;
import com.customhcf.hcf.faction.event.CaptureZoneLeaveEvent;
import com.customhcf.hcf.faction.event.FactionCreateEvent;
import com.customhcf.hcf.faction.event.FactionRemoveEvent;
import com.customhcf.hcf.faction.event.FactionRenameEvent;
import com.customhcf.hcf.faction.event.PlayerClaimEnterEvent;
import com.customhcf.hcf.faction.event.PlayerJoinFactionEvent;
import com.customhcf.hcf.faction.event.PlayerLeaveFactionEvent;
import com.customhcf.hcf.faction.event.PlayerLeftFactionEvent;
import com.customhcf.hcf.faction.struct.RegenStatus;
import com.customhcf.hcf.faction.struct.Role;
import com.customhcf.hcf.faction.type.Faction;
import com.customhcf.hcf.faction.type.PlayerFaction;
import com.customhcf.hcf.kothgame.CaptureZone;
import com.customhcf.hcf.kothgame.eotw.EOTWHandler;
import com.customhcf.hcf.kothgame.faction.CapturableFaction;
import com.customhcf.hcf.kothgame.faction.KothFaction;
import com.customhcf.hcf.user.FactionUser;
import com.customhcf.hcf.user.UserManager;
import com.google.common.base.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class FactionListener
implements Listener {
    private static final long FACTION_JOIN_WAIT_MILLIS = TimeUnit.SECONDS.toMillis(30);
    private static final String FACTION_JOIN_WAIT_WORDS = DurationFormatUtils.formatDurationWords((long)FACTION_JOIN_WAIT_MILLIS, (boolean)true, (boolean)true);
    private static final String LAND_CHANGED_META_KEY = "landChangedMessage";
    private static final long LAND_CHANGE_MSG_THRESHOLD = 225;
    private final HCF plugin;

    public FactionListener(HCF plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        if (e.getLine(0).equalsIgnoreCase("[KOTH]") && this.plugin.getFactionManager().getFaction(e.getLine(1)) instanceof KothFaction) {
            KothFaction kothFaction = (KothFaction)this.plugin.getFactionManager().getFaction(e.getLine(1));
            e.setLine(0, (Object)ChatColor.LIGHT_PURPLE + "KOTH");
            e.setLine(1, (Object)ChatColor.GOLD + kothFaction.getName());
            for (Claim claim : kothFaction.getClaims()) {
                Location location = claim.getCenter();
                e.setLine(2, ChatColor.RED.toString() + location.getBlockX() + " | " + location.getBlockZ());
            }
            e.setLine(3, (Object)ChatColor.RED + kothFaction.getCaptureZone().getDefaultCaptureWords());
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onFactionCreate(FactionCreateEvent event) {
        Faction faction = event.getFaction();
        if (faction instanceof PlayerFaction) {
            CommandSender sender = event.getSender();
            Bukkit.broadcastMessage((String)((Object)net.md_5.bungee.api.ChatColor.RED + "" + event.getFaction().getName() + (Object)net.md_5.bungee.api.ChatColor.YELLOW + " has been created by " + (Object)net.md_5.bungee.api.ChatColor.WHITE + "" + sender.getName()));
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onFactionRemove(FactionRemoveEvent event) {
        Faction faction = event.getFaction();
        if (faction instanceof PlayerFaction) {
            CommandSender sender = event.getSender();
            Bukkit.broadcastMessage((String)((Object)net.md_5.bungee.api.ChatColor.RED + "" + event.getFaction().getName() + (Object)net.md_5.bungee.api.ChatColor.YELLOW + " has been disbanded by " + (Object)net.md_5.bungee.api.ChatColor.WHITE + sender.getName()));
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onFactionRename(FactionRenameEvent event) {
        Faction faction = event.getFaction();
        if (faction instanceof PlayerFaction) {
            Bukkit.broadcastMessage((String)((Object)net.md_5.bungee.api.ChatColor.RED + event.getOriginalName() + (Object)net.md_5.bungee.api.ChatColor.YELLOW + " has been renamed to " + (Object)net.md_5.bungee.api.ChatColor.RED + "" + event.getNewName() + (Object)net.md_5.bungee.api.ChatColor.YELLOW + " by " + (Object)net.md_5.bungee.api.ChatColor.WHITE + event.getSender().getName()));
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onFactionRenameMonitor(FactionRenameEvent event) {
        Faction faction = event.getFaction();
        if (faction instanceof KothFaction) {
            ((KothFaction)faction).getCaptureZone().setName(event.getNewName());
        }
    }

    private long getLastLandChangedMeta(Player player) {
        long remaining;
        MetadataValue value = player.getMetadata("landChangedMessage", (Plugin)this.plugin);
        long millis = System.currentTimeMillis();
        long l = remaining = value == null ? 0 : value.asLong() - millis;
        if (remaining <= 0) {
            player.setMetadata("landChangedMessage", (MetadataValue)new FixedMetadataValue((Plugin)this.plugin, (Object)(millis + 225)));
        }
        return remaining;
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onCaptureZoneEnter(CaptureZoneEnterEvent event) {
        Player player = event.getPlayer();
        if (this.getLastLandChangedMeta(player) <= 0 && this.plugin.getUserManager().getUser(player.getUniqueId()).isCapzoneEntryAlerts()) {
            player.sendMessage((Object)net.md_5.bungee.api.ChatColor.YELLOW + "Now entering capture zone: " + event.getCaptureZone().getDisplayName() + (Object)net.md_5.bungee.api.ChatColor.YELLOW + '(' + event.getFaction().getName() + (Object)net.md_5.bungee.api.ChatColor.YELLOW + ')');
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onCaptureZoneLeave(CaptureZoneLeaveEvent event) {
        Player player = event.getPlayer();
        if (this.getLastLandChangedMeta(player) <= 0 && this.plugin.getUserManager().getUser(player.getUniqueId()).isCapzoneEntryAlerts()) {
            player.sendMessage((Object)net.md_5.bungee.api.ChatColor.YELLOW + "Now leaving capture zone: " + event.getCaptureZone().getDisplayName() + (Object)net.md_5.bungee.api.ChatColor.YELLOW + '(' + event.getFaction().getName() + (Object)net.md_5.bungee.api.ChatColor.YELLOW + ')');
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    private void onPlayerClaimEnter(PlayerClaimEnterEvent event) {
        Player player;
        Faction toFaction = event.getToFaction();
        if (toFaction.isSafezone()) {
            player = event.getPlayer();
            
            Damageable dplayer = (Damageable)player;
            double health = dplayer.getMaxHealth();
            
            player.setHealth(health);
            player.setFoodLevel(20);
            player.setFireTicks(0);
            player.setSaturation(4.0f);
        }
        if (this.getLastLandChangedMeta(player = event.getPlayer()) <= 0) {
            Faction fromFaction = event.getFromFaction();
            player.sendMessage((Object)net.md_5.bungee.api.ChatColor.YELLOW + "Now entering: " + toFaction.getDisplayName((CommandSender)player) + (Object)net.md_5.bungee.api.ChatColor.YELLOW + " (" + (toFaction.isDeathban() ? new StringBuilder().append((Object)net.md_5.bungee.api.ChatColor.RED).append("Deathban").toString() : new StringBuilder().append((Object)net.md_5.bungee.api.ChatColor.GREEN).append("Non-Deathban").toString()) + (Object)net.md_5.bungee.api.ChatColor.YELLOW + ')');
            player.sendMessage((Object)net.md_5.bungee.api.ChatColor.YELLOW + "Now leaving: " + fromFaction.getDisplayName((CommandSender)player) + (Object)net.md_5.bungee.api.ChatColor.YELLOW + " (" + (fromFaction.isDeathban() ? new StringBuilder().append((Object)net.md_5.bungee.api.ChatColor.RED).append("Deathban").toString() : new StringBuilder().append((Object)net.md_5.bungee.api.ChatColor.GREEN).append("Non-Deathban").toString()) + (Object)net.md_5.bungee.api.ChatColor.YELLOW + ')');
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPlayerLeftFaction(PlayerLeftFactionEvent event) {
        Optional<Player> optionalPlayer = event.getPlayer();
        if (optionalPlayer.isPresent()) {
            this.plugin.getUserManager().getUser(((Player)optionalPlayer.get()).getUniqueId()).setLastFactionLeaveMillis(System.currentTimeMillis());
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onPlayerPreFactionJoin(PlayerJoinFactionEvent event) {
        Faction faction = event.getFaction();
        Optional<Player> optionalPlayer = event.getPlayer();
        if (faction instanceof PlayerFaction && optionalPlayer.isPresent()) {
            Player player = (Player)optionalPlayer.get();
            PlayerFaction playerFaction = (PlayerFaction)faction;
            if (!this.plugin.getEotwHandler().isEndOfTheWorld() && playerFaction.getRegenStatus() == RegenStatus.PAUSED) {
                event.setCancelled(true);
                player.sendMessage((Object)net.md_5.bungee.api.ChatColor.RED + "You cannot join factions that are not regenerating DTR.");
                return;
            }
            long difference = this.plugin.getUserManager().getUser(player.getUniqueId()).getLastFactionLeaveMillis() - System.currentTimeMillis() + FACTION_JOIN_WAIT_MILLIS;
            if (difference > 0 && !player.hasPermission("hcf.faction.argument.staff.forcejoin")) {
                event.setCancelled(true);
                player.sendMessage((Object)net.md_5.bungee.api.ChatColor.RED + "You cannot join factions after just leaving within " + FACTION_JOIN_WAIT_WORDS + ". " + "You gotta wait another " + DurationFormatUtils.formatDurationWords((long)difference, (boolean)true, (boolean)true) + '.');
            }
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onFactionLeave(PlayerLeaveFactionEvent event) {
        Optional<Player> optional;
        Faction faction = event.getFaction();
        if (faction instanceof PlayerFaction && (optional = event.getPlayer()).isPresent()) {
            Player player = (Player)optional.get();
            if (this.plugin.getFactionManager().getFactionAt(player.getLocation()).equals(faction)) {
                event.setCancelled(true);
                player.sendMessage((Object)net.md_5.bungee.api.ChatColor.RED + "You cannot leave your faction whilst you remain in its' territory.");
            }
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
        if (playerFaction != null) {
            playerFaction.printDetails((CommandSender)player);
            playerFaction.broadcast((Object)net.md_5.bungee.api.ChatColor.GOLD + "Member Online: " + (Object)net.md_5.bungee.api.ChatColor.GREEN + playerFaction.getMember(player).getRole().getAstrix() + player.getName() + (Object)net.md_5.bungee.api.ChatColor.GOLD + '.', player.getUniqueId());
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
        if (playerFaction != null) {
            playerFaction.broadcast((Object)net.md_5.bungee.api.ChatColor.GOLD + "Member Offline: " + (Object)net.md_5.bungee.api.ChatColor.GREEN + playerFaction.getMember(player).getRole().getAstrix() + player.getName() + (Object)net.md_5.bungee.api.ChatColor.GOLD + '.');
        }
    }
}

