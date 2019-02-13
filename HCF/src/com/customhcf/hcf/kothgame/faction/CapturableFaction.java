/*
 * Decompiled with CFR 0_115.
 */
package com.customhcf.hcf.kothgame.faction;

import com.customhcf.hcf.kothgame.faction.EventFaction;
import java.util.Map;

public abstract class CapturableFaction
extends EventFaction {
    public CapturableFaction(String name) {
        super(name);
    }

    public CapturableFaction(Map<String, Object> map) {
        super(map);
    }
}

