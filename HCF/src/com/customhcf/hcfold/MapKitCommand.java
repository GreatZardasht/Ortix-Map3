/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.util.BukkitUtils
 *  org.bukkit.ChatColor
 *  org.bukkit.Server
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.TabCompleter
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.server.PluginDisableEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 */
package com.customhcf.hcfold;

import com.customhcf.hcf.HCF;
import com.customhcf.util.BukkitUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

@Deprecated
public class MapKitCommand
implements CommandExecutor,
TabCompleter,
Listener {
    private final Set<Inventory> tracking = new HashSet<Inventory>();

    public MapKitCommand(HCF plugin) {
        plugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage((Object)ChatColor.RED + "This command is only executable by players.");
            return true;
        }
        Player player = (Player)sender;
        player.sendMessage((Object)ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        player.sendMessage((Object)ChatColor.YELLOW + "Sharpness 1");
        player.sendMessage((Object)ChatColor.YELLOW + "Protection 1");
        player.sendMessage((Object)ChatColor.YELLOW + "No Debuff");
        player.sendMessage((Object)ChatColor.YELLOW + "Bard: Full Effects");
        player.sendMessage((Object)ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (this.tracking.contains((Object)event.getInventory())) {
            event.setCancelled(true);
        }
    }


    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPluginDisable(final PluginDisableEvent event) {
        for (final Inventory inventory : this.tracking) {
            final Collection<HumanEntity> viewers = new HashSet<HumanEntity>(inventory.getViewers());
            for (final HumanEntity viewer : viewers) {
                viewer.closeInventory();
            }
        }
    }
}