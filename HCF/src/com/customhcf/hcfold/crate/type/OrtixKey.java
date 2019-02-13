package com.customhcf.hcfold.crate.type;

import com.customhcf.hcfold.crate.EnderChestKey;
import com.customhcf.hcfold.crate.KeyListener;
import com.customhcf.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

/**
 * Created by nyang on 4/4/2017.
 */
public class OrtixKey extends EnderChestKey {

    public OrtixKey () {
        super("Sapphire" , 4);
        this.setupRarity(new ItemStack(Material.MAGMA_CREAM, 5), 6);
        this.setupRarity(new ItemStack(Material.QUARTZ, 3), 6);
        this.setupRarity(new ItemStack(Material.DIAMOND_BLOCK, 3), 6);
        this.setupRarity(new ItemStack(Material.IRON_BLOCK, 2), 6);
        this.setupRarity(new ItemStack(Material.GOLD_BLOCK, 4), 6);
        this.setupRarity(new ItemStack(Material.GHAST_TEAR, 1), 6);
        this.setupRarity(new ItemStack(Material.ENDER_PEARL, 8), 6);
        this.setupRarity(new ItemStack(Material.EXP_BOTTLE, 25), 6);
        this.setupRarity(new ItemStack(Material.GOLDEN_APPLE, 1, (short) 1), 6);
        this.setupRarity(new ItemStack(Material.MONSTER_EGG, 5, (short) 92), 6);
        this.setupRarity(new ItemStack(Material.BEACON, 1), 6);
        this.setupRarity(new ItemStack(Material.BLAZE_ROD, 4), 6);
        this.setupRarity(new ItemBuilder(Material.DIAMOND_PICKAXE, 1).enchant(Enchantment.LOOT_BONUS_BLOCKS, 3).build(), 6);
        this.setupRarity(new ItemBuilder(Material.DIAMOND_SPADE, 1).enchant(Enchantment.DIG_SPEED, 3).enchant(Enchantment.DURABILITY, 1).build(), 7);
        this.setupRarity(new ItemBuilder(Material.DIAMOND_SWORD, 1).enchant(Enchantment.DAMAGE_ALL, 1).build(), 7);
        this.setupRarity(new ItemBuilder(Material.DIAMOND_AXE, 1).enchant(Enchantment.DIG_SPEED, 3).build(), 7);
    }
}
