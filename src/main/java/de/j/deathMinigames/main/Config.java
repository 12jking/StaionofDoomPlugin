package de.j.deathMinigames.main;

import de.j.stationofdoom.enchants.CustomEnchantsEnum;
import de.j.stationofdoom.util.Tablist;
import org.bukkit.Location;

import de.j.stationofdoom.main.Main;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class Config {
    private volatile static Config instance;

    private volatile static boolean configSetUp;
    private volatile static int configParkourStartHeight;
    private final static int configParkourStartHeightDefault = 100;
    private volatile static int configParkourLength;
    private final static int configParkourLengthDefault = 10;
    private volatile static int configCostToLowerTheDifficulty;
    private final static int configCostToLowerTheDifficultyDefault = 6;
    private volatile static int configTimeToDecideWhenRespawning;
    private final static int configTimeToDecideWhenRespawningDefault = 10;

    private volatile static Location configWaitingListPosition;

    private Config(){}

    /**
     * Returns the single instance of this class.
     *
     * <p>This class is a singleton, meaning that only one instance of this class
     * will ever exist. This method will return the same instance every time it is
     * called.
     *
     * @return The single instance of this class.
     */
    public synchronized static Config getInstance(){
        if(instance == null){
            synchronized (Config.class){
                if (instance == null){
                    instance = new Config();
                }
            }
        }
        return instance;
    }

    /**
     * Initializes the configuration of the plugin.
     *
     * <p>This method sets up the default values in the configuration file of the plugin.
     * If the configuration file does not already contain the "KnownPlayers" key, it creates
     * a new empty list and saves the configuration file.
     *
     * <p>Finally, it calls the {@link #cloneConfigToPlugin()} method to copy the values
     * from the configuration file to the plugin's internal state.
     */
    public void initializeConfig() {
        cloneConfigToPlugin();
    }

    /**
     * Copies configuration values from the plugin's configuration file to the internal state.
     *
     * <p>This method checks for the presence of various configuration keys in the plugin's
     * configuration file and sets the corresponding internal state values. If a key is not
     * present, a default value is used.
     *
     * <ul>
     *   <li><b>SetUp</b>: If present, sets the setup state; defaults to false.</li>
     *   <li><b>ParkourStartHeight</b>: If present, sets the parkour start height; defaults to 100.</li>
     *   <li><b>ParkourLength</b>: If present, sets the parkour length; defaults to 10.</li>
     *   <li><b>CostToLowerTheDifficulty</b>: If present, sets the cost to lower the difficulty; defaults to 6.</li>
     *   <li><b>TimeToDecideWhenRespawning</b>: If present, sets the time to decide when respawning; defaults to 10.</li>
     *   <li><b>WaitingListPosition</b>: If present, sets the waiting list position.</li>
     * </ul>
     */
    public void cloneConfigToPlugin() {
        FileConfiguration config = Main.getPlugin().getConfig();
        handleCloneSetUp(config);
        handleCloneDatabase(config);
        handleCloneTablist(config);
        handleCloneCustomEnchants(config);
    }

    private void handleCloneSetUp(FileConfiguration config) {
        boolean setUp = true;
        if(config.contains("ParkourStartHeight")) {
            setParkourStartHeight(config.getInt("ParkourStartHeight"));
        }
        else {
            setParkourStartHeight(configParkourStartHeightDefault);
            setUp = false;
        }
        if(config.contains("ParkourLength")) {
            setParkourLength(config.getInt("ParkourLength"));
        }
        else {
            setParkourLength(configParkourLengthDefault);
            setUp = false;
        }
        if(config.contains("CostToLowerTheDifficulty")) {
            setCostToLowerTheDifficulty(config.getInt("CostToLowerTheDifficulty"));
        }
        else {
            setCostToLowerTheDifficulty(configCostToLowerTheDifficultyDefault);
            setUp = false;
        }
        if(config.contains("TimeToDecideWhenRespawning")) {
            setTimeToDecideWhenRespawning(config.getInt("TimeToDecideWhenRespawning"));
        }
        else {
            setTimeToDecideWhenRespawning(configTimeToDecideWhenRespawningDefault);
            setUp = false;
        }
        configSetUp = setUp;
    }

    private void handleCloneDatabase(FileConfiguration config) {
        if(!config.contains("Database")) {
            config.set("Database.host", "default");
            config.set("Database.port", "default");
            config.set("Database.user", "default");
            config.set("Database.database", "default");
            config.set("Database.password", "default");
            config.set("Database.applicationName", "default");
            config.set("Database.schema", "public");
            Main.getPlugin().saveConfig();
        }
    }

    private void handleCloneTablist(FileConfiguration config) {
        if(config.contains("Tablist")) {
            if(config.contains("Tablist.ServerName") && config.get("Tablist.ServerName") != null) {
                Tablist.setServerName(config.getString("Tablist.ServerName"));
            }
            if(config.contains("Tablist.HostedBy") && config.get("Tablist.HostedBy") != null) {
                Tablist.setHostedBy(config.getString("Tablist.HostedBy"));
            }
        }
    }

    private void handleCloneCustomEnchants(FileConfiguration config) {
        if(config.contains("CustomEnchants")) {
            boolean customEnchantBool;
            for (CustomEnchantsEnum customEnchantsEnum : CustomEnchantsEnum.values() ) {
                try {
                    customEnchantBool = config.getBoolean("CustomEnchants." + customEnchantsEnum.name());
                    customEnchantsEnum.setEnabled(customEnchantBool);
                }
                catch (Exception e) {
                    Main.getMainLogger().warning("Setting value for custom enchantment " + customEnchantsEnum.name() + " failed. Falling back to default true value.");
                    e.printStackTrace();
                    customEnchantsEnum.setEnabled(true);
                }
            }
        }
        else {
            config.createSection("CustomEnchants");
            for (CustomEnchantsEnum customEnchantsEnum : CustomEnchantsEnum.values() ) {
                config.set("CustomEnchants." + customEnchantsEnum.name(), true);
                customEnchantsEnum.setEnabled(true);
            }
            Main.getPlugin().saveConfig();
        }
    }

    public void cloneWaitingListLocationToPlugin(World world) {
        if(world == null) {
            Main.getMainLogger().severe("Ca not set waitingListLocation because world is null!");
            return;
        }
        if(Main.getPlugin().getConfig().contains("WaitingListPosition")) {
            int x = Main.getPlugin().getConfig().getInt("WaitingListPosition.x");
            int y = Main.getPlugin().getConfig().getInt("WaitingListPosition.y");
            int z = Main.getPlugin().getConfig().getInt("WaitingListPosition.z");
            configWaitingListPosition = new Location(world, x, y, z);
            Main.getMainLogger().info("set WaitingListPosition from config to: " + configWaitingListPosition.getBlockX() + ", " + configWaitingListPosition.getBlockY() + ", " + configWaitingListPosition.getBlockZ());
        }
        else {
            Main.getMainLogger().warning("WaitingListPosition not found in config!");
        }
    }

    /**
     * Sets the height at which the parkour minigame starts.
     * @param height The height at which the parkour minigame starts.
     */
    public synchronized void setParkourStartHeight(int height) {
        configParkourStartHeight = height;
        Main.getPlugin().getConfig().set("ParkourStartHeight", height);
        Main.getPlugin().saveConfig();
    }

    /**
     * Sets the length of the parkour course.
     *
     * <p>This method updates the internal configuration state and the
     * plugin's configuration file with the specified parkour length.
     * The configuration file is saved after updating.
     *
     * @param length The length of the parkour course to be set.
     */
    public synchronized void setParkourLength(int length) {
        configParkourLength = length;
        Main.getPlugin().getConfig().set("ParkourLength", length);
        Main.getPlugin().saveConfig();
    }

    /**
     * Sets the cost to lower the difficulty in diamonds.
     *
     * <p>This method updates the internal configuration state and the plugin's
     * configuration file with the specified cost to lower the difficulty.
     * The configuration file is saved after updating.
     *
     * @param cost The cost to lower the difficulty in diamonds.
     */
    public synchronized void setCostToLowerTheDifficulty(int cost) {
        
        configCostToLowerTheDifficulty = cost;
        Main.getPlugin().getConfig().set("CostToLowerTheDifficulty", cost);
        Main.getPlugin().saveConfig();
    }

    /**
     * Sets the time limit for minigame decision in seconds.
     *
     * <p>This method updates the internal configuration state and the plugin's
     * configuration file with the specified time limit for minigame decision.
     * The configuration file is saved after updating.
     *
     * @param time The time limit for minigame decision in seconds to be set.
     */
    public synchronized void setTimeToDecideWhenRespawning(int time) {
        configTimeToDecideWhenRespawning = time;
        Main.getPlugin().getConfig().set("TimeToDecideWhenRespawning", time);
        Main.getPlugin().saveConfig();
    }

    /**
     * Sets the waiting list position to the given location.
     *
     * <p>This method updates the internal configuration state and the plugin's
     * configuration file with the specified waiting list position.
     * The configuration file is saved after updating.
     *
     * @param location The waiting list position to be set.
     */
    public synchronized void setWaitingListPosition(Location location) {
        if(location == null) throw new NullPointerException("location is null!");
        configWaitingListPosition = location;
        Main.getPlugin().getConfig().set("WaitingListPosition.x", location.getBlockX());
        Main.getPlugin().getConfig().set("WaitingListPosition.y", location.getBlockY());
        Main.getPlugin().getConfig().set("WaitingListPosition.z", location.getBlockZ());
        Main.getPlugin().saveConfig();
        Main.getMainLogger().info("set WaitingListPosition to: " + configWaitingListPosition);
    }

    /**
     * Checks if the plugin is set up.
     *
     * @return true if the plugin is set up, false otherwise.
     */
    public boolean checkSetUp() {
        return configSetUp;
    }

    /**
     * Checks the parkour start height.
     *
     * <p>This method returns the internally stored parkour start height,
     * which is the height at which the parkour minigame starts.
     *
     * @return the parkour start height.
     */
    public int checkParkourStartHeight() {
        return configParkourStartHeight;
    }

    /**
     * Checks the parkour length.
     *
     * <p>This method returns the internally stored parkour length,
     * which is the length of the parkour course.
     *
     * @return the parkour length.
     */
    public int checkParkourLength() {
        return configParkourLength;
    }

    /**
     * Checks the cost to lower the difficulty.
     *
     * <p>This method returns the internally stored cost to lower the difficulty,
     * which is the cost in diamonds that a player must pay to lower their difficulty.
     *
     * @return the cost to lower the difficulty.
     */
    public int checkCostToLowerTheDifficulty() {
        return configCostToLowerTheDifficulty;
    }

    /**
     * Checks the time to decide when respawning.
     *
     * <p>This method returns the internally stored time to decide when respawning,
     * which is the time in seconds that a player has to decide which minigame to play
     * when respawning.
     *
     * @return the time to decide when respawning.
     */
    public int checkTimeToDecideWhenRespawning() {
        return configTimeToDecideWhenRespawning;
    }

    /**
     * Checks the waiting list location.
     *
     * <p>This method returns the internally stored location of the waiting list,
     * which is the location where players will be teleported to when they are in the
     * waiting list.
     *
     * <p>If the waiting list location is not set, this method will log a warning
     * and return <code>null</code>.
     *
     * @return the waiting list location, or <code>null</code> if not set.
     */
    public Location checkWaitingListLocation() {
        if(configWaitingListPosition != null) {
            return configWaitingListPosition;
        }
        else {
            Main.getMainLogger().warning("configWaitingListPosition is not setup");
            return null;
        }
    }

    public HashMap<String, String> getDatabaseConfig() {
        HashMap<String, String> databaseConnectionInfo = new HashMap<>();
        FileConfiguration config = Main.getPlugin().getConfig();
        String[] requiredFields = {"host", "port", "database", "user", "password", "applicationName", "schema"};
        for (String field : requiredFields) {
            String value = config.getString("Database." + field);
            if (value == null || value.equals("default")) {
                Main.getMainLogger().warning("Database." + field + " is not configured properly");
            }
            databaseConnectionInfo.put(field, value);
        }
        return databaseConnectionInfo;
    }

    public synchronized void setServerName(String serverName) {
        if(serverName == null) {
            Main.getMainLogger().warning("ServerName is null!");
            return;
        }
        if(!Main.getPlugin().getConfig().contains("Tablist.ServerName")) {
            Main.getPlugin().getConfig().set("Tablist.ServerName", serverName);
            Main.getPlugin().saveConfig();
        }
        else if(!Main.getPlugin().getConfig().getString("Tablist.ServerName").equals(serverName)) {
            Main.getPlugin().getConfig().set("Tablist.ServerName", serverName);
            Main.getPlugin().saveConfig();
        }
    }

    public String getServerName() {
        if(Main.getPlugin().getConfig().contains("Tablist.ServerName")) {
            return Main.getPlugin().getConfig().getString("Tablist.ServerName");
        }
        else {
            Main.getMainLogger().warning("ServerName not found in config!");
            return null;
        }
    }

    public synchronized void setHostedBy(String serverName) {
        if(!Main.getPlugin().getConfig().contains("Tablist.HostedBy") || !Main.getPlugin().getConfig().getString("Tablist.HostedBy").equals(serverName)) {
            Main.getPlugin().getConfig().set("Tablist.HostedBy", serverName);
            Main.getPlugin().saveConfig();
        }
    }

    public String getHostedBy() {
        if(Main.getPlugin().getConfig().contains("Tablist.HostedBy")) {
            return Main.getPlugin().getConfig().getString("Tablist.HostedBy");
        }
        else {
            Main.getMainLogger().warning("HostedBy not found in config!");
            return null;
        }
    }
}
