/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Enums
 *  com.google.common.base.Optional
 *  com.google.common.primitives.Ints
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Chunk
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Tameable
 */
package com.customhcf.base.command.module.essential;

import com.customhcf.base.command.BaseCommand;
import com.customhcf.util.BukkitUtils;
import com.google.common.base.Enums;
import com.google.common.base.Optional;
import com.google.common.primitives.Ints;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;

public class RemoveEntityCommand
extends BaseCommand {
    public RemoveEntityCommand() {
        super("removeentity", "Removes all of a specific entity.");
        this.setUsage("/(command) <worldName> <entityType> [removeCustomNamed] [radius]");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean removeCustomNamed;
        Integer radius;
        if (args.length < 2) {
            sender.sendMessage(this.getUsage(label));
            return true;
        }
        World world = Bukkit.getWorld((String)args[0]);
        Optional optionalType = Enums.getIfPresent((Class)EntityType.class, (String)args[1].toUpperCase());
        if (!optionalType.isPresent()) {
            sender.sendMessage((Object)ChatColor.RED + "Not an entity named '" + args[1] + "'.");
            return true;
        }
        EntityType entityType = (EntityType)optionalType.get();
        if (entityType == EntityType.PLAYER) {
            sender.sendMessage((Object)ChatColor.RED + "You cannot remove " + entityType.name() + " entities!");
            return true;
        }
        boolean bl = removeCustomNamed = args.length > 2 && Boolean.parseBoolean(args[2]);
        if (args.length > 3) {
            radius = Ints.tryParse((String)args[3]);
            if (radius == null) {
                sender.sendMessage((Object)ChatColor.RED + "'" + args[3] + "' is not a number.");
                return true;
            }
            if (radius <= 0) {
                sender.sendMessage((Object)ChatColor.RED + "Radius must be positive.");
                return true;
            }
        } else {
            radius = 0;
        }
        Location location = sender instanceof Player ? ((Player)sender).getLocation() : null;
        int removed = 0;
        for (Chunk chunk : world.getLoadedChunks()) {
            for (Entity entity : chunk.getEntities()) {
                LivingEntity livingEntity;
                if (entity.getType() != entityType || radius != 0 && (location == null || location.distanceSquared(entity.getLocation()) > (double)radius.intValue()) || !removeCustomNamed && (entity instanceof Tameable && ((Tameable)entity).isTamed() || entity instanceof LivingEntity && (livingEntity = (LivingEntity)entity).getCustomName() != null)) continue;
                entity.remove();
                ++removed;
            }
        }
        sender.sendMessage((Object)ChatColor.YELLOW + "Removed " + removed + " of " + entityType.getName() + '.');
        return true;
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        List<String> results = null;
        switch (args.length) {
            case 1: {
                final Collection<World> worlds = (Collection<World>)Bukkit.getWorlds();
                results = new ArrayList<String>(worlds.size());
                for (final World world : worlds) {
                    results.add(world.getName());
                }
                break;
            }
            case 2: {
                EntityType[] entityTypes = EntityType.values();
                results = new ArrayList(entityTypes.length);
                for (EntityType entityType : entityTypes) {
                    if (entityType == EntityType.UNKNOWN || entityType == EntityType.PLAYER) continue;
                    results.add(entityType.name());
                }
                break;
            }
            default: {
                return Collections.emptyList();
            }
        }
        return BukkitUtils.getCompletions(args, results);
    }
}

