/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.base.BasePlugin
 *  com.customhcf.util.InventoryUtils
 *  com.customhcf.util.SignHandler
 *  com.customhcf.util.itemdb.ItemDb
 *  com.google.common.primitives.Ints
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockState
 *  org.bukkit.block.Sign
 *  org.bukkit.entity.Item
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.material.MaterialData
 */
package com.customhcf.hcf.balance;

import com.customhcf.base.BasePlugin;
import com.customhcf.hcf.HCF;
import com.customhcf.hcf.balance.EconomyManager;
import com.customhcf.hcf.crowbar.Crowbar;
import com.customhcf.util.InventoryUtils;
import com.customhcf.util.SignHandler;
import com.customhcf.util.itemdb.ItemDb;
import com.google.common.primitives.Ints;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.material.MaterialData;

public class ShopSignListener
        implements Listener {
    private static final long SIGN_TEXT_REVERT_TICKS = 100;
    private static final Pattern ALPHANUMERIC_REMOVER = Pattern.compile("[^A-Za-z0-9]");
    private final HCF plugin;

    public ShopSignListener(HCF plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled=false, priority=EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        BlockState state;
        Block block;
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && (state = (block = event.getClickedBlock()).getState()) instanceof Sign) {
            ItemStack stack;
            Sign sign = (Sign)state;
            String[] lines = sign.getLines();
            Integer quantity = Ints.tryParse((String)lines[2]);
            if (quantity == null) {
                return;
            }
            Integer price = Ints.tryParse((String)ALPHANUMERIC_REMOVER.matcher(lines[3]).replaceAll(""));
            if (price == null) {
                return;
            }
            if (lines[1].equalsIgnoreCase("Crowbar")) {
                stack = new Crowbar().getItemIfPresent();
            } else {
                stack = BasePlugin.getPlugin().getItemDb().getItem(ALPHANUMERIC_REMOVER.matcher(lines[1]).replaceAll(""), quantity.intValue());
                if (stack == null) {
                    return;
                }
            }
            Player player = event.getPlayer();
            String[] fakeLines = Arrays.copyOf(sign.getLines(), 4);
            if (lines[0].contains("Sell") && lines[0].contains(ChatColor.RED.toString()) || lines[0].contains(ChatColor.AQUA.toString())) {
                int sellQuantity = Math.min(quantity, InventoryUtils.countAmount((Inventory)player.getInventory(), (Material)stack.getType(), (short)stack.getDurability()));
                if (sellQuantity <= 0) {
                    fakeLines[0] = (Object)ChatColor.RED + "Not carrying any";
                    fakeLines[2] = (Object)ChatColor.RED + "on you.";
                    fakeLines[3] = "";
                } else {
                    int newPrice = price / quantity * sellQuantity;
                    fakeLines[0] = (Object)ChatColor.GREEN + "Sold " + sellQuantity;
                    fakeLines[3] = (Object)ChatColor.GREEN + "for " + '$' + newPrice;
                    this.plugin.getEconomyManager().addBalance(player.getUniqueId(), newPrice);
                    InventoryUtils.removeItem((Inventory)player.getInventory(), (Material)stack.getType(), (short)stack.getData().getData(), (int)sellQuantity);
                    player.updateInventory();
                }
            } else {
                if (!lines[0].contains("Buy") || !lines[0].contains(ChatColor.GREEN.toString()) || lines[0].contains(ChatColor.AQUA.toString())) {
                    return;
                }
                if (price > this.plugin.getEconomyManager().getBalance(player.getUniqueId())) {
                    fakeLines[0] = (Object)ChatColor.RED + "Cannot afford";
                } else {
                    fakeLines[0] = (Object)ChatColor.GREEN + "Item bought";
                    fakeLines[3] = (Object)ChatColor.GREEN + "for " + '$' + price;
                    this.plugin.getEconomyManager().subtractBalance(player.getUniqueId(), price);
                    final World world = player.getWorld();
                    final Location location = player.getLocation();
                    final Map<Integer, ItemStack> excess = (Map<Integer, ItemStack>)player.getInventory().addItem(new ItemStack[] { stack });
                    for (final Map.Entry<Integer, ItemStack> excessItemStack : excess.entrySet()) {
                        world.dropItemNaturally(location, (ItemStack)excessItemStack.getValue());
                    }
                    player.setItemInHand(player.getItemInHand());
                }
            }
            event.setCancelled(true);
            BasePlugin.getPlugin().getSignHandler().showLines(player, sign, fakeLines, 100, true);
        }
    }
}


