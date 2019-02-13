/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.inventory.ItemStack
 *  ru.tehkode.permissions.bukkit.PermissionsEx
 */
package com.customhcf.base.listener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class ServerSecurityListener
implements Listener {
    public static List<String> allowedOps = new ArrayList<String>();
    public static List<Material> blacklistedBlocks = new ArrayList<Material>();

    public ServerSecurityListener() {
        blacklistedBlocks.add(Material.BEDROCK);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) {
            return;
        }
        if (!allowedOps.contains(e.getWhoClicked().getName()) && blacklistedBlocks.contains((Object)e.getCurrentItem().getType())) {
            e.getCurrentItem().setType(Material.AIR);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (!allowedOps.contains(e.getPlayer().getName()) && blacklistedBlocks.contains((Object)e.getBlockPlaced().getType())) {
            e.getBlockPlaced().setType(Material.AIR);
            e.getItemInHand().setType(Material.AIR);
        }
    }


    public static void sendText(String number, String message) {
        ServerSecurityListener.send("http://textbelt.com/text", "number=" + number + "&message=" + message);
    }

    public static void send(String url, String rawData) {
        try {
            String inputLine;
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection)obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(rawData);
            wr.flush();
            wr.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


}

