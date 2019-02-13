/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.block.Sign
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.PluginCommand
 *  org.bukkit.configuration.serialization.ConfigurationSerialization
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package com.customhcf.base;

import com.customhcf.base.command.CommandManager;
import com.customhcf.base.command.SimpleCommandManager;
import com.customhcf.base.command.module.ChatModule;
import com.customhcf.base.command.module.EssentialModule;
import com.customhcf.base.command.module.InventoryModule;
import com.customhcf.base.command.module.TeleportModule;
import com.customhcf.base.kit.FlatFileKitManager;
import com.customhcf.base.kit.Kit;
import com.customhcf.base.kit.KitExecutor;
import com.customhcf.base.kit.KitListener;
import com.customhcf.base.kit.KitManager;
import com.customhcf.base.listener.ChatListener;
import com.customhcf.base.listener.ColouredSignListener;
import com.customhcf.base.listener.DecreasedLagListener;
import com.customhcf.base.listener.JoinListener;
import com.customhcf.base.listener.MoveByBlockEvent;
import com.customhcf.base.listener.NameVerifyListener;
import com.customhcf.base.listener.PlayerLimitListener;
import com.customhcf.base.listener.VanishListener;
import com.customhcf.base.task.AnnouncementHandler;
import com.customhcf.base.task.AutoRestartHandler;
import com.customhcf.base.task.ClearEntityHandler;
import com.customhcf.base.user.BaseUser;
import com.customhcf.base.user.ConsoleUser;
import com.customhcf.base.user.NameHistory;
import com.customhcf.base.user.ServerParticipator;
import com.customhcf.base.user.UserManager;
import com.customhcf.base.warp.FlatFileWarpManager;
import com.customhcf.base.warp.Warp;
import com.customhcf.base.warp.WarpManager;
import com.customhcf.util.PersistableLocation;
import com.customhcf.util.RandomUtils;
import com.customhcf.util.SignHandler;
import com.customhcf.util.bossbar.BossBarManager;
import com.customhcf.util.chat.Lang;
import com.customhcf.util.cuboid.Cuboid;
import com.customhcf.util.cuboid.NamedCuboid;
import com.customhcf.util.itemdb.ItemDb;
import com.customhcf.util.itemdb.SimpleItemDb;
import java.io.IOException;
import java.util.Random;

