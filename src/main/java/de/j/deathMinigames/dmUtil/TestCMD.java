package de.j.deathMinigames.dmUtil;

import de.j.stationofdoom.util.Tablist;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TestCMD implements BasicCommand {

    /**
     * Determines if the given command sender can use this command.
     *
     * <p>This command can only be used by {@link Player}s.
     *
     * @param sender the command sender to check
     * @return true if the command sender can use this command, false otherwise
     */
    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        return sender instanceof Player;
    }

    /**
     * Executes the command for the given command source stack.
     *
     * <p>
     * This method retrieves the player from the command source stack and sends a test message followed by the
     * database connection information. Each key-value pair in the connection info is sent as a message to the player.
     *
     * @param stack the command source stack
     * @param args the arguments provided with the command
     */
    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        Player player = (Player) stack.getSender();
        player.sendMessage("test");
        Tablist.setHostetBy(args[0]);
    }
}
