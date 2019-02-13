/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.util.ItemBuilder
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.inventory.ItemStack
 */
package com.customhcf.hcfold.crate.type;

import com.customhcf.hcf.Utils.ConfigurationService;
import com.customhcf.hcfold.crate.EnderChestKey;
import com.customhcf.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class PalaceKey
extends EnderChestKey {
    public PalaceKey() {
        super("Palace", 4);
        this.setupRarity(new ItemBuilder(Material.BOW).enchant(Enchantment.ARROW_DAMAGE, 3).enchant(Enchantment.ARROW_FIRE, 1).enchant(Enchantment.ARROW_INFINITE, 1).enchant(Enchantment.DURABILITY, 5).displayName(ChatColor.RED.toString() + ChatColor.BOLD + "[Palace Bow]").build(), 10);
        this.setupRarity(new ItemBuilder(Material.BOW).enchant(Enchantment.ARROW_DAMAGE, 3).enchant(Enchantment.ARROW_FIRE, 1).enchant(Enchantment.ARROW_INFINITE, 1).enchant(Enchantment.DURABILITY, 5).displayName(ChatColor.RED.toString() + ChatColor.BOLD + "[Palace Bow]").build(), 10);
        this.setupRarity(new ItemBuilder(Material.DIAMOND_SWORD).enchant(Enchantment.DAMAGE_ALL, 1).enchant(Enchantment.FIRE_ASPECT, 1).enchant(Enchantment.DURABILITY, 5).displayName(ChatColor.RED.toString() + ChatColor.BOLD + "[Palace Fire]").build(), 10);
        this.setupRarity(new ItemBuilder(Material.DIAMOND_SWORD).enchant(Enchantment.DAMAGE_ALL, 1).enchant(Enchantment.FIRE_ASPECT, 1).enchant(Enchantment.DURABILITY, 5).displayName(ChatColor.RED.toString() + ChatColor.BOLD + "[Palace Fire]").build(), 10);
        this.setupRarity(new ItemStack(Material.BEACON, 1), 10);
        this.setupRarity(new ItemStack(Material.GOLD_BLOCK, 64), 10);
        this.setupRarity(new ItemStack(Material.DIAMOND_BLOCK, 64), 10);
        this.setupRarity(new ItemStack(Material.IRON_BLOCK, 64), 10);
        this.setupRarity(new ItemStack(Material.EMERALD_BLOCK, 64), 10);
        this.setupRarity(new ItemStack(Material.GHAST_TEAR, 2), 10);

    }

    @Override
    public ChatColor getColour() {
        return ChatColor.DARK_PURPLE;
    }

    @Override
    public boolean getBroadcastItems() {
        return true;
    }
}

