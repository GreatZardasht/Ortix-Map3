package com.customhcf.hcf.command;

import com.customhcf.hcf.Utils.Cooldowns;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Nathan'PC on 12/23/2016.
 */
public class RequestCommand implements CommandExecutor
{

    public static String format(int i)
    {
        int ms = i / 60;
        int ss = i % 60;
        String m = (ms < 10 ? "0" : "") + ms;
        String s = (ss < 10 ? "0" : "") + ss;
        String f = m + ":" + s;
        return f;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("request")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.YELLOW + "[Request] TODO: /request <message>");
                return true;
            }

            if (Cooldowns.isOnCooldown("request_cooldown", player)) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYou are currently on cooldown from using (request/helpop) " + format(Cooldowns.getCooldownForPlayerInt("request_cooldown", player))));
                return true;
            }

            StringBuilder str = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                str.append(args[i] + " ");
            }

            Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', "&e[Request] &7"+ player.getDisplayName() + ": " + str.toString()), "request.receive");
            Cooldowns.addCooldown("request_cooldown", player, 120);
            return true;

        }
        return false;
    }
}
