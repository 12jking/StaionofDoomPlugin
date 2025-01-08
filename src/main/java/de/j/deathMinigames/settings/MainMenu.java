package de.j.deathMinigames.settings;

import de.j.stationofdoom.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import de.j.deathMinigames.main.Config;

import java.util.ArrayList;

public class MainMenu implements InventoryHolder {
    private final Inventory inventory;

    public enum InventoryMenus {
        INTRODUCTION,
        DIFFICULTY,
        USES_PLUGIN,
        DIFFICULTY_SETTINGS,
        SETTINGS,
        SETUP,
        PARKOUR_START_HEIGHT,
        PARKOUR_LENGTH,
        COST_TO_LOWER_THE_DIFFICULTY,
        TIME_TO_DECIDE_WHEN_RESPAWNING,
        SET_HOST,
        SET_SERVER_NAME
    }

    public enum AnvilUIs {
        SET_SERVER_NAME,
        SET_HOST_NAME,
        DEFAULT // usage when the input slot item should have no name
    }

    public MainMenu() {
        this.inventory = Bukkit.createInventory(this, 9, "Settings");
    }

    public synchronized static GUI getIntroduction() {
        return introduction;
    }

    public synchronized static GUI getDifficulty() {
        return difficulty;
    }

    public synchronized static GUI getUsesPlugin() {
        return usesPlugin;
    }

    public synchronized static GUI getDifficultyPlayerSettings() {
        return difficultyPlayerSettings;
    }

    public synchronized static GUI getSetUp() {
        return setUp;
    }

    public synchronized static GUI getParkourStartHeight() {
        return parkourStartHeight;
    }

    public synchronized static GUI getParkourLength() {
        return parkourLength;
    }

    public synchronized static GUI getCostToLowerTheDifficulty() {
        return costToLowerTheDifficulty;
    }

    public synchronized static GUI getTimeToDecideWhenRespawning() {
        return timeToDecideWhenRespawning;
    }

    public synchronized static AnvilUI getSetHost() {
        return setHost;
    }

    public synchronized static AnvilUI getSetServerName() {
        return setServerName;
    }

    private static final GUI introduction = new GUI("Introduction", true, false);
    private static final GUI difficulty = new GUI("Difficulty", true, true);
    private static final GUI usesPlugin = new GUI("UsesPlugin", true, false);
    private static final GUI difficultyPlayerSettings = new GUI("Difficulty - Settings", false, false);
    private static final GUI setUp = new GUI("SetUp", false, false);
    private static final GUI parkourStartHeight = new GUI("ParkourStartHeight", false, false);
    private static final GUI parkourLength = new GUI("ParkourLength", false, false);
    private static final GUI costToLowerTheDifficulty = new GUI("CostToLowerTheDifficulty", false, false);
    private static final GUI timeToDecideWhenRespawning = new GUI("TimeToDecideWhenRespawning", false, false);
    private static final AnvilUI setHost = new AnvilUI(AnvilUIs.SET_HOST_NAME);
    private static final AnvilUI setServerName = new AnvilUI(AnvilUIs.SET_SERVER_NAME);

    /**
     * Opens the main menu for the given player, where the player can
     * click on sub-menus to view the settings.
     *
     * @param player The player to open the menu for
     */
    public void showPlayerSettings(Player player) {
        addSubmenus();
        showPlayerInv(player);
    }

    /**
     * Adds clickable items to the main menu that link to the sub-menus.
     * The items are added in the following order:
     * - "SetUp" (green if set up, red if not)
     * - "Introduction"
     * - "UsesPlugin"
     * - "Difficulty"
     */
    private void addSubmenus() {
        Config config = Config.getInstance();
        if(config.checkSetUp()) {
            addClickableItemStack("SetUp", Material.GREEN_CONCRETE, 1, 0);
        }
        else {
            addClickableItemStack("SetUp", Material.RED_CONCRETE, 1, 0);
        }
        addClickableItemStack("Introduction", Material.GREEN_CONCRETE, 1, 1);
        addClickableItemStack("UsesPlugin", Material.GREEN_CONCRETE, 1, 2);
        addClickableItemStack("Difficulty", Material.RED_CONCRETE, 1, 3);
        addClickableItemStack("SetHost", Material.BOOK, 1, 4);
        addClickableItemStack("SetServerName", Material.BOOK, 1, 5);
    }

