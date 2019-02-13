/*
 * Decompiled with CFR 0_115.
 *
 * Could not load the following classes:
 *  com.customhcf.base.BasePlugin
 *  com.customhcf.base.user.BaseUser
 *  com.customhcf.base.user.UserManager
 *  com.customhcf.util.BukkitUtils
 *  org.bukkit.ChatColor
 *  org.bukkit.GameMode
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.customhcf.hcf.scoreboard.provider;

import com.customhcf.base.BasePlugin;
import com.customhcf.hcf.*;
import com.customhcf.hcf.Utils.DateTimeFormats;
import com.customhcf.hcf.Utils.DurationFormatter;
import com.customhcf.hcf.chat.ChatHandler;
import com.customhcf.hcf.chat.Translate;
import com.customhcf.hcf.classes.PvpClass;
import com.customhcf.hcf.classes.bard.BardClass;
import com.customhcf.hcf.classes.type.MinerClass;
import com.customhcf.hcf.faction.type.PlayerFaction;
import com.customhcf.hcf.kothgame.EventTimer;
import com.customhcf.hcf.kothgame.eotw.EOTWHandler;
import com.customhcf.hcf.kothgame.faction.ConquestFaction;
import com.customhcf.hcf.kothgame.faction.EventFaction;
import com.customhcf.hcf.kothgame.tracker.ConquestTracker;
import com.customhcf.hcf.scoreboard.SidebarEntry;
import com.customhcf.hcf.scoreboard.SidebarProvider;
import com.customhcf.hcf.timer.GlobalTimer;
import com.customhcf.hcf.timer.PlayerTimer;
import com.customhcf.hcf.timer.Timer;
import com.customhcf.hcf.timer.type.*;
import com.customhcf.hcf.user.FactionUser;

import java.text.DecimalFormat;
import java.util.*;

import com.customhcf.util.BukkitUtils;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.*;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;


public class TimerSidebarProvider implements SidebarProvider
{
    public static final ThreadLocal<DecimalFormat> CONQUEST_FORMATTER;
    private static final SidebarEntry EMPTY_ENTRY_FILLER;
    private final HCF plugin;
    protected static final String STRAIGHT_LINE;

    public TimerSidebarProvider(final HCF plugin) {
        super();
        this.plugin = plugin;
    }

    private static String handleBardFormat(final long millis, final boolean trailingZero) {
        return (trailingZero ? DateTimeFormats.REMAINING_SECONDS_TRAILING : DateTimeFormats.REMAINING_SECONDS).get().format(millis * 0.001);
    }

    @Override
    public String getTitle() {
        return ChatColor.translateAlternateColorCodes('&', HCF.getPlugin().getConfig().getString("Server.Scoreboard.Title"));
    }


    @Override
    public List<SidebarEntry> getLines(final Player player) {
        List<SidebarEntry> lines = new ArrayList<SidebarEntry>();
        final EOTWHandler.EotwRunnable eotwRunnable = this.plugin.getEotwHandler().getRunnable();
        final PvpClass pvpClass = this.plugin.getPvpClassManager().getEquippedClass(player);
        EventTimer eventTimer = this.plugin.getTimerManager().eventTimer;
        List<SidebarEntry> conquestLines = null;
        EventFaction eventFaction = eventTimer.getEventFaction();
        final SotwTimer.SotwRunnable sotwRunnable = this.plugin.getSotwTimer().getSotwRunnable();
        final int lives = HCF.getPlugin().getDeathbanManager().getLives(player.getUniqueId());
        PermissionUser user = PermissionsEx.getUser(player.getName());
        List<String> groups = user.getParentIdentifiers();
        FactionUser hcf = HCF.getPlugin().getUserManager().getUser(player.getUniqueId());
        PlayerFaction faction = HCF.getPlugin().getFactionManager().getPlayerFaction(player.getUniqueId());

        // EOTW / SOTW
        //
        //
        //
        //
        //
        //

        if (sotwRunnable != null) {
            lines.add(new SidebarEntry(ChatColor.YELLOW.toString() + ChatColor.BOLD, Translate.c(HCF.getPlugin().getConfig().getString("Server.Scoreboard.SOTW")), ChatColor.GRAY + ": " + ChatColor.RED + DurationFormatter.getRemaining(sotwRunnable.getRemaining(), true)));
        }

        if (eotwRunnable != null) {
            long remaining3 = eotwRunnable.getTimeUntilStarting();
            if (remaining3 > 0L) {
                lines.add(new SidebarEntry(ChatColor.DARK_RED.toString() + ChatColor.BOLD, "EOTW" + ChatColor.RED + " (Starts", " In) " + ChatColor.GRAY + ": " + ChatColor.RED + HCF.getRemaining(remaining3, true)));
            }
            else if ((remaining3 = eotwRunnable.getTimeUntilCappable()) > 0L) {
                lines.add(new SidebarEntry(ChatColor.DARK_RED.toString() + ChatColor.BOLD, "EOTW" + ChatColor.RED + " (Cappable ", "In) " + ChatColor.GRAY + ": " + ChatColor.RED + HCF.getRemaining(remaining3, true)));
            }
        }
        

        // Classes
        //
        //
        //
        //
        //
        //

        if (pvpClass != null) {
            lines.add(new SidebarEntry(Translate.c(HCF.getPlugin().getConfig().getString("Server.Scoreboard.ActiveClass")), ChatColor.GRAY.toString() + ": " + pvpClass.getName(), ChatColor.RED));
        }
        if (pvpClass != null && pvpClass instanceof BardClass) {
            final BardClass bardClass = (BardClass) pvpClass;
            lines.add(new SidebarEntry(ChatColor.WHITE + " »" + ChatColor.GOLD.toString() + ChatColor.BOLD + "" + " Energy", ChatColor.GRAY + ": " + ChatColor.GRAY + handleBardFormat(bardClass.getEnergyMillis(player), true), ChatColor.GRAY + "/100"));
            final long remaining2 = bardClass.getRemainingBuffDelay(player);
            if (remaining2 > 0L) {
                lines.add(new SidebarEntry(ChatColor.RED + " » Effect", ChatColor.RED + " Cooldown", ChatColor.GRAY + ": " + ChatColor.GRAY + HCF.getRemaining(remaining2, true)));
            }
        }

        // MinerClass Addons
        //
        //
        //
        //
        //
        //

        if ((pvpClass instanceof MinerClass)) {
            MinerClass minerClass = (MinerClass) pvpClass;
            lines.add(new SidebarEntry(ChatColor.GOLD + "", ChatColor.AQUA + " » Diamonds", ChatColor.GRAY + ": " + ChatColor.RED + player.getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE)));
        }

        // Timers
        //
        //
        //
        //
        //
        //
        final Collection<Timer> timers = this.plugin.getTimerManager().getTimers();
        for (final Timer timer : timers) {
            if (timer instanceof PlayerTimer && !(timer instanceof NotchAppleTimer)) {
                final PlayerTimer playerTimer = (PlayerTimer) timer;
                final long remaining2 = playerTimer.getRemaining(player);
                if (remaining2 <= 0L) {
                    continue;
                }
                String timerName = playerTimer.getName();
                if (timerName.length() > 14) {
                    timerName = timerName.substring(0, timerName.length());
                }
                lines.add(new SidebarEntry(playerTimer.getScoreboardPrefix(), timerName + ChatColor.GRAY, ": " + ChatColor.RED + HCF.getRemaining(remaining2, true)));
            }

            if (timer instanceof NotchAppleTimer) {
                final PlayerTimer playerTimer = (PlayerTimer) timer;
                final long remaining2 = playerTimer.getRemaining(player);
                if (remaining2 <= 0L) {
                    continue;
                }
                String timerName = playerTimer.getName();
                if (timerName.length() > 14) {
                    timerName = timerName.substring(0, timerName.length());
                }
                lines.add(new SidebarEntry(playerTimer.getScoreboardPrefix(), timerName + ChatColor.GRAY, ": " + ChatColor.RED + HCF.getRemaining(remaining2, true)));

            }

            else if ((timer instanceof GlobalTimer))
            {
                GlobalTimer playerTimer2 = (GlobalTimer)timer;
                long remaining2 = playerTimer2.getRemaining1();
                if (remaining2 > 0L)
                {
                    String timerName = playerTimer2.getName();
                    if (timerName.length() > 14) {
                        timerName = timerName.substring(0, timerName.length());
                    }
                    lines.add(new SidebarEntry(ChatColor.RED.toString()  + playerTimer2.getScoreboardPrefix(), timerName + ChatColor.GRAY, ": " + ChatColor.GRAY + HCF.getRemaining(remaining2, true)));
                }
            }
        }

        Collection<Timer> timers1 = this.plugin.getTimerManager().getTimers();
        for (Timer timer1 : timers1) {
            if ((timer1 instanceof GlobalTimer))
            {
                GlobalTimer playerTimer3 = (GlobalTimer)timer1;
                long remaining3 = playerTimer3.getRemaining();
                if (remaining3 > 0L)
                {
                    String timerName1 = playerTimer3.getName();
                    if (timerName1.length() > 14) {
                        timerName1 = timerName1.substring(0, timerName1.length());
                    }
                    lines.add(new SidebarEntry(ChatColor.RED.toString() + playerTimer3.getScoreboardPrefix(), timerName1 + ChatColor.GRAY, ": " + ChatColor.RED + HCF.getRemaining(remaining3, true)));
                }
            }
        }

        // Staff Board
        //
        //
        //
        //
        //
        //
        
        if (player.hasPermission("base.command.staffmode") && (BasePlugin.getPlugin().getUserManager().getUser(player.getUniqueId()).isStaffUtil())) {
            if (HCF.toggle.contains(player) == true) {
                lines.add(new SidebarEntry(ChatColor.WHITE.toString() + ChatColor.BOLD + "" + ChatColor.YELLOW.toString(), Translate.c(HCF.getPlugin().getConfig().getString("Server.Scoreboard.StaffMode")),  " "));
            } else if (HCF.toggle.contains(player) == false) {
                lines.add(new SidebarEntry(ChatColor.WHITE.toString() + ChatColor.BOLD + "" + ChatColor.YELLOW.toString(), Translate.c(HCF.getPlugin().getConfig().getString("Server.Scoreboard.StaffMode")), " "));
            }
            if (player.hasPermission("base.command.staffmode") && com.customhcf.base.BasePlugin.getPlugin().getUserManager().getUser(player.getUniqueId()).isStaffUtil()) {
                lines.add(new SidebarEntry(ChatColor.GRAY.toString() + "" + ChatColor.YELLOW.toString(), Translate.c(HCF.getPlugin().getConfig().getString("Server.Scoreboard.StaffMode"))," "));
            }
            if (player.hasPermission("base.command.gamemode")) {
                lines.add(new SidebarEntry(ChatColor.GRAY.toString() + " » " + ChatColor.YELLOW.toString(), Translate.c(HCF.getPlugin().getConfig().getString("Server.Scoreboard.GameMode")) + ChatColor.GRAY + ": ", (player.getGameMode() == GameMode.CREATIVE) ? (ChatColor.GREEN + "Creative") : (ChatColor.RED + "Survival")));
            }
            if (player.hasPermission("base.command.vanish")) {
                if (com.customhcf.hcf.command.VanishCommand.v.contains(player) == true) {
                    lines.add(new SidebarEntry(ChatColor.GRAY
                            .toString() + " » " + ChatColor.YELLOW.toString(), Translate.c(HCF.getPlugin().getConfig().getString("Server.Scoreboard.Vanish")) + ChatColor.GRAY + ": ",ChatColor.GREEN + "Enabled"));
                } else if (com.customhcf.hcf.command.VanishCommand.v.contains(player) == false) {
                    lines.add(new SidebarEntry(ChatColor.GRAY
                            .toString() + " » " + ChatColor.YELLOW.toString(), Translate.c(HCF.getPlugin().getConfig().getString("Server.Scoreboard.Vanish")) + ChatColor.GRAY + ": ",ChatColor.RED + "Disable"));
                }
            }
            if (player.hasPermission("command.staffchat")) {
                if (ChatHandler.sc.contains(player)) {
                    lines.add(new SidebarEntry(ChatColor.GRAY
                            .toString() + " » " + ChatColor.YELLOW.toString(), Translate.c(HCF.getPlugin().getConfig().getString("Server.Scoreboard.StaffChat")) + ChatColor.GRAY + ": ",ChatColor.GREEN + "Enabled"));
                } else if (!ChatHandler.sc.contains(player)){
                    lines.add(new SidebarEntry(ChatColor.GRAY
                            .toString() + " » " + ChatColor.YELLOW.toString(), Translate.c(HCF.getPlugin().getConfig().getString("Server.Scoreboard.StaffChat")) + ChatColor.GRAY + ": ",ChatColor.RED + "Disabled"));
                }
            }
       }


        // CONQUEST

        if (eventFaction instanceof ConquestFaction) {
            ConquestFaction conquestFaction = (ConquestFaction)eventFaction;
            conquestLines = new ArrayList<SidebarEntry>();
            conquestLines.add(new SidebarEntry(ChatColor.BLUE.toString(), (Object)ChatColor.BOLD + conquestFaction.getName() + (Object)ChatColor.GRAY, ": "));
            conquestLines.add(new SidebarEntry("  " + ChatColor.RED.toString() + conquestFaction.getRed().getScoreboardRemaining(), (Object)ChatColor.RESET + " ", ChatColor.YELLOW.toString() + conquestFaction.getYellow().getScoreboardRemaining()));
            conquestLines.add(new SidebarEntry("  " + ChatColor.GREEN.toString() + conquestFaction.getGreen().getScoreboardRemaining(), (Object)ChatColor.RESET + " " + (Object)ChatColor.RESET, ChatColor.AQUA.toString() + conquestFaction.getBlue().getScoreboardRemaining()));
            ConquestTracker conquestTracker = (ConquestTracker)conquestFaction.getEventType().getEventTracker();
            int count = 0;
            for (Map.Entry<PlayerFaction, Integer> entry : conquestTracker.getFactionPointsMap().entrySet()) {
                String factionName = entry.getKey().getName();
                if (factionName.length() > 14) {
                    factionName = factionName.substring(0, 14);
                }
                conquestLines.add(new SidebarEntry((Object)ChatColor.LIGHT_PURPLE, (Object)((Object)ChatColor.BOLD + factionName), (Object)((Object)ChatColor.GRAY + ": " + (Object)ChatColor.YELLOW + entry.getValue())));
                if (++count != 3) continue;
                break;
            }
        }

        if (conquestLines != null && !conquestLines.isEmpty()) {
            conquestLines.addAll(lines);
            lines = conquestLines;
        }
        if (!lines.isEmpty()) {
            lines.add(0, new SidebarEntry(ChatColor.GRAY, ChatColor.STRIKETHROUGH + "--*---------", "---------*--"));
            lines.add(lines.size(), new SidebarEntry(ChatColor.GRAY, ChatColor.STRIKETHROUGH.toString() +ChatColor.STRIKETHROUGH + "--*---------",  "---------*--"));
        }
        return lines;
    }

    static {
        CONQUEST_FORMATTER = new ThreadLocal<DecimalFormat>() {
            @Override
            protected DecimalFormat initialValue() {
                return new DecimalFormat("##.#");
            }
        };
        EMPTY_ENTRY_FILLER = new SidebarEntry(" ", " ", " ");
        STRAIGHT_LINE = BukkitUtils.STRAIGHT_LINE_DEFAULT.substring(0, 14);
    }
}