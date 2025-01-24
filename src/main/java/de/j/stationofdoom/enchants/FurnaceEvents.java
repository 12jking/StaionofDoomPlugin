package de.j.stationofdoom.enchants;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class FurnaceEvents implements Listener {

    private final ArrayList<Material> pickaxes = new ArrayList<>() {{
       add(Material.WOODEN_PICKAXE);
       add(Material.STONE_PICKAXE);
       add(Material.GOLDEN_PICKAXE);
       add(Material.IRON_PICKAXE);
       add(Material.DIAMOND_PICKAXE);
       add(Material.NETHERITE_PICKAXE);
    }};
    private final ArrayList<Material> ores = new ArrayList<>() {{
       add(Material.IRON_ORE);
       add(Material.DEEPSLATE_IRON_ORE);
       add(Material.COPPER_ORE);
       add(Material.DEEPSLATE_COPPER_ORE);
       add(Material.GOLD_ORE);
       add(Material.DEEPSLATE_GOLD_ORE);
    }};
    /// Hashmap that contains the raw ores as key and the smelted ores as value
    private final HashMap<Material, Material> smeltedOres = new HashMap<>() {{
       put(Material.RAW_COPPER, Material.COPPER_INGOT);
       put(Material.RAW_IRON, Material.IRON_INGOT);
       put(Material.RAW_GOLD, Material.GOLD_INGOT);
    }};

    @EventHandler
    public void onFurnaceEvent(BlockBreakEvent event) {
        if(!CustomEnchantsEnum.FURNACE.isEnabled()) return;
        Player player = event.getPlayer();
        if (!pickaxes.contains(player.getInventory().getItemInMainHand().getType())) return;
        if (!CustomEnchants.checkEnchant(player.getInventory().getItemInMainHand(), CustomEnchantsEnum.FURNACE)) return;
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;
        Block block = event.getBlock();
        if (ores.contains(block.getType())) {
            event.setDropItems(false);
            Collection<ItemStack> drops = block.getDrops(player.getInventory().getItemInMainHand());
            drops.forEach(
                i -> block.getWorld().dropItem(block.getLocation(), smeltedOres.containsKey(i.getType()) ? new ItemStack(smeltedOres.get(i.getType()), i.getAmount()) : i)
            );
        }
    }
}
