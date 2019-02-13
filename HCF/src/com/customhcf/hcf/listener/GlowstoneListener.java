package com.customhcf.hcf.listener;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.Utils.DateTimeFormats;
import com.customhcf.hcf.kothgame.glowstone.GlowstoneFaction;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class GlowstoneListener
        implements Listener
{
    protected static final ImmutableMap<World.Environment, String> ENVIRONMENT_MAPPINGS = Maps.immutableEnumMap(ImmutableMap.of(World.Environment.NETHER, "Nether", World.Environment.NORMAL, "Overworld", World.Environment.THE_END, "The End"));


    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public void onBreakGlowstone(BlockBreakEvent e)
    {
        GlowstoneFaction faction = (GlowstoneFaction)HCF.getPlugin().getFactionManager().getFaction("Glowstone");
        if (!faction.isActive()) {
            return;
        }
        if ((HCF.getPlugin().getFactionManager().getFactionAt(e.getBlock()) == null) || (!HCF.getPlugin().getFactionManager().getFactionAt(e.getBlock()).equals(faction))) {
            return;
        }
        if (!e.getBlock().getType().equals(Material.GLOWSTONE)) {
            return;
        }
        if (!faction.getGlowstoneArea().contains(e.getBlock())) {
            return;
        }
        int there = 0;
        int gone = 0;
        for (Block block : faction.getGlowstoneArea()) {
            if (block.getType().equals(Material.GLOWSTONE)) {
                there++;
            } else {
                gone++;
            }
        }
        if (e.getBlock().getType().equals(Material.GLOWSTONE))
        {
            there--;
            gone++;
        }
        if (there == gone) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "Glowstone Island" + ChatColor.YELLOW + " there is " + ChatColor.GOLD + "50%" + ChatColor.YELLOW + " of remaining glowstone.");
        }
        if (gone > there)
        {
            int total = there + gone;
            int fifty = total / 2;
            int twenty = fifty / 2;
            if ((there == twenty) && (gone == fifty + twenty)) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "Glowstone Island" + ChatColor.YELLOW + " there is " + ChatColor.GOLD + "25%" + ChatColor.YELLOW + " of remaining glowstone.");
            }
        }
        if (there > gone)
        {
            int total = there + gone;
            int fifty = total / 2;
            int twenty = fifty / 2;
            if ((gone == twenty) && (there == fifty + twenty)) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "Glowstone Island" + ChatColor.YELLOW + " there is " + ChatColor.GOLD + "75%" + ChatColor.YELLOW + " of remaining glowstone. ");
            }
        }
        if (there == 0)
        {
            Bukkit.broadcastMessage(ChatColor.GOLD + "Glowstone Island" + ChatColor.YELLOW + " all of the glowstone has been mined. The Island will regenerate at: " + ChatColor.GOLD + DateTimeFormats.HR_MIN.format(faction.getTimeTillNextReset()) + " EST.");

            faction.setActive(false);
        }
        e.setCancelled(false);
    }
}
