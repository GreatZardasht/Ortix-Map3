/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Boat
 *  org.bukkit.entity.Vehicle
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.vehicle.VehicleCreateEvent
 */
package com.customhcf.hcf.fixes;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleCreateEvent;

public class BoatGlitchFixListener
implements Listener {
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public void onVehicleCreate(VehicleCreateEvent event) {
        Block belowBlock;
        Boat boat;
        Vehicle vehicle = event.getVehicle();
        if (vehicle instanceof Boat && (belowBlock = (boat = (Boat)vehicle).getLocation().add(0.0, -1.0, 0.0).getBlock()).getType() != Material.WATER && belowBlock.getType() != Material.STATIONARY_WATER) {
            boat.remove();
        }
    }
}

