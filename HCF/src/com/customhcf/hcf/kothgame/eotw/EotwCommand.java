/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.command.TabCompleter
 *  org.bukkit.conversations.Conversable
 *  org.bukkit.conversations.Conversation
 *  org.bukkit.conversations.ConversationContext
 *  org.bukkit.conversations.ConversationFactory
 *  org.bukkit.conversations.Prompt
 *  org.bukkit.conversations.StringPrompt
 *  org.bukkit.plugin.Plugin
 */
package com.customhcf.hcf.kothgame.eotw;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.kothgame.eotw.EOTWHandler;
import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.plugin.Plugin;

public class EotwCommand
implements CommandExecutor,
TabCompleter {
    private final ConversationFactory factory;

    public EotwCommand(HCF plugin) {
        this.factory = new ConversationFactory((Plugin)plugin).withFirstPrompt((Prompt)new EotwPrompt()).withEscapeSequence("/no").withTimeout(10).withModality(false).withLocalEcho(true);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage((Object)ChatColor.RED + "Command Console only.");
            return true;
        }
        Conversable conversable = (Conversable)sender;
        conversable.beginConversation(this.factory.buildConversation(conversable));
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

    private static final class EotwPrompt
    extends StringPrompt {
        private EotwPrompt() {
        }

        public String getPromptText(ConversationContext context) {
            return (Object)ChatColor.YELLOW + "Are you sure you want to do this? The server will be in EOTW mode, If EOTW mode is active, all claims whilst making Spawn a KOTH. " + "You will still have " + EOTWHandler.EOTW_WARMUP_WAIT_SECONDS + " seconds to cancel this using the same command though. " + "Type " + (Object)ChatColor.GREEN + "yes" + (Object)ChatColor.YELLOW + " to confirm or " + (Object)ChatColor.RED + "no" + (Object)ChatColor.YELLOW + " to deny.";
        }

        public Prompt acceptInput(ConversationContext context, String string) {
            if (string.equalsIgnoreCase("yes")) {
                boolean newStatus = !HCF.getPlugin().getEotwHandler().isEndOfTheWorld(false);
                Conversable conversable = context.getForWhom();
                if (conversable instanceof CommandSender) {
                    Command.broadcastCommandMessage((CommandSender)((CommandSender)conversable), (String)((Object)ChatColor.GOLD + "Set EOTW mode to " + newStatus + '.'));
                } else {
                    conversable.sendRawMessage((Object)ChatColor.GOLD + "Set EOTW mode to " + newStatus + '.');
                }
                HCF.getPlugin().getEotwHandler().setEndOfTheWorld(newStatus);
            } else if (string.equalsIgnoreCase("no")) {
                context.getForWhom().sendRawMessage((Object)ChatColor.BLUE + "Cancelled the process of setting EOTW mode.");
            } else {
                context.getForWhom().sendRawMessage((Object)ChatColor.RED + "Unrecognized response. Process of toggling EOTW mode has been cancelled.");
            }
            return Prompt.END_OF_CONVERSATION;
        }
    }

}

