package com.customhcf.hcf.listener;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.faction.event.FactionFocusEvent;
import com.customhcf.hcf.faction.type.PlayerFaction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;

public class FocusListener
        implements Listener
{
    @EventHandler
    public void onQuit(PlayerQuitEvent e)
    {
        for (PlayerFaction playerFaction : HCF.getPlugin().getFactionManager().getPlayerFactions()) {
            if ((playerFaction.getFocus() != null) && (playerFaction.getFocus() == e.getPlayer().getUniqueId()))
            {
                Bukkit.getPluginManager().callEvent(new FactionFocusEvent(playerFaction, e.getPlayer(), null));
                playerFaction.setFocus(null);
                return;
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e)
    {
        for (PlayerFaction playerFaction : HCF.getPlugin().getFactionManager().getPlayerFactions()) {
            if ((playerFaction.getFocus() != null) && (playerFaction.getFocus() == e.getEntity().getUniqueId()))
            {
                Bukkit.getPluginManager().callEvent(new FactionFocusEvent(playerFaction, e.getEntity(), null));
                playerFaction.setFocus(null);
                return;
            }
        }
    }
}
