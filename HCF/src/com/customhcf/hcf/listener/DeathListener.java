/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.inventory.ItemStack
 */
package com.customhcf.hcf.listener;

import com.customhcf.hcf.Utils.ConfigurationService;
import com.customhcf.hcf.HCF;
import com.customhcf.hcf.faction.struct.Role;
import com.customhcf.hcf.faction.type.Faction;
import com.customhcf.hcf.faction.type.PlayerFaction;
import com.customhcf.hcf.user.FactionUser;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.customhcf.util.JavaUtils;
import net.minecraft.server.v1_7_R4.EntityLightning;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntityWeather;
import net.minecraft.server.v1_7_R4.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class DeathListener
        implements Listener
{
    public static HashMap<UUID, ItemStack[]> PlayerInventoryContents = new HashMap();
    public static HashMap<UUID, ItemStack[]> PlayerArmorContents = new HashMap();
    private static final long BASE_REGEN_DELAY = TimeUnit.MINUTES.toMillis(60L);
    private final HCF plugin;

    public DeathListener(HCF plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.LOWEST)
    public void onPlayerDeathKillIncrement(PlayerDeathEvent event)
    {
        Player killer = event.getEntity().getKiller();
        if (killer != null)
        {
            FactionUser user = this.plugin.getUserManager().getUser(killer.getUniqueId());
            user.setKills(user.getKills() + 1);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        Player player = event.getEntity();
        PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player.getUniqueId());
        if (playerFaction != null)
        {
            Faction factionAt = this.plugin.getFactionManager().getFactionAt(player.getLocation());
            Role role = playerFaction.getMember(player.getUniqueId()).getRole();
            if (playerFaction.getDeathsUntilRaidable() >= -5.0D) {
                if (player.getWorld().getName().endsWith("_end")) {
                    playerFaction.setDeathsUntilRaidable(playerFaction.getDeathsUntilRaidable() - factionAt.getDtrLossEndMultiplier());
                    playerFaction.setRemainingRegenerationTime(BASE_REGEN_DELAY + playerFaction.getOnlinePlayers().size() * TimeUnit.MINUTES.toMillis(2L));
                    playerFaction.broadcast(ChatColor.GREEN + "Member Death: " + ConfigurationService.TEAMMATE_COLOUR + role.getAstrix() + player.getName() + ChatColor.YELLOW + " DTR:" + ChatColor.GRAY + " [" + playerFaction.getDtrColour() + JavaUtils.format(Double.valueOf(playerFaction.getDeathsUntilRaidable())) + ChatColor.WHITE + '/' + ChatColor.WHITE + playerFaction.getMaximumDeathsUntilRaidable() + ChatColor.GRAY + "].");
                } else {
                    playerFaction.setDeathsUntilRaidable(playerFaction.getDeathsUntilRaidable() - factionAt.getDtrLossMultiplier());
                    playerFaction.setRemainingRegenerationTime(BASE_REGEN_DELAY + playerFaction.getOnlinePlayers().size() * TimeUnit.MINUTES.toMillis(2L));
                    playerFaction.broadcast(ChatColor.GREEN + "Member Death: " + ConfigurationService.TEAMMATE_COLOUR + role.getAstrix() + player.getName() + ChatColor.YELLOW + " DTR:" + ChatColor.GRAY + " [" + playerFaction.getDtrColour() + JavaUtils.format(Double.valueOf(playerFaction.getDeathsUntilRaidable())) + ChatColor.WHITE + '/' + ChatColor.WHITE + playerFaction.getMaximumDeathsUntilRaidable() + ChatColor.GRAY + "].");
                }
            }
            else
            {
                playerFaction.setRemainingRegenerationTime(BASE_REGEN_DELAY + playerFaction.getOnlinePlayers().size() * TimeUnit.MINUTES.toMillis(2L));
                playerFaction.broadcast(ChatColor.GREEN + "Member Death: " + ConfigurationService.TEAMMATE_COLOUR + role.getAstrix() + ChatColor.YELLOW + " DTR:" + ChatColor.GRAY + " [" + playerFaction.getDtrColour() + JavaUtils.format(Double.valueOf(playerFaction.getDeathsUntilRaidable())) + ChatColor.WHITE + '/' + ChatColor.WHITE + playerFaction.getMaximumDeathsUntilRaidable() + ChatColor.GRAY + "].");
            }
        }
        PacketPlayOutSpawnEntityWeather packet;
        if (Bukkit.spigot().getTPS()[0] > 15.0D)
        {
            PlayerInventoryContents.put(player.getUniqueId(), player.getInventory().getContents());
            PlayerArmorContents.put(player.getUniqueId(), player.getInventory().getArmorContents());
            Location location = player.getLocation();
            WorldServer worldServer = ((CraftWorld)location.getWorld()).getHandle();
            EntityLightning entityLightning = new EntityLightning(worldServer, location.getX(), location.getY(), location.getZ(), false);
            packet = new PacketPlayOutSpawnEntityWeather(entityLightning);
            for (Player target : Bukkit.getServer().getOnlinePlayers()) {
                if (this.plugin.getUserManager().getUser(target.getUniqueId()).isShowLightning())
                {
                    ((CraftPlayer)target).getHandle().playerConnection.sendPacket(packet);
                    target.playSound(target.getLocation(), Sound.AMBIENCE_THUNDER, 1.0F, 1.0F);
                }
            }
        }
    }
}

