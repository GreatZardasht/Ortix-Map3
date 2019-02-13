package com.bizarrealex.azazel;

import org.bukkit.scheduler.*;
import org.bukkit.plugin.java.*;
import org.bukkit.plugin.*;
import org.bukkit.entity.*;
import com.bizarrealex.azazel.tab.*;
import org.bukkit.scoreboard.*;
import org.bukkit.*;
import java.util.*;

public class AzazelTask extends BukkitRunnable
{
    private final Azazel azazel;
    
    public AzazelTask(final Azazel azazel, final JavaPlugin plugin) {
        this.azazel = azazel;
        this.runTaskTimerAsynchronously((Plugin)plugin, 2L, 2L);
    }
    
    public void run() {
        final TabAdapter adapter = this.azazel.getAdapter();
        if (adapter != null) {
            for (final Player player : Bukkit.getOnlinePlayers()) {
                final Tab tab = this.azazel.getTabByPlayer(player);
                if (tab != null) {
                    final TabTemplate template = adapter.getTemplate(player);
                    if (template == null || (template.getLeft().isEmpty() && template.getMiddle().isEmpty() && template.getRight().isEmpty())) {
                        for (final Tab.TabEntryPosition position : tab.getPositions()) {
                            final Team team = player.getScoreboard().getTeam(position.getKey());
                            if (team != null) {
                                if (team.getPrefix() != null && !team.getPrefix().isEmpty()) {
                                    team.setPrefix("");
                                }
                                if (team.getSuffix() == null || team.getSuffix().isEmpty()) {
                                    continue;
                                }
                                team.setSuffix("");
                            }
                        }
                    }
                    else {
                        for (int i = 0; i < 20 - template.getLeft().size(); ++i) {
                            template.left("");
                        }
                        for (int i = 0; i < 20 - template.getMiddle().size(); ++i) {
                            template.middle("");
                        }
                        for (int i = 0; i < 20 - template.getRight().size(); ++i) {
                            template.right("");
                        }
                        final List<List<String>> rows = Arrays.asList(template.getLeft(), template.getMiddle(), template.getRight(), template.getFarRight());
                        for (int l = 0; l < rows.size(); ++l) {
                            for (int j = 0; j < rows.get(l).size(); ++j) {
                                final Team team2 = tab.getByLocation(l, j);
                                if (team2 != null) {
                                    final Map.Entry<String, String> prefixAndSuffix = this.getPrefixAndSuffix(rows.get(l).get(j));
                                    final String prefix = prefixAndSuffix.getKey();
                                    final String suffix = prefixAndSuffix.getValue();
                                    if (!team2.getPrefix().equals(prefix) || !team2.getSuffix().equals(suffix)) {
                                        team2.setPrefix(prefix);
                                        team2.setSuffix(suffix);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private Map.Entry<String, String> getPrefixAndSuffix(String text) {
        text = ChatColor.translateAlternateColorCodes('&', text);
        String prefix;
        String suffix;
        if (text.length() > 16) {
            final int splitAt = (text.charAt(15) == '§') ? 15 : 16;
            prefix = text.substring(0, splitAt);
            final String suffixTemp = ChatColor.getLastColors(prefix) + text.substring(splitAt);
            suffix = suffixTemp.substring(0, Math.min(suffixTemp.length(), 16));
        }
        else {
            prefix = text;
            suffix = "";
        }
        return new AbstractMap.SimpleEntry<String, String>(prefix, suffix);
    }
}
