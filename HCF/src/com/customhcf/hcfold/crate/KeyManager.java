/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.util.Config
 *  com.google.common.collect.HashBasedTable
 *  com.google.common.collect.Sets
 *  com.google.common.collect.Table
 *  org.bukkit.configuration.MemorySection
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.customhcf.hcfold.crate;

import com.customhcf.hcf.HCF;
import com.customhcf.hcfold.crate.Key;
import com.customhcf.hcfold.crate.type.*;
import com.customhcf.util.Config;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.configuration.MemorySection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class KeyManager {
    private final PalaceKey conquestKey;
    private final Tier1 tier1;
    private final Tier2 tier2;
    private final Tier3 tier3;
    private final KothKey kothKey;
    private final OrtixKey ortixKey;
    private final CrystalKey crystalKey;
    private final Table<UUID, String, Integer> depositedCrateMap;
    private final Set<Key> keys;
    private final Config config;

    public KeyManager(final HCF plugin) {
        super();
        this.depositedCrateMap = HashBasedTable.create();
        this.config = new Config(plugin, "key-data");
        this.keys = Sets.newHashSet(new Key[] {this.crystalKey = new CrystalKey(), this.tier3 = new Tier3(), this.kothKey = new KothKey(),  this.conquestKey = new PalaceKey(), this.tier1 = new Tier1(), this.tier2 = new Tier2(), this.ortixKey = new OrtixKey() });
        this.reloadKeyData();
    }


    public Map<String, Integer> getDepositedCrateMap(UUID uuid) {
        return this.depositedCrateMap.row(uuid);
    }

    public Set<Key> getKeys() {
        return this.keys;
    }

    public Tier1 getTier1() {
        return this.tier1;
    }

    public Tier2 getTier2() {
        return this.tier2;
    }

    public PalaceKey getConquestKey() {
        return this.conquestKey;
    }

    public Tier3 getTier3() { return this.tier3; }

    public KothKey getKothKey() { return this.kothKey; }

    public CrystalKey getCrystalKey() {
        return this.crystalKey;
    }


    public OrtixKey getOrtixKey() {
        return this.ortixKey;
    }


    public Key getKey(String name) {
        for (Key key : this.keys) {
            if (!key.getName().equalsIgnoreCase(name)) continue;
            return key;
        }
        return null;
    }

    @Deprecated
    public Key getKey(Class<? extends Key> clazz) {
        for (Key key : this.keys) {
            if (!clazz.isAssignableFrom(key.getClass())) continue;
            return key;
        }
        return null;
    }

    public Key getKey(ItemStack stack) {
        if (stack == null || !stack.hasItemMeta()) {
            return null;
        }
        for (Key key : this.keys) {
            ItemStack item = key.getItemStack();
            if (!item.getItemMeta().getDisplayName().equals(stack.getItemMeta().getDisplayName())) continue;
            return key;
        }
        return null;
    }

    public void reloadKeyData() {
        for (Key key : this.keys) {
            key.load(this.config);
        }
        Object object = this.config.get("deposited-key-map");
        if (object instanceof MemorySection) {
            MemorySection section = (MemorySection)object;
            for (String id : section.getKeys(false)) {
                object = this.config.get(section.getCurrentPath() + '.' + id);
                if (!(object instanceof MemorySection)) continue;
                section = (MemorySection)object;
                for (String key2 : section.getKeys(false)) {
                    this.depositedCrateMap.put(UUID.fromString(id), key2, this.config.getInt("deposited-key-map." + id + '.' + key2));
                }
            }
        }
    }

    public void saveKeyData() {
        for (Key key : this.keys) {
            key.save(this.config);
        }
        LinkedHashMap saveMap = new LinkedHashMap(this.depositedCrateMap.size());
        for (Map.Entry entry : this.depositedCrateMap.rowMap().entrySet()) {
            saveMap.put(((UUID)entry.getKey()).toString(), entry.getValue());
        }
        this.config.set("deposited-key-map", saveMap);
        this.config.save();
    }
}

