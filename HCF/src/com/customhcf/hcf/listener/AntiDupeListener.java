package com.customhcf.hcf.listener;

import com.comphenix.protocol.PacketType;
import com.customhcf.hcf.HCF;
import com.customhcf.hcf.user.FactionUser;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by nyang on 2/6/2017.
 */
public class AntiDupeListener implements Listener {

    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        if (event.getInventory().getType() == InventoryType.ANVIL) {
            if(event.getSlotType() == InventoryType.SlotType.RESULT) {
                if ((event.getCurrentItem().getType() == Material.CHEST)
                        || (event.getCurrentItem().getType() == Material.HOPPER)
                        || (event.getCurrentItem().getType() == Material.MINECART)
                        || (event.getCurrentItem().getType() == Material.STORAGE_MINECART)
                        || (event.getCurrentItem().getType() == Material.HOPPER_MINECART)
                        || (event.getCurrentItem().getType() == Material.EXPLOSIVE_MINECART)
                        || (event.getCurrentItem().getType() == Material.POWERED_MINECART)
                        || (event.getCurrentItem().getType() == Material.FURNACE)
                        || (event.getCurrentItem().getType() == Material.TRIPWIRE_HOOK)) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "Nice try! You are not allow to rename this item!");
                }
            }
        }
    }

    @EventHandler(ignoreCancelled=false, priority= EventPriority.HIGH)
    public void onBlockPlace(BlockPlaceEvent e)
    {
        Player player = e.getPlayer();
        ItemMeta blockMeta = player.getItemInHand().getItemMeta();
        if (player.isOp()) {
            return;
        }
        if (!blockMeta.getDisplayName().contains(" Key Reward"))
        {
            e.setBuild(true);
        }
        else
        {
            FactionUser factionUser = HCF.getPlugin().getUserManager().getUser(player.getUniqueId());

            if (!factionUser.isBlacklisted()) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ipban -s " + player.getName() + " Blacklisted from SaberPvP Network!");
                Bukkit.broadcastMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&c" + player.getName() + " has been &c&lblacklisted &cfrom FlarePvP Network!"));
                factionUser.setBlacklisted(true);
            }

            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInter(PlayerInteractEvent event) {
        Player player = (Player) event.getPlayer();
        Block block = event.getClickedBlock();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (player.getItemInHand() != null || player.getItemInHand().getType() != Material.AIR) {
                if (player.getItemInHand().getType() == Material.TRIPWIRE_HOOK) {
                    if (block.getType() != null || block.getType() != Material.AIR) {
                        if ((block.getType() == Material.HOPPER)
                                || (block.getType() == Material.MINECART)
                                || (block.getType() == Material.STORAGE_MINECART)
                                || (block.getType() == Material.HOPPER_MINECART)
                                || (block.getType() == Material.EXPLOSIVE_MINECART)
                                || (block.getType() == Material.POWERED_MINECART)
                                || (block.getType() == Material.FURNACE)
                                || (block.getType() == Material.TRIPWIRE_HOOK)
                                || (block.getType() == Material.DROPPER)
                                || (block.getType() == Material.DISPENSER)) {
                            FactionUser factionUser = HCF.getPlugin().getUserManager().getUser(player.getUniqueId());

                            if (!factionUser.isBlacklisted()) {
                                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ipban -s " + player.getName() + " Blacklisted from SaberPvP Network!");
                                Bukkit.broadcastMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&c" + player.getName() + " has been &c&lblacklisted &cfrom FlarePvP Network!"));
                                factionUser.setBlacklisted(true);
                            }

                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
