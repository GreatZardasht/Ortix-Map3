package com.customhcf.hcfold.crate.type;

import com.customhcf.hcfold.crate.EnderChestKey;
import com.customhcf.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class Tier2 extends EnderChestKey {
    public Tier2() {
        super("Tier2", 4);
        this.setupRarity(new ItemStack(Material.ENDER_PEARL, 16), 5);
        this.setupRarity(new ItemStack(Material.GOLDEN_APPLE, 1, (short) 1), 1);
        this.setupRarity(new ItemStack(Material.GOLD_BLOCK, 16), 15);
        this.setupRarity(new ItemStack(Material.GOLD_BLOCK, 19), 15);
        this.setupRarity(new ItemStack(Material.EMERALD_BLOCK, 8), 10);
        this.setupRarity(new ItemStack(Material.ENDER_PEARL, 32), 10);
        this.setupRarity(new ItemStack(Material.GOLDEN_APPLE, 1, (short) 1), 1);
        this.setupRarity(new ItemBuilder(Material.DIAMOND_SWORD).enchant(Enchantment.LOOT_BONUS_MOBS, 3).build(), 5);
        this.setupRarity(new ItemBuilder(Material.DIAMOND_PICKAXE).enchant(Enchantment.LOOT_BONUS_BLOCKS, 3).build(), 5);
        this.setupRarity(new ItemBuilder(Material.DIAMOND_PICKAXE).enchant(Enchantment.DIG_SPEED, 4).build(), 5);
    }

    @Override
    public ChatColor getColour() {
        return ChatColor.BLUE;
    }
}
