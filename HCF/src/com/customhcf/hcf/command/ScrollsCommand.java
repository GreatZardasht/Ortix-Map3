package com.customhcf.hcf.command;

import com.customhcf.hcf.donor.ScrollsHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by nyang on 4/6/2017.
 */
public class ScrollsCommand implements CommandExecutor {

    ScrollsHandler scrollsHandler = new ScrollsHandler();

    public boolean onCommand(CommandSender sender, Command cmd, String label ,String[] args) {
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("scrolls")) {
            if (args.length == 0) {
                sender.sendMessage("§8§m--------------------------------------");
                sender.sendMessage("§6§lScrolls Help");
                sender.sendMessage("§8§m--------------------------------------");
                sender.sendMessage("§6/scrolls <player> <type> §7- §eGives a scroll to a player!");
                sender.sendMessage("§6/scrolls list §7- §eShows a list of Scrolls availiable!");
                return true;
            }
        }
        if (args[0].equalsIgnoreCase("list")) {
            player.sendMessage("§e§lAvailiable Scrolls: §fSpawn");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage("§cThat player is not online!");
            return true;
        }

        if (args[1].equalsIgnoreCase("spawn")) {
            scrollsHandler.giveSpawnScroll(target, "§aYou have received a Spawn Scroll!");
            return true;
        }

        return false;
    }
}
