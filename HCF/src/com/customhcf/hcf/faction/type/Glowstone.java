package com.customhcf.hcf.faction.type;


import com.customhcf.hcf.faction.claim.Claim;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;

public class Glowstone
        extends ClaimableFaction
{
    public Glowstone()
    {
        super("Glowstone");
        World world = Bukkit.getServer().getWorld("world_nether");
        World.Environment environment = world.getEnvironment();
        if (environment == World.Environment.NETHER)
        {
            double radius = Double.valueOf(63.0D).doubleValue();
            addClaim(new Claim(this, new Location(world, radius, 0.0D, radius), new Location(world, -radius, world.getMaxHeight(), -radius)), null);
        }
    }

    public Glowstone(Map<String, Object> map)
    {
        super(map);
    }

    public String getDisplayName(CommandSender sender)
    {
        return ChatColor.YELLOW + getName();
    }
}
