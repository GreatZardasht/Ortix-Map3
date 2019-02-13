package com.customhcf.hcf.kothgame.glowstone;

import org.bukkit.*;
import org.bukkit.entity.Player;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class Glowstone {

    public static void setLocation() {
        int radius = GlowstoneConfig.radius_glowstone_cuboid;
        final int pX = GlowstoneConfig.coord_x;
        final int pY = GlowstoneConfig.coord_y;
        final int pZ = GlowstoneConfig.coord_z;
        final String resetmsg = GlowstoneConfig.GSMoutain_Reset;

        long time = TimeUnit.MINUTES.toSeconds(1800);
        Long hours = Long.valueOf(time);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("HCF"), new Runnable() {
            public void run() {
                Glowstone.setLocation();
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', resetmsg).replaceAll("%x%", String.valueOf(pX)).replaceAll("%y%", String.valueOf(pY)).replaceAll("%z%", String.valueOf(pZ)));
            }
        }, hours.longValue());
        for (int x = pX - radius; x <= pX + radius; x++) {
            for (int y = pY - radius; y <= pY + radius; y++) {
                for (int z = pZ - radius; z <= pZ + radius; z++) {
                    if (GlowstoneConfig.world_nether) {
                        Bukkit.getServer().getWorld("world_nether").getBlockAt(new Location(Bukkit.getServer().getWorld("world_nether"), x, y, z)).setType(Material.matchMaterial(GlowstoneConfig.material));
                    } else if (GlowstoneConfig.world) {
                        Bukkit.getServer().getWorld("world").getBlockAt(new Location(Bukkit.getServer().getWorld("world_nether"), x, y, z)).setType(Material.matchMaterial(GlowstoneConfig.material));
                    }
                }
            }
        }
        for (Player all : Bukkit.getServer().getOnlinePlayers()) {
            all.playSound(all.getLocation(), Sound.NOTE_PIANO, 30.0F, 1.0F);
        }
    }
}

