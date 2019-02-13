/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.base.kit.event.KitApplyEvent
 *  com.customhcf.util.BukkitUtils
 *  com.google.common.base.Optional
 *  com.google.common.base.Predicate
 *  org.apache.commons.lang3.time.DurationFormatUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.command.CommandSender
 *  org.bukkit.enchantments.EnchantmentTarget
 *  org.bukkit.entity.Arrow
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.player.PlayerRespawnEvent
 *  org.bukkit.inventory.ItemStack
 */
package com.customhcf.hcf.timer.type;

import com.customhcf.base.kit.event.KitApplyEvent;
import com.customhcf.hcf.HCF;
import com.customhcf.hcf.faction.event.PlayerClaimEnterEvent;
import com.customhcf.hcf.faction.event.PlayerJoinFactionEvent;
import com.customhcf.hcf.faction.event.PlayerLeaveFactionEvent;
import com.customhcf.hcf.faction.type.Faction;
import com.customhcf.hcf.timer.PlayerTimer;
import com.customhcf.hcf.timer.Timer;
import com.customhcf.hcf.timer.TimerRunnable;
import com.customhcf.hcf.timer.event.TimerClearEvent;
import com.customhcf.hcf.timer.event.TimerStartEvent;
import com.customhcf.hcf.visualise.VisualBlock;
import com.customhcf.hcf.visualise.VisualType;
import com.customhcf.hcf.visualise.VisualiseHandler;
import com.customhcf.util.BukkitUtils;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class SpawnTagTimer
extends PlayerTimer
implements Listener {
    private static final long NON_WEAPON_TAG = 5000;
    private final HCF plugin;

    public SpawnTagTimer(HCF plugin) {
        super("Combat", TimeUnit.SECONDS.toMillis(30));
        this.plugin = plugin;
    }

    @Override
    public String getScoreboardPrefix() {
        return ChatColor.RED.toString() + (Object)ChatColor.BOLD;
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
    public void onKitApply(KitApplyEvent event) {
        long remaining;
        Player player = event.getPlayer();
        if (!event.isForce() && (remaining = this.getRemaining(player)) > 0) {
            event.setCancelled(true);
            player.sendMessage((Object)ChatColor.RED + "You cannot apply kits whilst your " + this.getDisplayName() + (Object)ChatColor.RED + " timer is active [" + (Object)ChatColor.BOLD + HCF.getRemaining(remaining, true, false) + (Object)ChatColor.RED + " remaining]");
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onTimerStop(TimerClearEvent event) {
        Optional<UUID> optionalUserUUID;
        if (event.getTimer().equals(this) && (optionalUserUUID = event.getUserUUID()).isPresent()) {
            this.onExpire((UUID)optionalUserUUID.get());
        }
    }

    @Override
    public void onExpire(UUID userUUID) {
        Player player = Bukkit.getPlayer((UUID)userUUID);
        if (player == null) {
            return;
        }
        this.plugin.getVisualiseHandler().clearVisualBlocks(player, VisualType.SPAWN_BORDER, null);
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onFactionJoin(PlayerJoinFactionEvent event) {
        long remaining;
        Player player;
        Optional<Player> optional = event.getPlayer();
        if (optional.isPresent() && (remaining = this.getRemaining(player = (Player)optional.get())) > 0) {
            event.setCancelled(true);
            player.sendMessage((Object)ChatColor.RED + "You cannot join factions whilst your " + this.getDisplayName() + (Object)ChatColor.RED + " timer is active [" + (Object)ChatColor.BOLD + HCF.getRemaining(this.getRemaining(player), true, false) + (Object)ChatColor.RED + " remaining]");
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onFactionLeave(PlayerLeaveFactionEvent event) {
        Player player;
        Optional<Player> optional = event.getPlayer();
        if (optional.isPresent() && this.getRemaining(player = (Player)optional.get()) > 0) {
            event.setCancelled(true);
            player.sendMessage((Object)ChatColor.RED + "You cannot join factions whilst your " + this.getDisplayName() + (Object)ChatColor.RED + " timer is active [" + (Object)ChatColor.BOLD + HCF.getRemaining(this.getRemaining(player), true, false) + (Object)ChatColor.RED + " remaining]");
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onPreventClaimEnter(PlayerClaimEnterEvent event) {
        if (event.getEnterCause() == PlayerClaimEnterEvent.EnterCause.TELEPORT) {
            return;
        }
        Player player = event.getPlayer();
        if (!event.getFromFaction().isSafezone() && event.getToFaction().isSafezone() && this.getRemaining(player) > 0) {
            event.setCancelled(true);
            player.sendMessage((Object)ChatColor.RED + "You cannot enter " + event.getToFaction().getDisplayName((CommandSender)player) + (Object)ChatColor.RED + " whilst your " + this.getDisplayName() + (Object)ChatColor.RED + " timer is active [" + (Object)ChatColor.BOLD + HCF.getRemaining(this.getRemaining(player), true, false) + (Object)ChatColor.RED + " remaining]");
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity entity;
        Player attacker = BukkitUtils.getFinalAttacker((EntityDamageEvent)event, (boolean)true);
        if (attacker != null && (entity = event.getEntity()) instanceof Player) {
            Player attacked = (Player)entity;
            boolean weapon = event.getDamager() instanceof Arrow;
            if (!weapon) {
                ItemStack stack = attacker.getItemInHand();
                weapon = stack != null && EnchantmentTarget.WEAPON.includes(stack);
            }
            long duration = weapon ? this.defaultCooldown : 30000;
            this.setCooldown(attacked, attacked.getUniqueId(), Math.max(this.getRemaining(attacked), duration), true);
            this.setCooldown(attacker, attacker.getUniqueId(), Math.max(this.getRemaining(attacker), duration), true);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onTimerStart(TimerStartEvent event) {
        Optional<Player> optional;
        if (event.getTimer().equals(this) && (optional = event.getPlayer()).isPresent()) {
            Player player = (Player)optional.get();
            player.sendMessage((Object)ChatColor.YELLOW + "You are now spawn-tagged for " + (Object)ChatColor.AQUA + DurationFormatUtils.formatDurationWords((long)event.getDuration(), (boolean)true, (boolean)true) + (Object)ChatColor.AQUA + '.');
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        this.clearCooldown(event.getPlayer().getUniqueId());
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPreventClaimEnterMonitor(PlayerClaimEnterEvent event) {
        if (event.getEnterCause() == PlayerClaimEnterEvent.EnterCause.TELEPORT && !event.getFromFaction().isSafezone() && event.getToFaction().isSafezone()) {
            this.clearCooldown(event.getPlayer());
        }
    }
}

