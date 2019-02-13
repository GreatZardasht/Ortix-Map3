/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerItemConsumeEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.customhcf.hcf.timer.type;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.Utils.DurationFormatter;
import com.customhcf.hcf.timer.PlayerTimer;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import com.google.common.base.Predicate;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;

public class NotchAppleTimer
        extends PlayerTimer
        implements Listener
{
    public NotchAppleTimer(JavaPlugin plugin)
    {
        super("Gopple", TimeUnit.HOURS.toMillis(6L));
    }

    public String getScoreboardPrefix()
    {
        return ChatColor.GOLD.toString() + ChatColor.BOLD;
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPlayerConsume(PlayerItemConsumeEvent event)
    {
        ItemStack stack = event.getItem();
        if ((stack != null) && (stack.getType() == Material.GOLDEN_APPLE) && (stack.getDurability() == 1))
        {
            Player player = event.getPlayer();
            if (!setCooldown(player, player.getUniqueId(), this.defaultCooldown, false))
            {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You still have a " + getDisplayName() + ChatColor.RED + " cooldown for another " + ChatColor.BOLD + HCF.getRemaining(getRemaining(player), true, false) + ChatColor.RED + '.');
            }
        }
    }
}