    /**
     * Shows the main menu to the given player. The player can click on
     * the items in the inventory to view the settings.
     *
     * @param player The player to show the menu to
     * @throws IllegalStateException if the inventory is null
     */
    private void showPlayerInv(Player player) {
        if(inventory == null) {
            throw new IllegalStateException("Inventory is null");
        }
        player.openInventory(inventory);
    }

    /**
     * Adds a clickable item to the inventory.
     * Note: Click handling must be implemented in InventoryListener.
     * @param name  The display name of the item
     * @param material  The material type
     * @param amount    Item stack size
     * @param slotWhereToPutTheItem Inventory slot
     * @throws IllegalArgumentException if the slot is out of bounds
     */
    public void addClickableItemStack(String name, Material material, int amount, int slotWhereToPutTheItem) {
        if(name == null) throw new NullPointerException("name is null!");
        if(material == null) throw new NullPointerException("material is null!");
        if(amount <= 0) throw new IllegalArgumentException("amount must be greater than 0!");
        if(slotWhereToPutTheItem < 0 || slotWhereToPutTheItem >= inventory.getSize()) {
            throw new IllegalArgumentException("Invalid slot index: " + slotWhereToPutTheItem);
        }
        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);

        inventory.setItem(slotWhereToPutTheItem, itemStack);
    }

    /**
     * Sets the contents of the difficulty player settings inventory to show all difficulties
     * from 0 to maxDifficulty, with the given difficulty highlighted in green.
     * @param difficulty The difficulty to highlight in green, must be between 0 and maxDifficulty
     * @throws IllegalArgumentException if the difficulty is out of range
     */
    public void difficultySettingsSetInventoryContents(int difficulty) {
        int maxDifficulty = 10;
        if(difficulty < 0 || difficulty > maxDifficulty) {
            throw new IllegalArgumentException("Difficulty must be between 0 and " + maxDifficulty);
        }
        for(int i = 0; i <= maxDifficulty; i++) {
            MainMenu.getDifficultyPlayerSettings().addClickableItemStack(Integer.toString(i), Material.RED_CONCRETE_POWDER, 1, i);
        }
        MainMenu.getDifficultyPlayerSettings().addClickableItemStack(Integer.toString(difficulty), Material.GREEN_CONCRETE_POWDER, 1 ,difficulty);
    }

    /**
     * Sets the contents of the inventory used to show the variables importent for setting up the plugin.
     * The settings that are shown are the parcourse start height, the parcourse length,
     * the waiting list position, the cost to lower the difficulty and the time to decide
     * when respawning.
     */
    public void setUpSettingsSetInventoryContents() {
        Config config = Config.getInstance();
        Location waitingListPosition = config.checkWaitingListLocation();
        int startHeight = config.checkParkourStartHeight();
        int parkourLength = config.checkParkourLength();
        int costToLowerTheDifficulty = config.checkCostToLowerTheDifficulty();
        int timeToDecideWhenRespawning = config.checkTimeToDecideWhenRespawning();

        if(startHeight != 0) {
            getSetUp().addClickableItemStack("Parcour Start Height", Material.LADDER, startHeight, 0);
        }
        else {
            getSetUp().addClickableItemStack("Parcour Start Height", Material.LADDER, 1, 0);
        }
        if(parkourLength != 0) {
            getSetUp().addClickableItemStack("Parcour length", Material.LADDER, parkourLength, 1);
        }
        else {
            getSetUp().addClickableItemStack("Parcour length", Material.LADDER, 1, 1);
        }
        if(waitingListPosition != null) {
            ArrayList<String> lore = new ArrayList<>();
            lore.add("Current position:");
            lore.add(String.format("X: %d", waitingListPosition.getBlockX()));
            lore.add(String.format("Y: %d", waitingListPosition.getBlockY()));
            lore.add(String.format("Z: %d", waitingListPosition.getBlockZ()));
            getSetUp().addClickableItemStack("WaitingListPosition", Material.GREEN_CONCRETE_POWDER, 1, 2, lore);
        }
        else {
            getSetUp().addClickableItemStack("WaitingListPosition", Material.RED_CONCRETE_POWDER, 1, 2);
        }
        if(costToLowerTheDifficulty != 0) {
            getSetUp().addClickableItemStack("cost to lower the difficulty", Material.DIAMOND, costToLowerTheDifficulty, 3);
        }
        else {
            getSetUp().addClickableItemStack("cost to lower the difficulty", Material.DIAMOND, 1, 3);
        }
        if(timeToDecideWhenRespawning != 0) {
            getSetUp().addClickableItemStack("time to decide when respawning", Material.CLOCK, timeToDecideWhenRespawning, 4);
        }
        else {
            getSetUp().addClickableItemStack("time to decide when respawning", Material.CLOCK, 1, 4);
        }
    }

    /**
     * Sets the contents of the inventory used to set the parcourse start height.
     * The options are shown as clickable items in the inventory, with the current
     * start height being green and the other options being red.
     */
    public void parkourStartHeightSettingsSetInventoryContents() {
        Config config = Config.getInstance();
        int startHeight = config.checkParkourStartHeight();
        for(int i = 0; i < 29; i++) {
            if(startHeight == i*10) {
                MainMenu.getParkourStartHeight().addClickableItemStack(Integer.toString(i*10), Material.GREEN_CONCRETE_POWDER, 1, i);
            }
            else {
                MainMenu.getParkourStartHeight().addClickableItemStack(Integer.toString(i*10), Material.RED_CONCRETE_POWDER, 1, i);
            }
        }
    }

    /**
     * Sets the contents of the inventory used to set the parcourse length.
     * The options are shown as clickable items in the inventory, with the current
     * length being green and the other options being red.
     */
    public void parkourLengthSettingsSetInventoryContents() {
        Config config = Config.getInstance();
        int length = config.checkParkourLength();
        for(int i = 0; i < 20; i++) {
            if(length == i) {
                MainMenu.getParkourLength().addClickableItemStack(Integer.toString(i), Material.GREEN_CONCRETE_POWDER, 1, i);
            }
            else {
                MainMenu.getParkourLength().addClickableItemStack(Integer.toString(i), Material.RED_CONCRETE_POWDER, 1, i);
            }
        }
    }

    /**
     * Sets the contents of the inventory used to set the cost to lower the difficulty.
     * The options are shown as clickable items in the inventory, with the current
     * cost being green and the other options being red.
     */
    public void costToLowerTheDifficultySettingsSetInventoryContents() {
        Config config = Config.getInstance();
        int length = config.checkCostToLowerTheDifficulty();
        for(int i = 1; i < 11; i++) {
            if(length == i) {
                MainMenu.getCostToLowerTheDifficulty().addClickableItemStack(Integer.toString(i), Material.GREEN_CONCRETE_POWDER, 1, i-1);
            }
            else {
                MainMenu.getCostToLowerTheDifficulty().addClickableItemStack(Integer.toString(i), Material.RED_CONCRETE_POWDER, 1, i-1);
            }
        }
    }

    /**
     * Sets the contents of the inventory used to set the time to decide when respawning.
     * The options are shown as clickable items in the inventory, with the current
     * time being green and the other options being red. The options are from 5 seconds
     * to 30 seconds.
     */
    public void timeToDecideWhenRespawningSettingsSetInventoryContents() {
        Config config = Config.getInstance();
        int time = config.checkTimeToDecideWhenRespawning();
        for(int i = 5; i < 31; i++) {
            if(time == i) {
                MainMenu.getTimeToDecideWhenRespawning().addClickableItemStack(Integer.toString(time), Material.GREEN_CONCRETE_POWDER, 1, i-5);
            }
            else {
                MainMenu.getTimeToDecideWhenRespawning().addClickableItemStack(Integer.toString(i), Material.RED_CONCRETE_POWDER, 1, i-5);
            }
        }
    }

    /**
     * Returns the inventory associated with this MainMenu.
     *
     * @return The inventory associated with this MainMenu.
     */
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
