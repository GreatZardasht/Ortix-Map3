/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.google.common.primitives.Ints
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package com.customhcf.base.command.module.essential;

import com.customhcf.base.BaseConstants;
import com.customhcf.base.command.BaseCommand;
import com.customhcf.util.BukkitUtils;
import com.google.common.primitives.Ints;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EnchantCommand
extends BaseCommand {
    public EnchantCommand() {
        super("enchant", "Adds enchantment to items.");
        this.setUsage("/(command) <enchantment> <level> [playerName]");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String itemName;
        Player target;
        if (args.length < 2) {
            sender.sendMessage(this.getUsage());
            return true;
        }
        if (args.length > 2 && sender.hasPermission(command.getPermission() + ".others")) {
            target = BukkitUtils.playerWithNameOrUUID(args[2]);
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(this.getUsage(label));
                return true;
            }
            target = (Player)sender;
        }
        if (target == null || !BaseCommand.canSee(sender, target)) {
            sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
            return true;
        }
        Enchantment enchantment = Enchantment.getByName((String)args[0]);
        if (enchantment == null) {
            sender.sendMessage((Object)ChatColor.RED + "No enchantment named '" + args[0] + "' found.");
            return true;
        }
        ItemStack stack = target.getItemInHand();
        if (stack == null || stack.getType() == Material.AIR) {
            sender.sendMessage((Object)ChatColor.RED + target.getName() + " is not holding an item.");
            return true;
        }
        Integer level = Ints.tryParse((String)args[1]);
        if (level == null) {
            sender.sendMessage((Object)ChatColor.RED + "'" + args[1] + "' is not a number.");
            return true;
        }
        int maxLevel = enchantment.getMaxLevel();
        if (level > maxLevel && !sender.hasPermission(command.getPermission() + ".abovemaxlevel")) {
            sender.sendMessage((Object)ChatColor.RED + "The maximum enchantment level for " + enchantment.getName() + " is " + maxLevel + '.');
            return true;
        }
        if (!enchantment.canEnchantItem(stack) && !sender.hasPermission(command.getPermission() + ".anyitem")) {
            sender.sendMessage((Object)ChatColor.RED + "Enchantment " + enchantment.getName() + " cannot be applied to that item.");
            return true;
        }
        stack.addUnsafeEnchantment(enchantment, level.intValue());
        try {
            itemName = CraftItemStack.asNMSCopy((ItemStack)stack).getName();
        }
        catch (Error ex) {
            itemName = stack.getType().name();
        }
        Command.broadcastCommandMessage((CommandSender)sender, (String)((Object)ChatColor.YELLOW + "Enchanted " + enchantment.getName() + " at level " + level + " onto " + itemName + " of " + target.getName() + '.'));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 1: {
                Enchantment[] enchantments = Enchantment.values();
                ArrayList<String> results = new ArrayList<String>(enchantments.length);
                for (Enchantment enchantment : enchantments) {
                    results.add(enchantment.getName());
                }
                return BukkitUtils.getCompletions(args, results);
            }
            case 3: {
                return null;
            }
        }
        return Collections.emptyList();
    }
}

