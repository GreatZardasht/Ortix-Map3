package com.customhcf.hcf.kothgame.glowstone;

import com.customhcf.hcf.faction.type.ClaimableFaction;
import com.customhcf.util.cuboid.Cuboid;
import java.io.PrintStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class GlowstoneFaction
        extends ClaimableFaction
        implements ConfigurationSerializable
{
    private Long defaultMillisTillReset;
    private Long lastReset;
    private Long timeTillNextReset;
    private Cuboid glowstoneArea;
    boolean active;

    public GlowstoneFaction()
    {
        super("Glowstone");
        this.defaultMillisTillReset = Long.valueOf(TimeUnit.HOURS.toMillis(1L));
        this.lastReset = Long.valueOf(0L);
        this.timeTillNextReset = Long.valueOf(System.currentTimeMillis() + this.defaultMillisTillReset.longValue());
        this.active = false;
        this.glowstoneArea = null;
    }

    public Map<String, Object> serialize()
    {
        Map<String, Object> map = super.serialize();
        this.defaultMillisTillReset = Long.valueOf(TimeUnit.HOURS.toMillis(1L));
        this.lastReset = Long.valueOf(0L);
        this.timeTillNextReset = Long.valueOf(System.currentTimeMillis() + this.defaultMillisTillReset.longValue());
        map.put("glowstoneArea", this.glowstoneArea);
        return map;
    }

    public GlowstoneFaction(Map<String, Object> map)
    {
        super(map);
        this.defaultMillisTillReset = Long.valueOf(TimeUnit.HOURS.toMillis(1L));
        this.lastReset = Long.valueOf(0L);
        this.timeTillNextReset = Long.valueOf(System.currentTimeMillis() + this.defaultMillisTillReset.longValue());
        setDeathban(true);
        this.glowstoneArea = ((Cuboid)map.get("glowstoneArea"));
    }

    public void start()
    {
        this.lastReset = Long.valueOf(System.currentTimeMillis());
        this.timeTillNextReset = Long.valueOf(System.currentTimeMillis() + this.defaultMillisTillReset.longValue());

        this.active = true;
        System.out.println("Glowstone Faction is now ACTIVE");
        if (this.glowstoneArea == null) {
            return;
        }
        for (Block block : this.glowstoneArea) {
            block.setType(Material.GLOWSTONE);
        }
        Bukkit.broadcastMessage(ChatColor.GOLD + "Glowstone Island" + ChatColor.YELLOW + " has been " + ChatColor.GREEN + "regenerated" + ChatColor.YELLOW + '.');
    }

    public boolean isActive()
    {
        return this.active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public Long getDefaultMillisTillReset()
    {
        return this.defaultMillisTillReset;
    }

    public Long getLastReset()
    {
        return this.lastReset;
    }

    public void setLastReset(Long lastReset)
    {
        this.lastReset = lastReset;
    }

    public Long getTimeTillNextReset()
    {
        return this.timeTillNextReset;
    }

    public void setTimeTillNextReset(Long timeTillNextReset)
    {
        this.timeTillNextReset = timeTillNextReset;
    }

    public Cuboid getGlowstoneArea()
    {
        return this.glowstoneArea;
    }

    public void setGlowstoneArea(Cuboid glowstoneArea)
    {
        this.glowstoneArea = glowstoneArea;
    }
}
