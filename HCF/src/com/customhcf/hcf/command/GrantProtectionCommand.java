package com.customhcf.hcf.command;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.Utils.Lang;
import com.customhcf.hcf.faction.type.Faction;
import com.customhcf.hcf.timer.type.PvpProtectionTimer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class GrantProtectionCommand implements CommandExecutor {


    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        if (!(sender instanceof Player)) {
            player.sendMessage("Players only!");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("§8§m--------------------------------------");
            sender.sendMessage("§6§lPvPTimer Help ");
            sender.sendMessage("§8§m--------------------------------------");
            sender.sendMessage("§e/pvp enable §7- §fEnables PvPTimer.");
            sender.sendMessage("§e/pvp check §7- §fChecks your PvPTimer timeleft.");
            sender.sendMessage("§e/grant <player> protection§7- §fAbility to grant players PvPTimer.");
            sender.sendMessage("§8§m--------------------------------------");
            sender.sendMessage("");
            return true;
        }

       Player target = Bukkit.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage("NOT ONLINE");
            return true;
        }

        if (args.length == 1) {
            sender.sendMessage("§8§m--------------------------------------");
            sender.sendMessage("§6§lPvPTimer Help ");
            sender.sendMessage("§8§m--------------------------------------");
            sender.sendMessage("§e/pvp enable §7- §fEnables PvPTimer.");
            sender.sendMessage("§e/pvp check §7- §fChecks your PvPTimer timeleft.");
            sender.sendMessage("§e/grant <player> protection§7- §fAbility to grant players PvPTimer.");
            sender.sendMessage("§8§m--------------------------------------");
            sender.sendMessage("");
            return true;
        }

        if (args[1].equalsIgnoreCase("protection")) {
            PvpProtectionTimer protectionTimer = HCF.getPlugin().getTimerManager().pvpProtectionTimer;
            long remaining = HCF.getPlugin().getTimerManager().pvpProtectionTimer.getRemaining(player);
            long targetRemaining = HCF.getPlugin().getTimerManager().pvpProtectionTimer.getRemaining(target);
            Faction factionAt = HCF.getPlugin().getFactionManager().getFactionAt(target.getLocation());
            if (factionAt.isSafezone()) {
                if (targetRemaining <= 0L) {
                    protectionTimer.setCooldown(player, player.getUniqueId());
                    protectionTimer.setPaused(player, player.getUniqueId(), true);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.GRANTPVPTIMER_GRANTER.toString().replaceAll("%player%", target.getName())));
                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.GRANTPVPTIMER_GETTER.toString()));
                    return true;
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThat player still has Immunity timer [" + HCF.getRemaining(targetRemaining, true, false) + "s]"));
                    return true;
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.GRANTPVPTIMER_NOTSPAWN.toString()));
                return true;
            }
        }



        return true;
    }

}
