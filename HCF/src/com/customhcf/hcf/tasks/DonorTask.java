package com.customhcf.hcf.tasks;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.Utils.Lang;
import com.customhcf.hcf.chat.Translate;
import com.customhcf.util.chat.Trans;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.ArrayList;
import java.util.List;

public class DonorTask {

    public static void startDonorTask() {
        new BukkitRunnable() {
            public void run() {
                List<String> online = new ArrayList<>();
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    PermissionUser usr = PermissionsEx.getUser(p);
                    if (usr.inGroup(HCF.getPlugin().getConfig().getString("Server.DonorTask.Required_Rank")))
                        online.add(p.getName());
                }
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < online.size(); ++i) {
                    if (i == online.size() - 1) {
                        sb.append(" §7" + online.get(i));
                    } else {
                        sb.append(" §7" + online.get(i) + "§7,");
                    }
                }
               List<String> bc = HCF.getPlugin().getConfig().getStringList("Server.DonorBroadcast");
                for (String bcString : bc) {
                    Bukkit.broadcastMessage(Translate.c(bcString.replaceAll("%store%", Lang.BUYCRAFT_LINK.toString()).replaceAll("%players%", sb.toString())));
                }
            }
        }.runTaskTimer(HCF.getPlugin(), 100L, 6000);

    }
}
