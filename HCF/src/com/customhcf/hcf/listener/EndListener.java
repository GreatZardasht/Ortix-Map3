/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.base.ServerHandler
 *  com.customhcf.util.imagemessage.ImageChar
 *  com.customhcf.util.imagemessage.ImageMessage
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.PortalType
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.World$Environment
 *  org.bukkit.block.Block
 *  org.bukkit.entity.EnderDragon
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.ExperienceOrb
 *  org.bukkit.entity.Item
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.entity.CreatureSpawnEvent
 *  org.bukkit.event.entity.EntityCreatePortalEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDeathEvent
 *  org.bukkit.event.entity.EntityExplodeEvent
 *  org.bukkit.event.entity.EntityPortalEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerPortalEvent
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package com.customhcf.hcf.listener;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.faction.type.PlayerFaction;
import com.customhcf.util.imagemessage.ImageChar;
import com.customhcf.util.imagemessage.ImageMessage;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import javax.imageio.ImageIO;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.PortalType;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EndListener
implements Listener {
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof EnderDragon) {
            PlayerFaction faction = HCF.getPlugin().getFactionManager().getPlayerFaction(event.getEntity().getKiller().getUniqueId());
            String factionName = faction == null ? "Player: " + event.getEntity().getKiller().getName() : "Faction: " + faction.getName();
            for (int i = 0; i < 5; ++i) {
                Bukkit.broadcastMessage((String)"");
            }
            for (Player on : Bukkit.getServer().getOnlinePlayers()) {
                try {
                    BufferedImage imageToSend = ImageIO.read(HCF.getPlugin().getResource("enderdragon-art.png"));
                    String[] arrstring = new String[10];
                    arrstring[0] = "";
                    arrstring[1] = "";
                    arrstring[2] = "";
                    arrstring[3] = "";
                    arrstring[4] = "";
                    arrstring[5] = "";
                    arrstring[6] = (Object)ChatColor.RED + "[EnderDragon]";
                    arrstring[7] = (Object)ChatColor.YELLOW + "Slain by";
                    arrstring[8] = ChatColor.YELLOW.toString() + (Object)ChatColor.BOLD + factionName;
                    arrstring[9] = (Object)ChatColor.GRAY + (!factionName.contains("Faction: ") ? "" : event.getEntity().getKiller().getName());
                    new ImageMessage(imageToSend, 15, ImageChar.BLOCK.getChar()).appendText(arrstring).sendToPlayer(on);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ItemStack dragonEgg = new ItemStack(Material.DRAGON_EGG);
            ItemMeta itemMeta = dragonEgg.getItemMeta();
            SimpleDateFormat sdf = new SimpleDateFormat("M/d HH:mm:ss");
            itemMeta.setLore(Arrays.asList(ChatColor.DARK_PURPLE.toString() + (Object)ChatColor.BOLD + "Enderdragon " + (Object)ChatColor.YELLOW + "slain by " + (Object)ChatColor.AQUA + event.getEntity().getKiller().getName(), (Object)ChatColor.YELLOW + sdf.format(new Date()).replace(" AM", "").replace(" PM", "")));
            dragonEgg.setItemMeta(itemMeta);
            event.getEntity().getKiller().getInventory().addItem(new ItemStack[]{dragonEgg});
            if (!event.getEntity().getKiller().getInventory().contains(Material.DRAGON_EGG)) {
                event.getDrops().add(dragonEgg);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getClickedBlock() != null && e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.DRAGON_EGG) {
            Player player = e.getPlayer();
            ExperienceOrb exp = (ExperienceOrb)e.getClickedBlock().getWorld().spawn(e.getClickedBlock().getLocation(), (Class)ExperienceOrb.class);
            exp.setExperience(1);
            if (e.getClickedBlock().hasMetadata("broken")) {
                return;
            }
            Random random = new Random();
            Integer breaks = random.nextInt(1200) + 1;
            if (breaks == 185) {
                e.getClickedBlock().setMetadata("broken", (MetadataValue)new FixedMetadataValue((Plugin)HCF.getPlugin(), (Object)"broken"));
                if (HCF.getPlugin().getFactionManager().getPlayerFaction(player.getUniqueId()) != null) {
                    HCF.getPlugin().getFactionManager().getPlayerFaction(player.getUniqueId()).broadcast((Object)ChatColor.YELLOW + "Your ender-dragon egg has been broken. It will " + (Object)ChatColor.RED + "no longer " + (Object)ChatColor.YELLOW + "drop items.");
                } else {
                    player.sendMessage((Object)ChatColor.YELLOW + "Your ender-dragon egg has been broken. It will " + (Object)ChatColor.RED + "no longer " + (Object)ChatColor.YELLOW + "drop items.");
                }
                for (Entity nearby : player.getNearbyEntities(20.0, 350.0, 20.0)) {
                    if (!(nearby instanceof Player)) continue;
                    ((Player)nearby).sendMessage((Object)ChatColor.YELLOW + "Your ender-dragon egg has been broken. It will " + (Object)ChatColor.RED + "no longer " + (Object)ChatColor.YELLOW + "drop items.");
                    ((Player)nearby).playSound(e.getClickedBlock().getLocation(), Sound.ANVIL_BREAK, 10.0f, 10.0f);
                }
            }
            Integer rand = random.nextInt(100) + 1;
            Integer gunpowder = random.nextInt(8) + 1;
            Integer enderpearl = random.nextInt(4) + 1;
            if (rand == 15) {
                e.getClickedBlock().getWorld().dropItemNaturally(e.getClickedBlock().getLocation(), new ItemStack(Material.SULPHUR, gunpowder.intValue()));
            }
            if (rand == 17) {
                e.getClickedBlock().getWorld().dropItemNaturally(e.getClickedBlock().getLocation(), new ItemStack(Material.ENDER_PEARL, enderpearl.intValue()));
            }
        }
    }

    @EventHandler
    public void onEntityCreatePortal(EntityCreatePortalEvent event) {
        if (event.getEntity() instanceof Item && event.getPortalType() == PortalType.ENDER) {
            event.getBlocks().clear();
        }
    }

    @EventHandler
    public void onEnderDragonSpawn(CreatureSpawnEvent event) {
        if (event.getEntity().getType() == EntityType.ENDER_DRAGON) {
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
    }

    @EventHandler
    public void onCreatePortal(EntityCreatePortalEvent event) {
        if (event.getEntity().getType() == EntityType.ENDER_DRAGON) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.getEntity() instanceof EnderDragon) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityPortal(EntityPortalEvent event) {
        if (event.getEntity() instanceof EnderDragon) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPortal(PlayerPortalEvent event) {
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            return;
        }
        Player player = event.getPlayer();
        if (event.getTo().getWorld().getEnvironment() == World.Environment.NORMAL) {
            if (event.getFrom().getWorld().getEntitiesByClass((Class)EnderDragon.class).size() != 0) {
                event.setCancelled(true);
                event.setTo(event.getFrom());
                HCF.getPlugin().getMessage().sendMessage(event.getPlayer(), (Object)ChatColor.RED + "You cannot leave the end before the dragon is killed.");
            }
            event.useTravelAgent(false);
            event.setTo(HCF.getPlugin().getServerHandler().getEndExit());
        } else if (event.getTo().getWorld().getEnvironment() == World.Environment.THE_END) {
            if (HCF.getPlugin().getTimerManager().spawnTagTimer.hasCooldown(player)) {
                event.setCancelled(true);
                HCF.getPlugin().getMessage().sendMessage(event.getPlayer(), (Object)ChatColor.RED + "You cannot enter the end while spawn tagged.");
            }
            if (HCF.getPlugin().getTimerManager().pvpProtectionTimer.hasCooldown(player)) {
                event.setCancelled(true);
                HCF.getPlugin().getMessage().sendMessage(event.getPlayer(), (Object)ChatColor.RED + "You cannot enter the end while you have pvp protection.");
            }
            if ((!HCF.getPlugin().getServerHandler().isEnd() || HCF.getPlugin().getEotwHandler().isEndOfTheWorld()) && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                event.setCancelled(true);
                HCF.getPlugin().getMessage().sendMessage(event.getPlayer(), (Object)ChatColor.RED + "The End is currently disabled.");
            }
            event.useTravelAgent(false);
            event.setTo(event.getTo().getWorld().getSpawnLocation());
        }
        if (event.getPlayer().hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
            boolean found = false;
            for (PotionEffect potionEffect : event.getPlayer().getActivePotionEffects()) {
                if (!potionEffect.getType().equals((Object)PotionEffectType.INCREASE_DAMAGE)) continue;
                found = true;
            }
            if (found) {
                event.getPlayer().removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            }
        }
    }
}

