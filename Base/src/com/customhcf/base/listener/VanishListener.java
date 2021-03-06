/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_7_R4.Block
 *  net.minecraft.server.v1_7_R4.BlockChest
 *  net.minecraft.server.v1_7_R4.Blocks
 *  net.minecraft.server.v1_7_R4.EntityPlayer
 *  net.minecraft.server.v1_7_R4.Packet
 *  net.minecraft.server.v1_7_R4.PacketPlayOutBlockAction
 *  net.minecraft.server.v1_7_R4.PlayerConnection
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockState
 *  org.bukkit.block.Chest
 *  org.bukkit.block.DoubleChest
 *  org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.ExperienceOrb
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.event.entity.EntityTargetEvent
 *  org.bukkit.event.entity.EntityTargetEvent$TargetReason
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.event.inventory.InventoryType
 *  org.bukkit.event.player.PlayerBucketEmptyEvent
 *  org.bukkit.event.player.PlayerDropItemEvent
 *  org.bukkit.event.player.PlayerInteractEntityEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerPickupItemEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.inventory.DoubleChestInventory
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryHolder
 *  org.bukkit.inventory.InventoryView
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package com.customhcf.base.listener;

import com.customhcf.base.BasePlugin;
import com.customhcf.base.StaffPriority;
import com.customhcf.base.event.PlayerVanishEvent;
import com.customhcf.base.user.BaseUser;
import com.customhcf.base.user.UserManager;
import com.customhcf.util.BukkitUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.v1_7_R4.BlockChest;
import net.minecraft.server.v1_7_R4.Blocks;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutBlockAction;
import net.minecraft.server.v1_7_R4.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class VanishListener
implements Listener {
    private static final String CHEST_INTERACT_PERMISSION = "vanish.chestinteract";
    private static final String INVENTORY_INTERACT_PERMISSION = "vanish.inventorysee";
    private static final String FAKE_CHEST_PREFIX = "[F] ";
    private static final String BLOCK_INTERACT_PERMISSION = "vanish.build";
    private final Map<UUID, Location> fakeChestLocationMap = new HashMap<UUID, Location>();
    private final Set<Player> onlineVanishedPlayers = new HashSet<Player>();
    private final BasePlugin plugin;

    public VanishListener(BasePlugin plugin) {
        this.plugin = plugin;
    }

    public static void handleFakeChest(Player player, Chest chest, boolean open) {
        Inventory chestInventory = chest.getInventory();
        if (chestInventory instanceof DoubleChestInventory) {
            chest = (Chest)((DoubleChestInventory)chestInventory).getHolder().getLeftSide();
        }
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)new PacketPlayOutBlockAction(chest.getX(), chest.getY(), chest.getZ(), (net.minecraft.server.v1_7_R4.Block)Blocks.CHEST, 1, open ? 1 : 0));
        player.playSound(chest.getLocation(), open ? Sound.CHEST_OPEN : Sound.CHEST_CLOSE, 1.0f, 1.0f);
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        BaseUser baseUser;
        StaffPriority selfPriority;
        Player player = event.getPlayer();
        if (!this.onlineVanishedPlayers.isEmpty() && (selfPriority = StaffPriority.of(player)) != StaffPriority.ADMIN) {
            for (Player target : this.onlineVanishedPlayers) {
                if (!this.plugin.getUserManager().getUser(target.getUniqueId()).isVanished() || !StaffPriority.of(target).isMoreThan(selfPriority)) continue;
                player.hidePlayer(target);
            }
        }
        if ((baseUser = this.plugin.getUserManager().getUser(player.getUniqueId())).isVanished()) {
            this.onlineVanishedPlayers.add(player);
            player.sendMessage((Object)ChatColor.YELLOW + "You have joined vanished.");
            ArrayList<String> vanished = new ArrayList<String>();
            for (Player on : Bukkit.getServer().getOnlinePlayers()) {
                if (!on.hasPermission("vanish.chestinteract") || player.equals((Object)on) || !this.plugin.getUserManager().getUser(on.getUniqueId()).isVanished() || !player.canSee(on)) continue;
                vanished.add(on.getName());
            }
            player.sendMessage((Object)ChatColor.BLUE + "There are currently " + (Object)ChatColor.AQUA + vanished.size() + (Object)ChatColor.BLUE + " staff online and vanished.");
            baseUser.updateVanishedState(player, true);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (this.plugin.getUserManager().getUser(event.getPlayer().getUniqueId()).isVanished()) {
            this.onlineVanishedPlayers.remove((Object)event.getPlayer());
            event.setQuitMessage(null);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPlayerVanish(PlayerVanishEvent event) {
        if (event.isVanished()) {
            this.onlineVanishedPlayers.add(event.getPlayer());
        } else {
            this.onlineVanishedPlayers.remove((Object)event.getPlayer());
        }
    }


    @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
    public void onEntityTarget(EntityTargetEvent event) {
        if (event.getReason() == EntityTargetEvent.TargetReason.CUSTOM) {
            return;
        }
        Entity target = event.getTarget();
        Entity entity = event.getEntity();
        if ((entity instanceof ExperienceOrb || entity instanceof LivingEntity) && target instanceof Player && this.plugin.getUserManager().getUser(target.getUniqueId()).isVanished()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent e) {
        if (this.plugin.getUserManager().getUser(e.getPlayer().getUniqueId()).isVanished()) {
            e.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if (this.plugin.getUserManager().getUser(event.getPlayer().getUniqueId()).isVanished()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (this.plugin.getUserManager().getUser(event.getEntity().getUniqueId()).isVanished()) {
            event.setDeathMessage(null);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
    public void onEntityDamage(EntityDamageEvent event) {
        EntityDamageEvent.DamageCause cause = event.getCause();
        if (cause == EntityDamageEvent.DamageCause.VOID || cause == EntityDamageEvent.DamageCause.SUICIDE) {
            return;
        }
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player attacked = (Player)entity;
            BaseUser attackedUser = this.plugin.getUserManager().getUser(attacked.getUniqueId());
            Player attacker = BukkitUtils.getFinalAttacker(event, true);
            if (attackedUser.isVanished()) {
                if (attacker != null && StaffPriority.of(attacked) != StaffPriority.NONE) {
                    attacker.sendMessage((Object)ChatColor.RED + "That player is vanished.");
                }
                event.setCancelled(true);
                return;
            }
            if (attacker != null && this.plugin.getUserManager().getUser(attacker.getUniqueId()).isVanished()) {
                attacker.sendMessage((Object)ChatColor.RED + "You cannot attack players whilst vanished.");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        BaseUser baseUser = this.plugin.getUserManager().getUser(player.getUniqueId());
        if (baseUser.isVanished() && !player.hasPermission("vanish.build")) {
            event.setCancelled(true);
            player.sendMessage((Object)ChatColor.RED + "You cannot build whilst vanished.");
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        BaseUser baseUser = this.plugin.getUserManager().getUser(player.getUniqueId());
        if (baseUser.isVanished() && !player.hasPermission("vanish.build")) {
            event.setCancelled(true);
            player.sendMessage((Object)ChatColor.RED + "You cannot build whilst vanished.");
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        BaseUser baseUser = this.plugin.getUserManager().getUser(player.getUniqueId());
        if (baseUser.isVanished() && !player.hasPermission("vanish.build")) {
            event.setCancelled(true);
            player.sendMessage((Object)ChatColor.RED + "You cannot build whilst vanished.");
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        switch (event.getAction()) {
            case PHYSICAL: {
                if (!this.plugin.getUserManager().getUser(uuid).isVanished()) break;
                event.setCancelled(true);
                break;
            }
            case RIGHT_CLICK_BLOCK: {
                Block block = event.getClickedBlock();
                BlockState state = block.getState();
                if (!(state instanceof Chest) || !this.plugin.getUserManager().getUser(uuid).isVanished()) break;
                Chest chest = (Chest)state;
                Location chestLocation = chest.getLocation();
                InventoryType type = chest.getInventory().getType();
                if (type != InventoryType.CHEST || this.fakeChestLocationMap.putIfAbsent(uuid, chestLocation) != null) break;
                ItemStack[] contents = chest.getInventory().getContents();
                Inventory fakeInventory = Bukkit.createInventory((InventoryHolder)null, (int)contents.length, (String)("[F] " + type.getDefaultTitle()));
                fakeInventory.setContents(contents);
                event.setCancelled(true);
                player.openInventory(fakeInventory);
                VanishListener.handleFakeChest(player, chest, true);
                this.fakeChestLocationMap.put(uuid, chestLocation);
                break;
            }
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
    public void onInventoryClose(InventoryCloseEvent event) {
        BlockState blockState;
        Player player = (Player)event.getPlayer();
        Location chestLocation = this.fakeChestLocationMap.remove(player.getUniqueId());
        if (chestLocation != null && (blockState = chestLocation.getBlock().getState()) instanceof Chest) {
            VanishListener.handleFakeChest(player, (Chest)blockState, false);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
    public void onInventoryClick(InventoryClickEvent event) {
        Player player;
        ItemStack stack;
        HumanEntity humanEntity = event.getWhoClicked();
        if (humanEntity instanceof Player && this.fakeChestLocationMap.containsKey((player = (Player)humanEntity).getUniqueId()) && (stack = event.getCurrentItem()) != null && stack.getType() != Material.AIR && !player.hasPermission("vanish.chestinteract")) {
            event.setCancelled(true);
            player.sendMessage((Object)ChatColor.RED + "You cannot interact with fake chest inventories.");
        }
    }

}

