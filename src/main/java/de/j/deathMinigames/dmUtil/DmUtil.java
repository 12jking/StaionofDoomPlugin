package de.j.deathMinigames.dmUtil;

import de.j.deathMinigames.main.HandlePlayers;
import de.j.deathMinigames.main.PlayerData;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DmUtil {
    private static DmUtil instance;

    private DmUtil() {}

    public static DmUtil getInstance() {
        if(instance == null){
            synchronized (DmUtil.class){
                if (instance == null){
                    instance = new DmUtil();
                }
            }
        }
        return instance;
    }

    /**
     * Drops the items in the player's last death inventory at the given location.
     *
     * <p>This method will drop all items in the player's last death inventory at the given location. If the player
     * does not have a last death inventory, this method will do nothing.
     *
     * @param player    The player whose last death inventory is to be dropped. This parameter must not be
     *                  {@code null}.
     * @param location  The location at which the items are to be dropped. This parameter must not be
     *                  {@code null}.
     */
    public void dropInv(Player player, Location location) {
        assert player != null : "player is null!";
        PlayerData playerData = HandlePlayers.getKnownPlayers().get(player.getUniqueId());
        Inventory inv = Bukkit.createInventory(null, 9*6);
        inv.setContents(playerData.getLastDeathInventory().getContents());
        assert !inv.isEmpty() : "inv is empty!";
        for(int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
            if(item == null) continue;
            player.getWorld().dropItem(location, item);
        }
    }

    public boolean validatePlayerAndPlayerData(Player player) {
        return player != null &&
                HandlePlayers.getKnownPlayers() != null &&
                HandlePlayers.getKnownPlayers().get(player.getUniqueId()) != null;
    }

    /**
     * Plays a sound at a location
     * @param location  the location to play the sound at
     * @param volume    how loud the
     * @param sound     the sound to play
     */
    public void playSoundAtLocation(Location location, Float volume, Sound sound) {
        location.getWorld().playSound(location, sound, volume, 1f);
    }
}
