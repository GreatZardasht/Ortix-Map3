/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.util.command.CommandArgument
 *  org.apache.commons.lang3.StringUtils
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 */
package com.customhcf.hcfold.crate.argument;

import com.customhcf.hcf.HCF;
import com.customhcf.hcfold.crate.Key;
import com.customhcf.hcfold.crate.KeyManager;
import com.customhcf.util.command.CommandArgument;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class LootListArgument
extends CommandArgument {
    private final HCF plugin;

    public LootListArgument(HCF plugin) {
        super("list", "List all crate key types");
        this.plugin = plugin;
        this.permission = "hcf.command.loot.argument." + this.getName();
    }

    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        List keyNames = this.plugin.getKeyManager().getKeys().stream().map(Key::getDisplayName).collect(Collectors.toList());
        sender.sendMessage((Object)ChatColor.GRAY + "List of key types: " + StringUtils.join(keyNames, (String)new StringBuilder().append((Object)ChatColor.GRAY).append(", ").toString()));
        return true;
    }
}

