/*
 * Decompiled with CFR 0_115.
 */
package com.customhcf.base.command.module;

import com.customhcf.base.BasePlugin;
import com.customhcf.base.command.BaseCommandModule;
import com.customhcf.base.command.module.chat.*;

import java.util.Set;

public class ChatModule
extends BaseCommandModule {
    public ChatModule(BasePlugin plugin) {
        this.commands.add(new AnnouncementCommand(plugin));
        this.commands.add(new BroadcastCommand(plugin));
        this.commands.add(new ClearChatCommand());
        this.commands.add(new DisableChatCommand(plugin));
        this.commands.add(new SlowChatCommand(plugin));
        this.commands.add(new IgnoreCommand(plugin));
        this.commands.add(new MessageCommand(plugin));
        this.commands.add(new MessageSpyCommand(plugin));
        this.commands.add(new ReplyCommand(plugin));
        this.commands.add(new ToggleMessagesCommand(plugin));
        this.commands.add(new SoundsCommand(plugin));
    }
}

