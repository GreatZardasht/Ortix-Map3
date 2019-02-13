/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.base.BasePlugin
 *  com.customhcf.base.PlayTimeManager
 *  net.md_5.bungee.api.ChatColor
 *  org.apache.commons.lang3.time.DurationFormatUtils
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 */
package com.customhcf.hcf.fixes;

import com.customhcf.base.BasePlugin;
import com.customhcf.base.PlayTimeManager;
import com.customhcf.hcf.HCF;
import com.customhcf.hcf.faction.FactionManager;
import com.customhcf.hcf.faction.type.Faction;
import com.customhcf.hcf.faction.type.PlayerFaction;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PhaseListener
implements Listener {
    long gravityBlock = TimeUnit.HOURS.toMillis(6);
    long utilityBlock = TimeUnit.HOURS.toMillis(3);

    @EventHandler
    public void onMove(PlayerInteractEvent e) {
        if (e.getPlayer().getLocation().getBlock() != null && e.getPlayer().getLocation().getBlock().getType() == Material.TRAP_DOOR && !HCF.getPlugin().getFactionManager().getFactionAt(e.getPlayer().getLocation()).equals(HCF.getPlugin().getFactionManager().getPlayerFaction(e.getPlayer().getUniqueId()))) {
            e.getPlayer().sendMessage((Object)ChatColor.RED + "Glitch detected. Now reporting, and fixing.");
            e.getPlayer().teleport(e.getPlayer().getLocation().add(0.0, 1.0, 0.0));
        }
    }

}

