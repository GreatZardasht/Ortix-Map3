/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.util.BukkitUtils
 *  com.customhcf.util.chat.ClickAction
 *  com.customhcf.util.chat.Text
 *  com.customhcf.util.command.CommandArgument
 *  com.google.common.collect.ArrayListMultimap
 *  com.google.common.collect.ImmutableCollection
 *  com.google.common.collect.ImmutableMultimap
 *  com.google.common.collect.Multimap
 *  com.google.common.primitives.Ints
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.customhcf.hcf.faction.argument;

import com.customhcf.hcf.faction.FactionExecutor;
import com.customhcf.util.BukkitUtils;
import com.customhcf.util.chat.ClickAction;
import com.customhcf.util.chat.Text;
import com.customhcf.util.command.CommandArgument;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.primitives.Ints;
import java.util.Collection;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionHelpArgument
extends CommandArgument {
    private static final int HELP_PER_PAGE = 10;
    private final FactionExecutor executor;
    private ImmutableMultimap<Integer, Text> pages;

    public FactionHelpArgument(FactionExecutor executor) {
        super("help", "View help on how to use factions.");
        this.executor = executor;
    }

    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            this.showPage(sender, label, 1);
            return true;
        }
        Integer page = Ints.tryParse((String)args[1]);
        if (page == null) {
            sender.sendMessage((Object)ChatColor.RED + "'" + args[1] + "' is not a valid number.");
            return true;
        }
        this.showPage(sender, label, page);
        return true;
    }

    private void showPage(CommandSender sender, String label, int pageNumber) {
        if (this.pages == null) {
            boolean isPlayer = sender instanceof Player;
            int val = 1;
            int count = 0;
            ArrayListMultimap pages = ArrayListMultimap.create();
            for (CommandArgument argument : this.executor.getArguments()) {
                String permission;
                if (argument.equals((Object)this) || (permission = argument.getPermission()) != null && !sender.hasPermission(permission) || argument.isPlayerOnly() && !isPlayer) continue;
                pages.get((Object)val).add(new Text((Object)ChatColor.GOLD + "   /" + label + ' ' + argument.getName() + (Object)ChatColor.YELLOW + " - " + (Object)ChatColor.YELLOW + argument.getDescription()).setColor(ChatColor.GRAY).setClick(ClickAction.SUGGEST_COMMAND, "/" + label + " " + argument.getName()));
                if (++count % 10 != 0) continue;
                ++val;
            }
            this.pages = ImmutableMultimap.copyOf((Multimap)pages);
        }
        int totalPageCount = this.pages.size() / 10 + 1;
        if (pageNumber < 1) {
            sender.sendMessage((Object)ChatColor.RED + "You cannot view a page less than 1.");
            return;
        }
        if (pageNumber > totalPageCount) {
            sender.sendMessage((Object)ChatColor.RED + "There are only " + totalPageCount + " pages.");
            return;
        }
        sender.sendMessage((Object)ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        sender.sendMessage((Object)ChatColor.GOLD + ChatColor.BOLD.toString() + " Faction Help " + (Object)ChatColor.YELLOW + "[" + pageNumber + '/' + totalPageCount + ']');
        sender.sendMessage((Object)ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        for (Text message : this.pages.get(pageNumber)) {
            message.send(sender);
        }
        sender.sendMessage((Object)ChatColor.YELLOW + " Use " + (Object)ChatColor.GOLD + '/' + label + ' ' + this.getName() + " <#>" + (Object)ChatColor.YELLOW + " to view other pages.");
        if (pageNumber == 1) {
            sender.sendMessage((Object)ChatColor.GRAY + "Click a command to '" + (Object)ChatColor.ITALIC + "instantly" + (Object)ChatColor.GRAY + "' preform it.");
        }
        sender.sendMessage((Object)ChatColor.DARK_GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
    }
}

