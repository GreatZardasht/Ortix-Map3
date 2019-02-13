package com.customhcf.hcf.Utils;

import com.customhcf.hcf.chat.Translate;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathan'PC on 1/23/2017.
 */
public class StatsUtils {


    public static ItemStack getNewPickaxe() {
        ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
        updateItem(item, 0, 0, 0, 0, 0 ,0 ,0 ,0);
        return item;
    }

    public static void updateItem(ItemStack item, int diamond, int emerald, int gold, int redstone, int lapis, int iron, int coal, int stone) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();

        lore.add(Translate.c("&b------------------------"));
        lore.add(Translate.c("&bDiamonds: &f" + diamond));
        lore.add(Translate.c("&aEmeralds: &f" + emerald));
        lore.add(Translate.c("&6Gold: &f" + gold));
        lore.add(Translate.c("&cRedstone: &f" + redstone));
        lore.add(Translate.c("&9Lapis: &f" + lapis));
        lore.add(Translate.c("&7Iron: &f" + iron));
        lore.add(Translate.c("&0Coal: &f" + coal));
        lore.add(Translate.c("&8Stone: &f" + stone));
        lore.add(Translate.c("&b------------------------"));
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public static int getCount(ItemStack item, int index) {
        ItemMeta meta = item.getItemMeta();

        if(meta.hasLore()) {
            int value = Integer.valueOf(ChatColor.stripColor(meta.getLore().get(index).split(":")[1].trim()));
            return value;
        }
        return -1;
    }

}
