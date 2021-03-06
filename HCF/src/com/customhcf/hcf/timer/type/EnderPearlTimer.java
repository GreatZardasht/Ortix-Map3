/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.util.Config
 *  com.google.common.cache.Cache
 *  com.google.common.cache.CacheBuilder
 *  net.minecraft.server.v1_7_R4.Container
 *  net.minecraft.server.v1_7_R4.EntityPlayer
 *  net.minecraft.server.v1_7_R4.ItemStack
 *  net.minecraft.server.v1_7_R4.NetworkManager
 *  net.minecraft.server.v1_7_R4.Packet
 *  net.minecraft.server.v1_7_R4.PacketPlayOutSetSlot
 *  net.minecraft.server.v1_7_R4.PlayerConnection
 *  net.minecraft.server.v1_7_R4.PlayerInventory
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
 *  org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack
 *  org.bukkit.entity.EnderPearl
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.ProjectileLaunchEvent
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryDragEvent
 *  org.bukkit.event.player.PlayerItemHeldEvent
 *  org.bukkit.event.player.PlayerKickEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.projectiles.ProjectileSource
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package com.customhcf.hcf.timer.type;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.timer.PlayerTimer;
import com.customhcf.hcf.timer.TimerRunnable;
import com.customhcf.util.Config;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import net.minecraft.server.v1_7_R4.Container;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.ItemStack;
import net.minecraft.server.v1_7_R4.NetworkManager;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutSetSlot;
import net.minecraft.server.v1_7_R4.PlayerConnection;
import net.minecraft.server.v1_7_R4.PlayerInventory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class EnderPearlTimer
extends PlayerTimer
implements Listener {
    private static final long REFRESH_DELAY_TICKS = 2;
    private static final long REFRESH_DELAY_TICKS_18 = 20;
    private static final long EXPIRE_SHOW_MILLISECONDS = 1500;
    private final ConcurrentMap<Object, Object> itemNameFakes;
    private final JavaPlugin plugin;

    public EnderPearlTimer(JavaPlugin plugin) {
        super("EnderPearl", TimeUnit.SECONDS.toMillis(16));
        this.plugin = plugin;
        this.itemNameFakes = CacheBuilder.newBuilder().expireAfterWrite(this.defaultCooldown + 1500 + 5000, TimeUnit.MILLISECONDS).build().asMap();
    }

    @Override
    public String getScoreboardPrefix() {
        return ChatColor.YELLOW.toString() + (Object)ChatColor.BOLD;
    }

    @Override
    public void load(final Config config) {
        super.load(config);
        final Collection<UUID> cooldowned = this.cooldowns.keySet();
        for (final UUID uuid : cooldowned) {
            final Player player = Bukkit.getPlayer(uuid);
            if (player == null) {
                continue;
            }
            this.startDisplaying(player);
        }
    }


    @Override
    public void onExpire(UUID userUUID) {
        super.onExpire(userUUID);
        Player player = Bukkit.getPlayer((UUID)userUUID);
        if (player != null) {
            player.sendMessage((Object)ChatColor.GREEN + "You can now enderpearl!");
        }
    }

    @Override
    public TimerRunnable clearCooldown(UUID playerUUID) {
        TimerRunnable runnable = super.clearCooldown(playerUUID);
        if (runnable != null) {
            this.itemNameFakes.remove(playerUUID);
            return runnable;
        }
        return null;
    }

    @Override
    public void clearCooldown(Player player) {
        this.stopDisplaying(player);
        super.clearCooldown(player);
    }

    public void refund(Player player) {
        player.getInventory().addItem(new org.bukkit.inventory.ItemStack[]{new org.bukkit.inventory.ItemStack(Material.ENDER_PEARL, 1)});
        this.clearCooldown(player);
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        ProjectileSource source;
        EnderPearl enderPearl;
        Projectile projectile = event.getEntity();
        if (projectile instanceof EnderPearl && (source = (enderPearl = (EnderPearl)projectile).getShooter()) instanceof Player) {
            Player shooter = (Player)source;
            long remaining = this.getRemaining(shooter);
            if (remaining > 0) {
                shooter.sendMessage((Object)ChatColor.RED + "You cannot use this for another " + (Object)ChatColor.BOLD + HCF.getRemaining(remaining, true, false) + (Object)ChatColor.RED + '.');
                event.setCancelled(true);
                return;
            }
            if (this.setCooldown(shooter, shooter.getUniqueId(), this.defaultCooldown, true)) {
                this.startDisplaying(shooter);
            }
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.clearCooldown(event.getPlayer());
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPlayerKick(PlayerKickEvent event) {
        this.clearCooldown(event.getPlayer());
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        PearlNameFaker pearlNameFaker = (PearlNameFaker)((Object)this.itemNameFakes.get(player.getUniqueId()));
        if (pearlNameFaker != null) {
            int previousSlot = event.getPreviousSlot();
            org.bukkit.inventory.ItemStack item = player.getInventory().getItem(previousSlot);
            if (item == null) {
                return;
            }
            pearlNameFaker.setFakeItem(((CraftItemStack)item).handle, previousSlot);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onInventoryDrag(InventoryDragEvent event) {
        HumanEntity humanEntity = event.getWhoClicked();
        if (humanEntity instanceof Player) {
            Player player = (Player)humanEntity;
            PearlNameFaker pearlNameFaker = (PearlNameFaker)((Object)this.itemNameFakes.get(player.getUniqueId()));
            if (pearlNameFaker == null) {
                return;
            }
            for (Map.Entry entry : event.getNewItems().entrySet()) {
                if (((Integer)entry.getKey()).intValue() != player.getInventory().getHeldItemSlot()) continue;
                pearlNameFaker.setFakeItem(CraftItemStack.asNMSCopy((org.bukkit.inventory.ItemStack)player.getItemInHand()), player.getInventory().getHeldItemSlot());
                break;
            }
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onInventoryClick(InventoryClickEvent event) {
        HumanEntity humanEntity = event.getWhoClicked();
        if (humanEntity instanceof Player) {
            final Player player = (Player)humanEntity;
            PearlNameFaker pearlNameFaker = (PearlNameFaker)((Object)this.itemNameFakes.get(player.getUniqueId()));
            if (pearlNameFaker == null) {
                return;
            }
            int heldSlot = player.getInventory().getHeldItemSlot();
            if (event.getSlot() == heldSlot) {
                pearlNameFaker.setFakeItem(CraftItemStack.asNMSCopy((org.bukkit.inventory.ItemStack)player.getItemInHand()), heldSlot);
            } else if (event.getHotbarButton() == heldSlot) {
                pearlNameFaker.setFakeItem(CraftItemStack.asNMSCopy((org.bukkit.inventory.ItemStack)event.getCurrentItem()), event.getSlot());
                new BukkitRunnable(){

                    public void run() {
                        player.updateInventory();
                    }
                }.runTask((Plugin)this.plugin);
            }
        }
    }

    public void startDisplaying(Player player) {
        if (this.getRemaining(player) > 0) {
            PearlNameFaker pearlNameFaker = new PearlNameFaker(this, player);
            if (this.itemNameFakes.putIfAbsent(player.getUniqueId(), (Object)pearlNameFaker) == null) {
                long ticks = ((CraftPlayer)player).getHandle().playerConnection.networkManager.getVersion() >= 47 ? 20 : 2;
                pearlNameFaker.runTaskTimerAsynchronously((Plugin)this.plugin, ticks, ticks);
            }
        }
    }

    public void stopDisplaying(Player player) {
        PearlNameFaker pearlNameFaker = (PearlNameFaker)((Object)this.itemNameFakes.remove(player.getUniqueId()));
        if (pearlNameFaker != null) {
            pearlNameFaker.cancel();
        }
    }

    public static class PearlNameFaker
    extends BukkitRunnable {
        private final PlayerTimer timer;
        private final Player player;

        public PearlNameFaker(PlayerTimer timer, Player player) {
            this.timer = timer;
            this.player = player;
        }

        public void run() {
            org.bukkit.inventory.ItemStack stack = this.player.getItemInHand();
            if (stack != null && stack.getType() == Material.ENDER_PEARL) {
                long remaining = this.timer.getRemaining(this.player);
                ItemStack item = ((CraftItemStack)stack).handle;
                if (remaining > 0) {
                    item = item.cloneItemStack();
                    item.c((Object)ChatColor.YELLOW + "Enderpearl Cooldown: " + (Object)ChatColor.RED + HCF.getRemaining(remaining, true, true));
                    this.setFakeItem(item, this.player.getInventory().getHeldItemSlot());
                } else {
                    this.cancel();
                }
            }
        }

        public synchronized void cancel() throws IllegalStateException {
            super.cancel();
            this.setFakeItem(CraftItemStack.asNMSCopy((org.bukkit.inventory.ItemStack)this.player.getItemInHand()), this.player.getInventory().getHeldItemSlot());
        }

        public void setFakeItem(ItemStack nms, int index) {
            EntityPlayer entityPlayer = ((CraftPlayer)this.player).getHandle();
            if (index < PlayerInventory.getHotbarSize()) {
                index += 36;
            } else if (index > 35) {
                index = 8 - (index - 36);
            }
            entityPlayer.playerConnection.sendPacket((Packet)new PacketPlayOutSetSlot(entityPlayer.activeContainer.windowId, index, nms));
        }
    }

}

