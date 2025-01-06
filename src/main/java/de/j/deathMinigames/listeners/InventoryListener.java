package de.j.deathMinigames.listeners;

import de.j.deathMinigames.dmUtil.DmUtil;
import de.j.deathMinigames.main.HandlePlayers;
import de.j.deathMinigames.main.PlayerData;
import de.j.deathMinigames.settings.AnvilUI;
import de.j.deathMinigames.settings.MainMenu.InventoryMenus;
import de.j.stationofdoom.util.translations.TranslationFactory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.*;
import de.j.deathMinigames.main.Config;
import de.j.stationofdoom.main.Main;
import de.j.deathMinigames.minigames.Minigame;
import de.j.deathMinigames.settings.GUI;
import de.j.deathMinigames.settings.MainMenu;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.view.AnvilView;

import java.util.HashMap;
import java.util.UUID;

public class InventoryListener implements Listener {
    private Player playerClicked;

    /**
     * Handles InventoryClickEvent's for the main settings menu and submenus.
     *
     * @param event the InventoryClickEvent
     */
    @EventHandler
    public void onSettingsClick(InventoryClickEvent event) {
        Config config = Config.getInstance();
        MainMenu mainMenu = new MainMenu();
        InventoryHolder invHolder = event.getInventory().getHolder();
        Minigame minigame = Minigame.getInstance();
        UUID ID;
        int slot = event.getSlot();
        if(slot < 0) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        if(invHolder instanceof MainMenu) {
            handleMainMenuGUI(event, player, mainMenu, slot);
            reloadInventory(InventoryMenus.DIFFICULTY, mainMenu);
        }
        else if(invHolder instanceof GUI) {
            GUI gui = (GUI) invHolder;
            ID = gui.getUUID();
            if(ID == MainMenu.getIntroduction().getUUID()) {
                handleIntroductionGUI(event, player, mainMenu, slot, minigame);
            }
            else if (ID == MainMenu.getUsesPlugin().getUUID()) {
                handleUsesPluginGUI(event, player, mainMenu, slot, minigame);
            }
            else if (ID == MainMenu.getDifficulty().getUUID()) {
                handleDifficultyGUI(event, player, mainMenu, slot);
            }
            else if(ID == MainMenu.getDifficultyPlayerSettings().getUUID()) {
                handleDifficultyPlayerSettingsGUI(event, player, mainMenu, slot, minigame);
            }
            else if(ID == MainMenu.getSetUp().getUUID()) {
                handleSetUpGUI(event, player, mainMenu, slot, config);
            }
            else if (ID == MainMenu.getParkourStartHeight().getUUID()) {
                handleParkourStartHeightGUI(event, player, mainMenu, slot, config, minigame);
            }
            else if (ID == MainMenu.getParkourLength().getUUID()) {
                handleParkourLengthGUI(event, player, mainMenu, slot, config, minigame);
            }
            else if (ID == MainMenu.getCostToLowerTheDifficulty().getUUID()) {
                handleCostToLowerTheDifficultyGUI(event, player, mainMenu, slot, config, minigame);
            }
            else if (ID == MainMenu.getTimeToDecideWhenRespawning().getUUID()) {
                handleTimeToDecideWhenRespawningGUI(event, player, mainMenu, slot, config, minigame);
            }
        }
    }


    /**
     * Gets the player associated with the given position in the list of known players.
     * <p>
     * If the given position is negative or greater than or equal to the size of the list of known players, this method
     * will return null.
     *
     * @param placeInList the position in the list of known players
     * @return the player associated with the given position, or null if none exists
     */
    public Player getIndexAssociatedWithPlayerInKnownPlayersList(int placeInList) {
        HashMap<UUID, PlayerData> knownPlayers = HandlePlayers.getKnownPlayers();
        if (placeInList >= 0 && placeInList < knownPlayers.size()) {
            Player player = Bukkit.getPlayer(knownPlayers.keySet().stream().toList().get(placeInList));
            assert player != null;
            return player;
        }
        return null;
    }

