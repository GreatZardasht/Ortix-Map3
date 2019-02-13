package com.customhcf.hcf.command;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.faction.type.Faction;
import com.customhcf.hcf.user.FactionUser;
import com.customhcf.util.chat.ClickAction;
import com.customhcf.util.chat.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Nathan'PC on 12/19/2016.
 */
public class InfoCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("stats")) {
            if (args.length == 0) {
                int lives = HCF.getPlugin().getDeathbanManager().getLives(player.getUniqueId());
                sender.sendMessage("§7§m--------------------------");
                sender.sendMessage("§3§lPlayer Information");
                sender.sendMessage("§7§m--------------------------");
                sender.sendMessage("§bPlayer Name: §7" + player.getName());
                sender.sendMessage("§bRank: §7" + HCF.perms.getPrimaryGroup(player).toUpperCase());
                sender.sendMessage("§bKills: §7" + player.getStatistic(Statistic.PLAYER_KILLS));
                sender.sendMessage("§bDeaths: §7" + player.getStatistic(Statistic.DEATHS));
                sender.sendMessage("§bLives: §7" + lives);
                return true;
            }

            Player target = Bukkit.getServer().getPlayer(args[0]);

            if (target == null) {
                sender.sendMessage("§cThat player is currently no online!");
                return true;
            }
            int targetLives = HCF.getPlugin().getDeathbanManager().getLives(target.getUniqueId());
            FactionUser hcf = HCF.getPlugin().getUserManager().getUser(target.getUniqueId());
            sender.sendMessage("§7§m--------------------------");
            sender.sendMessage("§3§lPlayer Information");
            sender.sendMessage("§7§m--------------------------");
            sender.sendMessage("§bPlayer Name: §7" + target.getName());
            if (hcf.getDeathban() != null) {
                new Text(ChatColor.AQUA + "Deathbanned: §7" + (hcf.getDeathban().isActive() ? new StringBuilder().append((Object)ChatColor.GREEN).append("true").toString() : new StringBuilder().append((Object)ChatColor.RED).append("false").toString())).setHoverText((Object)ChatColor.AQUA + "Un-Deathbanned at: " + HCF.getRemaining(hcf.getDeathban().getExpiryMillis(), true, true)).send((CommandSender)player);
            } else {
                player.sendMessage((Object)ChatColor.AQUA + "Deathbanned: §7" + (Object)ChatColor.RED + "false");
            }
            sender.sendMessage("§bRank: §7" + HCF.perms.getPrimaryGroup(target).toUpperCase());
            sender.sendMessage("§bKills: §7" + target.getStatistic(Statistic.PLAYER_KILLS));
            sender.sendMessage("§bDeaths: §7" + target.getStatistic(Statistic.DEATHS));
            sender.sendMessage("§bLives: §7" + targetLives);
            return true;

        }
        return false;
    }
}
