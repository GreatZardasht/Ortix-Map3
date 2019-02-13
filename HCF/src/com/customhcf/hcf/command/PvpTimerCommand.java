package com.customhcf.hcf.command;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.timer.type.PvpProtectionTimer;
import com.customhcf.util.BukkitUtils;
import com.google.common.collect.ImmutableList;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PvpTimerCommand
        implements CommandExecutor,
        TabCompleter {
    private static final ImmutableList<String> COMPLETIONS = ImmutableList.of("enable", "time");
    private final HCF plugin;
    public static ArrayList<String> autobroadcast = new ArrayList<>();
    public PvpTimerCommand(HCF plugin) {
        this.plugin = plugin;
    }
    public static Player player;

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage((Object)ChatColor.RED + "This command is only executable by players.");
            return true;
        }
        Player player = (Player)sender;
        PvpProtectionTimer pvpTimer = this.plugin.getTimerManager().pvpProtectionTimer;
        if (args.length < 1) {
            this.printUsage(sender, label, pvpTimer);
            return true;
        }
        if (args[0].equalsIgnoreCase("enable") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("off")) {
            if (pvpTimer.getRemaining(player) > 0) {
                sender.sendMessage((Object)ChatColor.YELLOW + "Your " + pvpTimer.getDisplayName() + (Object)ChatColor.YELLOW + " is now disabled.");
                pvpTimer.clearCooldown(player);
                return true;
            }
            if (pvpTimer.isPaused(player)) {
                player.sendMessage((Object)ChatColor.YELLOW + "You will no longer be legible for your " + pvpTimer.getDisplayName() + (Object)ChatColor.YELLOW + " when you leave spawn.");
                return true;
            }
            sender.sendMessage((Object)ChatColor.YELLOW + "Your " + pvpTimer.getDisplayName() + (Object)ChatColor.YELLOW + " is currently not active.");
            return true;
        }
        if (!(args[0].equalsIgnoreCase("remaining") || args[0].equalsIgnoreCase("time") || args[0].equalsIgnoreCase("left"))) {
            this.printUsage(sender, label, pvpTimer);
            return true;
        }
        long remaining = pvpTimer.getRemaining(player);
        if (remaining <= 0) {
            sender.sendMessage((Object)ChatColor.YELLOW + "Your " + pvpTimer.getDisplayName() + (Object)ChatColor.YELLOW + " is currently not active.");
            return true;
        }
        sender.sendMessage((Object)ChatColor.YELLOW + "Your " + pvpTimer.getDisplayName() + (Object)ChatColor.YELLOW + " is active for another " + (Object)ChatColor.BOLD + HCF.getRemaining(remaining, true, false) + (Object)ChatColor.YELLOW + (pvpTimer.isPaused(player) ? " and is currently paused" : "") + '.');
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? BukkitUtils.getCompletions((String[])args, COMPLETIONS) : Collections.emptyList();
    }

    private void printUsage(CommandSender sender, String label, PvpProtectionTimer pvpTimer) {
        sender.sendMessage("§8§m--------------------------------------");
        sender.sendMessage("§6§lPvPTimer Help ");
        sender.sendMessage("§8§m--------------------------------------");
        sender.sendMessage("§e/pvp enable §7- §fEnables PvPTimer.");
        sender.sendMessage("§e/pvp check §7- §fChecks your PvPTimer timeleft.");
        sender.sendMessage("§e/grant <player> protection§7- §fAbility to grant players PvPTimer.");
        sender.sendMessage("§8§m--------------------------------------");
        sender.sendMessage("");
    }

}
