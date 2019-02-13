/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.gnu.trove.map.TObjectIntMap
 *  org.bukkit.entity.Player
 */
package com.customhcf.hcf.deathban;

import com.customhcf.hcf.deathban.Deathban;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import net.minecraft.util.gnu.trove.map.TObjectIntMap;
import org.bukkit.entity.Player;

public interface DeathbanManager {
    public static final long MAX_DEATHBAN_TIME = TimeUnit.HOURS.toMillis(8);

    public TObjectIntMap<UUID> getLivesMap();

    public int getLives(UUID var1);

    public int setLives(UUID var1, int var2);

    public int addLives(UUID var1, int var2);

    public int takeLives(UUID var1, int var2);

    public long getDeathBanMultiplier(Player var1);

    public Deathban applyDeathBan(Player var1, String var2);

    public Deathban applyDeathBan(UUID var1, Deathban var2);

    public void reloadDeathbanData();

    public void saveDeathbanData();
}

