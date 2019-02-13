package com.customhcf.hcf.chat;

import org.bukkit.ChatColor;

/**
 * Created by Nathan'PC on 1/20/2017.
 */
public class Translate {

    public static String prefix = c("&9&lPunish &c&l» ");

    public static String c (String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
