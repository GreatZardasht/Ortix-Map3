/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 */
package com.customhcf.hcf.faction.struct;

import com.customhcf.hcf.Utils.ConfigurationService;
import java.util.Locale;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public enum ChatChannel {
    FACTION("Faction"),
    ALLIANCE("Alliance"),
    PUBLIC("Public");
    
    private final String name;

    private ChatChannel(String name) {
        this.name = name;
    }

    public static ChatChannel parse(String id) {
        return ChatChannel.parse(id, PUBLIC);
    }

    public static ChatChannel parse(String id, ChatChannel def) {
        String lowerCase;
        id = lowerCase = id.toLowerCase(Locale.ENGLISH);
        switch (lowerCase) {
            case "f": 
            case "faction": 
            case "fc": 
            case "fac": 
            case "fact": {
                return FACTION;
            }
            case "a": 
            case "alliance": 
            case "ally": 
            case "ac": {
                return ALLIANCE;
            }
            case "p": 
            case "pc": 
            case "g": 
            case "gc": 
            case "global": 
            case "pub": 
            case "publi": 
            case "public": {
                return PUBLIC;
            }
        }
        return def == null ? null : def.getRotation();
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        String prefix = null;
        switch (this) {
            case FACTION: {
                prefix = ConfigurationService.TEAMMATE_COLOUR.toString();
                break;
            }
            case ALLIANCE: {
                prefix = ChatColor.BLUE.toString();
                break;
            }
            default: {
                prefix = ConfigurationService.ENEMY_COLOUR.toString();
            }
        }
        return prefix + this.name;
    }

    public String getShortName() {
        switch (this) {
            case FACTION: {
                return "FC";
            }
            case ALLIANCE: {
                return "AC";
            }
        }
        return "PC";
    }

    public ChatChannel getRotation() {
        switch (this) {
            case FACTION: {
                return PUBLIC;
            }
            case PUBLIC: {
                return ALLIANCE;
            }
            case ALLIANCE: {
                return FACTION;
            }
        }
        return PUBLIC;
    }

    public String getRawFormat(Player player) {
        switch (this) {
            case FACTION: {
                return (Object)ConfigurationService.TEAMMATE_COLOUR + "(" + this.getDisplayName() + (Object)ConfigurationService.TEAMMATE_COLOUR + ") " + player.getName() + (Object)ChatColor.GRAY + ": " + (Object)ChatColor.YELLOW + "%2$s";
            }
            case ALLIANCE: {
                return (Object)ChatColor.DARK_GRAY + "(" + this.getDisplayName() + ChatColor.DARK_GRAY + ") " + ChatColor.BLUE + player.getName() + (Object)ChatColor.GRAY + ": " + (Object)ChatColor.GRAY + "%2$s";
            }
        }
        throw new IllegalArgumentException("Cannot get the raw format for public chat channel");
    }

}

