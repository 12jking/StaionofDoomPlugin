package de.j.deathMinigames.settings;

import de.j.deathMinigames.main.Config;
import de.j.stationofdoom.main.Main;
import de.j.stationofdoom.util.translations.TranslationFactory;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class AnvilUI implements InventoryHolder {
    private final Location loc = new Location(Bukkit.getWorld("world"), ThreadLocalRandom.current().nextInt(0, 1000), ThreadLocalRandom.current().nextInt(0, 1000), ThreadLocalRandom.current().nextInt(0, 1000));
    private final ItemStack firstSlot = new ItemStack(Material.RED_CONCRETE);

    public AnvilUI(MainMenu.AnvilUIs title) {
        ItemMeta paperMeta = firstSlot.getItemMeta();
        switch (title) {
            case SET_HOST_NAME:
                if(Config.getInstance().getHostetBy() != null) paperMeta.displayName(Component.text(Config.getInstance().getHostetBy()));
                else paperMeta.displayName(Component.text("kein Name gesetzt / no name set"));
                break;
            case SET_SERVER_NAME:
                if(Config.getInstance().getServerName() != null) paperMeta.displayName(Component.text(Config.getInstance().getServerName()));
                else paperMeta.displayName(Component.text("kein Name gesetzt / no name set"));
                break;
            default:
                paperMeta.displayName(Component.text(""));
        }
        firstSlot.setItemMeta(paperMeta);
    }

    public void showInventory(Player playerToShowTheInvTo) {
        if(playerToShowTheInvTo == null) return;
        playerToShowTheInvTo.openAnvil(loc, true);
        playerToShowTheInvTo.getOpenInventory().getTopInventory().addItem(firstSlot);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }

    public boolean compareLocIDTo(Location loc) {
        if(loc.getBlockX() == this.loc.getBlockX() && loc.getBlockZ() == this.loc.getBlockZ()) return true;
        else return false;
    }
}
