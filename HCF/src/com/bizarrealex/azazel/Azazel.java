package com.bizarrealex.azazel;

import org.bukkit.plugin.java.*;
import com.bizarrealex.azazel.tab.*;
import java.util.concurrent.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.plugin.*;
import java.util.*;
import org.bukkit.craftbukkit.v1_7_R4.entity.*;
import org.bukkit.scheduler.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import net.minecraft.server.v1_7_R4.*;

public class Azazel implements Listener
{
    private final JavaPlugin plugin;
    private final Map<UUID, Tab> tabs;
    private TabAdapter adapter;
    
    public Azazel(final JavaPlugin plugin) {
        this.plugin = plugin;
        this.tabs = new ConcurrentHashMap<UUID, Tab>();
        if (Bukkit.getMaxPlayers() < 60) {
            Bukkit.getLogger().severe("There aren't 60 player slots, this will fuck up the tab list.");
        }
        for (final Player player : Bukkit.getOnlinePlayers()) {
            if (!this.tabs.containsKey(player.getUniqueId())) {
                this.tabs.put(player.getUniqueId(), new Tab(player, true, this));
            }
        }
        new AzazelTask(this, plugin);
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
    }
    
    public Azazel(final JavaPlugin plugin, final TabAdapter adapter) {
        this(plugin);
        this.adapter = adapter;
    }
    
    public Tab getTabByPlayer(final Player player) {
        return this.tabs.get(player.getUniqueId());
    }
    
    @EventHandler
    public void onPlayerJoinEvent(final PlayerJoinEvent event) {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            final PacketPlayOutPlayerInfo packet = PacketPlayOutPlayerInfo.removePlayer(((CraftPlayer)event.getPlayer()).getHandle());
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)packet);
        }
        new BukkitRunnable() {
            public void run() {
                Azazel.this.tabs.put(event.getPlayer().getUniqueId(), new Tab(event.getPlayer(), true, Azazel.this));
            }
        }.runTaskLater((Plugin)this.plugin, 1L);
    }
    
    @EventHandler
    public void onPlayerQuitEvent(final PlayerQuitEvent event) {
        this.tabs.remove(event.getPlayer().getUniqueId());
        for (final Player other : Bukkit.getOnlinePlayers()) {
            final EntityPlayer entityPlayer = ((CraftPlayer)other).getHandle();
            if (entityPlayer.playerConnection.networkManager.getVersion() >= 47) {
                final Tab tab = this.getTabByPlayer(event.getPlayer());
                if (tab == null || tab.getElevatedTeam() == null) {
                    continue;
                }
                tab.getElevatedTeam().removeEntry(event.getPlayer().getName());
            }
        }
    }
    
    public JavaPlugin getPlugin() {
        return this.plugin;
    }
    
    public TabAdapter getAdapter() {
        return this.adapter;
    }
    
    public void setAdapter(final TabAdapter adapter) {
        this.adapter = adapter;
    }
}
