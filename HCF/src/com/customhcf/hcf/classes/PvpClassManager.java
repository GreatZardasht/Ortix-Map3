/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  org.bukkit.Bukkit
 *  org.bukkit.Server
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 */
package com.customhcf.hcf.classes;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.classes.PvpClass;
import com.customhcf.hcf.classes.archer.ArcherClass;
import com.customhcf.hcf.classes.bard.BardClass;
import com.customhcf.hcf.classes.event.PvpClassEquipEvent;
import com.customhcf.hcf.classes.event.PvpClassUnequipEvent;
import com.customhcf.hcf.classes.type.MinerClass;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class PvpClassManager {
    private final Map<UUID, PvpClass> equippedClass = new HashMap<UUID, PvpClass>();
    private final Map<String, PvpClass> pvpClasses = new HashMap<String, PvpClass>();
    public ArcherClass archerClass;

    public PvpClassManager(HCF plugin) {
        this.pvpClasses.put("Archer", new ArcherClass(plugin));
        this.pvpClasses.put("Bard", new BardClass(plugin));
        this.pvpClasses.put("Miner", new MinerClass(plugin));
        for (PvpClass pvpClass : this.pvpClasses.values()) {
            if (!(pvpClass instanceof Listener)) continue;
            plugin.getServer().getPluginManager().registerEvents((Listener)pvpClass, (Plugin)plugin);
        }
    }

    public void onDisable() {
        for (Map.Entry<UUID, PvpClass> entry : new HashMap<UUID, PvpClass>(this.equippedClass).entrySet()) {
            this.setEquippedClass(Bukkit.getPlayer((UUID)entry.getKey()), null);
        }
        this.pvpClasses.clear();
        this.equippedClass.clear();
    }

    public Collection<PvpClass> getPvpClasses() {
        return this.pvpClasses.values();
    }

    public PvpClass getPvpClass(String name) {
        return this.pvpClasses.get(name);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public PvpClass getEquippedClass(Player player) {
        Map<UUID, PvpClass> map = this.equippedClass;
        synchronized (map) {
            return this.equippedClass.get(player.getUniqueId());
        }
    }

    public boolean hasClassEquipped(Player player, PvpClass pvpClass) {
        PvpClass equipped = this.getEquippedClass(player);
        return equipped != null && equipped.equals(pvpClass);
    }

    public void setEquippedClass(Player player, @Nullable PvpClass pvpClass) {
        PvpClass equipped = this.getEquippedClass(player);
        if (equipped != null) {
            if (pvpClass == null) {
                this.equippedClass.remove(player.getUniqueId());
                equipped.onUnequip(player);
                Bukkit.getPluginManager().callEvent((Event)new PvpClassUnequipEvent(player, equipped));
                return;
            }
        } else if (pvpClass == null) {
            return;
        }
        if (pvpClass.onEquip(player)) {
            this.equippedClass.put(player.getUniqueId(), pvpClass);
            Bukkit.getPluginManager().callEvent((Event)new PvpClassEquipEvent(player, pvpClass));
        }
    }
}

