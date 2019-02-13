/*
 * Decompiled with CFR 0_115.
 */
package com.customhcf.base.command.module;

import com.customhcf.base.BasePlugin;
import com.customhcf.base.command.BaseCommandModule;
import com.customhcf.base.command.module.teleport.BackCommand;
import com.customhcf.base.command.module.teleport.TeleportAllCommand;
import com.customhcf.base.command.module.teleport.TeleportCommand;
import com.customhcf.base.command.module.teleport.TeleportHereCommand;
import com.customhcf.base.command.module.teleport.TopCommand;
import com.customhcf.base.command.module.teleport.WorldCommand;
import com.customhcf.base.command.module.warp.WarpExecutor;
import java.util.Set;

public class TeleportModule
extends BaseCommandModule {
    public TeleportModule(BasePlugin plugin) {
        this.commands.add(new BackCommand(plugin));
        this.commands.add(new TeleportCommand());
        this.commands.add(new TeleportAllCommand());
        this.commands.add(new TeleportHereCommand());
        this.commands.add(new TopCommand());
        this.commands.add(new WorldCommand());
        this.commands.add(new WarpExecutor(plugin));
    }
}

