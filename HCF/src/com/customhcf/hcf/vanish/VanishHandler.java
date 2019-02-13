package com.customhcf.hcf.vanish;

import com.customhcf.hcf.command.VanishCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class VanishHandler {

    public void setVanish(Player p) {
        VanishCommand.v.add(p);
       for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (!player.hasPermission("g.vanish")) {
                player.hidePlayer(p);
            }
        }
    }
    public void unVanish(Player p) {
        VanishCommand.v.remove(p);
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            player.showPlayer(p);
        }
    }
}
