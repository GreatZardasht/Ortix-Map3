/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.time.DurationFormatUtils
 *  org.bukkit.ChatColor
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.customhcf.base.command.module.essential;

import com.customhcf.base.BaseConstants;
import com.customhcf.base.BasePlugin;
import com.customhcf.base.PlayTimeManager;
import com.customhcf.base.command.BaseCommand;
import com.customhcf.util.BukkitUtils;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayTimeCommand extends BaseCommand
{
    private final BasePlugin plugin;

    public PlayTimeCommand(final BasePlugin plugin) {
        super("playtime", "Check the playtime of another player.");
        this.setAliases(new String[] { "pt" });
        this.setUsage("/(command) [playerName]");
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        OfflinePlayer target;
        if (args.length >= 1) {
            target = BukkitUtils.offlinePlayerWithNameOrUUID(args[0]);
        }
        else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(this.getUsage(label));
                return true;
            }
            target = (OfflinePlayer)sender;
        }
        if (!target.hasPlayedBefore() && !target.isOnline()) {
            sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
            return true;
        }
        sender.sendMessage(ChatColor.BLUE + target.getName() + " has been playing for " + ChatColor.GREEN + DurationFormatUtils.formatDurationWords(this.plugin.getPlayTimeManager().getTotalPlayTime(target.getUniqueId()), true, true) + ChatColor.BLUE + " this map.");
        return true;
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        return (args.length == 1) ? null : Collections.emptyList();
    }
}
