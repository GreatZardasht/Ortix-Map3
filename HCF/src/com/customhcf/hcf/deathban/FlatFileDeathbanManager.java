/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.base.BasePlugin
 *  com.customhcf.base.PlayTimeManager
 *  com.customhcf.util.Config
 *  com.customhcf.util.PersistableLocation
 *  net.md_5.bungee.api.ChatColor
 *  net.minecraft.util.gnu.trove.map.TObjectIntMap
 *  net.minecraft.util.gnu.trove.map.hash.TObjectIntHashMap
 *  net.minecraft.util.gnu.trove.procedure.TObjectIntProcedure
 *  org.bukkit.Location
 *  org.bukkit.configuration.MemorySection
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.customhcf.hcf.deathban;

import com.customhcf.hcf.Utils.ConfigurationService;
import com.customhcf.hcf.HCF;
import com.customhcf.hcf.faction.type.Faction;
import com.customhcf.util.Config;
import com.customhcf.util.PersistableLocation;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.UUID;

import net.minecraft.util.gnu.trove.map.TObjectIntMap;
import net.minecraft.util.gnu.trove.map.hash.TObjectIntHashMap;
import org.bukkit.Location;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

public class FlatFileDeathbanManager
        implements DeathbanManager {
    private static final int MAX_DEATHBAN_MULTIPLIER = 300;
    private final HCF plugin;
    private TObjectIntMap<UUID> livesMap;
    private Config livesConfig;

    public FlatFileDeathbanManager(HCF plugin) {
        this.plugin = plugin;
        this.reloadDeathbanData();
    }

    @Override
    public TObjectIntMap<UUID> getLivesMap() {
        return this.livesMap;
    }

    @Override
    public int getLives(UUID uuid) {
        return this.livesMap.get((Object)uuid);
    }

    @Override
    public int setLives(UUID uuid, int lives) {
        this.livesMap.put(uuid, lives);
        return lives;
    }

    @Override
    public int addLives(UUID uuid, int amount) {
        return this.livesMap.adjustOrPutValue(uuid, amount, amount);
    }

    @Override
    public int takeLives(UUID uuid, int amount) {
        return this.setLives(uuid, this.getLives(uuid) - amount);
    }

    @Override
    public long getDeathBanMultiplier(Player player) {
        if (player.hasPermission("hcf.deathban.extra")) {
            for (int i = 5; i < 21600; --i) {
                if (!player.hasPermission("hcf.deathban.seconds." + i)) continue;
                return i / 1000;
            }
        }
        return ConfigurationService.DEFAULT_DEATHBAN_DURATION;
    }

    @Override
    public Deathban applyDeathBan(Player player, String reason) {
        Location location = player.getLocation();
        Faction factionAt = this.plugin.getFactionManager().getFactionAt(location);
        long duration = ConfigurationService.DEFAULT_DEATHBAN_DURATION;
        if (!factionAt.isDeathban()) {
            duration /= 2;
        }
        if (player.hasPermission("Default")) {
            duration = 10800000;
        }
        if (player.hasPermission("Tiger")) {
            duration = 9000000; //9000
        }
        if (player.hasPermission("Lion")) {
            duration = 8100000; // 8100
        }
        if (player.hasPermission("Cheetah")) {
            duration = 5400000; // 5400
        }
        if (player.hasPermission("Saber")) {
            duration = 1800000; // 1800
        }
        return this.applyDeathBan(player.getUniqueId(), new Deathban(reason, Math.min(MAX_DEATHBAN_TIME, duration), new PersistableLocation(location)));
    }

    @Override
    public Deathban applyDeathBan(UUID uuid, Deathban deathban) {
        this.plugin.getUserManager().getUser(uuid).setDeathban(deathban);
        return deathban;
    }

    @Override
    public void reloadDeathbanData() {
        this.livesConfig = new Config(this.plugin, "lives");
        final Object object = this.livesConfig.get("lives");
        if (object instanceof MemorySection) {
            final MemorySection section = (MemorySection)object;
            final Set<String> keys = (Set<String>)section.getKeys(false);
            this.livesMap = (TObjectIntMap<UUID>)new TObjectIntHashMap(keys.size(), 0.5f, 0);
            for (final String id : keys) {
                this.livesMap.put(UUID.fromString(id), this.livesConfig.getInt(section.getCurrentPath() + "." + id));
            }
        }
        else {
            this.livesMap = (TObjectIntMap<UUID>)new TObjectIntHashMap(10, 0.5f, 0);
        }
    }

    @Override
    public void saveDeathbanData() {
        LinkedHashMap saveMap = new LinkedHashMap(this.livesMap.size());
        this.livesMap.forEachEntry((uuid, i) -> {
                    saveMap.put(uuid.toString(), i);
                    return true;
                }
        );
        this.livesConfig.set("lives", saveMap);
        this.livesConfig.save();
    }
}