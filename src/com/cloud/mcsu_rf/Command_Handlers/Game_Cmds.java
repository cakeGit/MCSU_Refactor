package com.cloud.mcsu_rf.Command_Handlers;

import com.cloud.mcsu_rf.Game_Handlers.Game_Countdown_Task;
import com.cloud.mcsu_rf.Game_Handlers.Game_Main;
import com.cloud.mcsu_rf.MCSU_Main;
import com.cloud.mcsu_rf.Objects.Game;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Game_Cmds {

    public static MCSU_Main plugin = MCSU_Main.getPlugin(MCSU_Main.class);

    public static boolean listGames(CommandSender Sender, Command Cmd, String Label, String[] Args) {

        Sender.sendMessage("All registered games");

        for (Game game : Game_Main.RegisteredGames) {
            Sender.sendMessage("  " + game.getName());
        }

        return true;

    }

    public static boolean playGame(CommandSender Sender, Command Cmd, String Label, String[] Args) {

        Game_Countdown_Task gameStartCountdownTask;

        Sender.sendMessage(ChatColor.YELLOW + "Warning: If possible use /queuegame instead");
        Sender.sendMessage("Loading game " + Args[0]);

                    Sender.sendMessage("Loading game thinge");
                    for (org.bukkit.World World : Sender.getServer().getWorlds()) {
                        Sender.sendMessage(World.getName());
                        Sender.sendMessage(((Player) Sender).getWorld().getName());
                    }
                    Sender.sendMessage("shown you the" + Args[0]);

                    Game_Main.loadGame(
                            Args[0],
                            (Player) Sender,
                            Args
                    );
                    gameStartCountdownTask = new Game_Countdown_Task();
                    gameStartCountdownTask.runTaskTimer(plugin, 0, 20);
        return true;

    }

}