    /**
     * Reloads the inventory given as a parameter, to reflect the latest changes of the player's data.
     * @param inventory the inventory to reload
     * @param slot the slot of the player in the inventory
     * @param mainMenu the main menu to get the inventory from
     */
    public void reloadInventory(InventoryMenus inventory, int slot, MainMenu mainMenu) {
        Config config = Config.getInstance();
        PlayerData playerClickedData = HandlePlayers.getKnownPlayers().get(playerClicked.getUniqueId());
        if(playerClicked == null) {
            throw new IllegalStateException("No player selected");
        }
        switch (inventory) {
            case INTRODUCTION:
                if(playerClickedData.getIntroduction()) {
                    MainMenu.getIntroduction().addClickableItemStack(playerClicked.getName(), Material.GREEN_CONCRETE_POWDER, 1, slot);
                }
                else {
                    MainMenu.getIntroduction().addClickableItemStack(playerClicked.getName(), Material.RED_CONCRETE_POWDER, 1, slot);
                }
                break;
            case USES_PLUGIN:
                if(playerClickedData.getUsesPlugin()) {
                    MainMenu.getUsesPlugin().addClickableItemStack(playerClicked.getName(), Material.GREEN_CONCRETE_POWDER, 1, slot);
                }
                else {
                    MainMenu.getUsesPlugin().addClickableItemStack(playerClicked.getName(), Material.RED_CONCRETE_POWDER, 1, slot);
                }
                break;
            case DIFFICULTY_SETTINGS:
                int difficulty = playerClickedData.getDifficulty();
                mainMenu.difficultySettingsSetInventoryContents(difficulty);
                break;
            case SETTINGS:
                if(config.checkSetUp()) {
                    mainMenu.addClickableItemStack("SetUp", Material.GREEN_CONCRETE, 1, 0);
                }
                else {
                    mainMenu.addClickableItemStack("SetUp", Material.RED_CONCRETE, 1, 0);
                }
                mainMenu.addClickableItemStack("Introduction", Material.GREEN_CONCRETE, 1, 1);
                mainMenu.addClickableItemStack("UsesPlugin", Material.GREEN_CONCRETE, 1, 2);
                mainMenu.addClickableItemStack("Difficulty", Material.RED_CONCRETE, 1, 3);
                break;
        }
    }

    /**
     * Reloads the inventory given as a parameter, to reflect the latest changes of the player's data.
     * @param inventory the inventory to reload
     * @param mainMenu the main menu to get the inventory from
     */
    public void reloadInventory(InventoryMenus inventory, MainMenu mainMenu) {
        PlayerData playerData;
        switch (inventory) {
            case INTRODUCTION:
                HashMap<UUID, PlayerData> knownPlayers = HandlePlayers.getKnownPlayers();
                for(int i = 0; i < knownPlayers.size(); i++) {
                    Player currentPlayer = getIndexAssociatedWithPlayerInKnownPlayersList(i);
                    if(currentPlayer == null) continue;
                    playerData = knownPlayers.get(currentPlayer.getUniqueId());
                    MainMenu.getIntroduction().addClickableItemStack(currentPlayer.getName(), getMaterialBasedOnBoolean(playerData.getIntroduction()), 1, i);
                }
                break;
            case USES_PLUGIN:
                for(int i = 0; i < HandlePlayers.getKnownPlayers().size(); i++) {
                    Player currentPlayer = getIndexAssociatedWithPlayerInKnownPlayersList(i);
                    if(currentPlayer == null) continue;
                    playerData = HandlePlayers.getKnownPlayers().get(currentPlayer.getUniqueId());
                    MainMenu.getUsesPlugin().addClickableItemStack(currentPlayer.getName(), getMaterialBasedOnBoolean(playerData.getUsesPlugin()), 1, i);
                }
                break;
            case DIFFICULTY:
                MainMenu.getDifficulty().addPlayerHeads(HandlePlayers.getKnownPlayers());
                break;
            case DIFFICULTY_SETTINGS:
                playerData = HandlePlayers.getKnownPlayers().get(playerClicked.getUniqueId());
                int difficulty = playerData.getDifficulty();
                mainMenu.difficultySettingsSetInventoryContents(difficulty);
                break;
            case SETUP:
                mainMenu.setUpSettingsSetInventoryContents();
                break;
            case PARKOUR_START_HEIGHT:
                mainMenu.parkourStartHeightSettingsSetInventoryContents();
                break;
            case PARKOUR_LENGTH:
                mainMenu.parkourLengthSettingsSetInventoryContents();
                break;
            case COST_TO_LOWER_THE_DIFFICULTY:
                mainMenu.costToLowerTheDifficultySettingsSetInventoryContents();
                break;
            case TIME_TO_DECIDE_WHEN_RESPAWNING:
                mainMenu.timeToDecideWhenRespawningSettingsSetInventoryContents();
                break;
        }
    }

