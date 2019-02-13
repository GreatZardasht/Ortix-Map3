package com.customhcf.hcf.command;

import com.customhcf.hcf.Utils.Cooldowns;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Nathan'PC on 12/19/2016.
 */
public class ReportCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p =(Player) sender;
        if (cmd.getName().equalsIgnoreCase("report")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "Proper Use: /report <name> <reason>");
                return true;
            }

            if (Cooldowns.isOnCooldown("report_cooldown", p)) {
                p.sendMessage("§cYou cannot do this for another §l" + Cooldowns.getCooldownForPlayerInt("report_cooldown", p) + " §cseconds.");
                return true;
            }

            Player target = Bukkit.getServer().getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Your target that you are trying to report is offline!");
                return true;
            }

            StringBuilder str = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                str.append(args[i] + " ");
            }

            Bukkit.getServer().broadcast("§7§m================================", "report.show");
            Bukkit.getServer().broadcast("§c§lIncoming Reports from [HCF]", "report.show");
            Bukkit.getServer().broadcast("§7§m================================", "report.show");
            Bukkit.getServer().broadcast("§c§lReporter: §7" + sender.getName(), "report.show");
            Bukkit.getServer().broadcast("§c§lPlayer Reported: §7" + target.getName(), "report.show");
            Bukkit.getServer().broadcast("§cReasoning to this report: " + str.toString(), "report.show");
            sender.sendMessage("§aThanks for reporting that player!");
            Cooldowns.addCooldown("report_cooldown", p, 30);
            return true;

        }
        return true;
    }
}