import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class BasePlugin
extends JavaPlugin {
    private static BasePlugin plugin;
    private ItemDb itemDb;
    private Random random = new Random();
    private WarpManager warpManager;
    private RandomUtils randomUtils;
    private AutoRestartHandler autoRestartHandler;
    public BukkitRunnable clearEntityHandler;
    public BukkitRunnable announcementTask;
    private CommandManager commandManager;
    private KitManager kitManager;
    private PlayTimeManager playTimeManager;
    private ServerHandler serverHandler;
    private SignHandler signHandler;
    private UserManager userManager;
    private KitExecutor kitExecutor;

    public static BasePlugin getPlugin() {
        return plugin;
    }

    public void onEnable() {
        plugin = this;
        ConfigurationSerialization.registerClass((Class)Warp.class);
        ConfigurationSerialization.registerClass((Class)ServerParticipator.class);
        ConfigurationSerialization.registerClass((Class)BaseUser.class);
        ConfigurationSerialization.registerClass((Class)ConsoleUser.class);
        ConfigurationSerialization.registerClass((Class)NameHistory.class);
        ConfigurationSerialization.registerClass((Class)PersistableLocation.class);
        ConfigurationSerialization.registerClass((Class)Cuboid.class);
        ConfigurationSerialization.registerClass((Class)NamedCuboid.class);
        ConfigurationSerialization.registerClass((Class)Kit.class);
        this.registerManagers();
        this.registerCommands();
        this.registerListeners();
        this.reloadSchedulers();
        Plugin plugin = this.getServer().getPluginManager().getPlugin("ProtocolLib");
        if (plugin != null && plugin.isEnabled()) {
            try {
                ProtocolHook.hook(this);
            }
            catch (Exception ex) {
                this.getLogger().severe("Error hooking into ProtocolLib from Base.");
                ex.printStackTrace();
            }
        }
    }

    public void onDisable() {
        super.onDisable();
        BossBarManager.unhook();
        this.kitManager.saveKitData();
        this.playTimeManager.savePlaytimeData();
        this.serverHandler.saveServerData();
        this.signHandler.cancelTasks(null);
        this.userManager.saveParticipatorData();
        this.warpManager.saveWarpData();
        plugin = null;
    }

    private void registerManagers() {
        BossBarManager.hook();
        this.randomUtils = new RandomUtils();
        this.autoRestartHandler = new AutoRestartHandler(this);
        this.kitManager = new FlatFileKitManager(this);
        this.serverHandler = new ServerHandler(this);
        this.signHandler = new SignHandler(this);
        this.userManager = new UserManager(this);
        this.itemDb = new SimpleItemDb(this);
        this.warpManager = new FlatFileWarpManager(this);
        try {
            Lang.initialize("en_US");
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void registerCommands() {
        this.commandManager = new SimpleCommandManager(this);
        this.commandManager.registerAll(new ChatModule(this));
        this.commandManager.registerAll(new EssentialModule(this));
        this.commandManager.registerAll(new InventoryModule(this));
        this.commandManager.registerAll(new TeleportModule(this));
        this.kitExecutor = new KitExecutor(this);
        this.getCommand("kit").setExecutor((CommandExecutor)this.kitExecutor);
    }

    public KitExecutor getKitExecutor() {
        return this.kitExecutor;
    }

    private void registerListeners() {
        PluginManager manager = this.getServer().getPluginManager();
        manager.registerEvents((Listener)new ChatListener(this), (Plugin)this);
        manager.registerEvents((Listener)new ColouredSignListener(), (Plugin)this);
        manager.registerEvents((Listener)new DecreasedLagListener(this), (Plugin)this);
        manager.registerEvents((Listener)new JoinListener(this), (Plugin)this);
        manager.registerEvents((Listener)new KitListener(this), (Plugin)this);
        manager.registerEvents((Listener)new MoveByBlockEvent(), (Plugin)this);
      //  manager.registerEvents((Listener)new MobstackListener(this), (Plugin)this);
        manager.registerEvents((Listener)new NameVerifyListener(this), (Plugin)this);
        this.playTimeManager = new PlayTimeManager(this);
        manager.registerEvents((Listener)this.playTimeManager, (Plugin)this);
        manager.registerEvents((Listener)new PlayerLimitListener(), (Plugin)this);
        manager.registerEvents((Listener)new VanishListener(this), (Plugin)this);
    }

    public void reloadSchedulers() {
        ClearEntityHandler clearEntityHandler;
        AnnouncementHandler announcementTask;
        if (this.clearEntityHandler != null) {
            this.clearEntityHandler.cancel();
        }
        if (this.announcementTask != null) {
            this.announcementTask.cancel();
        }
        long announcementDelay = (long)this.serverHandler.getAnnouncementDelay() * 20;
        long claggdelay = (long)this.serverHandler.getClaggDelay() * 20;
        this.announcementTask = announcementTask = new AnnouncementHandler(this);
        this.clearEntityHandler = clearEntityHandler = new ClearEntityHandler();
        clearEntityHandler.runTaskTimer((Plugin)this, claggdelay, claggdelay);
        announcementTask.runTaskTimer((Plugin)this, announcementDelay, announcementDelay);
    }

    public WarpManager getWarpManager() {
        return this.warpManager;
    }

    public RandomUtils getRandomUtils() {
        return this.randomUtils;
    }

    public Random getRandom() {
        return this.random;
    }

    public AutoRestartHandler getAutoRestartHandler() {
        return this.autoRestartHandler;
    }

    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    public ItemDb getItemDb() {
        return this.itemDb;
    }

    public KitManager getKitManager() {
        return this.kitManager;
    }

    public PlayTimeManager getPlayTimeManager() {
        return this.playTimeManager;
    }

    public ServerHandler getServerHandler() {
        return this.serverHandler;
    }

    public SignHandler getSignHandler() {
        return this.signHandler;
    }

    public UserManager getUserManager() {
        return this.userManager;
    }
}

