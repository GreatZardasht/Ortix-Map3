package com.customhcf.hcf.staffmode.listener;

import com.customhcf.base.BasePlugin;
import com.customhcf.hcf.HCF;
import com.customhcf.hcf.command.VanishCommand;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

/**
 * Created by nyang on 3/25/2017.
 */
public class StaffListener implements Listener {

    @EventHandler
    public void onBuild (BlockPlaceEvent event) {
        if (HCF.toggle.contains(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak (BlockBreakEvent event) {
        if (HCF.toggle.contains(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventory (InventoryClickEvent event) {
        if (HCF.toggle.contains((Player)event.getWhoClicked())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop (PlayerDropItemEvent event) {
        if (HCF.toggle.contains(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickup (PlayerPickupItemEvent event) {
        if (HCF.toggle.contains(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onModLeave (PlayerQuitEvent event) {
        if (HCF.toggle.contains(event.getPlayer())) {
            HCF.toggle.remove(event.getPlayer());
            event.getPlayer().setGameMode(GameMode.SURVIVAL);
            event.getPlayer().getInventory().clear();
            BasePlugin.getPlugin().getUserManager().getUser(event.getPlayer().getUniqueId()).setStaffUtil(false);
        }
        if (VanishCommand.v.contains(event.getPlayer())) {
            VanishCommand.v.remove(event.getPlayer());
        }
    }


    @EventHandler
    public void fix(PlayerJoinEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            event.getPlayer().setGameMode(GameMode.SURVIVAL);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        Player player = (Player) event.getEntity();
        Player target  = (Player) event.getDamager();
        if (player instanceof Player) {
            if (target instanceof Player) {
                if (HCF.toggle.contains(target)) {
                    event.setCancelled(true);
                }
            }
        }
    }

}
