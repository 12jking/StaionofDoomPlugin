package de.j.stationofdoom.util;

import de.j.deathMinigames.main.Config;
import de.j.stationofdoom.main.Main;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.stream.Stream;

public class Tablist {
    private volatile static String serverName = Bukkit.getServer().getName();
    private volatile static String hostedBy = null;

    private static Scoreboard scoreboard;
    public static HashMap<Player, String> rank;
    //Map that contains the player as key and the header and footer as array
    private final HashMap<Audience, Component[]> playerListMap = new HashMap<>();

    public void setScoreboard() {
        assert !Main.isFolia();
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        rank = new HashMap<>();

        scoreboard.registerNewTeam("0Host");
        scoreboard.registerNewTeam("1Admin");
        scoreboard.registerNewTeam("2Developer");
        scoreboard.registerNewTeam("4Spieler");
        scoreboard.registerNewTeam("5AFK");

        MiniMessage mm = MiniMessage.miniMessage();

        scoreboard.getTeam("0Host").prefix(Component.text("Host ")
                .color(NamedTextColor.DARK_RED).decoration(TextDecoration.BOLD, true)
                .append(Component.text("| ").color(NamedTextColor.DARK_GRAY)));
        //scoreboard.getTeam("0Host")
        //        .prefix(mm.deserialize("<rainbow>Host </rainbow><dark_gray>| </dark_gray>"));
        scoreboard.getTeam("1Admin").prefix(Component.text("Admin ")
                .color(NamedTextColor.RED)
                .append(Component.text("| ").color(NamedTextColor.DARK_GRAY)));
        scoreboard.getTeam("2Developer").prefix(Component.text("Dev ")
                .color(NamedTextColor.GOLD)
                .append(Component.text("| ").color(NamedTextColor.DARK_GRAY)));
        scoreboard.getTeam("4Spieler").prefix(Component.text(""));
        scoreboard.getTeam("5AFK").prefix(Component.text("[")
                .color(NamedTextColor.DARK_BLUE)
                .append(Component.text("AFK")
                        .color(NamedTextColor.DARK_AQUA)
                        .append(Component.text("] ")
                                .color(NamedTextColor.DARK_BLUE)
                                .append(Component.text("| ").color(NamedTextColor.DARK_GRAY)))));

        for (Player on : Bukkit.getOnlinePlayers()){
            setTeam(on);
        }
    }

    public void setScoreboard(Player player, boolean afk) {
        assert !Main.isFolia();
        scoreboard.getTeam("0Host").prefix(Component.text("Host ")
                .color(NamedTextColor.DARK_RED).decoration(TextDecoration.BOLD, true)
                .append(Component.text("| ").color(NamedTextColor.DARK_GRAY)));
        scoreboard.getTeam("1Admin").prefix(Component.text("Admin ")
                .color(NamedTextColor.RED)
                .append(Component.text("| ").color(NamedTextColor.DARK_GRAY)));
        scoreboard.getTeam("2Developer").prefix(Component.text("Dev ")
                .color(NamedTextColor.GOLD)
                .append(Component.text("| ").color(NamedTextColor.DARK_GRAY)));
        scoreboard.getTeam("4Spieler").prefix(Component.text(""));
        scoreboard.getTeam("5AFK").prefix(Component.text("[")
                .color(NamedTextColor.DARK_BLUE)
                .append(Component.text("AFK")
                        .color(NamedTextColor.DARK_AQUA)
                        .append(Component.text("] ")
                                .color(NamedTextColor.DARK_BLUE)
                                .append(Component.text("| ").color(NamedTextColor.DARK_GRAY)))));

        setTeam(player, afk);

    }

    private void setTeam(Player player) {
        assert !Main.isFolia();
        String team = null;
        switch (player.getUniqueId().toString()) {
            case "050fee27-a1cc-4e78-953a-7cefaf0849a1" -> {//LP
                team = "0Host";
                rank.put(player, ChatColor.RED + "" + ChatColor.BOLD + "[Host]" + ChatColor.RESET + " ");
            }
            case "0565369c-ec68-4e7e-a90f-3492eb7002d8" -> {//MDHD
                team = "1Admin";
                rank.put(player, ChatColor.BLUE + "" + ChatColor.BOLD + "[Admin]" + ChatColor.RESET + " ");
            }
            //case "" -> {
            //    team = "2Developer";
            //    rank.put(player, ChatColor.GRAY + "[Dev]" + ChatColor.RESET + " ");
            //}
        }

        if (team == null) {
            team = "4Spieler";
            rank.put(player, "");
        }

        scoreboard.getTeam(team).addPlayer(player);
        player.setScoreboard(scoreboard);
    }

    private void setTeam(Player player, boolean afk) {
        assert !Main.isFolia();
        String team;
        if (afk) {
            team = "5AFK";
            rank.put(player, ChatColor.DARK_BLUE + "[" + ChatColor.DARK_AQUA + "AFK" + ChatColor.DARK_BLUE + "]");

            scoreboard.getTeam(team).addPlayer(player);
            player.setScoreboard(scoreboard);
        } else
            setTeam(player);
    }

    public void tab(Audience player, Component header, Component footer) {
        player.sendPlayerListHeaderAndFooter(header, footer);
    }

    public void tabTPS(Audience player, Component header, Component footer) {
        if (playerListMap.containsKey(player)) {
            if (playerListMap.get(player) != Stream.of(header, footer).toArray(Component[]::new)) {
                if (!PlainTextComponentSerializer.plainText().serialize(playerListMap.get(player)[0]).equals(PlainTextComponentSerializer.plainText().serialize(header))) {
                    player.sendPlayerListHeader(header);
                }
                if (!PlainTextComponentSerializer.plainText().serialize(playerListMap.get(player)[1]).equals(PlainTextComponentSerializer.plainText().serialize(footer))) {
                    player.sendPlayerListFooter(footer);
                }
            }

        } else
            player.sendPlayerListHeaderAndFooter(header, footer);
        playerListMap.put(player, Stream.of(header, footer).toArray(Component[]::new));
    }

    public void setAFK(Player player, boolean afk) {
        assert !Main.isFolia();
        if (afk) {
            setScoreboard(player, afk);
        } else
            setScoreboard(player, false);

    }

    public Component getTimeComponent(Player player) {
        double time = (player.getWorld().getFullTime() / 1000.0 + 6) % 24;
        String formattedTime = String.format("%02d:%02d", (int) time, (int) ((time % 1) * 60));
        return Component.text(formattedTime).color(NamedTextColor.GOLD);
    }

    public static String getServerName() {
        return serverName;
    }

    public static void setServerName(String serverName) {
        Tablist.serverName = serverName;
        if(Config.getInstance().getServerName() == null) Config.getInstance().setServerName(serverName);
        else if(!Config.getInstance().getServerName().equals(serverName)) {
            Config.getInstance().setServerName(serverName);
        }
    }

    public static void setHostedBy(String hostedBy) {
        if(hostedBy == null) {
            Main.getMainLogger().warning("Hosted by is null");
            return;
        }
        Tablist.hostedBy = hostedBy;
        if(Config.getInstance().getHostedBy() == null || !Config.getInstance().getHostedBy().equals(hostedBy)) {
            Config.getInstance().setHostedBy(hostedBy);
        }
    }

    public static String getHostedBy() {
        return hostedBy;
    }
}
