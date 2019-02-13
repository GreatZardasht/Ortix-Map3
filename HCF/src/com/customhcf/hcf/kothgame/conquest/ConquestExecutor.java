/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.util.command.ArgumentExecutor
 *  com.customhcf.util.command.CommandArgument
 */
package com.customhcf.hcf.kothgame.conquest;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.kothgame.conquest.ConquestSetpointsArgument;
import com.customhcf.util.command.ArgumentExecutor;
import com.customhcf.util.command.CommandArgument;

public class ConquestExecutor
extends ArgumentExecutor {
    public ConquestExecutor(HCF plugin) {
        super("conquest");
        this.addArgument((CommandArgument)new ConquestSetpointsArgument(plugin));
    }
}

