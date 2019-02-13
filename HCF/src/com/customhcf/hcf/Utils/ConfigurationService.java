/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  org.apache.commons.lang3.time.DurationFormatUtils
 *  org.bukkit.ChatColor
 *  org.bukkit.World
 *  org.bukkit.World$Environment
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.potion.PotionType
 */
package com.customhcf.hcf.Utils;

import com.customhcf.hcf.HCF;
import com.google.common.collect.ImmutableList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionType;

@Deprecated
public final class ConfigurationService
{
    public static final long LOGFILESYNC = 400L;
    public static final TimeZone SERVER_TIME_ZONE;
    public static final int WARZONE_RADIUS = 600;
    public static final int NWARZONE = 200;
    public static final String TEAMSPEAK_URL = "";
    public static final int SPAWN_BUFFER = 150;
    public static final double MAP_NUMBER = 1.0D;
    public static String KIT_MAP_NAME = null;
    public static final boolean KIT_MAP = false;
    public static final List<String> DISALLOWED_FACTION_NAMES;
    public static final Map<Enchantment, Integer> ENCHANTMENT_LIMITS;
    public static final Map<PotionType, Integer> POTION_LIMITS;
    public static final Map<World.Environment, Integer> BORDER_SIZES;
    public static final Map<World.Environment, Double> SPAWN_RADIUS_MAP;
    public static final int FACTION_PLAYER_LIMIT = 15;
    public static final ChatColor BASECOLOUR = ChatColor.GRAY;

    static
    {
        SERVER_TIME_ZONE = TimeZone.getTimeZone("EST");
        DISALLOWED_FACTION_NAMES = ImmutableList.of("kohieotw", "kohisotw", "hcteams", "hcteamseotw", "hcteamssotw", "exploitesquad", "staff", "mod", "owner", "dev", "admin", "ipvp",  "para");
        ENCHANTMENT_LIMITS = new HashMap<>();
        POTION_LIMITS = new EnumMap<>(PotionType.class);
        BORDER_SIZES = new EnumMap<>(World.Environment.class);
        SPAWN_RADIUS_MAP = new EnumMap<>(World.Environment.class);
        POTION_LIMITS.put(PotionType.INSTANT_DAMAGE, Integer.valueOf(0));
        POTION_LIMITS.put(PotionType.REGEN, Integer.valueOf(0));
        POTION_LIMITS.put(PotionType.STRENGTH, Integer.valueOf(0));
        POTION_LIMITS.put(PotionType.WEAKNESS, Integer.valueOf(0));
        POTION_LIMITS.put(PotionType.SLOWNESS, Integer.valueOf(1));
        POTION_LIMITS.put(PotionType.INVISIBILITY, Integer.valueOf(0));
        POTION_LIMITS.put(PotionType.POISON, Integer.valueOf(1));
        ENCHANTMENT_LIMITS.put(Enchantment.PROTECTION_ENVIRONMENTAL, Integer.valueOf(1));
        ENCHANTMENT_LIMITS.put(Enchantment.DAMAGE_ALL, Integer.valueOf(1));
        ENCHANTMENT_LIMITS.put(Enchantment.ARROW_KNOCKBACK, Integer.valueOf(0));
        ENCHANTMENT_LIMITS.put(Enchantment.KNOCKBACK, Integer.valueOf(0));
        ENCHANTMENT_LIMITS.put(Enchantment.FIRE_ASPECT, Integer.valueOf(0));
        ENCHANTMENT_LIMITS.put(Enchantment.THORNS, Integer.valueOf(0));
        ENCHANTMENT_LIMITS.put(Enchantment.ARROW_FIRE, Integer.valueOf(1));
        ENCHANTMENT_LIMITS.put(Enchantment.ARROW_DAMAGE, Integer.valueOf(2));
        BORDER_SIZES.put(World.Environment.NORMAL, Integer.valueOf(3000));
        BORDER_SIZES.put(World.Environment.NETHER, Integer.valueOf(1000));
        BORDER_SIZES.put(World.Environment.THE_END, Integer.valueOf(500));
        SPAWN_RADIUS_MAP.put(World.Environment.NORMAL, Double.valueOf(63D));
        SPAWN_RADIUS_MAP.put(World.Environment.NETHER, Double.valueOf(22.5D));
        SPAWN_RADIUS_MAP.put(World.Environment.THE_END, Double.valueOf(48.5D));
        DEFAULT_DEATHBAN_DURATION = TimeUnit.MINUTES.toMillis(90L);
    }

    public static final ChatColor TEAMMATE_COLOUR = ChatColor.GREEN;
    public static final ChatColor ALLY_COLOUR = ChatColor.LIGHT_PURPLE;
    public static final ChatColor ENEMY_COLOUR = ChatColor.RED;
    public static final ChatColor SAFEZONE_COLOUR = ChatColor.GOLD;
    public static final ChatColor ROAD_COLOUR = ChatColor.YELLOW;
    public static final ChatColor WARZONE_COLOUR = ChatColor.DARK_RED;
    public static final ChatColor WILDERNESS_COLOUR = ChatColor.DARK_GREEN;
    public static final String SCOREBOARD_TITLE = ChatColor.AQUA.toString() + ChatColor.BOLD +  "  BubbleHCF " + ChatColor.GRAY + " [Map 1]  ";
    public static final int MAX_ALLIES_PER_FACTION = 1;
    public static final long DTR_MILLIS_BETWEEN_UPDATES = TimeUnit.SECONDS.toMillis(45L);
    public static final String DTR_WORDS_BETWEEN_UPDATES = DurationFormatUtils.formatDurationWords(DTR_MILLIS_BETWEEN_UPDATES, true, true);
    public static final int CONQUEST_REQUIRED_WIN_POINTS = 300;
    public static long DEFAULT_DEATHBAN_DURATION;
    public static boolean CRATE_BROADCASTS = false;
}