package com.customhcf.hcf.command;

import com.customhcf.hcf.Utils.Lang;
import com.customhcf.util.ExperienceManager;
import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * Created by nyang on 4/2/2017.
 */
public class BottleXPCommand implements CommandExecutor {

    private static final String BOTTLED_EXP_DISPLAY_NAME = ChatColor.AQUA.toString() + "Bottled Exp";

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("bottle")) {
            int exp = new ExperienceManager(player).getCurrentExp();
            if (exp > 0) {
                createExpBottle(player, exp);
                player.setExp(0.0f);
                player.setLevel(0);
                player.sendMessage(Lang.BOTTLED_XP.toString());
                return true;
            } else {
                player.sendMessage(Lang.BOTTLED_XP_FAIL.toString());
                return true;
            }
        }
        return false;
    }

    private ItemStack createExpBottle(Player player, int experience) {
        ItemStack stack = new ItemStack(Material.EXP_BOTTLE, 1);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(BOTTLED_EXP_DISPLAY_NAME);
        meta.setLore((List) Lists.newArrayList((Object[])new String[]{ChatColor.WHITE.toString() + experience + (Object)ChatColor.GOLD + " Experience"}));
        stack.setItemMeta(meta);
        player.getInventory().addItem(stack);
        return stack;
    }

    private boolean isBottledExperience(ItemStack stack) {
        if (stack == null || !stack.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = stack.getItemMeta();
        return meta.hasDisplayName() && meta.getDisplayName().equals(BOTTLED_EXP_DISPLAY_NAME);
    }
}
