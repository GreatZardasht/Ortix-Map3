/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.util.BukkitUtils
 *  com.customhcf.util.cuboid.Cuboid
 *  com.google.common.collect.ImmutableCollection
 *  com.google.common.collect.ImmutableMultimap
 *  com.google.common.collect.ImmutableMultimap$Builder
 *  com.google.common.collect.ImmutableSet
 *  com.google.common.collect.Sets
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.TravelAgent
 *  org.bukkit.World
 *  org.bukkit.World$Environment
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.block.BlockState
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.AnimalTamer
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Hanging
 *  org.bukkit.entity.Horse
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.ThrownPotion
 *  org.bukkit.entity.Vehicle
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.block.BlockBurnEvent
 *  org.bukkit.event.block.BlockFadeEvent
 *  org.bukkit.event.block.BlockFormEvent
 *  org.bukkit.event.block.BlockFromToEvent
 *  org.bukkit.event.block.BlockIgniteEvent
 *  org.bukkit.event.block.BlockIgniteEvent$IgniteCause
 *  org.bukkit.event.block.BlockPistonExtendEvent
 *  org.bukkit.event.block.BlockPistonRetractEvent
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.block.LeavesDecayEvent
 *  org.bukkit.event.entity.CreatureSpawnEvent
 *  org.bukkit.event.entity.CreatureSpawnEvent$SpawnReason
 *  org.bukkit.event.entity.EntityChangeBlockEvent
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.event.entity.EntityTargetEvent
 *  org.bukkit.event.entity.EntityTargetEvent$TargetReason
 *  org.bukkit.event.entity.FoodLevelChangeEvent
 *  org.bukkit.event.entity.PotionSplashEvent
 *  org.bukkit.event.hanging.HangingBreakByEntityEvent
 *  org.bukkit.event.hanging.HangingPlaceEvent
 *  org.bukkit.event.player.PlayerBucketEmptyEvent
 *  org.bukkit.event.player.PlayerBucketFillEvent
 *  org.bukkit.event.player.PlayerInteractEntityEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerPortalEvent
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.event.vehicle.VehicleEnterEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.material.Cauldron
 *  org.bukkit.material.MaterialData
 *  org.bukkit.projectiles.ProjectileSource
 */
package com.customhcf.hcf.listener;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.faction.event.CaptureZoneEnterEvent;
import com.customhcf.hcf.faction.event.CaptureZoneLeaveEvent;
import com.customhcf.hcf.faction.event.PlayerClaimEnterEvent;
import com.customhcf.hcf.faction.struct.Raidable;
import com.customhcf.hcf.faction.struct.Role;
import com.customhcf.hcf.faction.type.*;
import com.customhcf.hcf.kothgame.CaptureZone;
import com.customhcf.hcf.kothgame.faction.CapturableFaction;
import com.customhcf.hcf.kothgame.faction.EventFaction;
import com.customhcf.hcf.kothgame.glowstone.GlowstoneFaction;
import com.customhcf.util.BukkitUtils;
import com.customhcf.util.cuboid.Cuboid;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TravelAgent;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.material.Cauldron;
import org.bukkit.material.MaterialData;
import org.bukkit.projectiles.ProjectileSource;

