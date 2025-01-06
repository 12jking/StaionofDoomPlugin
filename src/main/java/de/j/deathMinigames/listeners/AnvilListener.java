package de.j.deathMinigames.listeners;

import de.j.deathMinigames.dmUtil.DmUtil;
import de.j.deathMinigames.settings.MainMenu;
import de.j.stationofdoom.main.Main;
import de.j.stationofdoom.util.Tablist;
import de.j.stationofdoom.util.translations.TranslationFactory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.view.AnvilView;

public class AnvilListener implements Listener {
    private String serverName;
    private String hostName;
    private TranslationFactory tf = new TranslationFactory();

    @EventHandler
    public void onAnvilPrepare(PrepareAnvilEvent event) {
        Player player = (Player) event.getViewers().getFirst();
        if(player == null) {
            Main.getMainLogger().info("Anvil prepare event has no player");
            return;
        }
        Location loc = event.getInventory().getLocation();
        if(loc == null) {
            Main.getMainLogger().info("Anvil prepare event has no location");
            return;
        }
        AnvilView anvilView = event.getView();
        if(anvilView == null) {
            Main.getMainLogger().info("Anvil prepare event has no anvilView");
            return;
        }
        String renameText = anvilView.getRenameText();
        if(MainMenu.getSetHost().compareLocIDTo(loc)) {
            finishAnvilInvAfterOpening(event, player);
            if(renameText == null) return;
            hostName = renameText;
        }
        else if(MainMenu.getSetServerName().compareLocIDTo(loc)) {
            finishAnvilInvAfterOpening(event, player);
            if(renameText == null) return;
            serverName = renameText;
        }
    }

    @EventHandler
    public void onAnvilClick(InventoryClickEvent event) {
        if(event.getInventory().getType() == InventoryType.ANVIL) {
            Location loc = event.getInventory().getLocation();
            if (loc == null) return;
            Player player = (Player) event.getWhoClicked();
            if(player == null) return;
            if (MainMenu.getSetHost().compareLocIDTo(loc)) {
                event.setCancelled(true);
                if(event.getSlot() == 2) {
                    if (hostName == null) return;
                    Tablist.setHostetBy(hostName);
                    event.getView().close();
                    DmUtil.getInstance().playSoundAtLocation(player.getLocation(), 0.5f, Sound.BLOCK_ANVIL_USE);
                    player.sendMessage(Component.text("Host name: " + hostName).color(NamedTextColor.GOLD));
                }
            } else if (MainMenu.getSetServerName().compareLocIDTo(loc)) {
                event.setCancelled(true);
                if(event.getSlot() == 2) {
                    if (serverName == null) return;
                    Tablist.setServerName(serverName);
                    event.getView().close();
                    DmUtil.getInstance().playSoundAtLocation(player.getLocation(), 0.5f, Sound.BLOCK_ANVIL_USE);
                    player.sendMessage(Component.text("Server name: " + serverName).color(NamedTextColor.GOLD));
                }
            }
        }
    }

    @EventHandler
    public void onAnvilClose(InventoryCloseEvent event) {
        if(event.getInventory().getType() == InventoryType.ANVIL) {
            Location loc = event.getInventory().getLocation();
            if(loc == null) return;
            AnvilInventory anvilInventory = (AnvilInventory) event.getView().getTopInventory();
            if(MainMenu.getSetHost().compareLocIDTo(loc)) {
                anvilInventory.clear();
            }
            else if(MainMenu.getSetServerName().compareLocIDTo(loc)) {
                anvilInventory.clear();
            }
        }
    }

    private void finishAnvilInvAfterOpening(PrepareAnvilEvent event, Player player) {
        ItemStack output = new ItemStack(Material.GREEN_CONCRETE);
        ItemMeta outputItemMeta = output.getItemMeta();
        outputItemMeta.displayName(Component.text(tf.getTranslation(player, "anvilOutput")));
        output.setItemMeta(outputItemMeta);
        event.setResult(output);
        event.getView().setRepairCost(0);
    }
}
