package com.customhcf.hcf.tasks;

import com.customhcf.hcf.HCF;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

/**
 * Created by nyang on 4/2/2017.
 */
public class CraftTask {

    public static void gMelon() {
        ShapedRecipe cmelon = new ShapedRecipe(new ItemStack(Material.SPECKLED_MELON, 1));
        cmelon.shape(new String[] { "AAA", "CBA", "AAA" }).setIngredient('B', Material.MELON).setIngredient('C', Material.GOLD_NUGGET);
        Bukkit.getServer().addRecipe(cmelon);
    }

}

