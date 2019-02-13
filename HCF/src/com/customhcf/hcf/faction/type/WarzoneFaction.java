/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 */
package com.customhcf.hcf.faction.type;

import com.customhcf.hcf.Utils.ConfigurationService;

import java.util.Map;

import org.bukkit.command.CommandSender;

public class WarzoneFaction
extends Faction {
    public WarzoneFaction() {
        super("Warzone");
    }

    public WarzoneFaction(Map<String, Object> map) {
        super(map);
    }

    @Override
    public String getDisplayName(CommandSender sender) {
        return (Object)ConfigurationService.WARZONE_COLOUR + this.getName();
    }
}

