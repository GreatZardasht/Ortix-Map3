package com.customhcf.hcf.Utils;


import org.bukkit.ChatColor;

public class ChatUtils  {

    public static String NO_PERMISSIONS;
    public static String USAGE;
    public static String COOLDOWN;
    public static String REVIVED_PLAYER;
    public static String USER_NOT_DEATHBAN;
    public static String USER_NOT_FOUND;
    public static String BROADCAST;

    public static void reloadMessages() {
        NO_PERMISSIONS = "§cPermissions Denied!";
        USAGE = "§7/revive <playerName>";
        COOLDOWN = "§cYou already used this command! Please wait another %minutes% §cminutes.";
        REVIVED_PLAYER = "§cYou revived %target% from the faction %faction%";
        USER_NOT_DEATHBAN = "§c%player% §cis currently not deathban!";
        USER_NOT_FOUND = "§cThat user is not in our system files! Try again later...";
        BROADCAST = "§c%reviver% has brought %player% from the dead.";
    }

    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static String replace(String msg, String target, String replacement) {
        if (msg.contains(target))
            msg = msg.replace(target, replacement);
        return msg;
    }


}
