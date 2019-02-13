/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  com.google.common.collect.Sets
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockState
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.inventory.InventoryHolder
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.util.Vector
 */
package com.customhcf.hcf.fixes;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.faction.FactionManager;
import com.customhcf.hcf.faction.struct.Relation;
import com.customhcf.hcf.faction.type.ClaimableFaction;
import com.customhcf.hcf.faction.type.Faction;
import com.customhcf.hcf.faction.type.PlayerFaction;
import com.customhcf.hcf.faction.type.WarzoneFaction;
import com.customhcf.hcf.timer.TimerManager;
import com.customhcf.hcf.timer.type.EnderPearlTimer;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class PearlGlitchListener
        implements Listener {
    private final ImmutableSet<Material> blockedPearlTypes = Sets.immutableEnumSet(Material.THIN_GLASS, new Material[]{Material.IRON_FENCE, Material.FENCE, Material.NETHER_FENCE, Material.FENCE_GATE, Material.ACACIA_STAIRS, Material.BIRCH_WOOD_STAIRS, Material.BRICK_STAIRS, Material.COBBLESTONE_STAIRS, Material.DARK_OAK_STAIRS, Material.JUNGLE_WOOD_STAIRS, Material.NETHER_BRICK_STAIRS, Material.QUARTZ_STAIRS, Material.SANDSTONE_STAIRS, Material.SMOOTH_STAIRS, Material.SPRUCE_WOOD_STAIRS, Material.WOOD_STAIRS, Material.WOOD_STEP, Material.WOOD_DOUBLE_STEP, Material.STEP, Material.DOUBLE_STEP});
    private final HCF plugin;

    public PearlGlitchListener(HCF plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block block;
        Faction factionAt;
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.hasItem() && event.getItem().getType() == Material.ENDER_PEARL && (block = event.getClickedBlock()).getType().isSolid() && !(block.getState() instanceof InventoryHolder) && (factionAt = HCF.getPlugin().getFactionManager().getFactionAt(block.getLocation())) instanceof ClaimableFaction) {
            event.setCancelled(true);
            Player player = event.getPlayer();
            player.setItemInHand(event.getItem());
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
    public void onPearlClip(PlayerTeleportEvent event) {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            Location to = event.getTo();
            if (this.blockedPearlTypes.contains((Object)to.getBlock().getType())) {
                Player player = event.getPlayer();
                boolean refund = true;
                player.sendMessage((Object)ChatColor.DARK_PURPLE + ChatColor.BOLD.toString() + "Pearl has been removed for possible pearl glitching!");
                this.plugin.getTimerManager().enderPearlTimer.refund(player);
                event.setCancelled(true);
                return;
            }
            to.setX((double)to.getBlockX() + 0.5);
            to.setZ((double)to.getBlockZ() + 0.5);
            event.setTo(to);
        }
    }
}

