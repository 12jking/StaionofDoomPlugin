package de.j.stationofdoom.main;

import de.j.deathMinigames.commands.GameCMD;
import de.j.deathMinigames.commands.LeaderboardCMD;
import de.j.deathMinigames.listeners.*;
import de.j.deathMinigames.main.Config;
import de.j.deathMinigames.database.Database;
import de.j.deathMinigames.main.HandlePlayers;
import de.j.deathMinigames.main.InitWaitingListLocationOnJoin;
import de.j.stationofdoom.cmd.*;
import de.j.stationofdoom.crafting.DebugStick;
import de.j.stationofdoom.enchants.CustomEnchants;
import de.j.stationofdoom.enchants.FlightEvents;
import de.j.stationofdoom.enchants.FurnaceEvents;
import de.j.stationofdoom.enchants.TelepathyEvents;
import de.j.stationofdoom.listener.*;
import de.j.stationofdoom.util.EntityManager;
import de.j.stationofdoom.util.translations.ChangeLanguageGUI;
import de.j.stationofdoom.util.translations.LanguageChanger;
import de.j.stationofdoom.util.translations.TranslationFactory;
import de.j.stationofdoom.util.WhoIsOnline;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.logging.Logger;

public final class Main extends JavaPlugin {


    private static Main plugin;

    public static String version;

    @Override
    public void onLoad() {
        plugin = this;

        InputStreamReader in = new InputStreamReader(Objects.requireNonNull(Main.class.getResourceAsStream("/plugin.yml")));
        BufferedReader reader = new BufferedReader(in);

        try {
            String line;
            int lineNumber = 1;

            while ((line = reader.readLine()) != null) {
                if (lineNumber == 2) {
                    version = line.replace("version: ", "");
                    break;
                }
                lineNumber++;
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        Config config = Config.getInstance();
        config.initializeConfig();
        Database database = Database.getInstance();
        database.initDatabase();
        HandlePlayers.initKnownPlayersPlayerData();


        LifecycleEventManager<Plugin> manager = getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands COMMANDS = event.registrar();
            COMMANDS.register("afk", "", new StatusCMD());
            COMMANDS.register("ping", new PingCommand());
            COMMANDS.register("plversion", new VersionCMD());
            COMMANDS.register("language", new ChangeLanguageCMD());
            COMMANDS.register("deathpoint", new DeathPointCMD());
            COMMANDS.register("customenchant", new GetCustomEnchantsCMD());
            COMMANDS.register("voterestart", new VoteRestartCMD());
            COMMANDS.register("sit", new PlayerSitListener());
            COMMANDS.register("game", "game related commands", new GameCMD());
            COMMANDS.register("leaderboard", "showing the leaderboard of the minigame", new LeaderboardCMD());
        });

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new Bed(), this);
        pluginManager.registerEvents(new PlayerJoin(), this);
        pluginManager.registerEvents(new PlayerQuit(), this);
        pluginManager.registerEvents(new PlayerKillListener(), this);
        pluginManager.registerEvents(new ChatMessagesListener(), this);
        pluginManager.registerEvents(new EntityDeathListener(), this);
        pluginManager.registerEvents(new PlayerSitListener(), this);
        pluginManager.registerEvents(new PlayerSitListener(), this);
        pluginManager.registerEvents(new DeathPointCMD(), this);
        pluginManager.registerEvents(new TelepathyEvents(), this);
        pluginManager.registerEvents(new FlightEvents(), this);
        pluginManager.registerEvents(new AntiSwordDropListener(), this);
        pluginManager.registerEvents(new FurnaceEvents(), this);
        pluginManager.registerEvents(new ChangeLanguageGUI(), this);
        pluginManager.registerEvents(new BowComboListener(), this);
        pluginManager.registerEvents(new SaveItemsOnDeath(), this);
        pluginManager.registerEvents(new RespawnListener(), this);
        pluginManager.registerEvents(new JoinListener(), this);
        pluginManager.registerEvents(new InventoryListener(), this);
        pluginManager.registerEvents(new InitWaitingListLocationOnJoin(), this);
        pluginManager.registerEvents(new LeaveListener(), this);
        pluginManager.registerEvents(new AnvilListener(), this);

        getServer().addRecipe(new DebugStick());

        //CustomEnchants.register(); -> see custom enchants class for more info

        WhoIsOnline.init();

        TranslationFactory.initTranslations();
        LanguageChanger.init();
    }

    @Override
    public void onDisable() {
        EntityManager.removeOldEntities();
        WhoIsOnline.shutdown();
        HandlePlayers.copyAllPlayerDataIntoDatabase();
    }

    public static Main getPlugin(){
        return plugin;
    }

    public static Logger getMainLogger() {
        return getPlugin().getLogger();
    }

    public static boolean isFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static AsyncScheduler getAsyncScheduler() {
        return getPlugin().getServer().getAsyncScheduler();
    }

    public static GlobalRegionScheduler getGlobalRegionScheduler() {
        return getPlugin().getServer().getGlobalRegionScheduler();
    }

}
