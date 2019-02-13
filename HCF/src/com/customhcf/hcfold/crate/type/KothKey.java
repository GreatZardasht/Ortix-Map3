package com.customhcf.hcfold.crate.type;

import com.customhcf.hcf.Utils.ConfigurationService;
import com.customhcf.hcfold.crate.EnderChestKey;
import com.customhcf.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

/**
 * Created by nyang on 2/15/2017.
 */
public class KothKey extends EnderChestKey {

    public KothKey () {
        super("Koth" , 4);
        this.setupRarity(new ItemBuilder(Material.BOW).enchant(Enchantment.ARROW_DAMAGE, 3).enchant(Enchantment.ARROW_FIRE, 1).enchant(Enchantment.ARROW_INFINITE, 1).enchant(Enchantment.DURABILITY, 5).displayName(ChatColor.RED.toString() + ChatColor.BOLD + "[Koth Bow]").build(), 12);
        this.setupRarity(new ItemBuilder(Material.DIAMOND_SWORD).enchant(Enchantment.DAMAGE_ALL, 1).enchant(Enchantment.FIRE_ASPECT, 1).enchant(Enchantment.DURABILITY, 5).displayName(ChatColor.RED.toString() + ChatColor.BOLD + "[Koth Fire]").build(), 12);
        this.setupRarity(new ItemStack(Material.WEB, 64), 13);
        this.setupRarity(new ItemStack(Material.GOLD_BLOCK, 32), 12);
        this.setupRarity(new ItemStack(Material.DIAMOND_BLOCK, 32), 12);
        this.setupRarity(new ItemStack(Material.GHAST_TEAR, 1), 12);
        this.setupRarity(new ItemStack(Material.GOLDEN_APPLE, 64), 12);
        this.setupRarity(new ItemStack(Material.GOLDEN_APPLE, 1, (short) 1), 12);
    }


    @Override
    public ChatColor getColour() {
        return ChatColor.RED;
    }

    @Override
    public boolean getBroadcastItems() {
        return true;
    }
}
