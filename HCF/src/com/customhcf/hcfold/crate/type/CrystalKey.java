package com.customhcf.hcfold.crate.type;

import com.customhcf.hcfold.crate.EnderChestKey;
import com.customhcf.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

/**
 * Created by nyang on 4/8/2017.
 */
public class CrystalKey extends EnderChestKey {

    public CrystalKey () {
        super("Crystal", 3);
        this.setupRarity(new ItemBuilder(Material.DIAMOND_HELMET).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).enchant(Enchantment.DURABILITY, 3).build(), 10);
        this.setupRarity(new ItemBuilder(Material.DIAMOND_CHESTPLATE).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).enchant(Enchantment.DURABILITY, 3).build(), 10);
        this.setupRarity(new ItemBuilder(Material.DIAMOND_LEGGINGS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).enchant(Enchantment.DURABILITY, 3).build(), 10);
        this.setupRarity(new ItemBuilder(Material.DIAMOND_BOOTS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_FALL, 4).build(), 10);
        this.setupRarity(new ItemBuilder(Material.DIAMOND_PICKAXE).enchant(Enchantment.DIG_SPEED, 5).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.LOOT_BONUS_BLOCKS, 3).build(), 10);
        this.setupRarity(new ItemBuilder(Material.DIAMOND_SWORD).enchant(Enchantment.DAMAGE_ALL, 2).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.LOOT_BONUS_MOBS, 3).build(), 10);
        this.setupRarity(new ItemStack(Material.GOLDEN_APPLE, 1, (short) 1), 10);
        this.setupRarity(new ItemStack(Material.ENDER_PORTAL_FRAME, 4), 10);
        this.setupRarity(new ItemStack(Material.DIAMOND_BLOCK, 4), 10);
        this.setupRarity(new ItemStack(Material.GOLD_BLOCK, 4), 10);
    }
}
