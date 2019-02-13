/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.util.BukkitUtils
 *  com.customhcf.util.Config
 *  com.customhcf.util.GenericUtils
 *  com.google.common.base.Optional
 *  com.google.common.base.Predicate
 *  com.google.common.cache.Cache
 *  com.google.common.cache.CacheBuilder
 *  javax.annotation.Nullable
 *  org.apache.commons.lang3.time.DurationFormatUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.World$Environment
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Item
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.ThrownPotion
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockIgniteEvent
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.event.entity.PotionSplashEvent
 *  org.bukkit.event.player.PlayerBucketEmptyEvent
 *  org.bukkit.event.player.PlayerPickupItemEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.event.player.PlayerRespawnEvent
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.projectiles.ProjectileSource
 *  org.bukkit.util.Vector
 *  org.spigotmc.event.player.PlayerSpawnLocationEvent
 */
package com.customhcf.hcf.timer.type;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.command.PvpTimerCommand;
import com.customhcf.hcf.faction.FactionManager;
import com.customhcf.hcf.faction.claim.Claim;
import com.customhcf.hcf.faction.event.FactionClaimChangedEvent;
import com.customhcf.hcf.faction.event.PlayerClaimEnterEvent;
import com.customhcf.hcf.faction.event.cause.ClaimChangeCause;
import com.customhcf.hcf.faction.type.ClaimableFaction;
import com.customhcf.hcf.faction.type.Faction;
import com.customhcf.hcf.faction.type.PlayerFaction;
import com.customhcf.hcf.faction.type.RoadFaction;
import com.customhcf.hcf.kothgame.eotw.EOTWHandler;
import com.customhcf.hcf.timer.PlayerTimer;
import com.customhcf.hcf.timer.Timer;
import com.customhcf.hcf.timer.TimerRunnable;
import com.customhcf.hcf.timer.event.TimerClearEvent;
import com.customhcf.hcf.visualise.VisualBlock;
import com.customhcf.hcf.visualise.VisualType;
import com.customhcf.hcf.visualise.VisualiseHandler;
import com.customhcf.util.BukkitUtils;
import com.customhcf.util.Config;
import com.customhcf.util.GenericUtils;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class PvpProtectionTimer
        extends PlayerTimer
        implements Listener {
    private static final String PVP_COMMAND = "/pvp enable";
    private static final long ITEM_PICKUP_DELAY = TimeUnit.SECONDS.toMillis(30);
    private static final long ITEM_PICKUP_MESSAGE_DELAY = 1250;
    private static final String ITEM_PICKUP_MESSAGE_META_KEY = "pickupMessageDelay";
    private final ConcurrentMap<Object, Object> itemUUIDPickupDelays;
    private final HCF plugin;

    public PvpProtectionTimer(HCF plugin) {
        super("PvP Timer", TimeUnit.MINUTES.toMillis(30));
        this.plugin = plugin;
        this.itemUUIDPickupDelays = CacheBuilder.newBuilder().expireAfterWrite(ITEM_PICKUP_DELAY + 5000, TimeUnit.MILLISECONDS).build().asMap();
    }

    @Override
    public String getScoreboardPrefix() {
        return org.bukkit.ChatColor.GREEN.toString() + (Object)org.bukkit.ChatColor.BOLD;
    }

    @Override
    public void onExpire(UUID userUUID) {
        Player player = Bukkit.getPlayer((UUID)userUUID);
        if (player == null) {
            return;
        }
        if (this.getRemaining(player) <= 0) {
            this.plugin.getVisualiseHandler().clearVisualBlocks(player, VisualType.CLAIM_BORDER, null);
            player.sendMessage(org.bukkit.ChatColor.RED.toString() + "You no longer have your " + this.getDisplayName() + (Object)org.bukkit.ChatColor.RED + '.');
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onTimerStop(TimerClearEvent event) {
        Optional<UUID> optionalUserUUID;
        if (event.getTimer().equals(this) && (optionalUserUUID = event.getUserUUID()).isPresent()) {
            this.onExpire((UUID)optionalUserUUID.get());
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onClaimChange(FactionClaimChangedEvent event) {
        if (event.getCause() != ClaimChangeCause.CLAIM) {
            return;
        }
        final Collection<Claim> claims = event.getAffectedClaims();
        for (final Claim claim : claims) {
            final Collection<Player> players = (Collection<Player>)claim.getPlayers();
            for (final Player player : players) {
                if (this.getRemaining(player) <= 0) continue;
                Location location = player.getLocation();
                location.setX(claim.getMinimumX() - 1);
                location.setY(0);
                location.setZ(claim.getMinimumZ() - 1);
                location = BukkitUtils.getHighestLocation((Location)location, (Location)location);
                if (!player.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN)) continue;
                player.sendMessage((Object)org.bukkit.ChatColor.RED + "Land was claimed where you were standing. As you still have your " + this.getName() + ", you were teleported away.");
            }
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        this.setCooldown(player, player.getUniqueId());
        this.setPaused(player, player.getUniqueId(), true);
        player.sendMessage((Object)org.bukkit.ChatColor.YELLOW + "Once you leave Spawn your " + (Object) net.md_5.bungee.api.ChatColor.GOLD + " 30 minutes " + (Object) net.md_5.bungee.api.ChatColor.YELLOW + " of " + this.getName() + (Object)org.bukkit.ChatColor.YELLOW + " will start.");
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        World world = player.getWorld();
        Location location = player.getLocation();
        Iterator iterator = event.getDrops().iterator();
        while (iterator.hasNext()) {
            this.itemUUIDPickupDelays.put(world.dropItemNaturally(location, (ItemStack)iterator.next()).getUniqueId(), System.currentTimeMillis() + ITEM_PICKUP_DELAY);
            iterator.remove();
        }
        this.clearCooldown(player);
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        long remaining = this.getRemaining(player);
        if (remaining > 0) {
            event.setCancelled(true);
            player.sendMessage((Object)org.bukkit.ChatColor.RED + "You cannot empty buckets as your " + this.getDisplayName() + (Object)org.bukkit.ChatColor.RED + " is active [" + (Object)org.bukkit.ChatColor.BOLD + HCF.getRemaining(remaining, true, false) + (Object)org.bukkit.ChatColor.RED + " remaining]");
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onBlockIgnite(BlockIgniteEvent event) {
        Player player = event.getPlayer();
        if (player == null) {
            return;
        }
        long remaining = this.getRemaining(player);
        if (remaining > 0) {
            event.setCancelled(true);
            player.sendMessage((Object)org.bukkit.ChatColor.RED + "You cannot ignite blocks as your " + this.getDisplayName() + (Object)org.bukkit.ChatColor.RED + " is active [" + (Object)org.bukkit.ChatColor.BOLD + HCF.getRemaining(remaining, true, false) + (Object)org.bukkit.ChatColor.RED + " remaining]");
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onItemPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        long remaining = this.getRemaining(player);
        if (remaining > 0) {
            UUID itemUUID = event.getItem().getUniqueId();
            Long delay = (Long)this.itemUUIDPickupDelays.get(itemUUID);
            if (delay == null) {
                return;
            }
            long millis = System.currentTimeMillis();
            if (delay - millis > 0) {
                event.setCancelled(true);
                MetadataValue value = player.getMetadata("pickupMessageDelay", (Plugin)this.plugin);
                if (value != null && value.asLong() - millis <= 0) {
                    player.setMetadata("pickupMessageDelay", (MetadataValue)new FixedMetadataValue((Plugin)this.plugin, (Object)(millis + 1250)));
                    player.sendMessage((Object)org.bukkit.ChatColor.RED + "You cannot pick this item up for another " + (Object)org.bukkit.ChatColor.BOLD + DurationFormatUtils.formatDurationWords((long)remaining, (boolean)true, (boolean)true) + (Object)org.bukkit.ChatColor.RED + " as your " + this.getDisplayName() + (Object)org.bukkit.ChatColor.RED + " is active [" + (Object)org.bukkit.ChatColor.BOLD + HCF.getRemaining(remaining, true, false) + (Object)org.bukkit.ChatColor.RED + " remaining]");
                }
            } else {
                this.itemUUIDPickupDelays.remove(itemUUID);
            }
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        TimerRunnable runnable = (TimerRunnable)this.cooldowns.get(player.getUniqueId());
        if (runnable != null && runnable.getRemaining() > 0) {
            runnable.setPaused(true);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPlayerSpawnLocation(PlayerSpawnLocationEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPlayedBefore()) {
            if (!this.plugin.getEotwHandler().isEndOfTheWorld()) {
                this.setCooldown(player, player.getUniqueId());
                this.setPaused(player, player.getUniqueId(), true);
                player.sendMessage((Object)org.bukkit.ChatColor.YELLOW + "Once you leave Spawn your " + (Object) net.md_5.bungee.api.ChatColor.GOLD + " 30 minutes " + (Object) net.md_5.bungee.api.ChatColor.YELLOW + " of " + this.getName() + (Object)org.bukkit.ChatColor.YELLOW + " will start.");
            }
        } else if (this.isPaused(player) && this.getRemaining(player) > 0 && !this.plugin.getFactionManager().getFactionAt(event.getSpawnLocation()).isSafezone()) {
            this.setPaused(player, player.getUniqueId(), false);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPlayerClaimEnterMonitor(PlayerClaimEnterEvent event) {
        Player player = event.getPlayer();
        if (event.getTo().getWorld().getEnvironment() == World.Environment.THE_END) {
            this.clearCooldown(player);
            return;
        }
        Faction toFaction = event.getToFaction();
        Faction fromFaction = event.getFromFaction();
        if (fromFaction.isSafezone() && !toFaction.isSafezone()) {
            if (this.isPaused(player.getUniqueId())) {
                this.setPaused(player, player.getUniqueId(), false);
                player.sendMessage((Object)org.bukkit.ChatColor.YELLOW + "Your " + this.getDisplayName() + (Object)org.bukkit.ChatColor.YELLOW + " has started.");
                return;
            }
            if (this.getRemaining(player) > 0) {
                this.setPaused(player, player.getUniqueId(), false);
                player.sendMessage((Object)org.bukkit.ChatColor.YELLOW + "Your " + this.getDisplayName() + (Object)org.bukkit.ChatColor.YELLOW + " is no longer paused.");
            }
        } else if (!fromFaction.isSafezone() && toFaction.isSafezone() && this.getRemaining(player) > 0) {
            player.sendMessage((Object)org.bukkit.ChatColor.YELLOW + "Your " + this.getDisplayName() + (Object)org.bukkit.ChatColor.YELLOW + " is now paused.");
            this.setPaused(player, player.getUniqueId(), true);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onPlayerClaimEnter(PlayerClaimEnterEvent event) {
        long remaining;
        Player player = event.getPlayer();
        Faction toFaction = event.getToFaction();
        if (toFaction instanceof ClaimableFaction && (remaining = this.getRemaining(player)) > 0) {
            PlayerFaction playerFaction;
            if (event.getEnterCause() == PlayerClaimEnterEvent.EnterCause.TELEPORT && toFaction instanceof PlayerFaction && (playerFaction = this.plugin.getFactionManager().getPlayerFaction(player.getUniqueId())) != null && playerFaction.equals(toFaction)) {
                player.sendMessage((Object)org.bukkit.ChatColor.YELLOW + "You have entered your own claim, therefore your " + this.getDisplayName() + (Object)org.bukkit.ChatColor.YELLOW + " has been removed.");
                this.clearCooldown(player);
                return;
            }
            if (!toFaction.isSafezone() && !(toFaction instanceof RoadFaction)) {
                event.setCancelled(true);
                player.sendMessage((Object)org.bukkit.ChatColor.YELLOW + "You cannot enter " + toFaction.getDisplayName((CommandSender)player) + (Object)org.bukkit.ChatColor.YELLOW + " whilst your " + this.getDisplayName() + (Object)org.bukkit.ChatColor.RED + " is active [" + (Object)org.bukkit.ChatColor.BOLD + HCF.getRemaining(remaining, true, false) + (Object)org.bukkit.ChatColor.RED + " remaining]. " + "Use '" + (Object)org.bukkit.ChatColor.GOLD + "/pvp enable" + (Object)org.bukkit.ChatColor.RED + "' to remove this timer.");
            }
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player attacker = BukkitUtils.getFinalAttacker((EntityDamageEvent)event, (boolean)true);
            if (attacker == null) {
                return;
            }
            Player player = (Player)entity;
            long remaining = this.getRemaining(player);
            if (remaining > 0) {
                event.setCancelled(true);
                attacker.sendMessage((Object)org.bukkit.ChatColor.RED + player.getName() + " has their " + this.getDisplayName() + (Object)org.bukkit.ChatColor.RED + " for another " + (Object)org.bukkit.ChatColor.BOLD + HCF.getRemaining(remaining, true, false) + (Object)org.bukkit.ChatColor.RED + '.');
                return;
            }
            remaining = this.getRemaining(attacker);
            if (remaining > 0) {
                event.setCancelled(true);
                attacker.sendMessage((Object)org.bukkit.ChatColor.RED + "You cannot attack players whilst your " + this.getDisplayName() + (Object)org.bukkit.ChatColor.RED + " is active [" + (Object)org.bukkit.ChatColor.BOLD + HCF.getRemaining(remaining, true, false) + (Object)org.bukkit.ChatColor.RED + " remaining]. Use '" + (Object)org.bukkit.ChatColor.GOLD + "/pvp enable" + (Object)org.bukkit.ChatColor.RED + "' to allow pvp.");
            }
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
    public void onPotionSplash(PotionSplashEvent event) {
        ThrownPotion potion = event.getPotion();
        if (potion.getShooter() instanceof Player && BukkitUtils.isDebuff((ThrownPotion)potion)) {
            for (LivingEntity livingEntity : event.getAffectedEntities()) {
                if (!(livingEntity instanceof Player) || this.getRemaining((Player)livingEntity) <= 0) continue;
                event.setIntensity(livingEntity, 0.0);
            }
        }
    }

}


