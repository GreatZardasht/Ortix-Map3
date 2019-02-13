/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  net.minecraft.server.v1_7_R4.Entity
 *  net.minecraft.server.v1_7_R4.EntityLiving
 *  net.minecraft.server.v1_7_R4.EntityPlayer
 *  org.apache.commons.lang3.text.WordUtils
 *  org.bukkit.ChatColor
 *  org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity
 *  org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.PlayerDeathEvent
 */
package com.customhcf.hcf.listener;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.user.FactionUser;
import com.customhcf.hcf.user.UserManager;
import com.google.common.base.Preconditions;
import java.util.UUID;
import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.EntityLiving;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathMessageListener
implements Listener {
    private final HCF plugin;

    public DeathMessageListener(HCF plugin) {
        this.plugin = plugin;
    }

    public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)" + regex + "(?!.*?" + regex + ')', replacement);
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        String message = event.getDeathMessage();
        if (message == null || message.isEmpty()) {
            return;
        }
        event.setDeathMessage(this.getDeathMessage(message, (org.bukkit.entity.Entity)event.getEntity(), (org.bukkit.entity.Entity)this.getKiller(event)));
    }

    private CraftEntity getKiller(PlayerDeathEvent event) {
        EntityLiving lastAttacker = ((CraftPlayer)event.getEntity()).getHandle().aX();
        return lastAttacker == null ? null : lastAttacker.getBukkitEntity();
    }

    private String getDeathMessage(String input, org.bukkit.entity.Entity entity, org.bukkit.entity.Entity killer) {
        input = input.replaceFirst("\\[", (Object)ChatColor.GRAY + "[" + (Object)ChatColor.GRAY);
        input = DeathMessageListener.replaceLast(input, "]", (Object)ChatColor.GRAY + "]" + (Object)ChatColor.GRAY);
        if (entity != null) {
            input = input.replaceFirst("(?i)" + this.getEntityName(entity), (Object)ChatColor.RED + this.getDisplayName(entity) + (Object)ChatColor.YELLOW);
        }
        if (!(killer == null || entity != null && killer.equals((Object)entity))) {
            input = input.replaceFirst("(?i)" + this.getEntityName(killer), (Object)ChatColor.RED + this.getDisplayName(killer) + (Object)ChatColor.YELLOW);
        }
        return input;
    }

    private String getEntityName(org.bukkit.entity.Entity entity) {
        Preconditions.checkNotNull((Object)entity, (Object)"Entity cannot be null");
        return entity instanceof Player ? ((Player)entity).getName() : ((CraftEntity)entity).getHandle().getName();
    }

    private String getDisplayName(org.bukkit.entity.Entity entity) {
        Preconditions.checkNotNull((Object)entity, (Object)"Entity cannot be null");
        if (entity instanceof Player) {
            Player player = (Player)entity;
            return player.getName() + (Object)ChatColor.GRAY + '[' + (Object)ChatColor.GRAY + this.plugin.getUserManager().getUser(player.getUniqueId()).getKills() + (Object)ChatColor.GRAY + ']';
        }
        return WordUtils.capitalizeFully((String)entity.getType().name().replace('_', ' '));
    }
}

