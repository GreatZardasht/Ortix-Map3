package com.customhcf.hcf.lives.argument;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.command.ReviveCommand;
import com.customhcf.hcf.lives.argument.commands.*;
import com.customhcf.util.command.ArgumentExecutor;
import com.customhcf.util.command.CommandArgument;

public class LivesExecutor
        extends ArgumentExecutor {
    public LivesExecutor(HCF plugin) {
        super("lives");
        this.addArgument(new LivesCheckCommand(plugin));
        this.addArgument(new LivesGiveCommand(plugin));
        this.addArgument(new LivesReviveCommand(plugin));
        this.addArgument(new LivesSetCommand(plugin));
        this.addArgument(new LivesStaffGiveCommand(plugin));
    }
}