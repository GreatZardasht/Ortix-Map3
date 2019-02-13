package com.customhcf.hcf.timer.type;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.timer.GlobalTimer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.TimeUnit;

public class SotwTimer
{
    private SotwRunnable sotwRunnable;

    public boolean cancel() {
        if (this.sotwRunnable != null) {
            this.sotwRunnable.cancel();
            this.sotwRunnable = null;
            return true;
        }
        return false;
    }

    public void start(final long millis) {
        if (this.sotwRunnable == null) {
            (this.sotwRunnable = new SotwRunnable(this, millis)).runTaskLater((Plugin) HCF.getPlugin(), millis / 50L);
        }
    }

    public SotwRunnable getSotwRunnable() {
        return this.sotwRunnable;
    }

    public static class SotwRunnable extends BukkitRunnable
    {
        private SotwTimer sotwTimer;
        private long startMillis;
        private long endMillis;

        public SotwRunnable(final SotwTimer sotwTimer, final long duration) {
            super();
            this.sotwTimer = sotwTimer;
            this.startMillis = System.currentTimeMillis();
            this.endMillis = this.startMillis + duration;
        }

        public long getRemaining() {
            return this.endMillis - System.currentTimeMillis();
        }

        public void run() {
            Bukkit.broadcastMessage(ChatColor.RED.toString() + ChatColor.BOLD + "SOTW Protection is now over!");
            Bukkit.broadcastMessage(ChatColor.RED.toString() + ChatColor.BOLD + "You are no longer invincible.");
            this.cancel();
            this.sotwTimer.sotwRunnable = null;
        }
    }
}
