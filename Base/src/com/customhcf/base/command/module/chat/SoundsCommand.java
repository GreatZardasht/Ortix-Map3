package com.customhcf.base.command.module.chat;

import com.customhcf.base.BasePlugin;
import com.customhcf.base.command.BaseCommand;
import com.customhcf.base.user.BaseUser;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by nyang on 4/22/2017.
 */
public class SoundsCommand extends BaseCommand {

    public static BasePlugin plugin;

    public  SoundsCommand(BasePlugin plugin) {
        super("sounds", "Toggles private messages.");
        this.setUsage("/(command)");
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage((Object) ChatColor.RED + "This command is only executable for players.");
            return true;
        }
        Player player = (Player)sender;
        BaseUser baseUser = this.plugin.getUserManager().getUser(player.getUniqueId());
        boolean newToggled = !baseUser.isMessagingSounds();
        baseUser.setMessagingSounds(newToggled);
        sender.sendMessage((Object)ChatColor.YELLOW + "You have turned sounds " + (newToggled ? new StringBuilder().append((Object)ChatColor.GREEN).append("on").toString() : new StringBuilder().append((Object)ChatColor.RED).append("off").toString()) + (Object)ChatColor.YELLOW + '.');
        return true;
    }
}
