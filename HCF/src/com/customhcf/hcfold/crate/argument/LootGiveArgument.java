/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.util.command.CommandArgument
 *  com.google.common.primitives.Ints
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Item
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package com.customhcf.hcfold.crate.argument;

import com.customhcf.hcf.HCF;
import com.customhcf.hcfold.crate.Key;
import com.customhcf.hcfold.crate.KeyManager;
import com.customhcf.util.command.CommandArgument;
import com.google.common.primitives.Ints;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class LootGiveArgument
extends CommandArgument {
    private final HCF plugin;

    public LootGiveArgument(HCF plugin) {
        super("give", "Gives a crate key to a player");
        this.plugin = plugin;
        this.aliases = new String[]{"send"};
        this.permission = "hcf.command.loot.argument." + this.getName();
    }

    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName() + " <playerName> <type> <amount>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Integer quantity;
        if (args.length < 3) {
            sender.sendMessage((Object)ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        Player target = Bukkit.getPlayer((String)args[1]);
        if (target == null || sender instanceof Player && !((Player)sender).canSee(target)) {
            sender.sendMessage((Object)ChatColor.GOLD + "Player '" + (Object)ChatColor.WHITE + args[1] + (Object)ChatColor.GOLD + "' not found.");
            return true;
        }
        Key key = this.plugin.getKeyManager().getKey(args[2]);
        if (key == null) {
            sender.sendMessage((Object)ChatColor.RED + "There is no key type named '" + args[2] + "'.");
            return true;
        }
        if (args.length >= 4) {
            quantity = Ints.tryParse((String)args[3]);
            if (quantity == null) {
                sender.sendMessage((Object)ChatColor.RED + "'" + args[3] + "' is not a number.");
                return true;
            }
        } else {
            quantity = 1;
        }
        if (quantity <= 0) {
            sender.sendMessage((Object)ChatColor.RED + "You can only give keys in positive quantities.");
            return true;
        }
        ItemStack stack = key.getItemStack().clone();
        int maxAmount = 16;
        if (quantity > 64) {
            sender.sendMessage((Object)ChatColor.RED + "You cannot give keys in quantities more than " + 64 + '.');
            return true;
        }
        stack.setAmount(quantity.intValue());
        PlayerInventory inventory = target.getInventory();
        Location location = target.getLocation();
        World world = target.getWorld();
        final Map<Integer, ItemStack> excess = (Map<Integer, ItemStack>)inventory.addItem(new ItemStack[] { stack });
        for (final ItemStack entry : excess.values()) {
            world.dropItemNaturally(location, entry);
        }
        sender.sendMessage((Object)ChatColor.GREEN + "Given " + quantity + "x " + key.getDisplayName() + (Object)ChatColor.GREEN + " key to " + target.getName() + '.');
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2) {
            return null;
        }
        if (args.length == 3) {
            return this.plugin.getKeyManager().getKeys().stream().map(Key::getName).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}

