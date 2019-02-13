package com.customhcf.hcfold.crate.type;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.faction.type.PlayerFaction;
import com.customhcf.hcfold.crate.EnderChestKey;
import com.customhcf.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Kurry on 8/6/2016.
 */
public class Tier1 extends EnderChestKey {

    public Tier1() {
        super("Tier1", 3);
        this.setupRarity(new ItemStack(Material.BLAZE_ROD, 4), 10);
        this.setupRarity(new ItemStack(Material.SPIDER_EYE, 4), 10);
        this.setupRarity(new ItemStack(Material.GLOWSTONE_DUST, 8), 10);
        this.setupRarity(new ItemStack(Material.POTATO, 4), 10);
        this.setupRarity(new ItemStack(Material.GOLD_BLOCK, 16), 10);
        this.setupRarity(new ItemStack(Material.EMERALD_BLOCK, 8), 10);
        this.setupRarity(new ItemStack(Material.ENDER_PEARL, 6), 10);
        this.setupRarity(new ItemBuilder(Material.DIAMOND_SWORD).enchant(Enchantment.LOOT_BONUS_MOBS, 2).build(), 10);
        this.setupRarity(new ItemBuilder(Material.DIAMOND_PICKAXE).enchant(Enchantment.LOOT_BONUS_BLOCKS, 2).build(), 10);
        this.setupRarity(new ItemBuilder(Material.DIAMOND_PICKAXE).enchant(Enchantment.DIG_SPEED, 3).build(), 10);
    }

    @Override
    public ChatColor getColour() {
        return ChatColor.BLUE;
    }
}
