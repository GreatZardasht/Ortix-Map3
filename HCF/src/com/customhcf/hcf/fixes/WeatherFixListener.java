/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.World
 *  org.bukkit.World$Environment
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.weather.WeatherChangeEvent
 */
package com.customhcf.hcf.fixes;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherFixListener
implements Listener {
    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e) {
        if (e.getWorld().getEnvironment() == World.Environment.NORMAL) {
            e.getWorld().setWeatherDuration(0);
        }
    }
}

