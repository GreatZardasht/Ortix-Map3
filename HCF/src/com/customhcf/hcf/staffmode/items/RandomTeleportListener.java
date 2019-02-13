package com.customhcf.hcf.staffmode.items;

import com.customhcf.hcf.HCF;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by nyang on 3/25/2017.
 */
public class RandomTeleportListener implements Listener {

    @EventHandler
    public void onRandomTeleport(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK && (HCF.toggle.contains(player))) {
            if (item != null) {
                if (item.getType() == Material.WATCH) {
                    if (item.getItemMeta().getDisplayName().contains("Random Teleporter")) {
                        Random r = new Random();
                        ArrayList<Player> players = new ArrayList();
                        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                            if (online != player) {
                                players.add(online);
                            }
                        }
                        int index = r.nextInt(players.size());
                            Player loc = (Player) players.get(index);
                            player.teleport(loc);
                            player.sendMessage("§eTelporting you to §6" + loc.getName() + "§e...");
                    }
                }
            } else {
            }
        }
    }
}
