/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.serialization.ConfigurationSerializable
 */
package com.customhcf.hcf.faction.type;

import com.customhcf.hcf.faction.claim.Claim;
import com.customhcf.hcf.faction.type.ClaimableFaction;
import com.customhcf.hcf.faction.type.Faction;
import java.util.Map;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class EndPortalFaction
extends ClaimableFaction
implements ConfigurationSerializable {
    public EndPortalFaction() {
        super("EndPortal");
        World overworld = Bukkit.getWorld((String)"world");
        int maxHeight = overworld.getMaxHeight();
        int min = 985;
        int max = 1015;
        this.addClaim(new Claim(this, new Location(overworld, 985.0, 0.0, 985.0), new Location(overworld, 1015.0, (double)maxHeight, 1015.0)), null);
        this.addClaim(new Claim(this, new Location(overworld, -1015.0, (double)maxHeight, -1015.0), new Location(overworld, -985.0, 0.0, -985.0)), null);
        this.addClaim(new Claim(this, new Location(overworld, -1015.0, 0.0, 985.0), new Location(overworld, -985.0, (double)maxHeight, 1015.0)), null);
        this.addClaim(new Claim(this, new Location(overworld, 985.0, 0.0, -1015.0), new Location(overworld, 1015.0, (double)maxHeight, -985.0)), null);
        this.safezone = false;
    }

    public EndPortalFaction(Map<String, Object> map) {
        super(map);
    }

    @Override
    public String getDisplayName(CommandSender sender) {
        return (Object)ChatColor.DARK_AQUA + this.getName().replace("EndPortal", "End Portal");
    }

    @Override
    public boolean isDeathban() {
        return true;
    }
}

