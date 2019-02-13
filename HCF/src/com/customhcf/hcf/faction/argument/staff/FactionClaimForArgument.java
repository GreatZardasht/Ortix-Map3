/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.util.command.CommandArgument
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package com.customhcf.hcf.faction.argument.staff;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.faction.claim.Claim;
import com.customhcf.hcf.faction.type.ClaimableFaction;
import com.customhcf.hcf.faction.type.Faction;
import com.customhcf.hcf.faction.type.PlayerFaction;
import com.customhcf.util.command.CommandArgument;
import com.customhcf.util.cuboid.Cuboid;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionClaimForArgument
        extends CommandArgument
{
    public FactionClaimForArgument(HCF plugin)
    {
        super("claimfor", "Claims for a faction");
        this.plugin = plugin;
        this.permission = ("command.faction." + getName());
    }

    public String getUsage(String label)
    {
        return '/' + label + ' ' + getName() + " <factioName> [shouldClearClaims]";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length < 2)
        {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return true;
        }
        Faction faction = this.plugin.getFactionManager().getFaction(args[1]);
        if (!(faction instanceof ClaimableFaction))
        {
            sender.sendMessage(ChatColor.RED + "This is not a claimable faction (cannot contain claims)");
            return true;
        }
        if ((faction instanceof PlayerFaction))
        {
            sender.sendMessage(ChatColor.RED + "You cannot claim player factions like this. Try /f claim wand");
            return true;
        }
        WorldEditPlugin worldEditPlugin = this.plugin.getWorldEdit();
        if (worldEditPlugin == null)
        {
            sender.sendMessage(ChatColor.RED + "WorldEdit must be installed to set event claim areas.");
            return true;
        }
        Player player = (Player)sender;
        Selection selection = worldEditPlugin.getSelection(player);
        if (selection == null)
        {
            sender.sendMessage(ChatColor.RED + "You must make a WorldEdit selection to do this.");
            return true;
        }
        if ((selection.getWidth() < MIN_EVENT_CLAIM_AREA) || (selection.getLength() < MIN_EVENT_CLAIM_AREA))
        {
            sender.sendMessage(ChatColor.RED + "Event claim areas must be at least " + MIN_EVENT_CLAIM_AREA + 'x' + MIN_EVENT_CLAIM_AREA + '.');
            return true;
        }
        ClaimableFaction claimableFaction = (ClaimableFaction)faction;
        if ((args.length == 3) && ((args[2].toLowerCase().contains("true")) || (args[2].toLowerCase().contains("yes"))) && (((ClaimableFaction)faction).getClaims().size() > 0))
        {
            sender.sendMessage(ChatColor.YELLOW + "Set claim for " + faction.getDisplayName(sender) + ChatColor.YELLOW + '.');
            if (claimableFaction.getClaims().size() > 0)
            {
                for (Claim claim : claimableFaction.getClaims()) {
                    claimableFaction.removeClaim(claim, sender);
                }
                claimableFaction.getClaims().clear();
            }
            claimableFaction.setClaim(new Cuboid(selection.getMinimumPoint(), selection.getMaximumPoint()), player);
            return true;
        }
        claimableFaction.addClaim(new Claim(claimableFaction, selection.getMinimumPoint(), selection.getMaximumPoint()), player);
        sender.sendMessage(ChatColor.YELLOW + "Added claim for " + faction.getDisplayName(sender) + ChatColor.YELLOW + '.');
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        switch (args.length)
        {
            case 2:
                List<String> results = new ArrayList(this.plugin.getFactionManager().getClaimableFactions().size());
                for (ClaimableFaction claimableFaction : this.plugin.getFactionManager().getClaimableFactions()) {
                    results.add(claimableFaction.getName());
                }
                return results;
        }
        return Collections.emptyList();
    }

    private static final int MIN_EVENT_CLAIM_AREA = 2;
    private final HCF plugin;
}