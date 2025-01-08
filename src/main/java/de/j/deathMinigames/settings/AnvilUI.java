package de.j.deathMinigames.settings;

import de.j.deathMinigames.main.Config;
import de.j.stationofdoom.main.Main;
import de.j.stationofdoom.util.Tablist;
import de.j.stationofdoom.util.translations.TranslationFactory;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class AnvilUI implements InventoryHolder {
    private Location loc = null;
    private ItemStack input = new ItemStack(Material.RED_CONCRETE);
    Player player = null;
    private MainMenu.AnvilUIs title;

    public AnvilUI(MainMenu.AnvilUIs title) {
        if(title == null) return;
        this.title = title;
        createUniqueLocation();
        setInputMeta();
    }

    public void showInventory(Player playerToShowTheInvTo) {
        if(playerToShowTheInvTo == null) return;
        playerToShowTheInvTo.openAnvil(loc, true);
        player.getOpenInventory().getTopInventory().setItem(0, input);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }

    /**
     * Random location used as a unique identifier for the anvil UI.
     * This prevents conflicts between multiple anvil UIs.
     */
    private void createUniqueLocation() {
        World world = Bukkit.getWorld("world");
        if(world == null) throw new IllegalStateException("world `world´ could not be found");
        loc = new Location(world,
                ThreadLocalRandom.current().nextInt(0, 1000),
                ThreadLocalRandom.current().nextInt(0, 1000),
                ThreadLocalRandom.current().nextInt(0, 1000));
    }

    public boolean compareLocIDTo(Location loc) {
        return loc.getBlockX() == this.loc.getBlockX() && loc.getBlockZ() == this.loc.getBlockZ();
    }

    public void setPlayerForTranslations(Player player) {
        this.player = player;
        setInputMeta();
    }

    private void setInputMeta() {
        ItemMeta inputMeta = input.getItemMeta();
        String inputItemName = null;
        if(player == null) {
            inputMeta.displayName(Component.text("default"));
        }
        else {
            switch (title) {
                case SET_HOST_NAME -> inputItemName = Tablist.getHostetBy();
                case SET_SERVER_NAME -> inputItemName = Tablist.getServerName();
            }
            if(inputItemName == null) {
                inputMeta.displayName(Component.text(new TranslationFactory().getTranslation(player, "noNameSet")));
            }
            else {
                inputMeta.displayName(Component.text(inputItemName));
            }
        }
        input.setItemMeta(inputMeta);
        if(player != null) {
            showInventory(player);
        }
    }
}
