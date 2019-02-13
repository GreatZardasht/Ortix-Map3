/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.time.DurationFormatUtils
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.customhcf.hcf.faction;

import com.customhcf.hcf.faction.claim.Claim;
import com.customhcf.hcf.faction.type.ClaimableFaction;
import com.customhcf.hcf.faction.type.Faction;
import com.customhcf.hcf.faction.type.PlayerFaction;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface FactionManager {
    public static final long MAX_DTR_REGEN_MILLIS = TimeUnit.HOURS.toMillis(3);
    public static final String MAX_DTR_REGEN_WORDS = DurationFormatUtils.formatDurationWords((long)MAX_DTR_REGEN_MILLIS, (boolean)true, (boolean)true);

    public Map<String, ?> getFactionNameMap();

    public Collection<Faction> getFactions();

    public abstract Collection<ClaimableFaction> getClaimableFactions();

    public abstract Collection<PlayerFaction> getPlayerFactions();

    public Claim getClaimAt(Location var1);

    public Claim getClaimAt(World var1, int var2, int var3);

    public Faction getFactionAt(Location var1);

    public Faction getFactionAt(Block var1);

    public Faction getFactionAt(World var1, int var2, int var3);

    public Faction getFaction(String var1);

    public Faction getFaction(UUID var1);

    @Deprecated
    public PlayerFaction getContainingPlayerFaction(String var1);

    @Deprecated
    public PlayerFaction getPlayerFaction(Player var1);

    public PlayerFaction getPlayerFaction(UUID var1);

    public Faction getContainingFaction(String var1);

    public boolean containsFaction(Faction var1);

    public boolean createFaction(Faction var1);

    public boolean createFaction(Faction var1, CommandSender var2);

    public boolean removeFaction(Faction var1, CommandSender var2);

    public void reloadFactionData();

    public void saveFactionData();
}

