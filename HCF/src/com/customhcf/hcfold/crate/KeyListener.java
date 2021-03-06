/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.util.InventoryUtils
 *  com.customhcf.util.chat.Text
 *  com.customhcf.util.chat.TextUtils
 *  net.minecraft.server.v1_7_R4.IChatBaseComponent
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockState
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Item
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.inventory.InventoryAction
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.event.inventory.InventoryDragEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryHolder
 *  org.bukkit.inventory.InventoryView
 *  org.bukkit.inventory.ItemStack
 */
package com.customhcf.hcfold.crate;

import com.customhcf.hcf.HCF;
import com.customhcf.hcfold.crate.EnderChestKey;
import com.customhcf.hcfold.crate.Key;
import com.customhcf.hcfold.crate.KeyManager;
import com.customhcf.util.InventoryUtils;
import com.customhcf.util.chat.Text;
import com.customhcf.util.chat.TextUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class KeyListener
implements Listener {
    private final HCF plugin;

    public KeyListener(HCF plugin) {
        this.plugin = plugin;
    }

    public HashMap<Player, ItemStack> prices = new HashMap<>();

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onBlockPlace(BlockPlaceEvent event) {
        Key key = this.plugin.getKeyManager().getKey(event.getItemInHand());
        if (key != null) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        Inventory topInventory = event.getView().getTopInventory();
        if (inventory != null && topInventory != null && topInventory.equals((Object)inventory) && topInventory.getTitle().endsWith(" Crates Reward")) {
            Player player = (Player)event.getPlayer();
            Location location = player.getLocation();
            World world = player.getWorld();
            boolean isEmpty = true;
            for (ItemStack stack : topInventory.getContents()) {
                if (stack == null || stack.getType() == Material.AIR) continue;
                world.dropItemNaturally(location, stack);
                isEmpty = false;
            }
            if (!isEmpty) {
                player.sendMessage(ChatColor.YELLOW.toString() + (Object)ChatColor.BOLD + "You closed your loot crate reward inventory, dropped on the ground for you.");
            }
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onInventoryDrag(InventoryDragEvent event) {
        Inventory inventory = event.getInventory();
        Inventory topInventory = event.getView().getTopInventory();
        if (inventory != null && topInventory != null && topInventory.equals((Object)inventory) && topInventory.getTitle().endsWith(" Crates Reward")) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        Inventory topInventory = event.getView().getTopInventory();
        if (clickedInventory == null || topInventory == null || !topInventory.getTitle().endsWith(" Crates Reward")) {
            return;
        }
        InventoryAction action = event.getAction();
        if (!topInventory.equals((Object)clickedInventory) && action == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            event.setCancelled(true);
        } else if (topInventory.equals((Object)clickedInventory) && (action == InventoryAction.PLACE_ALL || action == InventoryAction.PLACE_ONE || action == InventoryAction.PLACE_SOME)) {
            event.setCancelled(true);
        }
    }

    private void decrementHand(Player player) {
        ItemStack stack = player.getItemInHand();
        if (stack.getAmount() <= 1) {
            player.setItemInHand(new ItemStack(Material.AIR, 1));
        } else {
            stack.setAmount(stack.getAmount() - 1);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack stack = event.getItem();
        if (action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Key key = this.plugin.getKeyManager().getKey(stack);
        if (key == null) {
            return;
        }
        Block block = event.getClickedBlock();
        BlockState state = block.getState();
        if (key instanceof EnderChestKey && block.getType() == Material.ENDER_CHEST) {
            InventoryView openInventory = player.getOpenInventory();
            Inventory topInventory = openInventory.getTopInventory();
            if (topInventory != null && topInventory.getTitle().endsWith(" Key Reward")) {
                return;
            }
            EnderChestKey enderChestKey = (EnderChestKey)key;
            boolean broadcastLoot = enderChestKey.getBroadcastItems();
            int rolls = enderChestKey.getRolls();
            int size = InventoryUtils.getSafestInventorySize((int)rolls);
            Inventory inventory = Bukkit.createInventory((InventoryHolder)player, (int)size, (String)(enderChestKey.getName() + " Key Reward"));
            ItemStack[] loot = enderChestKey.getLoot();
            if (loot == null) {
                player.sendMessage((Object)ChatColor.RED + "That key has no loot defined, please inform an admin.");
                return;
            }
            ArrayList<ItemStack> finalLoot = new ArrayList<ItemStack>();
            Random random = this.plugin.getRandom();
            for (int i = 0; i < rolls; ++i) {
                ItemStack item = loot[random.nextInt(loot.length)];
                if (item == null || item.getType() == Material.AIR) continue;
                finalLoot.add(item);
                inventory.setItem(i, item);
            }
            if (broadcastLoot) {
                Text text = new Text();
                text.append((IChatBaseComponent)new Text(player.getName()).setColor(ChatColor.AQUA));
                text.append((IChatBaseComponent)new Text(" has obtained ").setColor(ChatColor.YELLOW));
                text.append((IChatBaseComponent)TextUtils.joinItemList(finalLoot, (String)", ", (boolean)true));
                text.append((IChatBaseComponent)new Text(" from a ").setColor(ChatColor.YELLOW));
                text.append((IChatBaseComponent)new Text(enderChestKey.getDisplayName()).setColor(enderChestKey.getColour()));
                text.append((IChatBaseComponent)new Text(" key.").setColor(ChatColor.YELLOW));
                text.broadcast();
            }
            Location location = block.getLocation();
            player.openInventory(inventory);
            player.playSound(location, Sound.LEVEL_UP, 1.0f, 1.0f);
            this.decrementHand(player);
            event.setCancelled(true);
        }
    }

}

