/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.base.ServerHandler
 *  org.apache.commons.lang3.time.DurationFormatUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.World$Environment
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package com.customhcf.hcf.kothgame.eotw;

import com.customhcf.base.ServerHandler;
import com.customhcf.hcf.HCF;
import com.customhcf.hcf.faction.FactionManager;
import com.customhcf.hcf.faction.claim.Claim;
import com.customhcf.hcf.faction.type.ClaimableFaction;
import com.customhcf.hcf.faction.type.Faction;
import com.customhcf.hcf.listener.BorderListener;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class EOTWHandler {
    public static final int BORDER_DECREASE_MINIMUM = 1000;
    public static final int BORDER_DECREASE_AMOUNT = 200;
    public static final long BORDER_DECREASE_TIME_MILLIS = TimeUnit.MINUTES.toMillis(5);
    public static final int BORDER_DECREASE_TIME_SECONDS = (int)(BORDER_DECREASE_TIME_MILLIS / 1000);
    public static final String BORDER_DECREASE_TIME_WORDS = org.apache.commons.lang3.time.DurationFormatUtils.formatDurationWords((long)BORDER_DECREASE_TIME_MILLIS, (boolean)true, (boolean)true);
    public static final String BORDER_DECREASE_TIME_ALERT_WORDS = org.apache.commons.lang3.time.DurationFormatUtils.formatDurationWords((long)(BORDER_DECREASE_TIME_MILLIS / 2), (boolean)true, (boolean)true);
    public static final long EOTW_WARMUP_WAIT_MILLIS = TimeUnit.MINUTES.toMillis(5);
    public static final int EOTW_WARMUP_WAIT_SECONDS = (int)(EOTW_WARMUP_WAIT_MILLIS / 1000);
    private static final long EOTW_CAPPABLE_WAIT = TimeUnit.MINUTES.toMillis(15);
    private final HCF plugin;
    private EotwRunnable runnable;

    public EOTWHandler(HCF plugin) {
        this.plugin = plugin;
    }

    public EotwRunnable getRunnable() {
        return this.runnable;
    }

    public boolean isEndOfTheWorld() {
        return this.isEndOfTheWorld(true);
    }

    public void setEndOfTheWorld(boolean yes) {
        if (yes == this.isEndOfTheWorld(false)) {
            return;
        }
        if (yes) {
            this.runnable = new EotwRunnable(this.plugin.getServerHandler().getWorldBorder().intValue());
            this.runnable.runTaskTimer((Plugin)this.plugin, 1, 100);
        } else if (this.runnable != null) {
            this.runnable.cancel();
            this.runnable = null;
        }
    }

    public boolean isEndOfTheWorld(boolean ignoreWarmup) {
        return this.runnable != null && (!ignoreWarmup || this.runnable.getElapsedMilliseconds() > 0);
    }

    public static final class EotwRunnable
            extends BukkitRunnable {
        private static final PotionEffect WITHER = new PotionEffect(PotionEffectType.WITHER, 200, 0);
        private boolean hasInformedStarted = false;
        private long startStamp;
        private int borderSize;

        public EotwRunnable(int borderSize) {
            this.borderSize = borderSize;
            this.startStamp = System.currentTimeMillis() + EOTWHandler.EOTW_WARMUP_WAIT_MILLIS;
        }

        public long getTimeUntilStarting() {
            long difference = System.currentTimeMillis() - this.startStamp;
            return difference > 0 ? 0 : Math.abs(difference);
        }

        public long getTimeUntilCappable() {
            return EOTW_CAPPABLE_WAIT - this.getElapsedMilliseconds();
        }

        public long getElapsedMilliseconds() {
            return System.currentTimeMillis() - this.startStamp;
        }

        public void run() {
            long elapsedMillis = this.getElapsedMilliseconds();
            int elapsedSeconds = (int)Math.round((double)elapsedMillis / 1000.0);
            if (!this.hasInformedStarted && elapsedSeconds >= 0) {
                for (Faction faction : HCF.getPlugin().getFactionManager().getFactions()) {
                    if (!(faction instanceof ClaimableFaction)) continue;
                    ClaimableFaction claimableFaction = (ClaimableFaction)faction;
                    for (Claim claims : claimableFaction.getClaims()) {
                        claimableFaction.removeClaim(claims, (CommandSender)Bukkit.getConsoleSender());
                    }
                    claimableFaction.getClaims().clear();
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "f setdtr all -6");
                }
                this.hasInformedStarted = true;
                Bukkit.broadcastMessage((String)(ChatColor.DARK_RED.toString() + (Object)ChatColor.BOLD + "EndOfTheWorld" + (Object)ChatColor.RED + " has began. Border will decrease by " + 200 + " blocks every " + EOTWHandler.BORDER_DECREASE_TIME_WORDS + " until at " + 1000 + " blocks."));
                return;
            }
            for (Player on : Bukkit.getServer().getOnlinePlayers()) {
                if (BorderListener.isWithinBorder(on.getLocation())) continue;
                on.sendMessage((Object)ChatColor.RED + "EndOfTheWorld is active and your outside of the border. You will get wither.");
                on.addPotionEffect(WITHER, true);
            }
            if (HCF.getPlugin().getServerHandler().getWorldBorder().intValue() <= 1000) {
                return;
            }
            int newBorderSize = this.borderSize - 200;
            if (newBorderSize <= 1000) {
                return;
            }
            if (elapsedSeconds % EOTWHandler.BORDER_DECREASE_TIME_SECONDS == 0) {
                int borderSize;
                World.Environment normal = World.Environment.NORMAL;
                this.borderSize = borderSize = newBorderSize;
                HCF.getPlugin().getServerHandler().setServerBorder(normal, Integer.valueOf(borderSize));
                Bukkit.broadcastMessage((String)((Object)ChatColor.GOLD + "Border has been decreased to " + (Object)ChatColor.YELLOW + newBorderSize + (Object)ChatColor.GOLD + " blocks."));
            } else if ((long)elapsedSeconds % TimeUnit.MINUTES.toSeconds(5) == 0) {
                Bukkit.broadcastMessage((String)((Object)ChatColor.GOLD + "Border decreasing to " + (Object)ChatColor.YELLOW + newBorderSize + (Object)ChatColor.GOLD + " blocks in " + (Object)ChatColor.YELLOW + EOTWHandler.BORDER_DECREASE_TIME_ALERT_WORDS + (Object)ChatColor.GOLD + '.'));
            }
        }
    }

}
