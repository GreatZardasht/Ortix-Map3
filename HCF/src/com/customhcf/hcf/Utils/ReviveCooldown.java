package com.customhcf.hcf.Utils;

import com.customhcf.hcf.HCF;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nathan on 10/5/2016.
 */
public class ReviveCooldown {

    private static HashMap<String, Integer> cooldown = new HashMap<String, Integer>();

    public static HashMap<String, Integer> getCooldowns() {
        return cooldown;
    }

    public static void timer() {
        HCF.getPlugin().getServer().getScheduler().scheduleSyncRepeatingTask(HCF.getPlugin(), new Runnable() {
            public void run() {
                for (Map.Entry<String, Integer> entry : cooldown.entrySet()) {
                    if (entry.getValue() <= 1) {
                        cooldown.remove(entry.getKey());
                        continue;
                    }
                    cooldown.put(entry.getKey(), entry.getValue());
                }
            }
        }, 0L, 20L);
    }
}
