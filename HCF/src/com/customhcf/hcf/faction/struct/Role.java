/*
 * Decompiled with CFR 0_115.
 */
package com.customhcf.hcf.faction.struct;

public enum Role {
    LEADER("Leader", "[L]"),
    CO("Co-Leader", "[CL]"),
    CAPTAIN("Captain", "[C]"),
    MEMBER("Member", "");
    
    private final String name;
    private final String astrix;

    private Role(String name, String astrix) {
        this.name = name;
        this.astrix = astrix;
    }

    public String getName() {
        return this.name;
    }

    public String getAstrix() {
        return this.astrix;
    }
}

