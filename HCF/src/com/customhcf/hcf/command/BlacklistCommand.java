package com.customhcf.hcf.command;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.Utils.Lang;
import com.customhcf.hcf.user.FactionUser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class BlacklistCommand implements CommandExecutor, Listener {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("blacklist")) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aPlease specify a player and a reason to be blacklisted! (/blacklist <player> <reason>)"));
                return true;
            }

            StringBuilder str = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                str.append(args[i] + " ");
            }

            OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(args[0]);
            FactionUser factionUser = HCF.getPlugin().getUserManager().getUser(target.getUniqueId());
            if (!factionUser.isBlacklisted()) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ipban -s " + target.getName() + " " + str.toString());
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Lang.BLACKLISTED.toString().replaceAll("%player%", target.getName()).replaceAll("%reason%", str.toString())));
                factionUser.setBlacklisted(true);
                return true;
            } else {
                sender.sendMessage(Lang.BLACKLIST_ALREADY.toString());
                return true;
            }
        }
        if (cmd.getName().equalsIgnoreCase("unblacklist")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "/unblacklist <player>");

                return true;
            }
            OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(args[0]);

            FactionUser factionUser = HCF.getPlugin().getUserManager().getUser(target.getUniqueId());
            if (factionUser.isBlacklisted()) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "unban -s " + target.getName());
                factionUser.setBlacklisted(false);
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "This player is not blacklisted!");
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onBlacklistJoin(PlayerJoinEvent event) {
        Player player = (Player) event.getPlayer();
        FactionUser factionUser = HCF.getPlugin().getUserManager().getUser(player.getUniqueId());
        if (factionUser.isBlacklisted()) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ipban -s " + player.getName() + " Blacklisted!");
            Bukkit.broadcastMessage(Lang.BLACKLIST_AUTOBAN.toString().replaceAll("%player%", player.getName()));
        } else {
            return;
        }
    }
}