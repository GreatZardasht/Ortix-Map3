/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.base.BasePlugin
 *  com.customhcf.base.user.BaseUser
 *  com.customhcf.base.user.UserManager
 *  com.google.common.base.Predicate
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.entity.AnimalTamer
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Horse
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Vehicle
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerChangedWorldEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerKickEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.event.vehicle.VehicleEnterEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package com.customhcf.hcf.listener;

import com.customhcf.base.BasePlugin;
import com.customhcf.base.user.BaseUser;
import com.customhcf.base.user.UserManager;
import com.customhcf.hcf.HCF;
import com.customhcf.hcf.deathban.DeathbanManager;
import com.customhcf.hcf.user.FactionUser;
import com.customhcf.hcf.visualise.VisualBlock;
import com.customhcf.hcf.visualise.VisualType;
import com.customhcf.hcf.visualise.VisualiseHandler;
import com.customhcf.hcfold.crate.Key;
import com.customhcf.hcfold.crate.KeyManager;
import com.google.common.base.Predicate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class CoreListener
implements Listener {
    private final HCF plugin;

    public CoreListener(HCF plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onHorseStep(VehicleEnterEvent e) {
        Horse horse;
        if (e.getVehicle().getType() == EntityType.HORSE && (horse = (Horse)e.getVehicle()).getOwner() != e.getEntered()) {
            ((Player)e.getEntered()).sendMessage((Object)ChatColor.RED + "This horse is not yours, this horse belongs to " + (Object)horse.getOwner());
            e.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onPlayerJoinKit(PlayerJoinEvent event) {
        BaseUser user = BasePlugin.getPlugin().getUserManager().getUser(event.getPlayer().getUniqueId());
        if (!user.hasStartKit()) {
            Player player = event.getPlayer();
        }
    }


    @EventHandler(ignoreCancelled=true, priority=EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage((String)null);
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.LOWEST)
    public void onPlayerQuit(PlayerKickEvent event) {
        event.setLeaveMessage((String)null);
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage((String)null);
        Player player = event.getPlayer();
        this.plugin.getVisualiseHandler().clearVisualBlocks(player, null, null, false);
        this.plugin.getUserManager().getUser(player.getUniqueId()).setShowClaimMap(false);
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.LOWEST)
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        this.plugin.getVisualiseHandler().clearVisualBlocks(player, null, null, false);
        this.plugin.getUserManager().getUser(player.getUniqueId()).setShowClaimMap(false);
    }

    @EventHandler
    public void onCreatureDenySpawn(CreatureSpawnEvent event) {
        Entity entity = (Entity) event.getEntity();
        if (entity.getType() == EntityType.HORSE || entity.getType() == EntityType.SHEEP || entity.getType() == EntityType.PIG || entity.getType() == EntityType.WITCH || entity.getType() == EntityType.CHICKEN || entity.getType() == EntityType.CREEPER) {
            if (entity.getWorld().getName().endsWith("_end")) {
                event.setCancelled(false);
            } else {
                event.setCancelled(true);
            }
        }
    }
}