    private Material getMaterialBasedOnBoolean(boolean bool) {
        if(bool) {
            return Material.GREEN_CONCRETE_POWDER;
        }
        else {
            return Material.RED_CONCRETE_POWDER;
        }
    }

/**
 * Handles the main menu GUI interaction when a player clicks on an inventory slot.
 *
 * This method cancels the inventory click event and opens the corresponding
 * submenu based on the clicked slot:
 * - Slot 0: Opens the "SetUp" submenu.
 * - Slot 1: Opens the "Introduction" submenu.
 * - Slot 2: Opens the "UsesPlugin" submenu.
 * - Slot 3: Opens the "Difficulty" submenu.
 *
 * @param event The InventoryClickEvent triggered by the player's click.
 * @param player The player who clicked the inventory.
 * @param mainMenu The main menu instance to manage GUI interactions.
 * @param slot The slot number that was clicked by the player.
 */
    private void handleMainMenuGUI(InventoryClickEvent event, Player player, MainMenu mainMenu, int slot) {
        event.setCancelled(true);
        switch (slot) {
            case 0:
                reloadInventory(InventoryMenus.SETUP, mainMenu);
                MainMenu.getSetUp().addBackButton(player);
                MainMenu.getSetUp().showInventory(player);
                break;
            case 1:
                reloadInventory(InventoryMenus.INTRODUCTION, mainMenu);
                MainMenu.getIntroduction().addBackButton(player);
                MainMenu.getIntroduction().showInventory(player);
                break;
            case 2:
                reloadInventory(InventoryMenus.USES_PLUGIN, mainMenu);
                MainMenu.getUsesPlugin().addBackButton(player);
                MainMenu.getUsesPlugin().showInventory(player);
                break;
            case 3:
                MainMenu.getDifficulty().addBackButton(player);
                MainMenu.getDifficulty().showInventory(player);
                break;
            case 4:
                MainMenu.getSetHost().showInventory(player);
                break;
            case 5:
                MainMenu.getSetServerName().showInventory(player);
                break;
        }
    }

    /**
     * Handles the introduction GUI interaction when a player clicks on an inventory slot.
     * @param event The InventoryClickEvent triggered by the player's click.
     * @param player The player who clicked the inventory.
     * @param mainMenu The main menu instance to manage GUI interactions.
     * @param slot The slot number that was clicked by the player.
     * @param minigame The minigame instance to manage GUI interactions.
     */
    private void handleIntroductionGUI(InventoryClickEvent event, Player player, MainMenu mainMenu, int slot, Minigame minigame) {
        HashMap<UUID, PlayerData> knownPlayers = HandlePlayers.getKnownPlayers();
        event.setCancelled(true);
        if(slot == 53) {
            mainMenu.showPlayerSettings(player);
        }
        else if (slot <= knownPlayers.size()) {
            playerClicked = getIndexAssociatedWithPlayerInKnownPlayersList(slot);
            if(playerClicked == null) {
                player.sendMessage(Component.text(new TranslationFactory().getTranslation(player, "somethingWentWrong")).color(NamedTextColor.RED));
            }
            PlayerData playerClickedData = knownPlayers.get(playerClicked.getUniqueId());
            if(playerClickedData.getIntroduction()) {
                minigame.playSoundToPlayer(player, 0.5F, Sound.ENTITY_ITEM_BREAK);
                playerClickedData.setIntroduction(false);
            } else if (!playerClickedData.getIntroduction()) {
                minigame.playSoundToPlayer(player, 0.5F, Sound.BLOCK_ANVIL_USE);
                playerClickedData.setIntroduction(true);
            }
            reloadInventory(InventoryMenus.INTRODUCTION, slot, mainMenu);
            player.sendMessage(Component.text("Changed Introduction of " + playerClicked.getName() + " to " + playerClickedData.getIntroduction()).color(NamedTextColor.RED));
        }
    }

    /**
     * Handles the usesPlugin GUI interaction when a player clicks on an inventory slot.
     * @param event The InventoryClickEvent triggered by the player's click.
     * @param player The player who clicked the inventory.
     * @param mainMenu The main menu instance to manage GUI interactions.
     * @param slot The slot number that was clicked by the player.
     * @param minigame The minigame instance.
     */
    private void handleUsesPluginGUI(InventoryClickEvent event, Player player, MainMenu mainMenu, int slot, Minigame minigame) {
        HashMap<UUID, PlayerData> knownPlayers = HandlePlayers.getKnownPlayers();
        event.setCancelled(true);
        if(slot == 53) {
            mainMenu.showPlayerSettings(player);
        }
        else if (slot <= knownPlayers.size()) {
            playerClicked = getIndexAssociatedWithPlayerInKnownPlayersList(slot);
            if(playerClicked == null) player.sendMessage(Component.text(new TranslationFactory().getTranslation(player, "somethingWentWrong")).color(NamedTextColor.RED));
            PlayerData playerClickedData = knownPlayers.get(playerClicked.getUniqueId());
            if(playerClickedData.getUsesPlugin()) {
                minigame.playSoundToPlayer(player, 0.5F, Sound.ENTITY_ITEM_BREAK);
                playerClickedData.setUsesPlugin(false);
            } else if (!playerClickedData.getUsesPlugin()) {
                minigame.playSoundToPlayer(player, 0.5F, Sound.BLOCK_ANVIL_USE);
                playerClickedData.setUsesPlugin(true);
            }
            reloadInventory(InventoryMenus.USES_PLUGIN, slot, mainMenu);
            player.sendMessage(Component.text("Changed UsesPlugin of " + playerClickedData.getName() + " to " + playerClickedData.getUsesPlugin()).color(NamedTextColor.RED));
        }
    }

