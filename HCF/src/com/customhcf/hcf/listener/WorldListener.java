/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.EnderDragon
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Squid
 *  org.bukkit.entity.Wither
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockFadeEvent
 *  org.bukkit.event.block.BlockFromToEvent
 *  org.bukkit.event.block.BlockIgniteEvent
 *  org.bukkit.event.block.BlockIgniteEvent$IgniteCause
 *  org.bukkit.event.entity.CreatureSpawnEvent
 *  org.bukkit.event.entity.EntityChangeBlockEvent
 *  org.bukkit.event.entity.EntityExplodeEvent
 *  org.bukkit.event.entity.EntityPortalEvent
 *  org.bukkit.event.inventory.InventoryOpenEvent
 *  org.bukkit.event.player.PlayerBedEnterEvent
 *  org.bukkit.event.player.PlayerRespawnEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.material.EnderChest
 *  org.spigotmc.event.player.PlayerSpawnLocationEvent
 */
package com.customhcf.hcf.listener;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.balance.EconomyManager;
import java.util.List;
import java.util.UUID;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.material.EnderChest;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class WorldListener
implements Listener {
    public static final String DEFAULT_WORLD_NAME = "world";
    private final HCF plugin;

    public WorldListener(HCF plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled=false, priority=EventPriority.HIGH)
    public void onEntityExplode(EntityExplodeEvent event) {
        event.blockList().clear();
        if (event.getEntity() instanceof EnderDragon) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled=false, priority=EventPriority.HIGH)
    public void onBlockChange(BlockFromToEvent event) {
        if (event.getBlock().getType() == Material.DRAGON_EGG) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onEntityPortalEnter(EntityPortalEvent event) {
        if (event.getEntity() instanceof EnderDragon) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onBedEnter(PlayerBedEnterEvent event) {
        event.setCancelled(true);
        event.getPlayer().sendMessage((Object)ChatColor.RED + "Beds are disabled on this server.");
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public void onWitherChangeBlock(EntityChangeBlockEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Wither || entity instanceof EnderDragon) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onBlockFade(BlockFadeEvent event) {
        switch (event.getBlock().getType()) {
            case SNOW: 
            case ICE: {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent event) {
        if (event.getPlayer().getWorld().getName().contains("_the_end")) {
            World w = Bukkit.getServer().getWorld(HCF.getPlugin().getConfig().getString("endentrance.world"));
            double x = HCF.getPlugin().getConfig().getDouble("endentrance.x");
            double y = HCF.getPlugin().getConfig().getDouble("endentrance.y");
            double z = HCF.getPlugin().getConfig().getDouble("endentrance.z");
            event.getPlayer().teleport(new Location(w, x, y, z));
        }
    }

  //  @EventHandler
  //  public void onChangeWorld1(PlayerChangedWorldEvent event) {
  //      if (event.getPlayer().getWorld().getName().equalsIgnoreCase("world")) {
   //         World w = Bukkit.getServer().getWorld(HCF.getPlugin().getConfig().getString("endexit.world"));
   //         double x = HCF.getPlugin().getConfig().getDouble("endexit.x");
   //         double y = HCF.getPlugin().getConfig().getDouble("endexit.y");
   //         double z = HCF.getPlugin().getConfig().getDouble("endexit.z");
    //        event.getPlayer().teleport(new Location(w, x, y, z));
   //     }
   // }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        event.setRespawnLocation(Bukkit.getWorld((String)"world").getSpawnLocation().add(0.5, 0.0, 0.5));
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
    public void onPlayerSpawn(PlayerSpawnLocationEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPlayedBefore()) {
            this.plugin.getEconomyManager().addBalance(player.getUniqueId(), 250);
            event.setSpawnLocation(Bukkit.getWorld((String)"world").getSpawnLocation().add(0.5, 0.0, 0.5));
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory() instanceof EnderChest) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (event.getCause() == BlockIgniteEvent.IgniteCause.SPREAD) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getEntity() instanceof Squid) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getPlayer().getWorld().getName().contains("_the_end")) {
            if (event.getTo().getBlock().getType() == Material.STATIONARY_WATER) {
                World w = Bukkit.getServer().getWorld(HCF.getPlugin().getConfig().getString("endexit.world"));
                double x = HCF.getPlugin().getConfig().getDouble("endexit.x");
                double y = HCF.getPlugin().getConfig().getDouble("endexit.y");
                double z = HCF.getPlugin().getConfig().getDouble("endexit.z");
                event.getPlayer().teleport(new Location(w, x, y, z));
            }
        }
    }

    @EventHandler
    public void onMove1(PlayerMoveEvent event) {
        if (event.getPlayer().getWorld().getName().contains("_nether")) {
            if (event.getTo().getBlock().getType() == Material.STATIONARY_WATER) {
                event.getPlayer().teleport(new Location(Bukkit.getWorld("world"), 0, 75, 0));
            }
        }
    }

}

