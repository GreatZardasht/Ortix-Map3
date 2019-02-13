package com.customhcf.hcf.listener;

import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

/**
 * Created by Nathan'PC on 11/13/2016.
 */
public class CreeperFriendlyListener implements Listener {

    @EventHandler
    public void onAgro(EntityExplodeEvent evt){
        if (evt.getEntity() instanceof Creeper) {
            evt.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetLivingEntityEvent event){
        if (event.getTarget() instanceof Player){
            if (event.getEntity() instanceof Creeper){
                event.setCancelled(true);
                return;
            }
        }
    }
}
