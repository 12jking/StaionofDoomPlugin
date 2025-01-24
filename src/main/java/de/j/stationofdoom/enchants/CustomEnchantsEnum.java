package de.j.stationofdoom.enchants;

import de.j.stationofdoom.util.translations.Translation;
import de.j.stationofdoom.util.translations.TranslationFactory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public enum CustomEnchantsEnum {

    TELEPATHY("Telepathy", new Translation("TelepathyEnchantment"), 10),
    FLIGHT("Flight", new Translation("FlightEnchantment"), 32),
    FURNACE("Furnace", new Translation("FurnaceEnchantment"), 20);

    private final String name;
    private final Translation loreName;
    private final int price;
    private final TranslationFactory tf;
    private boolean enabled;

    CustomEnchantsEnum(String name, Translation loreName, int price) {
        this.name = name;
        this.loreName = loreName;
        this.price = price;
        tf = new TranslationFactory();
        this.enabled = true;
    }

    public String getName() {
        return name;
    }

    public Component getLoreName(Player player) {
        return Component.text(tf.getTranslation(player, loreName.getKey())).color(NamedTextColor.GRAY);
    }

    public int getPrice() {
        return price;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return this.enabled;
    }
}
