package com.customhcf.hcf.listener;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.faction.type.Faction;
import com.customhcf.hcf.kothgame.faction.KothFaction;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * Created by nyang on 4/2/2017.
 */
public class KothCapListener implements Listener {

    @EventHandler
    public void onCommandProccess(PlayerCommandPreprocessEvent event) {
        String msg = event.getMessage().toLowerCase();
        Faction kothFaction =  HCF.getPlugin().getFactionManager().getFactionAt(event.getPlayer().getLocation());
        if (msg.equalsIgnoreCase("/f home")) {
            if (kothFaction.getName().contains("koth")) {
                event.setCancelled(true);
            }
        }
    }
}