    /**
     * Handles the difficulty GUI interaction when a player clicks on an inventory slot.
     * @param event The InventoryClickEvent triggered by the player's click.
     * @param player The player who clicked the inventory.
     * @param mainMenu The main menu instance to manage GUI interactions.
     * @param slot The slot number that was clicked by the player.
     */
    private void handleDifficultyGUI(InventoryClickEvent event, Player player, MainMenu mainMenu, int slot) {
        event.setCancelled(true);
        if (slot == 53) {
            mainMenu.showPlayerSettings(player);
        } else if (slot <= HandlePlayers.getKnownPlayers().size()) {
            playerClicked = getIndexAssociatedWithPlayerInKnownPlayersList(slot);
            player.sendMessage(Component.text(new TranslationFactory().getTranslation(player, "somethingWentWrong")).color(NamedTextColor.RED));
            reloadInventory(InventoryMenus.DIFFICULTY_SETTINGS, slot, mainMenu);
            MainMenu.getDifficultyPlayerSettings().addBackButton(player);
            MainMenu.getDifficultyPlayerSettings().showInventory(player);
        }
    }

    /**
     * Handles the difficulty player settings GUI interaction when a player clicks on an inventory slot.
     * @param event The InventoryClickEvent triggered by the player's click.
     * @param player The player who clicked the inventory.
     * @param mainMenu The main menu instance to manage GUI interactions.
     * @param slot The slot number that was clicked by the player.
     * @param minigame The minigame instance.
     */
    private void handleDifficultyPlayerSettingsGUI(InventoryClickEvent event, Player player, MainMenu mainMenu, int slot, Minigame minigame) {
        event.setCancelled(true);
        if(slot == 53) {
            mainMenu.showPlayerSettings(player);
        }
        else if (slot < 11){
            minigame.playSoundToPlayer(player, 0.5F, Sound.BLOCK_ANVIL_USE);
            PlayerData playerClickedData = HandlePlayers.getKnownPlayers().get(playerClicked.getUniqueId());
            playerClickedData.setDifficulty(slot);
            reloadInventory(InventoryMenus.DIFFICULTY_SETTINGS, slot, mainMenu);
            player.sendMessage(Component.text("Changed Difficulty of " + playerClickedData.getName() + " to " + playerClickedData.getDifficulty()).color(NamedTextColor.RED));
        }
    }

    /**
     * Handles the set up GUI interaction when a player clicks on an inventory slot.
     * @param event The InventoryClickEvent triggered by the player's click.
     * @param player The player who clicked the inventory.
     * @param mainMenu The main menu instance to manage GUI interactions.
     * @param slot The slot number that was clicked by the player.
     * @param config The config instance.
     */
    private void handleSetUpGUI(InventoryClickEvent event, Player player, MainMenu mainMenu, int slot, Config config) {
        event.setCancelled(true);
        if(slot == 53) {
            mainMenu.showPlayerSettings(player);
        }
        else if (slot <= 4){
            switch (slot) {
                case 0:
                    reloadInventory(InventoryMenus.PARKOUR_START_HEIGHT, mainMenu);
                    MainMenu.getParkourStartHeight().addBackButton(player);
                    MainMenu.getParkourStartHeight().showInventory(player);
                    break;
                case 1:
                    reloadInventory(InventoryMenus.PARKOUR_LENGTH, mainMenu);
                    MainMenu.getParkourLength().addBackButton(player);
                    MainMenu.getParkourLength().showInventory(player);
                    break;
                case 2:
                    config.setWaitingListPosition(player.getLocation());
                    reloadInventory(InventoryMenus.SETUP, mainMenu);
                    player.sendMessage(Component.text(new TranslationFactory().getTranslation(player, "setWaitingListPosition")).color(NamedTextColor.GREEN));
                    break;
                case 3:
                    reloadInventory(InventoryMenus.COST_TO_LOWER_THE_DIFFICULTY, mainMenu);
                    MainMenu.getCostToLowerTheDifficulty().addBackButton(player);
                    MainMenu.getCostToLowerTheDifficulty().showInventory(player);
                    break;
                case 4:
                    reloadInventory(InventoryMenus.TIME_TO_DECIDE_WHEN_RESPAWNING, mainMenu);
                    MainMenu.getTimeToDecideWhenRespawning().addBackButton(player);
                    MainMenu.getTimeToDecideWhenRespawning().showInventory(player);
                    break;
            }
        }
    }

