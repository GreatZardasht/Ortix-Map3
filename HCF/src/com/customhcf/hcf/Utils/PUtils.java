package com.customhcf.hcf.Utils;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.deathban.Deathban;
import com.customhcf.hcf.user.FactionUser;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PUtils {

    public static boolean isDeathbanned(UUID uuid) {
        FactionUser user = HCF.getPlugin().getUserManager().getUser(uuid);
        Deathban deathban = user.getDeathban();
        if (deathban == null || !deathban.isActive())
            return false;
        return true;
    }

    public static void revivePlayer(UUID uuid) {
        FactionUser user = HCF.getPlugin().getUserManager().getUser(uuid);
        user.removeDeathban();
    }

    public static boolean hasCooldown(Player player) {
        if (ReviveCooldown.getCooldowns().containsKey(player.getUniqueId().toString()))
            return true;
        return false;
    }

    public static Integer getCooldown(Player player) {
        int cooldown = ReviveCooldown.getCooldowns().get(player.getUniqueId().toString()) / 60;
        return cooldown;
    }

    public static void addCooldown(Player player) {
        ReviveCooldown.getCooldowns().put(player.getUniqueId().toString(), 3600);
    }


}

