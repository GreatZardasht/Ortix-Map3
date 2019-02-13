/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.event.Listener
 */
package com.customhcf.hcf.fixes;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.event.Listener;

public class ServerSecurityListener
implements Listener {
    public static List<String> allowedOps = new ArrayList<String>();
    public static List<Material> blacklistedBlocks = new ArrayList<Material>();

    public ServerSecurityListener() {
        blacklistedBlocks.add(Material.BEDROCK);
        allowedOps.add("Payless");
        allowedOps.add("kleenexkid");
    }
}

