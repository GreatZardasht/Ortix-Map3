package com.customhcf.hcf.command;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.donor.listeners.ScrollsListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EzSpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("spawn")){
            if (args.length == 0) {
                sendLocation(player);
                return true;
            }
            Player target  = Bukkit.getPlayer(args[0]);

            if (target == null) {
                player.sendMessage(ChatColor.YELLOW + "Player isnt online");
                return true;
            }
            player.sendMessage(ChatColor.GREEN + target.getName() + " has been teleported to spawn!");
            sendLocation(target);
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("setspawn")) {
            if (cmd.getName().equalsIgnoreCase("setspawn")) {
                HCF.getPlugin().getConfig().set("spawn.world", player.getLocation().getWorld().getName());
                HCF.getPlugin().getConfig().set("spawn.x", player.getLocation().getX());
                HCF.getPlugin().getConfig().set("spawn.y", player.getLocation().getY());
                HCF.getPlugin().getConfig().set("spawn.z", player.getLocation().getZ());
                HCF.getPlugin().getConfig().set("spawn.yaw", player.getLocation().getYaw());
                HCF.getPlugin().getConfig().set("spawn.pitch", player.getLocation().getPitch());
                HCF.getPlugin().saveConfig();
                player.sendMessage(ChatColor.GREEN + "Spawn set!");
                return true;
            }
        }
        return false;
    }

    public boolean sendLocation(Player player) {
        World w = Bukkit.getServer().getWorld(HCF.getPlugin().getConfig().getString("spawn.world"));
        double x = HCF.getPlugin().getConfig().getDouble("spawn.x");
        double y = HCF.getPlugin().getConfig().getDouble("spawn.y");
        double z = HCF.getPlugin().getConfig().getDouble("spawn.z");
        float yaw = (float) HCF.getPlugin().getConfig().getDouble("spawn.yaw");
        float pitch = (float) HCF.getPlugin().getConfig().getDouble("spawn.pitch");
        player.teleport(new Location(w, x, y, z, yaw, pitch));
        return false;
    }
}
