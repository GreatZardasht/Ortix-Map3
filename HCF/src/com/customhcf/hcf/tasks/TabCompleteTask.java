package com.customhcf.hcf.tasks;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.customhcf.hcf.HCF;
import com.customhcf.util.chat.ClickAction;
import com.customhcf.util.chat.Text;
import org.apache.logging.log4j.core.net.Protocol;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by nyang on 3/25/2017.
 */
public class TabCompleteTask {

    public static void startTabCompletes() {

        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(HCF.getPlugin(), new PacketType[] { PacketType.Play.Client.TAB_COMPLETE })
        {
            public void onPacketReceiving(PacketEvent event)
            {
                if (event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE)
                {
                    Player p = event.getPlayer();
                    PacketContainer packetContainer = event.getPacket();
                    String tab = (String)packetContainer.getStrings().read(0);
                    if (tab.toLowerCase().startsWith(".") || tab.toLowerCase().startsWith("-")) {
                        for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
                            if (staff.hasPermission("command.cheats.notify")) {
                                staff.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + p.getName() + " &7has tabcompleted! &7[&8" + tab.toLowerCase() + "&7]"));
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        });
    }

    public static void stopShowPlugins() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(HCF.getPlugin(), new PacketType[] { PacketType.Play.Client.TAB_COMPLETE}) {
            public void onPacketReceiving(PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE) {
                    Player player = event.getPlayer();
                    PacketContainer container = event.getPacket();
                    String tab = container.getStrings().read(0);
                    if (tab.toLowerCase().contains("/bukkit:ver") || tab.toLowerCase().contains("/about") || tab.toLowerCase().contains("/bukkit:about") || tab.toLowerCase().contains("/ver")) {
                        event.setCancelled(true);
                    }
                }
            }
        });
    }
}
