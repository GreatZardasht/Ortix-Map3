package com.bizarrealex.azazel.tab;

import org.bukkit.scoreboard.*;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.entity.*;
import com.bizarrealex.azazel.*;
import java.util.concurrent.*;
import org.bukkit.craftbukkit.v1_7_R4.entity.*;
import net.minecraft.server.v1_7_R4.*;
import org.bukkit.*;
import org.apache.commons.lang.*;
import java.util.*;
import net.minecraft.util.com.mojang.authlib.*;
import java.lang.reflect.*;

public class Tab
{
    private Scoreboard scoreboard;
    private Team elevatedTeam;
    private Map<TabEntryPosition, String> entries;
    
    public Tab(final Player player, final boolean hook, final Azazel azazel) {
        this.entries = new ConcurrentHashMap<TabEntryPosition, String>();
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        if (hook && !player.getScoreboard().equals(Bukkit.getScoreboardManager().getMainScoreboard())) {
            this.scoreboard = player.getScoreboard();
        }
        this.elevatedTeam = this.scoreboard.registerNewTeam((String)this.getBlanks().get(this.getBlanks().size() - 1));
        for (final Player other : Bukkit.getOnlinePlayers()) {
            this.getElevatedTeam(other, azazel).addEntry(other.getName());
            final Tab tab = azazel.getTabByPlayer(other);
            if (tab != null) {
                tab.getElevatedTeam(player, azazel).addEntry(player.getName());
            }
            final PacketPlayOutPlayerInfo packet = PacketPlayOutPlayerInfo.removePlayer(((CraftPlayer)other).getHandle());
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)packet);
        }
        player.setScoreboard(this.scoreboard);
        this.initialize(player);
    }
    
    public Team getElevatedTeam(final Player player, final Azazel azazel) {
        if (player.hasMetadata("HydrogenPrefix")) {
            final String prefix = ChatColor.getLastColors(player.getDisplayName().replace(ChatColor.RESET + "", ""));
            String name = this.getBlanks().get(this.getBlanks().size() - 1) + prefix;
            if (name.length() > 16) {
                name = name.substring(0, 15);
            }
            Team team = this.scoreboard.getTeam(name);
            if (team == null) {
                team = this.scoreboard.registerNewTeam(name);
                team.setPrefix(prefix);
            }
            return team;
        }
        return this.elevatedTeam;
    }
    
    public Set<TabEntryPosition> getPositions() {
        return this.entries.keySet();
    }
    
    public Team getByLocation(final int x, final int y) {
        for (final TabEntryPosition position : this.entries.keySet()) {
            if (position.getX() == x && position.getY() == y) {
                return this.scoreboard.getTeam(position.getKey());
            }
        }
        return null;
    }
    
    private void initialize(final Player player) {
        if (((CraftPlayer)player).getHandle().playerConnection.networkManager.getVersion() >= 47) {
            for (int x = 0; x < 4; ++x) {
                for (int y = 0; y < 20; ++y) {
                    final String key = this.getNextBlank();
                    final TabEntryPosition position = new TabEntryPosition(x, y, key, this.scoreboard);
                    System.out.println(x * y);
                    this.entries.put(position, key);
                    ((CraftPlayer)player).getHandle().playerConnection.sendPacket(getPlayerPacket(this.entries.get(position)));
                    Team team = this.scoreboard.getTeam(position.getKey());
                    if (team == null) {
                        team = this.scoreboard.registerNewTeam(position.getKey());
                    }
                    team.addEntry((String)this.entries.get(position));
                }
            }
        }
        else {
            for (int i = 0; i < 60; ++i) {
                final int x2 = i % 3;
                final int y2 = i / 3;
                final String key2 = this.getNextBlank();
                final TabEntryPosition position2 = new TabEntryPosition(x2, y2, key2, this.scoreboard);
                this.entries.put(position2, key2);
                ((CraftPlayer)player).getHandle().playerConnection.sendPacket(getPlayerPacket(this.entries.get(position2)));
                Team team2 = this.scoreboard.getTeam(position2.getKey());
                if (team2 == null) {
                    team2 = this.scoreboard.registerNewTeam(position2.getKey());
                }
                team2.addEntry((String)this.entries.get(position2));
            }
        }
    }
    
    private String getNextBlank() {
    Label_0010:
        for (final String blank : this.getBlanks()) {
            if (this.scoreboard.getTeam(blank) != null) {
                continue;
            }
            for (final String identifier : this.entries.values()) {
                if (identifier.equals(blank)) {
                    continue Label_0010;
                }
            }
            return blank;
        }
        return null;
    }
    
    public List<String> getBlanks() {
        final List<String> toReturn = new ArrayList<String>();
        for (final ChatColor color : ChatColor.values()) {
            for (int i = 0; i < 4; ++i) {
                final String identifier = StringUtils.repeat(color + "", 4 - i) + ChatColor.RESET;
                toReturn.add(identifier);
            }
        }
        return toReturn;
    }
    
    private static Packet getPlayerPacket(final String name) {
        final PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
        try {
            final Field action = PacketPlayOutPlayerInfo.class.getDeclaredField("action");
            final Field username = PacketPlayOutPlayerInfo.class.getDeclaredField("username");
            final Field player = PacketPlayOutPlayerInfo.class.getDeclaredField("player");
            action.setAccessible(true);
            username.setAccessible(true);
            player.setAccessible(true);
            action.set(packet, 0);
            username.set(packet, name);
            player.set(packet, new GameProfile(UUID.randomUUID(), name));
        }
        catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
        catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
        return (Packet)packet;
    }
    
    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }
    
    public Team getElevatedTeam() {
        return this.elevatedTeam;
    }
    
    public void setElevatedTeam(final Team elevatedTeam) {
        this.elevatedTeam = elevatedTeam;
    }
    
    public static class UpdatedPacketPlayOutPlayerInfo extends PacketPlayOutPlayerInfo
    {
    }
    
    public static class TabEntryPosition
    {
        private final int x;
        private final int y;
        private final String key;
        
        public TabEntryPosition(final int x, final int y, final String key, final Scoreboard scoreboard) {
            this.x = x;
            this.y = y;
            this.key = key;
        }
        
        public int getX() {
            return this.x;
        }
        
        public int getY() {
            return this.y;
        }
        
        public String getKey() {
            return this.key;
        }
    }
}
