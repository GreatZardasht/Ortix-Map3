package com.customhcf.base.command.module;

import com.customhcf.base.BasePlugin;
import com.customhcf.base.command.BaseCommandModule;
import com.customhcf.base.command.module.essential.AmivisCommand;
import com.customhcf.base.command.module.essential.AutoRestartCommand;
import com.customhcf.base.command.module.essential.DonateCommand;
import com.customhcf.base.command.module.essential.EnchantCommand;
import com.customhcf.base.command.module.essential.FeedCommand;
import com.customhcf.base.command.module.essential.FlyCommand;
import com.customhcf.base.command.module.essential.FreezeCommand;
import com.customhcf.base.command.module.essential.GamemodeCommand;
import com.customhcf.base.command.module.essential.HatCommand;
import com.customhcf.base.command.module.essential.KillCommand;
import com.customhcf.base.command.module.essential.NoteCommand;
import com.customhcf.base.command.module.essential.PlayTimeCommand;
import com.customhcf.base.command.module.essential.RemoveEntityCommand;
import com.customhcf.base.command.module.essential.RenameCommand;
import com.customhcf.base.command.module.essential.RepairCommand;
import com.customhcf.base.command.module.essential.RulesCommand;
import com.customhcf.base.command.module.essential.SetMaxPlayersCommand;
import com.customhcf.base.command.module.essential.SpeedCommand;
import com.customhcf.base.command.module.essential.StopLagCommand;
import com.customhcf.base.command.module.essential.SudoCommand;
import com.customhcf.base.command.module.essential.ToggleDonorOnly;
import com.customhcf.base.command.module.essential.WhoisCommand;

public class EssentialModule
extends BaseCommandModule {
    public EssentialModule(BasePlugin plugin) {
        this.commands.add(new ToggleDonorOnly(plugin));
      //  this.commands.add(new RequestCommand());
        this.commands.add(new AmivisCommand(plugin));
        this.commands.add(new DonateCommand());
        this.commands.add(new AutoRestartCommand(plugin));
        this.commands.add(new EnchantCommand());
        this.commands.add(new NoteCommand());
        this.commands.add(new FeedCommand());
        this.commands.add(new FlyCommand());
        this.commands.add(new FreezeCommand(plugin));
        this.commands.add(new GamemodeCommand());
        this.commands.add(new HatCommand());
        this.commands.add(new KillCommand());
        this.commands.add(new PlayTimeCommand(plugin));
        this.commands.add(new RemoveEntityCommand());
        this.commands.add(new RenameCommand());
        this.commands.add(new RepairCommand());
        this.commands.add(new RulesCommand(plugin));
        this.commands.add(new SetMaxPlayersCommand());
        this.commands.add(new SpeedCommand());
        this.commands.add(new StopLagCommand(plugin));
        this.commands.add(new SudoCommand());
        this.commands.add(new WhoisCommand(plugin));
    }
}

