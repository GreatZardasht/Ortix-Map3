/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package com.customhcf.base.command.module.inventory;

import com.customhcf.base.BaseConstants;
import com.customhcf.base.BasePlugin;
import com.customhcf.base.command.BaseCommand;
import com.customhcf.util.BukkitUtils;
import com.customhcf.util.itemdb.ItemDb;

import java.io.Console;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.google.common.primitives.Ints;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class GiveCommand
        extends BaseCommand
{
    public GiveCommand()
    {
        super("give", "Gives an item to a player.");
        setUsage("/(command) <playerName> <itemName> [quantity]");
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length < 2)
        {
            sender.sendMessage(ChatColor.RED + getUsage());
            return true;
        }
        if (Bukkit.getPlayer(args[0]) == null)
        {
            sender.sendMessage(String.format(Constants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, new Object[] { args[0] }));
            return true;
        }
        Integer ammount = Integer.valueOf(0);
        Player t = Bukkit.getPlayer(args[0]);
        if (BasePlugin.getPlugin().getItemDb().getItem(args[1]) == null)
        {
            sender.sendMessage(ChatColor.RED + "Item or ID not found.");
            return true;
        }
        if (args.length == 2)
        {
            if (!t.getInventory().addItem(new ItemStack[] { BasePlugin.getPlugin().getItemDb().getItem(args[1], BasePlugin.getPlugin().getItemDb().getItem(args[1]).getMaxStackSize()) }).isEmpty())
            {
                sender.sendMessage(ChatColor.RED + "The inventory of the player is full.");
                return true;
            }
            ammount = Integer.valueOf(64);
        }
        if (args.length == 3)
        {
            if (!t.getInventory().addItem(new ItemStack[] { BasePlugin.getPlugin().getItemDb().getItem(args[1], Ints.tryParse(args[2]).intValue()) }).isEmpty())
            {
                sender.sendMessage(ChatColor.RED + "The inventory of the player is full.");
                return true;
            }
            ammount = Ints.tryParse(args[2]);
        }
        if (args.length > 3)
        {
            String[] encahntsName = args[3].split("-");
            String name = "";
            if (args.length == 5) {
                name = args[4].replace("Name:", "");
            }
            ItemStack itemStack = BasePlugin.getPlugin().getItemDb().getItem(args[1], Ints.tryParse(args[2]).intValue());
            ItemMeta itemMeta;
            if (!name.equalsIgnoreCase(""))
            {
                itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name).replaceAll("-", " "));
                itemStack.setItemMeta(itemMeta);
            }
            for (String enchants : encahntsName)
            {
                String[] split = enchants.split(":");
                if (Enchantment.getByName(split[0]) != null) {
                    itemStack.addUnsafeEnchantment(Enchantment.getByName(split[0]), Ints.tryParse(split[1]).intValue());
                } else {
                    System.out.println(split[0]);
                }
            }
            if (!t.getInventory().addItem(new ItemStack[] { itemStack }).isEmpty())
            {
                sender.sendMessage(ChatColor.RED + "The inventory of the player is full.");
                return true;
            }
            ammount = Ints.tryParse(args[2]);
        }
        Command.broadcastCommandMessage(sender, ChatColor.WHITE + "Gave " + ChatColor.GOLD + ammount + ChatColor.WHITE + ", " + ChatColor.GOLD + BasePlugin.getPlugin().getItemDb().getName(BasePlugin.getPlugin().getItemDb().getItem(args[1])) + ChatColor.WHITE + " to " + ChatColor.BLUE + t.getName(), true);
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        switch (args.length)
        {
            case 1:
                return null;
            case 4:
                Enchantment[] enchantments = Enchantment.values();
                List<String> results = new ArrayList(enchantments.length);
                for (Enchantment enchantment : enchantments) {
                    results.add(enchantment.getName());
                }
                return BukkitUtils.getCompletions(args, results);
            case 5:
                results = new ArrayList();
                results.add("Name:");
                return results;
        }
        return Collections.emptyList();
    }
}


