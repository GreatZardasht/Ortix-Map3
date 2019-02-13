package com.customhcf.hcf.listener;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.timer.type.PvpProtectionTimer;
import com.customhcf.hcf.timer.type.SpawnTagTimer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class ElevatorClass
        implements Listener {
    HCF plugin;
    private boolean elevatorSign;
    public ElevatorClass(HCF plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        if (e.getLine(0).equalsIgnoreCase("[Elevator]") && e.getLine(1).equalsIgnoreCase("Up")) {
            e.setLine(0, (Object)ChatColor.GREEN + "[Elevator]");
            e.setLine(1, (Object)ChatColor.YELLOW + "UP");
        }
        if (e.getLine(0).equalsIgnoreCase("[Elevator]") && e.getLine(1).equalsIgnoreCase("Down")) {
            e.setLine(0, (Object)ChatColor.GREEN + "[Elevator]");
            e.setLine(1, (Object)ChatColor.YELLOW + "DOWN");
        }

    }

    @EventHandler
    public void onSignElevator(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) {
            return;
        }

        Block block = e.getClickedBlock();
        BlockState state = block.getState();
        if (state instanceof Sign) {
            Sign sign = (Sign)state;
            String lineZero = sign.getLine(0);
            String lineOne = sign.getLine(1);
            if (!ChatColor.stripColor(lineZero).equalsIgnoreCase("[Elevator]") && !ChatColor.stripColor(lineOne).equalsIgnoreCase("UP")) {
                return;
            }
            e.getPlayer().teleport(this.teleportSpot(sign.getLocation(), sign.getLocation().getBlockY(), 254));
            return;
        }
    }

    @EventHandler
    public void onSignElevator1(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) {
            return;
        }

        Block block = e.getClickedBlock();
        BlockState state = block.getState();
        if (state instanceof Sign) {
            Sign sign = (Sign)state;
            String lineZero = sign.getLine(0);
            String lineOne = sign.getLine(1);
            if (!ChatColor.stripColor(lineZero).equalsIgnoreCase("[Elevator]") && !ChatColor.stripColor(lineOne).equalsIgnoreCase("DOWN")) {
                return;
            }
            e.getPlayer().teleport(this.teleportSpot1(sign.getLocation(), sign.getLocation().getBlockY(), 254));
            return;
        }
    }

    public void tele(Player player, Location from) {
        player.teleport(this.teleportSpot(from, from.getBlockY(), 254));
    }


    public Location teleportSpot(Location loc, int min, int max) {
        for (int k = min; k < max; ++k) {
            Material m1 = new Location(loc.getWorld(), (double)loc.getBlockX(), (double)k, (double)loc.getBlockZ()).getBlock().getType();
            Material m2 = new Location(loc.getWorld(), (double)loc.getBlockX(), (double)(k + 1), (double)loc.getBlockZ()).getBlock().getType();
            if (!m1.equals((Object)Material.AIR) || !m2.equals((Object)Material.AIR)) continue;
            return new Location(loc.getWorld(), (double)loc.getBlockX(), (double)k, (double)loc.getBlockZ());
        }
        return new Location(loc.getWorld(), (double)loc.getBlockX(), (double)loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ()), (double)loc.getBlockZ());
    }

    public Location teleportSpot1(Location loc, int min, int max) {
        for (int k = min; k < max; ++k) {
            Material m1 = new Location(loc.getWorld(), (double)loc.getBlockX(), (double)k, (double)loc.getBlockZ()).getBlock().getType();
            Material m2 = new Location(loc.getWorld(), (double)loc.getBlockX(), (double)(k - 1), (double)loc.getBlockZ()).getBlock().getType();
            if (!m1.equals((Object)Material.AIR) || !m2.equals((Object)Material.AIR)) continue;
            return new Location(loc.getWorld(), (double)loc.getBlockX(), (double)k, (double)loc.getBlockZ());
        }
        return new Location(loc.getWorld(), (double)loc.getBlockX(), (double)loc.getBlockY() - 3, (double)loc.getBlockZ());
    }
}

