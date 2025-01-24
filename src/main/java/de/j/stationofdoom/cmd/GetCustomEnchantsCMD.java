package de.j.stationofdoom.cmd;

import de.j.stationofdoom.enchants.CustomEnchants;
import de.j.stationofdoom.enchants.CustomEnchantsEnum;
import de.j.stationofdoom.main.Main;
import de.j.stationofdoom.util.translations.TranslationFactory;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class GetCustomEnchantsCMD implements BasicCommand {

    @Override
    public void execute(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {
        assert commandSourceStack.getSender() instanceof Player;
        Player player = (Player) commandSourceStack.getSender();
        TranslationFactory translations = new TranslationFactory();
        if (args.length == 1) {
            MiniMessage mm = MiniMessage.miniMessage();
            switch (args[0].toLowerCase()) {
                case "telepathy" -> {
                    if(!CustomEnchantsEnum.TELEPATHY.isEnabled()) {
                        player.sendMessage(Component.text(translations.getTranslation(player, "enchantNotEnabled")).color(NamedTextColor.RED));
                        return;
                    }
                    player.sendMessage(mm.deserialize(translations.getTranslation(player, "BuyCustomEnchant", "<red>", "<aqua>", "Telepathy", "<red>", "<bold>", CustomEnchantsEnum.TELEPATHY.getPrice(), "<reset>", "<red>")));
                    Component f = Component
                            .text(translations.getTranslation(player, "ClickThisMessage"))
                            .color(NamedTextColor.RED)
                            .clickEvent(ClickEvent.runCommand("/customenchant telepathy confirm"));
                    player.sendMessage(f);
                }
                case "flight" -> {
                    if(!CustomEnchantsEnum.FLIGHT.isEnabled()) {
                        player.sendMessage(Component.text(translations.getTranslation(player, "enchantNotEnabled")).color(NamedTextColor.RED));
                        return;
                    }
                    player.sendMessage(mm.deserialize(translations.getTranslation(player, "BuyCustomEnchant", "<red>", "<aqua>", "Flight", "<red>", "<bold>", CustomEnchantsEnum.FLIGHT.getPrice(), "<reset>", "<red>")));
                    Component f = Component
                            .text(translations.getTranslation(player, "ClickThisMessage"))
                            .color(NamedTextColor.RED)
                            .clickEvent(ClickEvent.runCommand("/customenchant flight confirm"));
                    player.sendMessage(f);
                }
                case "furnace" -> {
                    if(!CustomEnchantsEnum.FURNACE.isEnabled()) {
                        player.sendMessage(Component.text(translations.getTranslation(player, "enchantNotEnabled")).color(NamedTextColor.RED));
                        return;
                    }
                    player.sendMessage(mm.deserialize(translations.getTranslation(player, "BuyCustomEnchant", "<red>", "<aqua>", "Furnace", "<red>", "<bold>", CustomEnchantsEnum.FURNACE.getPrice(), "<reset>", "<red>")));
                    Component f = Component
                            .text(translations.getTranslation(player, "ClickThisMessage"))
                            .color(NamedTextColor.RED)
                            .clickEvent(ClickEvent.runCommand("/customenchant furnace confirm"));
                    player.sendMessage(f);
                }
                default ->
                        Main.getMainLogger().severe("How did we end up here? Please contact the dev or the admin");
            }
        } else if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "telepathy":
                    if(!CustomEnchantsEnum.TELEPATHY.isEnabled()) {
                        player.sendMessage(Component.text(translations.getTranslation(player, "enchantNotEnabled")).color(NamedTextColor.RED));
                        return;
                    }
                    if (args[1].equalsIgnoreCase("confirm")) {
                        Inventory inventory = player.getInventory();
                        if (inventory.contains(Material.DIAMOND)) {
                            for (ItemStack itemStack : inventory.getContents()) {
                                if (itemStack != null) {
                                    if (itemStack.getType() == Material.DIAMOND) {
                                        if (itemStack.getAmount() >= 10) {
                                            ItemStack item = player.getInventory().getItemInMainHand();
                                            if (item.getType() == Material.DIAMOND_PICKAXE || item.getType() == Material.NETHERITE_PICKAXE || item.getType() == Material.DIAMOND_AXE || item.getType() == Material.NETHERITE_AXE || item.getType() == Material.DIAMOND_SHOVEL || item.getType() == Material.NETHERITE_SHOVEL) {

                                                CustomEnchants.enchant(item, CustomEnchantsEnum.TELEPATHY, player);

                                                itemStack.setAmount(itemStack.getAmount() - CustomEnchantsEnum.TELEPATHY.getPrice());
                                            } else
                                                player.sendMessage(Component.text(translations.getTranslation(player, "TakeDiaOrNetheriteToolInHand"))
                                                        .color(NamedTextColor.RED));
                                            break;
                                        }
                                    }
                                }

                            }
                        } else
                            player.sendMessage(Component.text(translations.getTranslation(player, "NoDia")));
                        break;
                    }

                case "flight":
                    if(!CustomEnchantsEnum.FLIGHT.isEnabled()) {
                        player.sendMessage(Component.text(translations.getTranslation(player, "enchantNotEnabled")).color(NamedTextColor.RED));
                        return;
                    }
                    if (args[1].equalsIgnoreCase("confirm")) {
                        Inventory inventory = player.getInventory();
                        if (inventory.contains(Material.DIAMOND)) {
                            for (ItemStack itemStack : inventory.getContents()) {
                                if (itemStack != null) {
                                    if (itemStack.getType() == Material.DIAMOND) {
                                        if (itemStack.getAmount() >= 10) {
                                            ItemStack item = player.getInventory().getItemInMainHand();
                                            if (item.getType() == Material.DIAMOND_SWORD || item.getType() == Material.NETHERITE_SWORD) {
                                                CustomEnchants.enchant(item, CustomEnchantsEnum.FLIGHT, player);
                                                itemStack.setAmount(itemStack.getAmount() - CustomEnchantsEnum.FLIGHT.getPrice());
                                            } else
                                                player.sendMessage(Component.text(translations.getTranslation(player, "TakeDiaOrNetheriteSwordInHand"))
                                                        .color(NamedTextColor.RED));
                                            break;
                                        }
                                    }
                                }

                            }
                        } else
                            player.sendMessage(Component.text(translations.getTranslation(player, "NoDia")));
                        break;
                    }

                case "furnace":
                    if(!CustomEnchantsEnum.FURNACE.isEnabled()) {
                        player.sendMessage(Component.text(translations.getTranslation(player, "enchantNotEnabled")).color(NamedTextColor.RED));
                        return;
                    }
                    if (args[1].equalsIgnoreCase("confirm")) {
                        Inventory inventory = player.getInventory();
                        if (inventory.contains(Material.DIAMOND)) {
                            for (ItemStack itemStack : inventory.getContents()) {
                                if (itemStack != null) {
                                    if (itemStack.getType() == Material.DIAMOND) {
                                        if (itemStack.getAmount() >= CustomEnchantsEnum.FURNACE.getPrice()) {
                                            ItemStack item = player.getInventory().getItemInMainHand();
                                            if (item.getType() == Material.DIAMOND_PICKAXE || item.getType() == Material.NETHERITE_PICKAXE) {
                                                CustomEnchants.enchant(item, CustomEnchantsEnum.FURNACE, player);
                                                itemStack.setAmount(itemStack.getAmount() - CustomEnchantsEnum.FURNACE.getPrice());
                                            } else
                                                player.sendMessage(Component.text(translations.getTranslation(player, "TakeDiaOrNetheritePickaxeInHand"))
                                                        .color(NamedTextColor.RED));
                                            break;
                                        }
                                    }
                                }

                            }
                        } else
                            player.sendMessage(Component.text(translations.getTranslation(player, "NoDia")));
                        break;
                    }

                default:
                    Main.getMainLogger().severe("How did we end up here? Please contact the dev or the admin");
            }
        }
    }

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        return sender instanceof Player;
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {
        Collection<String> suggestions = new ArrayList<>();
        if (args.length == 0) {
            if(CustomEnchantsEnum.TELEPATHY.isEnabled()) {
                suggestions.add("telepathy");
            }
            if(CustomEnchantsEnum.FLIGHT.isEnabled()) {
                suggestions.add("flight");
            }
            if(CustomEnchantsEnum.FURNACE.isEnabled()) {
                suggestions.add("furnace");
            }
        }
        return suggestions;
    }
}
