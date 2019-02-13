package com.customhcf.hcf.Utils;

import com.customhcf.hcf.Utils.ChatUtils;
import com.customhcf.hcf.Utils.Cooldowns;
import com.customhcf.hcf.HCF;
import com.customhcf.hcf.Utils.PUtils;
import com.customhcf.hcf.deathban.Deathban;
import com.customhcf.hcf.user.FactionUser;
import com.customhcf.util.BukkitUtils;
import com.customhcf.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ReviveSetup {
    private HCF plugin;

    public ReviveSetup(HCF plugin) {
        this.plugin = plugin;
    }

    Config config = new Config(plugin, "revive");


    public static void onCommand(CommandSender sender, Command cmd , String[] args) {
        Player p = (Player) sender;
        if (args.length != 0) {
            sender.sendMessage(ChatUtils.color(ChatUtils.USAGE)); return;
        }

        FactionUser user = HCF.getPlugin().getUserManager().getUser(p.getUniqueId());


        UUID uuid = null;
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if ((!target.hasPlayedBefore()) && (!target.isOnline())) {
            sender.sendMessage(ChatUtils.color(ChatUtils.USER_NOT_FOUND));
        }
        UUID targetUUID = target.getUniqueId();
        FactionUser factionTarget = HCF.getPlugin().getUserManager().getUser(targetUUID);
        Deathban deathban = factionTarget.getDeathban();
        if ((deathban == null) || (!deathban.isActive())) {
            sender.sendMessage(ChatUtils.color(ChatUtils.USER_NOT_DEATHBAN)); return;
        }
        if (PUtils.hasCooldown(p)) {
            sender.sendMessage(ChatUtils.color(ChatUtils.replace(ChatUtils.COOLDOWN, "%minutes%", String.valueOf(PUtils.getCooldown(p))))); return;
        }
        PUtils.revivePlayer(uuid);
        sender.sendMessage(ChatUtils.color(ChatUtils.replace(ChatUtils.replace(ChatUtils.REVIVED_PLAYER, "%target%", target.getName()), "%faction%", HCF.getPlugin().getFactionManager().getPlayerFaction(p.getUniqueId()).getDisplayName((CommandSender)p))));
        Bukkit.getServer().broadcastMessage(ChatUtils.color(ChatUtils.replace(ChatUtils.replace(ChatUtils.BROADCAST, "%reviver%", p.getName()), "%player%", target.getName())));
        PUtils.addCooldown(p);

    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length != 1) {
            return Collections.emptyList();
        }
        List<String> results = new ArrayList();
        for (FactionUser factionUser : this.plugin.getUserManager().getUsers().values())
        {
            Deathban deathban = factionUser.getDeathban();
            if ((deathban != null) && (deathban.isActive()))
            {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(factionUser.getUserUUID());
                String name = offlinePlayer.getName();
                if (name != null) {
                    results.add(name);
                }
            }
        }
        return BukkitUtils.getCompletions(args, results);
    }
}


