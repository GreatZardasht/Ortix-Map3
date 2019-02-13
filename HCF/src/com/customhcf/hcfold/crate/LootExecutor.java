/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.util.command.ArgumentExecutor
 *  com.customhcf.util.command.CommandArgument
 */
package com.customhcf.hcfold.crate;

import com.customhcf.hcf.HCF;
import com.customhcf.hcfold.crate.argument.LootBankArgument;
import com.customhcf.hcfold.crate.argument.LootBroadcastsArgument;
import com.customhcf.hcfold.crate.argument.LootDepositArgument;
import com.customhcf.hcfold.crate.argument.LootGiveArgument;
import com.customhcf.hcfold.crate.argument.LootListArgument;
import com.customhcf.hcfold.crate.argument.LootWithdrawArgument;
import com.customhcf.util.command.ArgumentExecutor;
import com.customhcf.util.command.CommandArgument;

public class LootExecutor
extends ArgumentExecutor {
    public LootExecutor(HCF plugin) {
        super("keys");
        this.addArgument((CommandArgument)new LootBroadcastsArgument());
        this.addArgument((CommandArgument)new LootGiveArgument(plugin));
        this.addArgument((CommandArgument)new LootListArgument(plugin));
    }
}