    /**
     * Handles the parkour start height GUI interaction when a player clicks on an inventory slot.
     * @param event The InventoryClickEvent triggered by the player's click.
     * @param player The player who clicked the inventory.
     * @param mainMenu The main menu instance to manage GUI interactions.
     * @param slot The slot number that was clicked by the player.
     * @param config The config instance.
     * @param minigame The minigame instance.
     */
    private void handleParkourStartHeightGUI(InventoryClickEvent event, Player player, MainMenu mainMenu, int slot, Config config, Minigame minigame) {
        event.setCancelled(true);
        if(slot == 53) {
            mainMenu.showPlayerSettings(player);
        }
        else if (slot <= 36){
            minigame.playSoundToPlayer(player, 0.5F, Sound.BLOCK_ANVIL_USE);
            int parkourStartHeight = slot * 10;
            config.setParkourStartHeight(parkourStartHeight);
            reloadInventory(InventoryMenus.PARKOUR_START_HEIGHT, mainMenu);
        }
    }

    /**
     * Handles the parkour length GUI interaction when a player clicks on an inventory slot.
     * @param event The InventoryClickEvent triggered by the player's click.
     * @param player The player who clicked the inventory.
     * @param mainMenu The main menu instance to manage GUI interactions.
     * @param slot The slot number that was clicked by the player.
     * @param config The config instance.
     * @param minigame The minigame instance.
     */
    private void handleParkourLengthGUI(InventoryClickEvent event, Player player, MainMenu mainMenu, int slot, Config config, Minigame minigame) {
        event.setCancelled(true);
        if(slot == 53) {
            mainMenu.showPlayerSettings(player);
        }
        else if (slot < 20) {
            minigame.playSoundToPlayer(player, 0.5F, Sound.BLOCK_ANVIL_USE);
            config.setParkourLength(slot);
            reloadInventory(InventoryMenus.PARKOUR_LENGTH, mainMenu);
        }
    }

    /**
     * Handles the cost to lower the difficulty GUI interaction when a player clicks on an inventory slot.
     * @param event The InventoryClickEvent triggered by the player's click.
     * @param player The player who clicked the inventory.
     * @param mainMenu The main menu instance to manage GUI interactions.
     * @param slot The slot number that was clicked by the player.
     * @param config The config instance.
     * @param minigame The minigame instance.
     */
    private void handleCostToLowerTheDifficultyGUI(InventoryClickEvent event, Player player, MainMenu mainMenu, int slot, Config config, Minigame minigame) {
        event.setCancelled(true);
        if(slot == 53) {
            mainMenu.showPlayerSettings(player);
        }
        else if (slot < 9) {
            minigame.playSoundToPlayer(player, 0.5F, Sound.BLOCK_ANVIL_USE);
            slot = slot + 1;
            config.setCostToLowerTheDifficulty(slot);
            reloadInventory(InventoryMenus.COST_TO_LOWER_THE_DIFFICULTY, mainMenu);
        }
    }

    /**
     * Handles the time to decide when respawning GUI interaction when a player clicks on an inventory slot.
     * @param event The InventoryClickEvent triggered by the player's click.
     * @param player The player who clicked the inventory.
     * @param mainMenu The main menu instance to manage GUI interactions.
     * @param slot The slot number that was clicked by the player.
     * @param config The config instance.
     * @param minigame The minigame instance.
     */
    private void handleTimeToDecideWhenRespawningGUI(InventoryClickEvent event, Player player, MainMenu mainMenu, int slot, Config config, Minigame minigame) {
        event.setCancelled(true);
        if(slot == 53) {
            mainMenu.showPlayerSettings(player);
        }
        else if (slot < 29) {
            minigame.playSoundToPlayer(player, 0.5F, Sound.BLOCK_ANVIL_USE);
            slot = slot + 5;
            config.setTimeToDecideWhenRespawning(slot);
            reloadInventory(InventoryMenus.TIME_TO_DECIDE_WHEN_RESPAWNING, mainMenu);
        }
    }
}
