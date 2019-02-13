/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.gnu.trove.map.TObjectLongMap
 *  net.minecraft.util.gnu.trove.map.hash.TObjectLongHashMap
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.entity.Arrow
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.projectiles.ProjectileSource
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package com.customhcf.hcf.classes.archer;

import com.customhcf.hcf.Utils.Cooldowns;
import com.customhcf.hcf.HCF;
import com.customhcf.hcf.classes.PvpClass;

import java.util.*;
import java.util.concurrent.TimeUnit;
import net.minecraft.util.gnu.trove.map.TObjectLongMap;
import net.minecraft.util.gnu.trove.map.hash.TObjectLongHashMap;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class ArcherClass
        extends PvpClass
        implements Listener {
    public static final HashMap<UUID, UUID> TAGGED = new HashMap();
    private static final PotionEffect ARCHER_SPEED_EFFECT;
    private static final PotionEffect ARCHER_JUMP_EFFECT;
    private static final int ARCHER_SPEED_COOLDOWN_DELAY;
    private final TObjectLongMap<UUID> archerSpeedCooldowns = new TObjectLongHashMap();
    private final HCF plugin;

    public ArcherClass(HCF plugin) {
        super("Archer", TimeUnit.SECONDS.toMillis(1L));
        this.plugin = plugin;
        this.passiveEffects.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
        this.passiveEffects.add(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0));
        this.passiveEffects.add(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
    }

    @Override
    public boolean onEquip(Player player) {
        if (!super.onEquip(player)) {
            return false;
        }
        return true;
    }

    @EventHandler
    public void onPlayerClickFeather(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (this.plugin.getPvpClassManager().getEquippedClass(p) != null && this.plugin.getPvpClassManager().getEquippedClass(p).equals(this) && p.getItemInHand().getType() == Material.FEATHER) {
            if (Cooldowns.isOnCooldown("Archer_item_cooldown1", p)) {
                p.sendMessage((Object)ChatColor.RED + "You cannot use this for another " + ChatColor.BOLD.toString() + HCF.getRemaining(Cooldowns.getCooldownForPlayerLong("Archer_item_cooldown1", p), true) + (Object)ChatColor.RED + '.');
                e.setCancelled(true);
                return;
            }
            Cooldowns.addCooldown("Archer_item_cooldown1", p, ARCHER_SPEED_COOLDOWN_DELAY);
            if (p.getItemInHand().getAmount() == 1) {
                p.getInventory().remove(p.getItemInHand());
            } else {
                p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
            }
            p.addPotionEffect(ARCHER_JUMP_EFFECT);
        }
    }

    @EventHandler
    public void onPlayerClickSugar(PlayerInteractEvent e) {
        final Player p = e.getPlayer();
        if (this.plugin.getPvpClassManager().getEquippedClass(p) != null && this.plugin.getPvpClassManager().getEquippedClass(p).equals(this) && p.getItemInHand().getType() == Material.SUGAR) {
            if (Cooldowns.isOnCooldown("Archer_item_cooldown", p)) {
                p.sendMessage((Object)ChatColor.RED + "You cannot use this for another " + ChatColor.BOLD.toString() + HCF.getRemaining(Cooldowns.getCooldownForPlayerLong("Archer_item_cooldown", p), true) + (Object)ChatColor.RED + '.');
                e.setCancelled(true);
                return;
            }
            Cooldowns.addCooldown("Archer_item_cooldown", p, ARCHER_SPEED_COOLDOWN_DELAY);
            if (p.getItemInHand().getAmount() == 1) {
                p.getInventory().remove(p.getItemInHand());
            } else {
                p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
            }
            p.removePotionEffect(PotionEffectType.SPEED);
            p.addPotionEffect(ARCHER_SPEED_EFFECT);
            BukkitTask bukkitTask = new BukkitRunnable(){

                public void run() {
                    if (ArcherClass.this.isApplicableFor(p)) {
                        p.removePotionEffect(PotionEffectType.SPEED);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
                    }
                }
            }.runTaskLater((Plugin)this.plugin, 180);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (TAGGED.containsKey(e.getPlayer().getUniqueId())) {
            TAGGED.remove(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Arrow arrow;
        ProjectileSource source;
        Entity entity = event.getEntity();
        Entity damager = event.getDamager();
        if (entity instanceof Player && damager instanceof Arrow && (source = (arrow = (Arrow)damager).getShooter()) instanceof Player) {
            Player damaged = (Player)event.getEntity();
            Player shooter = (Player)source;
            PvpClass equipped = this.plugin.getPvpClassManager().getEquippedClass(shooter);
            if (equipped == null || !equipped.equals(this)) {
                return;
            }
            if (this.plugin.getTimerManager().archerTimer.getRemaining((Player)entity) == 0 || this.plugin.getTimerManager().archerTimer.getRemaining((Player)entity) < TimeUnit.SECONDS.toMillis(5)) {
                if (this.plugin.getPvpClassManager().getEquippedClass(damaged) != null && this.plugin.getPvpClassManager().getEquippedClass(damaged).equals(this)) {
                    return;
                }
                this.plugin.getTimerManager().archerTimer.setCooldown((Player)entity, entity.getUniqueId());
                TAGGED.put(damaged.getUniqueId(), shooter.getUniqueId());
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    Collection<? extends Player> c = Arrays.asList(Bukkit.getOnlinePlayers());
                    this.plugin.getScoreboardHandler().getPlayerBoard(player.getUniqueId()).addUpdates(c);
                }
                shooter.sendMessage((Object)ChatColor.YELLOW + "You marked " + (Object)ChatColor.GOLD + damaged.getName() + " for 10 seconds " + (Object)ChatColor.GRAY + "[Damage: " + (Object)ChatColor.RED + event.getDamage() + (Object)ChatColor.GRAY + "]");
                damaged.sendMessage((Object)ChatColor.GOLD + "Archer Tagged! " + (Object)ChatColor.YELLOW + "You have been Archer Tagged, any damage you take by a player will be increased by 25%");
            }
        }
    }

    @Override
    public boolean isApplicableFor(Player player) {
        PlayerInventory playerInventory = player.getInventory();
        ItemStack helmet = playerInventory.getHelmet();
        if (helmet == null || helmet.getType() != Material.LEATHER_HELMET) {
            return false;
        }
        ItemStack chestplate = playerInventory.getChestplate();
        if (chestplate == null || chestplate.getType() != Material.LEATHER_CHESTPLATE) {
            return false;
        }
        ItemStack leggings = playerInventory.getLeggings();
        if (leggings == null || leggings.getType() != Material.LEATHER_LEGGINGS) {
            return false;
        }
        ItemStack boots = playerInventory.getBoots();
        return boots != null && boots.getType() == Material.LEATHER_BOOTS;
    }

    static {
        ARCHER_JUMP_EFFECT = new PotionEffect(PotionEffectType.SPEED, 180, 5);
        ARCHER_SPEED_EFFECT = new PotionEffect(PotionEffectType.SPEED, 180, 3);
        ARCHER_SPEED_COOLDOWN_DELAY = 30;
    }

}