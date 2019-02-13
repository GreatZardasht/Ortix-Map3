package com.customhcf.hcf;

import com.customhcf.hcf.Utils.*;
import com.customhcf.base.BasePlugin;
import com.customhcf.base.ServerHandler;
import com.customhcf.hcf.balance.*;
import com.customhcf.hcf.classes.bard.EffectRestorer;
import com.customhcf.hcf.classes.PvpClassManager;
import com.customhcf.hcf.classes.archer.ArcherClass;
import com.customhcf.hcf.command.*;
import com.customhcf.hcf.deathban.Deathban;
import com.customhcf.hcf.deathban.DeathbanListener;
import com.customhcf.hcf.deathban.DeathbanManager;
import com.customhcf.hcf.deathban.FlatFileDeathbanManager;
import com.customhcf.hcf.donor.listeners.ScrollsListener;
import com.customhcf.hcf.faction.FactionExecutor;
import com.customhcf.hcf.faction.FactionManager;
import com.customhcf.hcf.faction.FactionMember;
import com.customhcf.hcf.faction.FlatFileFactionManager;
import com.customhcf.hcf.faction.claim.Claim;
import com.customhcf.hcf.faction.claim.ClaimHandler;
import com.customhcf.hcf.faction.claim.ClaimWandListener;
import com.customhcf.hcf.faction.claim.Subclaim;
import com.customhcf.hcf.faction.type.*;
import com.customhcf.hcf.fixes.*;
import com.customhcf.hcf.kothgame.CaptureZone;
import com.customhcf.hcf.kothgame.EventExecutor;
import com.customhcf.hcf.kothgame.EventScheduler;
import com.customhcf.hcf.kothgame.conquest.ConquestExecutor;
import com.customhcf.hcf.kothgame.eotw.EOTWHandler;
import com.customhcf.hcf.kothgame.eotw.EotwCommand;
import com.customhcf.hcf.kothgame.eotw.EotwListener;
import com.customhcf.hcf.kothgame.faction.CapturableFaction;
import com.customhcf.hcf.kothgame.faction.ConquestFaction;
import com.customhcf.hcf.kothgame.faction.KothFaction;
import com.customhcf.hcf.kothgame.glowstone.GlowstoneConfig;
import com.customhcf.hcf.kothgame.glowstone.GlowstoneFaction;
import com.customhcf.hcf.kothgame.koth.KothExecutor;
import com.customhcf.hcf.listener.*;
import com.customhcf.hcf.listener.stats.kills.StatTrackListener;
import com.customhcf.hcf.listener.stats.StatsPickaxeListener;
import com.customhcf.hcf.listener.stats.kills.lore.KillLoreListener;
import com.customhcf.hcf.lives.argument.LivesExecutor;
import com.customhcf.hcf.scoreboard.ScoreboardHandler;
import com.customhcf.hcf.staffmode.cmd.StaffModeCommand;
import com.customhcf.hcf.staffmode.items.*;
import com.customhcf.hcf.staffmode.listener.StaffListener;
import com.customhcf.hcf.tasks.CraftTask;
import com.customhcf.hcf.tasks.DonorTask;
import com.customhcf.hcf.tasks.TabCompleteTask;
import com.customhcf.hcf.timer.SOTWTimer;
import com.customhcf.hcf.timer.TimerExecutor;
import com.customhcf.hcf.timer.TimerManager;
import com.customhcf.hcf.timer.type.PvpProtectionTimer;
import com.customhcf.hcf.timer.type.SotwTimer;
import com.customhcf.hcf.user.FactionUser;
import com.customhcf.hcf.user.UserManager;
import com.customhcf.hcf.visualise.ProtocolLibHook;
import com.customhcf.hcf.visualise.VisualiseHandler;
import com.customhcf.hcf.visualise.WallBorderListener;
import com.customhcf.hcfold.EventSignListener;
import com.customhcf.hcfold.combatlog.CombatLogListener;
import com.customhcf.hcfold.crate.KeyListener;
import com.customhcf.hcfold.crate.KeyManager;
import com.customhcf.hcfold.crate.LootExecutor;
import com.google.common.base.Joiner;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import net.milkbowl.vault.permission.Permission;
import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.*;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class HCF extends JavaPlugin {

    public static Player target;
    public static Permission perms;
    public static ArrayList<Player> toggle = new ArrayList<>();
    public static final Joiner SPACE_JOINER = Joiner.on(' ');
    public static final Joiner COMMA_JOINER = Joiner.on(", ");
    private static final long MINUTE = TimeUnit.MINUTES.toMillis(1);
    private static final long HOUR = TimeUnit.HOURS.toMillis(1);
    private static HCF plugin;
    private Message message;
    public EventScheduler eventScheduler;
    private Random random = new Random();
    public String scoreboardTitle;
    private WorldEditPlugin worldEdit;
    private FoundDiamondsListener foundDiamondsListener;
    private ClaimHandler claimHandler;
    private SotwTimer sotwTimer;
    private KeyManager keyManager;
    private DeathbanManager deathbanManager;
    private EconomyManager economyManager;
    private EOTWHandler eotwHandler;
    private FactionManager factionManager;
    private PvpClassManager pvpClassManager;
    private ScoreboardHandler scoreboardHandler;
    private TimerManager timerManager;
    private UserManager userManager;
    private VisualiseHandler visualiseHandler;
    public String epearl;
    public String ctag;
    public String pvptimer;
    public String log;
    public String stuck;
    public String tele;
    public String armor;
    public static ArrayList<Player> freeze = new ArrayList<>();
    public static ArrayList<String> list = new ArrayList<String>();
    public CombatLogListener combatLogListener;
    public EffectRestorer effectRestorer;

    public static ArrayList<Player> invsee = new ArrayList<>();

    public File rdYML = new File("/data/reclaims.yml");


    public FileConfiguration redeemed = YamlConfiguration.loadConfiguration(this.rdYML);

    public void saveCustomYml(FileConfiguration ymlConfig, File ymlFile)
    {
        try
        {
            ymlConfig.save(ymlFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public static HCF getPlugin() {
        return plugin;
    }

    public static String getReaming(long millis) {
        return HCF.getRemaining(millis, true, true);
    }

    public static String getRemaining(long millis, boolean milliseconds) {
        return HCF.getRemaining(millis, milliseconds, true);
    }

    public static String getRemaining(long duration, boolean milliseconds, boolean trail) {
        if (milliseconds && duration < MINUTE) {
            return (trail ? DateTimeFormats.REMAINING_SECONDS_TRAILING : DateTimeFormats.REMAINING_SECONDS).get().format((double)duration * 0.001) + 's';
        }
        return DurationFormatUtils.formatDuration((long)duration, (String)((duration >= HOUR ? "HH:" : "") + "mm:ss"));
    }

    public void onEnable() {
        plugin = this;
        ProtocolLibHook.hook(this);
        Plugin wep = Bukkit.getPluginManager().getPlugin("WorldEdit");
        this.worldEdit = wep instanceof WorldEditPlugin && wep.isEnabled() ? (WorldEditPlugin) wep : null;
        this.registerConfiguration();
        this.registerCommands();
        this.registerManagers();
        com.customhcf.hcf.kothgame.glowstone.Glowstone.setLocation();
        GlowstoneConfig.init(this);
        Cooldowns.createCooldown("revive_cooldown");
        this.registerListeners();
        Cooldowns.createCooldown("Assassin_item_cooldown");
        Cooldowns.createCooldown("Archer_item_cooldown");
        Cooldowns.createCooldown("report_cooldown");
        Cooldowns.createCooldown("miner_dig_cooldown");
        Cooldowns.createCooldown("focus_cooldown");
        Cooldowns.createCooldown("request_cooldown");
        Cooldowns.createCooldown("spawnscroll");
        getConfig().options().copyDefaults(true);
        saveConfig();
        setupPermissions();
        saveDefaultConfig();
        DonorTask.startDonorTask();
        TabCompleteTask.startTabCompletes();
        TabCompleteTask.stopShowPlugins();
        registerDefaultCommands();
        CraftTask.gMelon();
        ShapedRecipe cmelon = new ShapedRecipe(new ItemStack(Material.SPECKLED_MELON, 1));
        cmelon.shape(new String[] { "AAA", "CBA", "AAA" }).setIngredient('B', Material.MELON).setIngredient('C', Material.GOLD_NUGGET);
        Bukkit.getServer().addRecipe(cmelon);
        setupLanguage();
    }


    private void saveData() {
        this.deathbanManager.saveDeathbanData();
        this.economyManager.saveEconomyData();
        this.factionManager.saveFactionData();
        this.keyManager.saveKeyData();
        this.userManager.saveUserData();
    }

    public void onDisable() {
        combatLogListener.removeCombatLoggers();
        this.pvpClassManager.onDisable();
        this.scoreboardHandler.clearBoards();
        this.saveData();
        plugin = null;
        saveConfig();
        Bukkit.getServer().clearRecipes();

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (toggle.contains(player)) {
                toggle.remove(player);
                player.getInventory().clear();
                player.sendMessage(ChatColor.RED + "Server restarted so you were kicked out of modmode!");
            }
        }

    }

    public void setupLanguage()
    {
        File localFile = new File(getDataFolder(), "lang.yml");
        if (!localFile.exists()) {
            try
            {
                getDataFolder().mkdir();
                localFile.createNewFile();
            }
            catch (IOException localIOException1)
            {
                getLogger().severe("Error: " + localIOException1.getMessage());
            }
        }
        try
        {
            YamlConfiguration localYamlConfiguration = YamlConfiguration.loadConfiguration(localFile);
            Lang[] arrayOfLanguage;
            int j = (arrayOfLanguage = Lang.values()).length;
            for (int i = 0; i < j; i++)
            {
                Lang localLanguage = arrayOfLanguage[i];
                if (localYamlConfiguration.getString(localLanguage.getPath()) == null) {
                    localYamlConfiguration.set(localLanguage.getPath(), localLanguage.getValue());
                }
            }
            localYamlConfiguration.save(localFile);

            Lang.setLangFile(localYamlConfiguration);
        }
        catch (IOException localIOException2)
        {
            getLogger().severe("Error: " + localIOException2.getMessage());
        }
    }

    private void registerConfiguration() {
        ConfigurationSerialization.registerClass((Class)CaptureZone.class);
        ConfigurationSerialization.registerClass((Class)Deathban.class);
        ConfigurationSerialization.registerClass((Class)Claim.class);
        ConfigurationSerialization.registerClass((Class)Subclaim.class);
        ConfigurationSerialization.registerClass((Class)Deathban.class);
        ConfigurationSerialization.registerClass((Class)FactionUser.class);
        ConfigurationSerialization.registerClass((Class)ClaimableFaction.class);
        ConfigurationSerialization.registerClass((Class)ConquestFaction.class);
        ConfigurationSerialization.registerClass((Class)CapturableFaction.class);
        ConfigurationSerialization.registerClass((Class)KothFaction.class);
        ConfigurationSerialization.registerClass((Class)EndPortalFaction.class);
        ConfigurationSerialization.registerClass((Class)Faction.class);
        ConfigurationSerialization.registerClass((Class)FactionMember.class);
        ConfigurationSerialization.registerClass((Class)PlayerFaction.class);
        ConfigurationSerialization.registerClass((Class)RoadFaction.class);
        ConfigurationSerialization.registerClass((Class)RoadFaction.class);
        ConfigurationSerialization.registerClass((Class)SpawnFaction.class);
        ConfigurationSerialization.registerClass((Class)RoadFaction.NorthRoadFaction.class);
        ConfigurationSerialization.registerClass((Class)RoadFaction.EastRoadFaction.class);
        ConfigurationSerialization.registerClass((Class)RoadFaction.SouthRoadFaction.class);
        ConfigurationSerialization.registerClass((Class)RoadFaction.WestRoadFaction.class);
        ConfigurationSerialization.registerClass(GlowstoneFaction.class);

    }

    private void registerListeners() {
        PluginManager manager = this.getServer().getPluginManager();

        manager.registerEvents((Listener) this, this);
        manager.registerEvents(new DonorOnlyListener(), (Plugin)this);
        manager.registerEvents(new ArcherClass(this), (Plugin)this);
        manager.registerEvents(new PortalTrapFixListener(), (Plugin)this);
        manager.registerEvents(new KeyListener(this), (Plugin)this);
        manager.registerEvents(new WeatherFixListener(), (Plugin)this);
        manager.registerEvents(new NoPermissionClickListener(), (Plugin)this);
        manager.registerEvents(new AutoSmeltOreListener(), (Plugin)this);
        manager.registerEvents(new BlockHitFixListener(), (Plugin)this);
        manager.registerEvents(new BlockJumpGlitchFixListener(), (Plugin)this);
        manager.registerEvents(new BoatGlitchFixListener(), (Plugin)this);
        manager.registerEvents(new BookDeenchantListener(), (Plugin)this);
        manager.registerEvents(new BorderListener(), (Plugin)this);
        manager.registerEvents(new ChatListener(this), (Plugin)this);
        manager.registerEvents(new ClaimWandListener(this), (Plugin)this);
        manager.registerEvents(new CoreListener(this), (Plugin)this);
        manager.registerEvents((Listener)new DeathListener(this), (Plugin)this);
        manager.registerEvents((Listener)new DeathMessageListener(this), (Plugin)this);
        manager.registerEvents((Listener)new DeathSignListener(this), (Plugin)this);
        manager.registerEvents((Listener)new DeathbanListener(this), (Plugin)this);
        manager.registerEvents((Listener)new EnchantLimitListener(), (Plugin)this);
        manager.registerEvents((Listener)new EnderChestRemovalListener(), (Plugin)this);
        manager.registerEvents((Listener)new EntityLimitListener(), (Plugin)this);
        manager.registerEvents((Listener)new FlatFileFactionManager(this), (Plugin)this);
        manager.registerEvents((Listener)new EndListener(), (Plugin)this);
        manager.registerEvents((Listener)new EotwListener(this), (Plugin)this);
        manager.registerEvents((Listener)new EventSignListener(), (Plugin)this);
        manager.registerEvents((Listener)new ExpMultiplierListener(), (Plugin)this);
        manager.registerEvents((Listener)new FactionListener(this), (Plugin)this);
        this.foundDiamondsListener = new FoundDiamondsListener(this);
        manager.registerEvents(new GlowstoneListener(), this);
        manager.registerEvents((Listener)this.foundDiamondsListener, (Plugin)this);
        manager.registerEvents((Listener)new FurnaceSmeltSpeederListener(), (Plugin)this);
        manager.registerEvents((Listener)new InfinityArrowFixListener(), (Plugin)this);
        manager.registerEvents((Listener)new KitListener(this), (Plugin)this);
        manager.registerEvents((Listener)new ServerSecurityListener(), (Plugin)this);
        manager.registerEvents((Listener)new PhaseListener(), (Plugin)this);
        manager.registerEvents((Listener)new HungerFixListener(), (Plugin)this);
        manager.registerEvents((Listener)new PearlGlitchListener(this), (Plugin)this);
        manager.registerEvents((Listener)new PotionLimitListener(), (Plugin)this);
        manager.registerEvents((Listener)new FactionsCoreListener(this), (Plugin)this);
        manager.registerEvents((Listener)new SignSubclaimListener(this), (Plugin)this);
        manager.registerEvents((Listener)new ShopSignListener(this), (Plugin)this);
        manager.registerEvents((Listener)new SkullListener(), (Plugin)this);
        manager.registerEvents((Listener)new BeaconStrengthFixListener(), (Plugin)this);
        manager.registerEvents((Listener)new VoidGlitchFixListener(), (Plugin)this);
        manager.registerEvents((Listener)new WallBorderListener(this), (Plugin)this);
        manager.registerEvents((Listener)new WorldListener(this), (Plugin)this);
        manager.registerEvents((Listener)new UnRepairableListener(), (Plugin)this);
        manager.registerEvents(new SotwListener(this), (Plugin)this);
        manager.registerEvents(new StatTrackListener(), this);
        manager.registerEvents(new HelpCommand(), this);
        manager.registerEvents(new CobbleListener(), this);
        manager.registerEvents(new FirstJoinListener(), this);
        manager.registerEvents(new CreeperFriendlyListener(), this);
        manager.registerEvents(new ElevatorClass(this), this);
        manager.registerEvents(new StatsPickaxeListener(), this);
        manager.registerEvents(new BlacklistCommand(), this);
        manager.registerEvents(new CrashFixListener(), this);
        manager.registerEvents(new AntiDupeListener(), this);
        manager.registerEvents(combatLogListener = new CombatLogListener(this), this);
        manager.registerEvents(new StrengthListener(), this);
        manager.registerEvents(new CrowbarListener(this), this);
        manager.registerEvents(new FocusListener(), this);
        manager.registerEvents(new KillLoreListener(), this);
        manager.registerEvents(new KothCapListener(), this);
        manager.registerEvents(new BottleExpListener(), this);
        manager.registerEvents(new ScrollsListener(), this);

        // STAFFMODE LISTENERS
        manager.registerEvents(new ExamineListener(), this);
        manager.registerEvents(new FreezeListener(), this);
        manager.registerEvents(new RandomTeleportListener(), this);
        manager.registerEvents(new StaffOnlineListener(), this);
        manager.registerEvents(new StaffListener(), this);

    }

    private void registerDefaultCommands() {
        this.getCommand("logout").setExecutor((CommandExecutor)new LogoutCommand(this));
        this.getCommand("pay").setExecutor((CommandExecutor)new PayCommand(this));
        this.getCommand("ores").setExecutor(new OresCommand());
        this.getCommand("help").setExecutor(new HelpCommand());
        this.getCommand("coords").setExecutor(new CoordsCommand());
        this.getCommand("pvptimer").setExecutor(new PvpTimerCommand(this));
        this.getCommand("stats").setExecutor(new InfoCommand());
        this.getCommand("cobble").setExecutor(new CobbleCommand());
        this.getCommand("report").setExecutor(new ReportCommand());
        this.getCommand("request").setExecutor(new RequestCommand());
        getCommand("mapkit").setExecutor(new MapKitCommand());
        this.getCommand("focus").setExecutor(new FocusCommand());
        this.getCommand("who").setExecutor(new WhoCommand());
        getCommand("ping").setExecutor(new PingCommand());
        this.getCommand("teamspeak").setExecutor(new TeamspeakCommand());
        getCommand("bottle").setExecutor(new BottleXPCommand());
        getCommand("grant").setExecutor(new GrantProtectionCommand());
        getCommand("scrolls").setExecutor(new ScrollsCommand());
        getCommand("panic").setExecutor(new PanicCommand());
    }

    private void registerCommands() {
        this.getCommand("sotw").setExecutor(new SotwCommand(this));
        this.getCommand("conquest").setExecutor((CommandExecutor)new ConquestExecutor(this));
        this.getCommand("economy").setExecutor((CommandExecutor)new EconomyCommand(this));
        this.getCommand("eotw").setExecutor((CommandExecutor)new EotwCommand(this));
        this.getCommand("serverevents").setExecutor((CommandExecutor)new EventExecutor(this));
        this.getCommand("faction").setExecutor((CommandExecutor)new FactionExecutor(this));
        this.getCommand("koth").setExecutor((CommandExecutor)new KothExecutor(this));
        this.getCommand("timer").setExecutor((CommandExecutor)new TimerExecutor(this));
        this.getCommand("reclaim").setExecutor(new ReclaimCommand());
        this.getCommand("staffrevive").setExecutor(new StaffReviveCommand(this));
        this.getCommand("revive").setExecutor(new ReviveCommand(this));
        this.getCommand("lives").setExecutor(new LivesExecutor(this));
        this.getCommand("spawn").setExecutor(new EzSpawnCommand());
        this.getCommand("blacklist").setExecutor(new BlacklistCommand());
        this.getCommand("unblacklist").setExecutor(new BlacklistCommand());
        this.getCommand("setborder").setExecutor(new SetBorderCommand());
        this.getCommand("toggleend").setExecutor(new ToggleEnd(this));
        this.getCommand("refund").setExecutor(new RestoreInventoryCommand());
        this.getCommand("staffchat").setExecutor(new StaffChatCommand());
        this.getCommand("mutechat").setExecutor(new MuteChatCommand());
        this.getCommand("vanish").setExecutor(new com.customhcf.hcf.command.VanishCommand());
        this.getCommand("unmutechat").setExecutor(new MuteChatCommand());
        this.getCommand("crowbar").setExecutor(new CrowbarCommand());
        this.getCommand("feedall").setExecutor(new FeedAllCommand());
        this.getCommand("keyall").setExecutor(new KeyAllCommand());
        getCommand("commandtoggle").setExecutor(new ToggleCommandCommand());
        getCommand("setendexit").setExecutor(new EndCommand());
        getCommand("setendentrance").setExecutor(new EndCommand());
        getCommand("staffmode").setExecutor(new StaffModeCommand());
        getCommand("key").setExecutor(new LootExecutor(this));
        getCommand("setspawn").setExecutor(new EzSpawnCommand());

        final Map<String, Map<String, Object>> map = (Map<String, Map<String, Object>>)this.getDescription().getCommands();
        for (final Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
            final PluginCommand command = this.getCommand((String)entry.getKey());
            command.setPermission("command." + entry.getKey());
            command.setPermissionMessage(ChatColor.RED + "You do not have permission for this command.");
        }
    }

    private void registerManagers() {
        this.claimHandler = new ClaimHandler(this);
        this.deathbanManager = new FlatFileDeathbanManager(this);
        this.economyManager = new FlatFileEconomyManager(this);
        this.eotwHandler = new EOTWHandler(this);
        this.eventScheduler = new EventScheduler(this);
        this.factionManager = new FlatFileFactionManager(this);
        this.pvpClassManager = new PvpClassManager(this);
        this.timerManager = new TimerManager(this);
        this.scoreboardHandler = new ScoreboardHandler(this);
        this.userManager = new UserManager(this);
        this.visualiseHandler = new VisualiseHandler();
        this.sotwTimer = new SotwTimer();
        this.keyManager = new KeyManager(this);
        this.message = new Message(this);

    }


    public Message getMessage() {
        return this.message;
    }

    public ServerHandler getServerHandler() {
        return BasePlugin.getPlugin().getServerHandler();
    }

    public Random getRandom() {
        return this.random;
    }

    public WorldEditPlugin getWorldEdit() {
        return this.worldEdit;
    }

    public KeyManager getKeyManager() {
        return this.keyManager;
    }

    public ClaimHandler getClaimHandler() {
        return this.claimHandler;
    }

    public DeathbanManager getDeathbanManager() {
        return this.deathbanManager;
    }

    public EconomyManager getEconomyManager() {
        return this.economyManager;
    }
    
    public EOTWHandler getEotwHandler() {
        return this.eotwHandler;
    }

    public FactionManager getFactionManager() {
        return this.factionManager;
    }
    
    public PvpClassManager getPvpClassManager() {
        return this.pvpClassManager;
    }

    public ScoreboardHandler getScoreboardHandler() {
        return this.scoreboardHandler;
    }

    public TimerManager getTimerManager() {
        return this.timerManager;
    }

    public UserManager getUserManager() {
        return this.userManager;
    }

    public VisualiseHandler getVisualiseHandler() {
        return this.visualiseHandler;
    }

    public SotwTimer getSotwTimer() {
        return this.sotwTimer;
    }

    public CombatLogListener getCombatLogListener() {
        return this.combatLogListener;
    }

    private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = (Permission)rsp.getProvider();
        return perms != null;
    }
    public EffectRestorer getEffectRestorer() {
        return this.effectRestorer;
    }


}