public class FactionsCoreListener
        implements Listener
{
    public static final String PROTECTION_BYPASS_PERMISSION = "faction.protection.bypass";
    private static final ImmutableMultimap<Object, Object> ITEM_BLOCK_INTERACTABLES = ImmutableMultimap.builder().put(Material.DIAMOND_HOE, Material.GRASS).put(Material.GOLD_HOE, Material.GRASS).put(Material.IRON_HOE, Material.GRASS).put(Material.STONE_HOE, Material.GRASS).put(Material.WOOD_HOE, Material.GRASS).build();
    private static final ImmutableSet<Material> BLOCK_INTERACTABLES = Sets.immutableEnumSet(Material.BED, new Material[] { Material.BED_BLOCK, Material.BEACON, Material.FENCE_GATE, Material.IRON_DOOR, Material.TRAP_DOOR, Material.WOOD_DOOR, Material.WOODEN_DOOR, Material.IRON_DOOR_BLOCK, Material.CHEST, Material.TRAPPED_CHEST, Material.FURNACE, Material.BURNING_FURNACE, Material.BREWING_STAND, Material.HOPPER, Material.DROPPER, Material.DISPENSER, Material.STONE_BUTTON, Material.WOOD_BUTTON, Material.ENCHANTMENT_TABLE, Material.WORKBENCH, Material.ANVIL, Material.LEVER, Material.FIRE });
    private final HCF plugin;

    public FactionsCoreListener(HCF plugin)
    {
        this.plugin = plugin;
    }

    public static boolean attemptBuild(Entity entity, Location location, String denyMessage)
    {
        return attemptBuild(entity, location, denyMessage, false);
    }

    @EventHandler
    public void checkblock(BlockPlaceEvent e)
    {
        Faction factionatt = HCF.getPlugin().getFactionManager().getFactionAt(e.getPlayer().getLocation());
        if (((factionatt instanceof GlowstoneFaction)) &&
                (!e.getPlayer().isOp()))
        {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.YELLOW + "You may not place blocks in the territory of " + ChatColor.GOLD + "Glowstone Island" + ChatColor.YELLOW + ".");
        }
    }

    public static boolean attemptBuild(Entity entity, Location location, String denyMessage, boolean isInteraction)
    {
        boolean result = false;
        if ((entity instanceof Player))
        {
            Player player = (Player)entity;
            if ((player.getGameMode() == GameMode.CREATIVE) && (player.hasPermission("faction.protection.bypass"))) {
                return true;
            }
            if (player.getWorld().getEnvironment() == World.Environment.THE_END)
            {
                player.sendMessage(ChatColor.RED + "You cannot build in the end.");
                return false;
            }
            Faction factionAt = HCF.getPlugin().getFactionManager().getFactionAt(location);
            if (!(factionAt instanceof ClaimableFaction)) {
                result = true;
            } else if (((factionAt instanceof Raidable)) && (((Raidable)factionAt).isRaidable())) {
                result = true;
            }
            if (((factionAt instanceof GlowstoneFaction)) &&
                    (entity.getWorld().getBlockAt(location).getType() == Material.GLOWSTONE) &&
                    (((GlowstoneFaction)factionAt).getGlowstoneArea().contains(location))) {
                result = true;
            }
            if (((factionAt instanceof RoadFaction)) &&
                    (player.getLocation().getBlockY() < 30)) {
                result = true;
            }
            if ((factionAt instanceof PlayerFaction))
            {
                PlayerFaction playerFaction = HCF.getPlugin().getFactionManager().getPlayerFaction(player.getUniqueId());
                if ((playerFaction != null) && (playerFaction.equals(factionAt))) {
                    result = true;
                }
            }
            if (result)
            {
                if ((!isInteraction) && (Math.abs(location.getBlockX()) <= 128) && (Math.abs(location.getBlockZ()) <= 128))
                {
                    if (denyMessage != null) {
                        player.sendMessage(ChatColor.YELLOW + "You cannot build within " + ChatColor.GRAY + 128 + ChatColor.YELLOW + " blocks from spawn.");
                    }
                    return false;
                }
            }
            else if (denyMessage != null) {
                player.sendMessage(String.format(denyMessage, new Object[] { factionAt.getDisplayName(player) }));
            }
        }
        return result;
    }

    public static boolean canBuildAt(Location from, Location to)
    {
        Faction toFactionAt = HCF.getPlugin().getFactionManager().getFactionAt(to);
        if (((toFactionAt instanceof Raidable)) && (!((Raidable)toFactionAt).isRaidable()))
        {
            Faction fromFactionAt = HCF.getPlugin().getFactionManager().getFactionAt(from);
            if (!toFactionAt.equals(fromFactionAt)) {
                return false;
            }
        }
        return true;
    }

    private void handleMove(PlayerMoveEvent event, PlayerClaimEnterEvent.EnterCause enterCause)
    {
        Location from = event.getFrom();
        Location to = event.getTo();
        if ((from.getBlockX() == to.getBlockX()) && (from.getBlockZ() == to.getBlockZ())) {
            return;
        }
        Player player = event.getPlayer();
        boolean cancelled = false;
        Faction fromFaction = this.plugin.getFactionManager().getFactionAt(from);
        Faction toFaction = this.plugin.getFactionManager().getFactionAt(to);
        CapturableFaction capturableFaction;
        if (!Objects.equals(fromFaction, toFaction))
        {
            PlayerClaimEnterEvent calledEvent = new PlayerClaimEnterEvent(player, from, to, fromFaction, toFaction, enterCause);
            Bukkit.getPluginManager().callEvent(calledEvent);
            cancelled = calledEvent.isCancelled();
        }
        else if ((toFaction instanceof CapturableFaction))
        {
            capturableFaction = (CapturableFaction)toFaction;
            for (CaptureZone captureZone : capturableFaction.getCaptureZones())
            {
                Cuboid cuboid = captureZone.getCuboid();
                if (cuboid != null)
                {
                    boolean containsFrom = cuboid.contains(from);
                    boolean containsTo = cuboid.contains(to);
                    if ((containsFrom) && (!containsTo))
                    {
                        CaptureZoneLeaveEvent calledEvent2 = new CaptureZoneLeaveEvent(player, capturableFaction, captureZone);
                        Bukkit.getPluginManager().callEvent(calledEvent2);
                        cancelled = calledEvent2.isCancelled();
                        break;
                    }
                    if ((!containsFrom) && (containsTo))
                    {
                        CaptureZoneEnterEvent calledEvent3 = new CaptureZoneEnterEvent(player, capturableFaction, captureZone);
                        Bukkit.getPluginManager().callEvent(calledEvent3);
                        cancelled = calledEvent3.isCancelled();
                        break;
                    }
                }
            }
        }
        if (cancelled) {
            if (enterCause == PlayerClaimEnterEvent.EnterCause.TELEPORT)
            {
                event.setCancelled(true);
            }
            else
            {
                from.add(0.5D, 0.0D, 0.5D);
                event.setTo(from);
            }
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event)
    {
        handleMove(event, PlayerClaimEnterEvent.EnterCause.MOVEMENT);
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public void onPlayerMove(PlayerTeleportEvent event)
    {
        handleMove(event, PlayerClaimEnterEvent.EnterCause.TELEPORT);
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onBlockIgnite(BlockIgniteEvent event)
    {
        switch (event.getCause())
        {
        }
        Faction factionAt = this.plugin.getFactionManager().getFactionAt(event.getBlock().getLocation());
        if (((factionAt instanceof ClaimableFaction)) && (!(factionAt instanceof PlayerFaction))) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
    public void onStickyPistonExtend(BlockPistonExtendEvent event)
    {
        Block block = event.getBlock();
        Block targetBlock = block.getRelative(event.getDirection(), event.getLength() + 1);
        if ((targetBlock.isEmpty()) || (targetBlock.isLiquid()))
        {
            Faction targetFaction = this.plugin.getFactionManager().getFactionAt(targetBlock.getLocation());
            if (((targetFaction instanceof Raidable)) && (!((Raidable)targetFaction).isRaidable()) && (!targetFaction.equals(this.plugin.getFactionManager().getFactionAt(block)))) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
    public void onStickyPistonRetract(BlockPistonRetractEvent event)
    {
        if (!event.isSticky()) {
            return;
        }
        Location retractLocation = event.getRetractLocation();
        Block retractBlock = retractLocation.getBlock();
        if ((!retractBlock.isEmpty()) && (!retractBlock.isLiquid()))
        {
            Block block = event.getBlock();
            Faction targetFaction = this.plugin.getFactionManager().getFactionAt(retractLocation);
            if (((targetFaction instanceof Raidable)) && (!((Raidable)targetFaction).isRaidable()) && (!targetFaction.equals(this.plugin.getFactionManager().getFactionAt(block)))) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onBlockFromTo(BlockFromToEvent event)
    {
        Block toBlock = event.getToBlock();
        Block fromBlock = event.getBlock();
        Material fromType = fromBlock.getType();
        Material toType = toBlock.getType();
        if (((toType == Material.REDSTONE_WIRE) || (toType == Material.TRIPWIRE)) && ((fromType == Material.AIR) || (fromType == Material.STATIONARY_LAVA) || (fromType == Material.LAVA))) {
            toBlock.setType(Material.AIR);
        }
        if (((toBlock.getType() == Material.WATER) || (toBlock.getType() == Material.STATIONARY_WATER) || (toBlock.getType() == Material.LAVA) || (toBlock.getType() == Material.STATIONARY_LAVA)) && (!canBuildAt(fromBlock.getLocation(), toBlock.getLocation()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onPlayerTeleport(PlayerTeleportEvent event)
    {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL)
        {
            Faction toFactionAt = this.plugin.getFactionManager().getFactionAt(event.getTo());
            if ((toFactionAt.isSafezone()) && (!this.plugin.getFactionManager().getFactionAt(event.getFrom()).isSafezone()))
            {
                Player player = event.getPlayer();
                player.sendMessage(ChatColor.RED + "You cannot Enderpearl into safe-zones, used Enderpearl has been refunded.");
                this.plugin.getTimerManager().enderPearlTimer.refund(player);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onPlayerPortal(PlayerPortalEvent event)
    {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL)
        {
            Location from = event.getFrom();
            Location to = event.getTo();
            Player player = event.getPlayer();

            Faction fromFac = this.plugin.getFactionManager().getFactionAt(from);
            if (fromFac.isSafezone())
            {
                event.setTo(to.getWorld().getSpawnLocation().add(0.5D, 0.0D, 0.5D));
                event.useTravelAgent(false);
                player.sendMessage(ChatColor.YELLOW + "You have been teleported to Overworld Spawn.");
                return;
            }
            if ((event.useTravelAgent()) && (to.getWorld().getEnvironment() == World.Environment.NORMAL))
            {
                TravelAgent travelAgent = event.getPortalTravelAgent();
                if (!travelAgent.getCanCreatePortal()) {
                    return;
                }
                Location foundPortal = travelAgent.findPortal(to);
                if (foundPortal != null) {
                    return;
                }
                Faction factionAt = this.plugin.getFactionManager().getFactionAt(to);
                if ((factionAt instanceof ClaimableFaction))
                {
                    Faction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player.getUniqueId());
                    if (playerFaction != factionAt)
                    {
                        player.sendMessage(ChatColor.YELLOW + "Portal would have created portal in territory of " + factionAt.getDisplayName(player) + ChatColor.YELLOW + '.');
                        event.setCancelled(true);
                    }
                    else if ((factionAt instanceof SpawnFaction))
                    {
                        player.sendMessage(ChatColor.YELLOW + "This portal would send you to spawn so the teleport cancelled.");
                        event.setCancelled(true);
                    }
                    else if ((factionAt instanceof EventFaction))
                    {
                        player.sendMessage(ChatColor.YELLOW + "This portal would send you to an event faction so the portal event cancelled.");
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onEntityDamage(EntityDamageEvent event)
    {
        Entity entity = event.getEntity();
        if ((entity instanceof Player))
        {
            Player player = (Player)entity;
            Faction playerFactionAt = this.plugin.getFactionManager().getFactionAt(player.getLocation());
            EntityDamageEvent.DamageCause cause = event.getCause();
            if ((playerFactionAt.isSafezone()) && (cause != EntityDamageEvent.DamageCause.SUICIDE)) {
                event.setCancelled(true);
            }
            Player attacker = BukkitUtils.getFinalAttacker(event, true);
            if (attacker != null)
            {
                Faction attackerFactionAt = this.plugin.getFactionManager().getFactionAt(attacker.getLocation());
                if (attackerFactionAt.isSafezone())
                {
                    event.setCancelled(true);
                    this.plugin.getMessage().sendMessage(attacker, ChatColor.RED + "You cannot attack players whilst in safe-zones.");
                    return;
                }
                if (playerFactionAt.isSafezone())
                {
                    this.plugin.getMessage().sendMessage(attacker, ChatColor.RED + "You cannot attack players that are in safe-zones.");
                    return;
                }
                PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player.getUniqueId());
                PlayerFaction attackerFaction;
                if ((playerFaction != null) && ((attackerFaction = this.plugin.getFactionManager().getPlayerFaction(attacker.getUniqueId())) != null))
                {
                    Role role = playerFaction.getMember(player).getRole();
                    String astrix = role.getAstrix();
                    if (attackerFaction.equals(playerFaction))
                    {
                        this.plugin.getMessage().sendMessage(attacker, ChatColor.YELLOW + "You cannot hurt faction members " + ChatColor.GRAY + '[' + player.getName() + ']' + ChatColor.YELLOW + '.');
                        event.setCancelled(true);
                    }
                    else if (attackerFaction.getAllied().contains(playerFaction.getUniqueID()))
                    {
                        this.plugin.getMessage().sendMessage(attacker, ChatColor.YELLOW + "You are hurting an ally " + ChatColor.GRAY + '[' + player.getName() + ']' + ChatColor.YELLOW + '.');
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onVehicleEnter(VehicleEnterEvent event)
    {
        Entity entered = event.getEntered();
        if ((entered instanceof Player))
        {
            Vehicle vehicle = event.getVehicle();
            if ((vehicle instanceof Horse))
            {
                Horse horse = (Horse)event.getVehicle();
                AnimalTamer owner = horse.getOwner();
                if ((owner != null) && (!owner.equals(entered)))
                {
                    ((Player)entered).sendMessage(ChatColor.YELLOW + "You cannot ride a horse that belongs to " + ChatColor.RED + owner.getName() + ChatColor.YELLOW + '.');
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onFoodLevelChange(FoodLevelChangeEvent event)
    {
        Entity entity = event.getEntity();
        if (((entity instanceof Player)) && (((Player)entity).getFoodLevel() < event.getFoodLevel()) && (this.plugin.getFactionManager().getFactionAt(entity.getLocation()).isSafezone())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public void onPotionSplash(PotionSplashEvent event)
    {
        ThrownPotion potion = event.getEntity();
        if (!BukkitUtils.isDebuff(potion)) {
            return;
        }
        Faction factionAt = this.plugin.getFactionManager().getFactionAt(potion.getLocation());
        if (factionAt.isSafezone())
        {
            event.setCancelled(true);
            return;
        }
        ProjectileSource source = potion.getShooter();
        Player player;
        if ((source instanceof Player))
        {
            player = (Player)source;
            for (LivingEntity affected : event.getAffectedEntities()) {
                if (((affected instanceof Player)) && (!player.equals(affected)))
                {
                    Player target = (Player)affected;
                    if ((!target.equals(source)) &&

                            (this.plugin.getFactionManager().getFactionAt(target.getLocation()).isSafezone())) {
                        event.setIntensity(affected, 0.0D);
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onEntityTarget(EntityTargetEvent event)
    {
        switch (event.getReason())
        {
            case CLOSEST_PLAYER:
            case RANDOM_TARGET:
                Entity target = event.getTarget();
                if (((event.getEntity() instanceof LivingEntity)) && ((target instanceof Player)))
                {
                    Faction factionAt = this.plugin.getFactionManager().getFactionAt(target.getLocation());
                    Faction playerFaction;
                    if ((factionAt.isSafezone()) || (((playerFaction = this.plugin.getFactionManager().getPlayerFaction(target.getUniqueId())) != null) && (factionAt.equals(playerFaction)))) {
                        event.setCancelled(true);
                    }
                }
                break;
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (!event.hasBlock()) {
            return;
        }
        Block block = event.getClickedBlock();
        Action action = event.getAction();
        if ((action == Action.PHYSICAL) &&
                (!attemptBuild(event.getPlayer(), block.getLocation(), null))) {
            event.setCancelled(true);
        }
        if (action == Action.RIGHT_CLICK_BLOCK)
        {
            boolean canBuild = !BLOCK_INTERACTABLES.contains(block.getType());
            if (canBuild)
            {
                Material itemType = event.hasItem() ? event.getItem().getType() : null;
                if ((itemType != null) && (ITEM_BLOCK_INTERACTABLES.containsKey(itemType)) && (ITEM_BLOCK_INTERACTABLES.get(itemType).contains(event.getClickedBlock().getType())))
                {
                    canBuild = false;
                }
                else
                {
                    MaterialData materialData = block.getState().getData();
                    if ((materialData instanceof Cauldron))
                    {
                        Cauldron cauldron = (Cauldron)materialData;
                        if ((!cauldron.isEmpty()) && (event.hasItem()) && (event.getItem().getType() == Material.GLASS_BOTTLE)) {
                            canBuild = false;
                        }
                    }
                }
            }
            if ((!canBuild) && (!attemptBuild(event.getPlayer(), block.getLocation(), ChatColor.YELLOW + "You cannot do this in the territory of %1$s" + ChatColor.YELLOW + '.', true))) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onBlockBurn(BlockBurnEvent event)
    {
        Faction factionAt = this.plugin.getFactionManager().getFactionAt(event.getBlock().getLocation());
        if (((factionAt instanceof WarzoneFaction)) || (((factionAt instanceof Raidable)) && (!((Raidable)factionAt).isRaidable()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onBlockFade(BlockFadeEvent event)
    {
        Faction factionAt = this.plugin.getFactionManager().getFactionAt(event.getBlock().getLocation());
        if (((factionAt instanceof ClaimableFaction)) && (!(factionAt instanceof PlayerFaction))) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onLeavesDelay(LeavesDecayEvent event)
    {
        Faction factionAt = this.plugin.getFactionManager().getFactionAt(event.getBlock().getLocation());
        if (((factionAt instanceof ClaimableFaction)) && (!(factionAt instanceof PlayerFaction))) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onBlockForm(BlockFormEvent event)
    {
        Faction factionAt = this.plugin.getFactionManager().getFactionAt(event.getBlock().getLocation());
        if (((factionAt instanceof ClaimableFaction)) && (!(factionAt instanceof PlayerFaction))) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onEntityChangeBlock(EntityChangeBlockEvent event)
    {
        Entity entity = event.getEntity();
        if (((entity instanceof LivingEntity)) && (!attemptBuild(entity, event.getBlock().getLocation(), null))) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event)
    {
        if (!attemptBuild(event.getPlayer(), event.getBlock().getLocation(), ChatColor.YELLOW + "You cannot build in the territory of %1$s" + ChatColor.YELLOW + '.')) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onBlockPlace(BlockPlaceEvent event)
    {
        if (!attemptBuild(event.getPlayer(), event.getBlockPlaced().getLocation(), ChatColor.YELLOW + "You cannot build in the territory of %1$s" + ChatColor.YELLOW + '.')) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onBucketFill(PlayerBucketFillEvent event)
    {
        if (!attemptBuild(event.getPlayer(), event.getBlockClicked().getLocation(), ChatColor.YELLOW + "You cannot build in the territory of %1$s" + ChatColor.YELLOW + '.')) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onBucketEmpty(PlayerBucketEmptyEvent event)
    {
        if (!attemptBuild(event.getPlayer(), event.getBlockClicked().getLocation(), ChatColor.YELLOW + "You cannot build in the territory of %1$s" + ChatColor.YELLOW + '.')) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event)
    {
        Entity remover = event.getRemover();
        if (((remover instanceof Player)) && (!attemptBuild(remover, event.getEntity().getLocation(), ChatColor.YELLOW + "You cannot build in the territory of %1$s" + ChatColor.YELLOW + '.'))) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onHangingPlace(HangingPlaceEvent event)
    {
        if (!attemptBuild(event.getPlayer(), event.getEntity().getLocation(), ChatColor.YELLOW + "You cannot build in the territory of %1$s" + ChatColor.YELLOW + '.')) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onHangingDamageByEntity(EntityDamageByEntityEvent event)
    {
        Entity entity = event.getEntity();
        if ((entity instanceof Hanging))
        {
            Player attacker = BukkitUtils.getFinalAttacker(event, false);
            if (!attemptBuild(attacker, entity.getLocation(), ChatColor.YELLOW + "You cannot build in the territory of %1$s" + ChatColor.YELLOW + '.')) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.LOW)
    public void onHangingInteractByPlayer(PlayerInteractEntityEvent event)
    {
        Entity entity = event.getRightClicked();
        if (((entity instanceof Hanging)) && (!attemptBuild(event.getPlayer(), entity.getLocation(), ChatColor.YELLOW + "You cannot build in the territory of %1$s" + ChatColor.YELLOW + '.'))) {
            event.setCancelled(true);
        }
    }
}
