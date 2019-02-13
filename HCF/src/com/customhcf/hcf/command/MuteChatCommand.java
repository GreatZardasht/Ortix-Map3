package com.customhcf.hcf.command;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.Utils.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuteChatCommand implements CommandExecutor {

    static boolean chatMuted = false;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (cmd.getName().equalsIgnoreCase("mutechat")) {
            if (isChatMuted())
            {
                sender.sendMessage(Lang.MUTECHAT_ALREADYMUTED.toString());
            }
            else
            {
                setChatMute(true);
                sender.sendMessage(Lang.MUTECHAT_MUTED.toString());
                Bukkit.getServer().broadcastMessage(Lang.MUTECHAT_MUTEDBROADCAST.toString().replaceAll("%player%", sender.getName()));
            }
        }
        if (cmd.getName().equalsIgnoreCase("unmutechat")) {
            if (!isChatMuted())
            {
                sender.sendMessage(Lang.UNMUTECHAT_NOTMUTED.toString());
            }
            else
            {
                setChatMute(false);
                sender.sendMessage(Lang.UNMUTECHAT_UNMUTE.toString());
                Bukkit.getServer().broadcastMessage(Lang.UNMUTECHAT_UNMUTEBROADCAST.toString().replaceAll("%player%", sender.getName()));
            }
        }
        return false;
    }

    public static boolean isChatMuted()
    {
        return chatMuted;
    }

    public static void setChatMute(boolean trueorfalse)
    {
        chatMuted = trueorfalse;
    }
}
