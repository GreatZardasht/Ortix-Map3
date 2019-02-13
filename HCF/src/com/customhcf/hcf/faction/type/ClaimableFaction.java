/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.util.BukkitUtils
 *  com.customhcf.util.GenericUtils
 *  com.customhcf.util.cuboid.Cuboid
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.Maps
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.World$Environment
 *  org.bukkit.command.CommandSender
 *  org.bukkit.event.Event
 */
package com.customhcf.hcf.faction.type;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.balance.EconomyManager;
import com.customhcf.hcf.faction.FactionMember;
import com.customhcf.hcf.faction.claim.Claim;
import com.customhcf.hcf.faction.claim.ClaimHandler;
import com.customhcf.hcf.faction.event.FactionClaimChangeEvent;
import com.customhcf.hcf.faction.event.FactionClaimChangedEvent;
import com.customhcf.hcf.faction.event.cause.ClaimChangeCause;
import com.customhcf.hcf.faction.type.Faction;
import com.customhcf.hcf.faction.type.PlayerFaction;
import com.customhcf.util.BukkitUtils;
import com.customhcf.util.GenericUtils;
import com.customhcf.util.cuboid.Cuboid;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;

public class ClaimableFaction
        extends Faction
{
    protected static final ImmutableMap<World.Environment, String> ENVIRONMENT_MAPPINGS = Maps.immutableEnumMap(ImmutableMap.of(World.Environment.NETHER, "Nether", World.Environment.NORMAL, "Overworld", World.Environment.THE_END, "The End"));
    protected final Set<Claim> claims;

    public ClaimableFaction(String name)
    {
        super(name);
        this.claims = new HashSet();
    }

    public ClaimableFaction(Map<String, Object> map)
    {
        super(map);
        (this.claims = new HashSet()).addAll(GenericUtils.createList(map.get("claims"), Claim.class));
    }

    public Map<String, Object> serialize()
    {
        Map<String, Object> map = super.serialize();
        map.put("claims", new ArrayList(this.claims));
        return map;
    }

    public void printDetails(CommandSender sender)
    {
        sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        sender.sendMessage(getDisplayName(sender));
        for (Claim claim : this.claims)
        {
            Location location = claim.getCenter();
            sender.sendMessage(ChatColor.YELLOW + "  Location: " + ChatColor.YELLOW.toString() + (String)ENVIRONMENT_MAPPINGS.get(location.getWorld().getEnvironment()) + ", " + location.getBlockX() + " | " + location.getBlockZ());
        }
        sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
    }

    public Set<Claim> getClaims()
    {
        return this.claims;
    }

    public boolean addClaim(Claim claim, CommandSender sender)
    {
        return addClaims(Collections.singleton(claim), sender);
    }

    public void setClaim(Cuboid cuboid, CommandSender sender)
    {
        removeClaims(getClaims(), sender);
        Location min = cuboid.getMinimumPoint();
        min.setY(0);
        Location max = cuboid.getMaximumPoint();
        max.setY(256);
        addClaim(new Claim(this, min, max), sender);
    }

    public boolean addClaims(Collection<Claim> adding, CommandSender sender)
    {
        if (sender == null) {
            sender = Bukkit.getConsoleSender();
        }
        FactionClaimChangeEvent event = new FactionClaimChangeEvent(sender, ClaimChangeCause.CLAIM, adding, this);
        Bukkit.getPluginManager().callEvent(event);
        if ((event.isCancelled()) || (!this.claims.addAll(adding))) {
            return false;
        }
        Bukkit.getPluginManager().callEvent(new FactionClaimChangedEvent(sender, ClaimChangeCause.CLAIM, adding));
        return true;
    }

    public boolean removeClaim(Claim claim, CommandSender sender)
    {
        return removeClaims(Collections.singleton(claim), sender);
    }

    public boolean removeClaims(Collection<Claim> removing, CommandSender sender)
    {
        if (sender == null) {
            sender = Bukkit.getConsoleSender();
        }
        int previousClaims = this.claims.size();
        FactionClaimChangeEvent event = new FactionClaimChangeEvent(sender, ClaimChangeCause.UNCLAIM, removing, this);
        Bukkit.getPluginManager().callEvent(event);
        if ((event.isCancelled()) || (!this.claims.removeAll(removing))) {
            return false;
        }
        if ((this instanceof PlayerFaction))
        {
            PlayerFaction playerFaction = (PlayerFaction)this;
            Location home = playerFaction.getHome();
            HCF plugin = HCF.getPlugin();
            int refund = 0;
            for (Claim claim : removing)
            {
                refund += plugin.getClaimHandler().calculatePrice(claim, previousClaims, true);
                if (previousClaims > 0) {
                    previousClaims--;
                }
                if ((home != null) && (claim.contains(home)))
                {
                    playerFaction.setHome(null);
                    playerFaction.broadcast(ChatColor.RED.toString() + ChatColor.BOLD + "Your factions' home was unset as its residing claim was removed.");
                    break;
                }
            }
            plugin.getEconomyManager().addBalance(playerFaction.getLeader().getUniqueId(), refund);
            playerFaction.broadcast(ChatColor.YELLOW + "Faction leader was refunded " + ChatColor.GREEN + '$' + refund + ChatColor.YELLOW + " due to a land unclaim.");
        }
        Bukkit.getPluginManager().callEvent(new FactionClaimChangedEvent(sender, ClaimChangeCause.UNCLAIM, removing));
        return true;
    }
}