/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.util.command.CommandArgument
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.World$Environment
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 */
package com.customhcf.hcf.faction.argument;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.faction.FactionExecutor;
import com.customhcf.hcf.faction.FactionManager;
import com.customhcf.hcf.faction.type.Faction;
import com.customhcf.hcf.faction.type.PlayerFaction;
import com.customhcf.hcf.kothgame.faction.EventFaction;
import com.customhcf.hcf.timer.PlayerTimer;
import com.customhcf.hcf.timer.Timer;
import com.customhcf.hcf.timer.TimerManager;
import com.customhcf.hcf.timer.type.EnderPearlTimer;
import com.customhcf.hcf.timer.type.PvpProtectionTimer;
import com.customhcf.hcf.timer.type.SpawnTagTimer;
import com.customhcf.hcf.timer.type.TeleportTimer;
import com.customhcf.util.command.CommandArgument;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class FactionHomeArgument
        extends CommandArgument {
    private final FactionExecutor factionExecutor;
    private final HCF plugin;

    public FactionHomeArgument(FactionExecutor factionExecutor, HCF plugin) {
        super("home", "Teleport to the faction home.");
        this.factionExecutor = factionExecutor;
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
            return true;
        }
        Player player = (Player) sender;
        if ((args.length >= 2) && (args[1].equalsIgnoreCase("set"))) {
            this.factionExecutor.getArgument("sethome").onCommand(sender, command, label, args);
            return true;
        }
        UUID uuid = player.getUniqueId();
        PlayerTimer timer = this.plugin.getTimerManager().enderPearlTimer;
        long remaining = timer.getRemaining(player);
        if (remaining > 0L) {
            sender.sendMessage(ChatColor.RED + "You cannot warp whilst your " + timer.getDisplayName() + ChatColor.RED + " timer is active [" + ChatColor.BOLD + HCF.getRemaining(remaining, true, false) + ChatColor.RED + " remaining]");
            return true;
        }
        if ((remaining = (timer = this.plugin.getTimerManager().spawnTagTimer).getRemaining(player)) > 0L) {
            sender.sendMessage(ChatColor.RED + "You cannot warp whilst your " + timer.getDisplayName() + ChatColor.RED + " timer is active [" + ChatColor.BOLD + HCF.getRemaining(remaining, true, false) + ChatColor.RED + " remaining]");
            return true;
        }
        PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(uuid);
        if (playerFaction == null) {
            sender.sendMessage(ChatColor.RED + "You are not in a faction.");
            return true;
        }
        Location home = playerFaction.getHome();
        if (home == null) {
            sender.sendMessage(ChatColor.RED + "Your faction does not have a home set.");
            return true;
        }
        Faction factionAt = this.plugin.getFactionManager().getFactionAt(player.getLocation());

        long millis = 0L;
        if (factionAt.isSafezone()) {
            millis = 0L;
        } else {
            switch (player.getWorld().getEnvironment()) {
                case THE_END:
                    sender.sendMessage(ChatColor.RED + "You cannot teleport to your faction home whilst in The End.");
                    return true;
                case NETHER:
                    millis = 10000L;
                    break;
                default:
                    millis = 10000L;
            }
        }
        if ((!factionAt.equals(playerFaction)) && ((factionAt instanceof PlayerFaction))) {
            millis *= 2L;
        }
        if (this.plugin.getTimerManager().pvpProtectionTimer.getRemaining(player.getUniqueId()) > 0L) {
            player.sendMessage(ChatColor.RED + "You still have PvP Protection, you must enable it first.");
            return true;
        }
        this.plugin.getTimerManager().teleportTimer.teleport(player, home, millis, ChatColor.GREEN + "Sending you to your faction home in " + ChatColor.LIGHT_PURPLE + HCF.getRemaining(millis, true, false) + ChatColor.GRAY + ". Do not move or take damage.", PlayerTeleportEvent.TeleportCause.COMMAND);
        return true;
    }
}
