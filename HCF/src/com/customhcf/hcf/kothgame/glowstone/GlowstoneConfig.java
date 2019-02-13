package com.customhcf.hcf.kothgame.glowstone;


import com.customhcf.util.Config;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class GlowstoneConfig {

    public static void init(JavaPlugin plugin) {
        Config config = new Config(plugin, "glowstone");
        plugin.getLogger().info("Loading in glowstone.yml (Glowstone Mountain)");

        radius_glowstone_cuboid = config.getInt("radius_glowstone_cuboid", radius_glowstone_cuboid);
        coord_x = config.getInt("coord_x", coord_x);
        coord_y = config.getInt("coord_y", coord_y);
        coord_z = config.getInt("coord_z", coord_z);
        world_nether = config.getBoolean("world_nether", world_nether);
        world = config.getBoolean("world", world);
        material = config.getString("material", material);
        GSMoutain_Reset = config.getString("GSMountain_Reset", GSMoutain_Reset);
        GSMoutain_Reset = ChatColor.translateAlternateColorCodes('&', GSMoutain_Reset);

    }

    public static int radius_glowstone_cuboid;
    public static int coord_x;
    public static int coord_y;
    public static int coord_z;
    public static boolean world_nether;
    public static boolean world;
    public static String material;
    public static String GSMoutain_Reset;


    static {

    }
}
