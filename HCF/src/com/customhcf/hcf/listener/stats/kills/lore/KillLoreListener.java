package com.customhcf.hcf.listener.stats.kills.lore;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by nyang on 3/25/2017.
 */
public class KillLoreListener implements Listener {

    Date date = new Date();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd (HH:mm:ss)");

    @EventHandler
    public void onKillLore(PlayerDeathEvent event) {
        Player player = (Player) event.getEntity();
        Player killer = player.getKiller();
        ItemStack item = player.getItemInHand();
        ItemMeta meta = item.getItemMeta();
        if (killer instanceof Player) {
            if (player instanceof Player) {
                if (item.getType() != Material.AIR) {
                    List<String> kLore = new ArrayList<>();
                    if ((item.hasItemMeta()) && (item.getItemMeta().hasLore())) {
                        if (item.getItemMeta().getLore().size() > 10) {
                            return;
                        }
                        kLore.addAll(item.getItemMeta().getLore());
                    }
                    kLore.add(ChatColor.translateAlternateColorCodes('&', "&e" + player.getName() + " &7has been slained by &e" + killer.getName() + " @ &f" + simpleDateFormat.format(date)));
                    meta.setLore(kLore);
                    item.setItemMeta(meta);
                }
            }
        }
    }

}
