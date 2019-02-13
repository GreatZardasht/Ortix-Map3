/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_7_R4.EntityPlayer
 *  org.bukkit.Location
 *  org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.FoodLevelChangeEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerLoginEvent
 *  org.bukkit.event.player.PlayerLoginEvent$Result
 *  org.bukkit.event.player.PlayerMoveEvent
 *  ru.tehkode.permissions.bukkit.PermissionsEx
 */
package com.customhcf.hcf.fixes;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.faction.FactionManager;
import com.customhcf.hcf.faction.type.Faction;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class HungerFixListener
implements Listener {
    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        EntityPlayer entityPlayer = ((CraftPlayer)event.getPlayer()).getHandle();
        entityPlayer.knockbackReductionX = 0.6f;
        entityPlayer.knockbackReductionY = 0.55f;
        entityPlayer.knockbackReductionZ = 0.6f;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (HCF.getPlugin().getFactionManager().getFactionAt(e.getPlayer().getLocation()).isSafezone() && e.getPlayer().getFoodLevel() < 20) {
            e.getPlayer().setFoodLevel(20);
            e.getPlayer().setSaturation(20.0f);
        }
    }

    @EventHandler
    public void onHungerChange(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player)e.getEntity();
            p.setSaturation(1000.0f);
            p.setFoodLevel(20);
            if (HCF.getPlugin().getFactionManager().getFactionAt(p.getLocation()).isSafezone()) {
                p.setSaturation(20.0f);
                p.setHealth(20.0);
            }
            p.setSaturation(10.0f);
        }
    }
}

