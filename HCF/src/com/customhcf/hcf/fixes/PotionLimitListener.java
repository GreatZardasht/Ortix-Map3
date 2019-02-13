/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockState
 *  org.bukkit.block.BrewingStand
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.ThrownPotion
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.entity.PotionSplashEvent
 *  org.bukkit.event.inventory.BrewEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerItemConsumeEvent
 *  org.bukkit.inventory.BrewerInventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.potion.Potion
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.potion.PotionType
 *  org.bukkit.projectiles.ProjectileSource
 */
package com.customhcf.hcf.fixes;

import com.customhcf.hcf.Utils.ConfigurationService;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class PotionLimitListener
implements Listener {
    private static final int EMPTY_BREW_TIME = 400;

    public int getMaxLevel(PotionType type) {
        return ConfigurationService.POTION_LIMITS.getOrDefault((Object)type, type.getMaxLevel());
    }

    @EventHandler
    public void onSplash(PotionSplashEvent e) {
        if (e.getEntity() != null) {
            for (PotionEffect effect : e.getEntity().getEffects()) {
                if (effect == null || effect.getType() == null || !effect.getType().equals(PotionEffectType.INCREASE_DAMAGE) || !effect.getType().equals(PotionEffectType.INVISIBILITY)) continue;
                e.setCancelled(true);
                ((Player)e.getEntity().getShooter()).getItemInHand().setType(Material.AIR);
            }
        }
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        PotionType type;
        Potion potion;
        if (e.getItem() != null && e.getItem().getType() == Material.POTION && e.getItem().getDurability() != 0 && (potion = Potion.fromItemStack((ItemStack)e.getItem())) != null && (type = potion.getType()) != null && type == PotionType.STRENGTH) {
            e.setCancelled(true);
            e.setItem(null);
        }
    }

    @EventHandler
    public void onSplashEvent(PlayerInteractEvent event) {
        ItemStack itemStack = event.getItem();
        if (itemStack != null) {
            if (itemStack.getType() == Material.POTION) {
                if (itemStack.getDurability() == 16424) {
                    event.setCancelled(true);
                }

                if (itemStack.getDurability() == 16456) {
                    event.setCancelled(true);
                }

                if (itemStack.getDurability() == 16424) {
                    event.setCancelled(true);
                }

                if (itemStack.getDurability() == 16460) {
                    event.setCancelled(true);
                }

                if (itemStack.getDurability() == 16428) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void specificInvis(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        if (event.getItem().getType() == Material.POTION) {
            if (event.getItem().getDurability() == 8270 ) {
                player.sendMessage(ChatColor.RED + "You cannot use this version of Invisiblity! Only 3:00 Invis.");
                event.setCancelled(true);
            }

            if (event.getItem().getDurability() == 8268) {
                event.setCancelled(true);
            }

            if (event.getItem().getDurability() == 8236) {
                event.setCancelled(true);
            }

            if (event.getItem().getDurability() == 8264) {
                event.setCancelled(true);
            }

            if (event.getItem().getDurability() == 8232) {
                event.setCancelled(true);
            }
        }
    }
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onBrew(BrewEvent event) {
        if (event.getContents() == null) {
            return;
        }
        for (ItemStack stack : event.getContents().getContents()) {
            if (stack.getType() == null) {
                return;
            }
            if (stack.getType() != Material.BLAZE_POWDER) continue;
            event.setCancelled(true);
        }
        if (!this.testValidity(event.getResults())) {
            event.setCancelled(true);
        }
    }

    private boolean testValidity(ItemStack[] contents) {
        for (ItemStack stack : contents) {
            PotionType type;
            Potion potion;
            if (stack == null || stack.getType() != Material.POTION || stack.getDurability() == 0 || (potion = Potion.fromItemStack((ItemStack)stack)) == null || (type = potion.getType()) == null) continue;
            if ((potion.hasExtendedDuration() || potion.getLevel() != 1) && potion.getLevel() > this.getMaxLevel(type)) {
                return false;
            }
            if (type == PotionType.STRENGTH) {
                return false;
            }
            if (type == PotionType.POISON && potion.hasExtendedDuration()) {
                return false;
            }
            if (type != PotionType.SLOWNESS || !potion.hasExtendedDuration()) continue;
            return false;
        }
        return true;
    }
}

