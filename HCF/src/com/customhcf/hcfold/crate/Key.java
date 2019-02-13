/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.util.Config
 *  org.bukkit.ChatColor
 *  org.bukkit.inventory.ItemStack
 */
package com.customhcf.hcfold.crate;

import com.customhcf.util.Config;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public abstract class Key {
    private String name;

    public Key(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract ChatColor getColour();

    public String getDisplayName() {
        return (Object)this.getColour() + this.name;
    }

    public abstract ItemStack getItemStack();

    public void load(Config config) {
    }

    public void save(Config config) {
    }
}

