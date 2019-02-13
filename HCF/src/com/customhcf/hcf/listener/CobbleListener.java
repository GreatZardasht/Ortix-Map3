package com.customhcf.hcf.listener;

import com.customhcf.hcf.command.CobbleCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Nathan'PC on 12/20/2016.
 */
public class CobbleListener implements Listener {

    @EventHandler
    public void onCobblePickup(PlayerPickupItemEvent e)
    {
        Player p = e.getPlayer();
        if ((CobbleCommand.cobbletoggle.contains(p.getName())) && (e.getItem().getItemStack().getType() == Material.COBBLESTONE)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        Player p = event.getPlayer();
        CobbleCommand.cobbletoggle.remove(p.getName());
    }
}
