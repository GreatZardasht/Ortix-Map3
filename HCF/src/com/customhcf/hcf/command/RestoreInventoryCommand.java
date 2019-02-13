package com.customhcf.hcf.command;

import com.customhcf.hcf.listener.DeathListener;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by nyang on 2/14/2017.
 */
public class RestoreInventoryCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String s, String[] args)
    {
        String Usage = ChatColor.RED + "/" + s + " <playerName> <reason>";
        if (!(cs instanceof Player))
        {
            cs.sendMessage(ChatColor.RED + "You must be a player");
            return true;
        }
        Player p = (Player)cs;
        if (args.length < 2)
        {
            cs.sendMessage(Usage);
            return true;
        }
        if (Bukkit.getPlayer(args[0]) == null)
        {
            p.sendMessage(ChatColor.RED + "Player must be online");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (DeathListener.PlayerInventoryContents.containsKey(target.getUniqueId()))
        {
            target.getInventory().setContents((ItemStack[])DeathListener.PlayerInventoryContents.get(target.getUniqueId()));
            target.getInventory().setArmorContents((ItemStack[])DeathListener.PlayerArmorContents.get(target.getUniqueId()));
            String reason = StringUtils.join(args, ' ', 1, args.length);
            Command.broadcastCommandMessage(p, ChatColor.GREEN + target.getName() +"'s inventory has been restored successfully!");
            Command.broadcastCommandMessage(p, ChatColor.GREEN + "Reason: " + reason.toString());
            DeathListener.PlayerArmorContents.remove(target.getUniqueId());
            DeathListener.PlayerInventoryContents.remove(target.getUniqueId());
            return true;
        }
        p.sendMessage(ChatColor.RED + "Restore Information: Already refunded or Server Restarted!");
        return true;
    }
}
