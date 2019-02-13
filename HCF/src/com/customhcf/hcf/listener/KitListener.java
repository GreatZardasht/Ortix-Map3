/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.base.kit.event.KitApplyEvent
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 */
package com.customhcf.hcf.listener;

import com.customhcf.base.kit.event.KitApplyEvent;
import com.customhcf.hcf.HCF;
import com.customhcf.hcf.faction.FactionManager;
import com.customhcf.hcf.faction.type.Faction;
import com.customhcf.hcf.faction.type.PlayerFaction;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class KitListener
implements Listener {
    private final HCF plugin;

    public KitListener(HCF plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public void onKitApply(KitApplyEvent event) {
        PlayerFaction playerFaction;
        Player player = event.getPlayer();
        Location location = player.getLocation();
        Faction factionAt = this.plugin.getFactionManager().getFactionAt(location);
        if (!(factionAt.isSafezone() || (playerFaction = this.plugin.getFactionManager().getPlayerFaction(player)) != null && playerFaction.equals(factionAt))) {
            player.sendMessage((Object)ChatColor.RED + "Kits can only be applied in safe-zones or your own claims.");
            event.setCancelled(true);
        }
    }
}

