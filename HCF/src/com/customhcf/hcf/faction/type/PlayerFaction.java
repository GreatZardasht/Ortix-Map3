/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.customhcf.base.BasePlugin
 *  com.customhcf.base.PlayTimeManager
 *  com.customhcf.util.BukkitUtils
 *  com.customhcf.util.GenericUtils
 *  com.customhcf.util.JavaUtils
 *  com.customhcf.util.PersistableLocation
 *  com.google.common.base.Objects
 *  com.google.common.base.Preconditions
 *  com.google.common.base.Predicate
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  javax.annotation.Nullable
 *  org.apache.commons.lang3.StringUtils
 *  org.apache.commons.lang3.time.DurationFormatUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 */
package com.customhcf.hcf.faction.type;

import com.customhcf.base.BasePlugin;
import com.customhcf.hcf.Utils.ConfigurationService;
import com.customhcf.hcf.HCF;
import com.customhcf.hcf.deathban.Deathban;
import com.customhcf.hcf.faction.FactionMember;
import com.customhcf.hcf.faction.event.FactionDtrChangeEvent;
import com.customhcf.hcf.faction.event.PlayerJoinedFactionEvent;
import com.customhcf.hcf.faction.event.PlayerLeaveFactionEvent;
import com.customhcf.hcf.faction.event.PlayerLeftFactionEvent;
import com.customhcf.hcf.faction.event.cause.FactionLeaveCause;
import com.customhcf.hcf.faction.struct.Raidable;
import com.customhcf.hcf.faction.struct.RegenStatus;
import com.customhcf.hcf.faction.struct.Relation;
import com.customhcf.hcf.faction.struct.Role;
import com.customhcf.hcf.timer.type.TeleportTimer;
import com.customhcf.hcf.user.FactionUser;
import com.customhcf.util.BukkitUtils;
import com.customhcf.util.GenericUtils;
import com.customhcf.util.JavaUtils;
import com.customhcf.util.PersistableLocation;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import javax.annotation.Nullable;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class PlayerFaction
        extends ClaimableFaction
        implements Raidable
{
    private static final UUID[] EMPTY_UUID_ARRAY = new UUID[0];
    protected final Map requestedRelations = new HashMap();
    protected final Map relations = new HashMap();
    protected final Map members = new HashMap();
    protected final Set<String> invitedPlayerNames;
    protected PersistableLocation home;
    protected String announcement;
    protected boolean open;
    protected int balance;
    protected double deathsUntilRaidable;
    protected long regenCooldownTimestamp;
    private long lastDtrUpdateTimestamp;
    protected UUID focus;

    public PlayerFaction(String name)
    {
        super(name);
        this.invitedPlayerNames = new TreeSet(String.CASE_INSENSITIVE_ORDER);
        this.deathsUntilRaidable = 1.0D;
    }

    public PlayerFaction(Map map)
    {
        super(map);
        this.invitedPlayerNames = new TreeSet(String.CASE_INSENSITIVE_ORDER);
        this.deathsUntilRaidable = 1.0D;
        Iterator object = GenericUtils.castMap(map.get("members"), String.class, FactionMember.class).entrySet().iterator();
        while (object.hasNext())
        {
            Map.Entry entry = (Map.Entry)object.next();
            this.members.put(UUID.fromString((String)entry.getKey()), entry.getValue());
        }
        this.invitedPlayerNames.addAll(GenericUtils.createList(map.get("invitedPlayerNames"), String.class));
        Object object1 = map.get("home");
        if (object1 != null) {
            this.home = ((PersistableLocation)object1);
        }
        object1 = map.get("announcement");
        if (object1 != null) {
            this.announcement = ((String)object1);
        }
        Iterator entry2 = GenericUtils.castMap(map.get("relations"), String.class, String.class).entrySet().iterator();
        while (entry2.hasNext())
        {
            Map.Entry entry1 = (Map.Entry)entry2.next();
            this.relations.put(UUID.fromString((String)entry1.getKey()), Relation.valueOf((String)entry1.getValue()));
        }
        entry2 = GenericUtils.castMap(map.get("requestedRelations"), String.class, String.class).entrySet().iterator();
        while (entry2.hasNext())
        {
            Map.Entry entry1 = (Map.Entry)entry2.next();
            this.requestedRelations.put(UUID.fromString((String)entry1.getKey()), Relation.valueOf((String)entry1.getValue()));
        }
        this.open = ((Boolean)map.get("open")).booleanValue();
        this.balance = ((Integer)map.get("balance")).intValue();
        this.deathsUntilRaidable = ((Double)map.get("deathsUntilRaidable")).doubleValue();
        this.regenCooldownTimestamp = Long.parseLong((String)map.get("regenCooldownTimestamp"));
        this.lastDtrUpdateTimestamp = Long.parseLong((String)map.get("lastDtrUpdateTimestamp"));
    }

    public Map serialize()
    {
        Map map = super.serialize();
        HashMap relationSaveMap = new HashMap(this.relations.size());
        Iterator requestedRelationsSaveMap = this.relations.entrySet().iterator();
        while (requestedRelationsSaveMap.hasNext())
        {
            Map.Entry entrySet = (Map.Entry)requestedRelationsSaveMap.next();
            relationSaveMap.put(((UUID)entrySet.getKey()).toString(), ((Relation)entrySet.getValue()).name());
        }
        map.put("relations", relationSaveMap);
        HashMap requestedRelationsSaveMap1 = new HashMap(this.requestedRelations.size());
        Iterator entrySet1 = this.requestedRelations.entrySet().iterator();
        while (entrySet1.hasNext())
        {
            Map.Entry saveMap = (Map.Entry)entrySet1.next();
            requestedRelationsSaveMap1.put(saveMap.getKey().toString(), ((Relation)saveMap.getValue()).name());
        }
        map.put("requestedRelations", requestedRelationsSaveMap1);
        Set entrySet2 = this.members.entrySet();
        LinkedHashMap saveMap1 = new LinkedHashMap(this.members.size());
        Iterator var6 = entrySet2.iterator();
        while (var6.hasNext())
        {
            Map.Entry entry = (Map.Entry)var6.next();
            saveMap1.put(entry.getKey().toString(), entry.getValue());
        }
        map.put("members", saveMap1);
        map.put("invitedPlayerNames", new ArrayList(this.invitedPlayerNames));
        if (this.home != null) {
            map.put("home", this.home);
        }
        if (this.announcement != null) {
            map.put("announcement", this.announcement);
        }
        map.put("open", Boolean.valueOf(this.open));
        map.put("balance", Integer.valueOf(this.balance));
        map.put("deathsUntilRaidable", Double.valueOf(this.deathsUntilRaidable));
        map.put("regenCooldownTimestamp", Long.toString(this.regenCooldownTimestamp));
        map.put("lastDtrUpdateTimestamp", Long.toString(this.lastDtrUpdateTimestamp));
        return map;
    }

    public boolean setMember(UUID playerUUID, FactionMember factionMember)
    {
        return setMember(null, playerUUID, factionMember, false);
    }

    public boolean setMember(UUID playerUUID, FactionMember factionMember, boolean force)
    {
        return setMember(null, playerUUID, factionMember, force);
    }

    public boolean setMember(Player player, FactionMember factionMember)
    {
        return setMember(player, player.getUniqueId(), factionMember, false);
    }

    public boolean setMember(Player player, FactionMember factionMember, boolean force)
    {
        return setMember(player, player.getUniqueId(), factionMember, force);
    }

    public UUID getFocus() {
        return this.focus;
    }

    public void setFocus(UUID uuid) {
        this.focus = uuid;
    }

    private boolean setMember(Player player, UUID playerUUID, FactionMember factionMember, boolean force)
    {
        if (factionMember == null)
        {
            if (!force)
            {
                PlayerLeaveFactionEvent event = player == null ? new PlayerLeaveFactionEvent(playerUUID, this, FactionLeaveCause.LEAVE) : new PlayerLeaveFactionEvent(player, this, FactionLeaveCause.LEAVE);
                Bukkit.getPluginManager().callEvent(event);
                if (event.isCancelled()) {
                    return false;
                }
            }
            this.members.remove(playerUUID);
            setDeathsUntilRaidable(Math.min(this.deathsUntilRaidable, getMaximumDeathsUntilRaidable()));
            PlayerLeftFactionEvent event2 = player == null ? new PlayerLeftFactionEvent(playerUUID, this, FactionLeaveCause.LEAVE) : new PlayerLeftFactionEvent(player, this, FactionLeaveCause.LEAVE);
            Bukkit.getPluginManager().callEvent(event2);
            return true;
        }
        PlayerJoinedFactionEvent eventPre = player == null ? new PlayerJoinedFactionEvent(playerUUID, this) : new PlayerJoinedFactionEvent(player, this);
        Bukkit.getPluginManager().callEvent(eventPre);
        this.lastDtrUpdateTimestamp = System.currentTimeMillis();
        this.invitedPlayerNames.remove(factionMember.getName());
        this.members.put(playerUUID, factionMember);
        return true;
    }

    public Collection<UUID> getAllied() {
        return (Collection<UUID>)Maps.filterValues((Map)this.relations, (Predicate)new Predicate<Relation>() {
            public boolean apply(@Nullable final Relation relation) {
                return relation == Relation.ALLY;
            }
        }).keySet();
    }

    public List<PlayerFaction> getAlliedFactions() {
        final Collection<UUID> allied = this.getAllied();
        final Iterator<UUID> iterator = allied.iterator();
        final List<PlayerFaction> results = new ArrayList<PlayerFaction>(allied.size());
        while (iterator.hasNext()) {
            final Faction faction = HCF.getPlugin().getFactionManager().getFaction(iterator.next());
            if (faction instanceof PlayerFaction) {
                results.add((PlayerFaction)faction);
            }
            else {
                iterator.remove();
            }
        }
        return results;
    }

    public Map<UUID, Relation> getRequestedRelations()
    {
        return this.requestedRelations;
    }

    public Map<UUID, Relation> getRelations()
    {
        return this.relations;
    }

    public Map<UUID, FactionMember> getMembers()
    {
        return ImmutableMap.copyOf(this.members);
    }

    public Set getOnlinePlayers()
    {
        return getOnlinePlayers((CommandSender)null);
    }

    public Set getOnlinePlayers(CommandSender sender)
    {
        Set entrySet = getOnlineMembers(sender).entrySet();
        HashSet results = new HashSet(entrySet.size());
        Iterator var4 = entrySet.iterator();
        while (var4.hasNext())
        {
            Map.Entry entry = (Map.Entry)var4.next();
            results.add(Bukkit.getPlayer((UUID)entry.getKey()));
        }
        return results;
    }

    public Map getOnlineMembers()
    {
        return getOnlineMembers(null);
    }

    public Map<UUID, FactionMember> getOnlineMembers(CommandSender sender)
    {
        Player senderPlayer = (sender instanceof Player) ? (Player)sender : null;
        HashMap<UUID, FactionMember> results = new HashMap();
        Iterator<Map.Entry<UUID, FactionMember>> iterator = this.members.entrySet().iterator();
        while (iterator.hasNext())
        {
            Map.Entry<UUID, FactionMember> entry = (Map.Entry)iterator.next();
            Player target = Bukkit.getPlayer((UUID)entry.getKey());
            if ((target != null) && (
                    (senderPlayer == null) || (senderPlayer.canSee(target)))) {
                results.put(entry.getKey(), entry.getValue());
            }
        }
        return results;
    }

    public FactionMember getLeader()
    {
        Map<UUID, FactionMember> members = this.members;
        Iterator<Map.Entry<UUID, FactionMember>> iterator = members.entrySet().iterator();
        Map.Entry<UUID, FactionMember> entry;
        do
        {
            if (!iterator.hasNext()) {
                return null;
            }
        } while (((FactionMember)(entry = (Map.Entry)iterator.next()).getValue()).getRole() != Role.LEADER);
        return (FactionMember)entry.getValue();
    }

    @Deprecated
    public FactionMember getMember(String memberName)
    {
        UUID uuid = Bukkit.getOfflinePlayer(memberName).getUniqueId();
        if (uuid == null) {
            return null;
        }
        FactionMember factionMember = (FactionMember)this.members.get(uuid);
        return factionMember;
    }

    public FactionMember getMember(Player player)
    {
        return getMember(player.getUniqueId());
    }

    public FactionMember getMember(UUID memberUUID)
    {
        return (FactionMember)this.members.get(memberUUID);
    }

    public Set<String> getInvitedPlayerNames()
    {
        return this.invitedPlayerNames;
    }

    public Location getHome()
    {
        return this.home == null ? null : this.home.getLocation();
    }

    public void setHome(Location home)
    {
        if ((home == null) && (this.home != null))
        {
            TeleportTimer timer = HCF.getPlugin().getTimerManager().teleportTimer;
            Iterator var3 = getOnlinePlayers().iterator();
            while (var3.hasNext())
            {
                Player player = (Player)var3.next();
                Location destination = (Location)timer.getDestination(player);
                if (Objects.equal(destination, this.home.getLocation()))
                {
                    timer.clearCooldown(player);
                    player.sendMessage(ChatColor.RED + "Your home was unset, so your " + timer.getDisplayName() + ChatColor.RED + " timer has been cancelled");
                }
            }
        }
        this.home = (home == null ? null : new PersistableLocation(home));
    }

    public String getAnnouncement()
    {
        return this.announcement;
    }

    public void setAnnouncement(@Nullable String announcement)
    {
        this.announcement = announcement;
    }

    public boolean isOpen()
    {
        return this.open;
    }

    public void setOpen(boolean open)
    {
        this.open = open;
    }

    public int getBalance()
    {
        return this.balance;
    }

    public void setBalance(int balance)
    {
        this.balance = balance;
    }

    public boolean isRaidable()
    {
        return this.deathsUntilRaidable <= 0.0D;
    }

    public double getDeathsUntilRaidable()
    {
        return getDeathsUntilRaidable(true);
    }

    public double getMaximumDeathsUntilRaidable()
    {
        if (this.members.size() == 1) {
            return 1.1D;
        }
        return Math.min(5.0D, this.members.size() * 0.9D);
    }

    public double getDeathsUntilRaidable(boolean updateLastCheck)
    {
        if (updateLastCheck) {
            updateDeathsUntilRaidable();
        }
        return this.deathsUntilRaidable;
    }

    public ChatColor getDtrColour()
    {
        updateDeathsUntilRaidable();
        if (this.deathsUntilRaidable < 0.0D) {
            return ChatColor.RED;
        }
        if (this.deathsUntilRaidable < 1.0D) {
            return ChatColor.YELLOW;
        }
        return ChatColor.GREEN;
    }

    private void updateDeathsUntilRaidable()
    {
        if (getRegenStatus() == RegenStatus.REGENERATING)
        {
            long now = System.currentTimeMillis();
            long millisPassed = now - this.lastDtrUpdateTimestamp;
            if (millisPassed >= ConfigurationService.DTR_MILLIS_BETWEEN_UPDATES)
            {
                long remainder = millisPassed % ConfigurationService.DTR_MILLIS_BETWEEN_UPDATES;
                int multiplier = (int)((millisPassed + remainder) / ConfigurationService.DTR_MILLIS_BETWEEN_UPDATES);
                double increase = multiplier * 0.1D;
                this.lastDtrUpdateTimestamp = (now - remainder);
                setDeathsUntilRaidable(this.deathsUntilRaidable + increase);
            }
        }
    }

    public double setDeathsUntilRaidable(double deathsUntilRaidable)
    {
        return setDeathsUntilRaidable(deathsUntilRaidable, true);
    }

    private double setDeathsUntilRaidable(double deathsUntilRaidable, boolean limit)
    {
        deathsUntilRaidable = deathsUntilRaidable * 100.0D / 100.0D;
        if (limit) {
            deathsUntilRaidable = Math.min(deathsUntilRaidable, getMaximumDeathsUntilRaidable());
        }
        if (deathsUntilRaidable - this.deathsUntilRaidable != 0.0D)
        {
            FactionDtrChangeEvent event = new FactionDtrChangeEvent(FactionDtrChangeEvent.DtrUpdateCause.REGENERATION, this, this.deathsUntilRaidable, deathsUntilRaidable);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled())
            {
                deathsUntilRaidable = event.getNewDtr();
                if ((deathsUntilRaidable > 0.0D) && (deathsUntilRaidable <= 0.0D)) {
                    HCF.getPlugin().getLogger().info("Faction " + getName() + " is now raidable.");
                }
                this.lastDtrUpdateTimestamp = System.currentTimeMillis();
                return this.deathsUntilRaidable = deathsUntilRaidable;
            }
        }
        return this.deathsUntilRaidable;
    }

    protected long getRegenCooldownTimestamp()
    {
        return this.regenCooldownTimestamp;
    }

    public long getRemainingRegenerationTime()
    {
        return this.regenCooldownTimestamp == 0L ? 0L : this.regenCooldownTimestamp - System.currentTimeMillis();
    }

    public void setRemainingRegenerationTime(long millis)
    {
        long systemMillis = System.currentTimeMillis();
        this.regenCooldownTimestamp = (systemMillis + millis);
        this.lastDtrUpdateTimestamp = (systemMillis + ConfigurationService.DTR_MILLIS_BETWEEN_UPDATES * 2L);
    }

    public RegenStatus getRegenStatus()
    {
        if (getRemainingRegenerationTime() > 0L) {
            return RegenStatus.PAUSED;
        }
        if (getMaximumDeathsUntilRaidable() > this.deathsUntilRaidable) {
            return RegenStatus.REGENERATING;
        }
        return RegenStatus.FULL;
    }

    public void printStats(CommandSender sender)
    {
        Integer combinedKills = Integer.valueOf(0);
        Integer combinedDiamonds = Integer.valueOf(0);
        Long combinedPlaytime = null;
        sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        while (this.members.entrySet().iterator().hasNext())
        {
            Map.Entry entry = (Map.Entry)this.members.entrySet().iterator().next();
            FactionUser user = HCF.getPlugin().getUserManager().getUser((UUID)entry.getKey());
            int kills = user.getKills();
            combinedKills = Integer.valueOf(combinedKills.intValue() + kills);
            int diamonds = user.getDiamondsMined();
            combinedDiamonds = Integer.valueOf(combinedDiamonds.intValue() + diamonds);
            long playTime = BasePlugin.getPlugin().getPlayTimeManager().getTotalPlayTime((UUID)entry.getKey());
            combinedPlaytime = Long.valueOf(combinedPlaytime.longValue() + playTime);
        }
        sender.sendMessage(ChatColor.YELLOW + "  Kills: " + ChatColor.GRAY + combinedKills);
        sender.sendMessage(ChatColor.YELLOW + "  Diamonds: " + ChatColor.GRAY + combinedDiamonds);
        sender.sendMessage(ChatColor.YELLOW + "  PlayTime: " + ChatColor.GRAY + combinedPlaytime);
    }

    public void printDetails(CommandSender sender)
    {
        String leaderName = null;
        HashSet allyNames = new HashSet(1);
        Iterator combinedKills = this.relations.entrySet().iterator();
        while (combinedKills.hasNext())
        {
            Map.Entry memberNames = (Map.Entry)combinedKills.next();
            Faction captainNames = HCF.getPlugin().getFactionManager().getFaction((UUID)memberNames.getKey());
            Faction coNames = HCF.getPlugin().getFactionManager().getFaction((UUID) memberNames.getKey());
            if ((captainNames instanceof PlayerFaction))
            {
                PlayerFaction playerFaction = (PlayerFaction)captainNames;
                allyNames.add(ChatColor.BLUE +playerFaction.getDisplayName(sender) + " " + ChatColor.GRAY + '[' + ChatColor.GRAY + playerFaction.getOnlinePlayers(sender).size() + ChatColor.GRAY + '/' + ChatColor.GRAY + playerFaction.members.size() + ChatColor.GRAY + ']');
            }

            if ((coNames instanceof  PlayerFaction)) {
                PlayerFaction playerFaction = (PlayerFaction) coNames;
                allyNames.add(ChatColor.BLUE + playerFaction.getDisplayName(sender) + " " + ChatColor.GRAY + '[' + ChatColor.GRAY + playerFaction.getOnlinePlayers(sender).size() + ChatColor.GRAY + '/' + ChatColor.GRAY + playerFaction.members.size() + ChatColor.GRAY + ']');
            }
        }
        int combinedKills1 = 0;
        HashSet memberNames1 = new HashSet();
        HashSet<String> captainNames1 = new HashSet();
        HashSet<String> coNames1 = new HashSet<>();
        Iterator playerFaction1 = this.members.entrySet().iterator();
        while (playerFaction1.hasNext())
        {
            Map.Entry entry = (Map.Entry)playerFaction1.next();
            FactionMember factionMember = (FactionMember)entry.getValue();
            Player target = factionMember.toOnlinePlayer();
            FactionUser user = HCF.getPlugin().getUserManager().getUser((UUID)entry.getKey());
            int kills = user.getKills();
            combinedKills1 += kills;
            Deathban deathban = user.getDeathban();
            ChatColor colour = deathban != null && deathban.isActive() ? ChatColor.RED : (target == null || sender instanceof Player && !((Player)sender).canSee(target) ? ChatColor.GRAY : ChatColor.GREEN);
            if ((deathban != null) && (deathban.isActive())) {
                colour = ChatColor.RED;
            } else if ((target != null) && ((!(sender instanceof Player)) || (((Player)sender).canSee(target)))) {
                colour = ChatColor.GREEN;
            } else {
                colour = ChatColor.GRAY;
            }
            String memberName = (Object)colour + factionMember.getName() + (Object)ChatColor.YELLOW + '[' + (Object)ChatColor.GREEN + kills + (Object)ChatColor.YELLOW + ']';
            memberNames1.add(memberName);
            if (factionMember.getRole() == Role.CAPTAIN) {
                captainNames1.add(memberName);
            }

            for (String members : captainNames1) {
                memberNames1.remove(members);
            }
            factionMember.getRole();
            if (factionMember.getRole() == Role.LEADER) {
                leaderName = memberName;
            }
            if (memberNames1.contains(leaderName)) {
                memberNames1.remove(leaderName);
            }
        }
        sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        sender.sendMessage(ChatColor.YELLOW + "Faction Name: " + this.getDisplayName(sender) + ChatColor.GRAY + " [" + this.getOnlineMembers().size() + "/" + this.getMembers().size() + "] " + ChatColor.YELLOW + "Home: " + ChatColor.GRAY + ((this.home == null) ? "No home set!" : (ChatColor.GRAY.toString() + this.home.getLocation().getBlockX() + " | " + this.home.getLocation().getBlockZ())));
        if (!allyNames.isEmpty()) {
            sender.sendMessage((Object) ChatColor.YELLOW + "Allies: " + StringUtils.join(allyNames, new StringBuilder().append(ChatColor.GRAY).append(", ").toString()));
        }
        if (leaderName != null) {
            sender.sendMessage(ChatColor.YELLOW + "Leader: " + ChatColor.RED + leaderName);
        }
        if (!captainNames1.isEmpty()) {
            sender.sendMessage(ChatColor.YELLOW + "Captains: " + ChatColor.RED + StringUtils.join(captainNames1, new StringBuilder().append(ChatColor.GRAY).append(", ").toString()));
        }
        if (!memberNames1.isEmpty()) {
            sender.sendMessage(ChatColor.YELLOW + "Members: " + ChatColor.RED + StringUtils.join(memberNames1, new StringBuilder().append(ChatColor.GRAY).append(", ").toString()));
        }
        sender.sendMessage(ChatColor.YELLOW + "Balance: " + ChatColor.GREEN + '$' + this.balance);
        sender.sendMessage(ChatColor.YELLOW + "Deaths until Raidable: " + this.getRegenStatus().getSymbol() + this.getDtrColour() + JavaUtils.format((Number)this.getDeathsUntilRaidable(false)));
        final long dtrRegenRemaining = this.getRemainingRegenerationTime();
        if (dtrRegenRemaining > 0L) {
            sender.sendMessage(ChatColor.YELLOW + "Time until Regen: " + ChatColor.GREEN + DurationFormatUtils.formatDurationWords(dtrRegenRemaining, true, true));
        }
        sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
    }

    public void broadcast(String message)
    {
        broadcast(message, EMPTY_UUID_ARRAY);
    }

    public void broadcast(String[] messages)
    {
        broadcast(messages, EMPTY_UUID_ARRAY);
    }

    public void broadcast(String message, @Nullable UUID... ignore)
    {
        broadcast(new String[] { message }, ignore);
    }

    public void broadcast(String[] messages, UUID... ignore)
    {
        Preconditions.checkNotNull(messages, "Messages cannot be null");
        Preconditions.checkArgument(messages.length > 0, "Message array cannot be empty");
        Collection<Player> players = getOnlinePlayers();
        Collection<UUID> ignores = ignore.length == 0 ? Collections.emptySet() : Sets.newHashSet(ignore);
        for (Player player : players) {
            if (!ignores.contains(player.getUniqueId())) {
                player.sendMessage(messages);
            }
        }
    }
}
