package de.j.stationofdoom.crafting;

import de.j.stationofdoom.main.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

public class DebugStick extends ShapelessRecipe {

    public DebugStick() {
        super(new NamespacedKey(Main.getPlugin(), "DebugStick"), new ItemStack(Material.DEBUG_STICK));
        addIngredient(1, Material.DIAMOND);
        addIngredient(1, Material.STICK);
    }

    /*public ShapelessRecipe debugStickRecipe() {
        NamespacedKey key = new NamespacedKey(Main.getPlugin(), "DebugStick");
        ItemStack item = new ItemStack(Material.DEBUG_STICK);

        ShapelessRecipe recipe = new ShapelessRecipe(key, item);
        recipe.addIngredient(1, Material.DIAMOND);
        recipe.addIngredient(1, Material.STICK);

        return recipe;
    }*/
}
