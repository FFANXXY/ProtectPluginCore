package fansyr.protect_core;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class CommandGM implements CommandExecutor {

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c只有玩家可以使用此命令.");
            return true;
        }

            if (sender instanceof Player) {
                if(args.length == 1) {
                    if (args[0].equals("1")) {
                        Player player = (Player) sender;
                        player.setGameMode(GameMode.CREATIVE);
                        player.sendMessage("已经设置为创造模式");
                        return true;
                    } else if (args[0].equals("0")) {
                        Player player = (Player) sender;
                        player.setGameMode(GameMode.SURVIVAL);
                        player.sendMessage("已经设置为生存模式");
                        return true;
                    } else if (args[0].equals("2")) {
                        Player player = (Player) sender;
                        player.setGameMode(GameMode.SPECTATOR);
                        player.sendMessage("已经设置为旁观模式");
                        return true;
                    } else if (args[0].equals("3")) {
                        Player player = (Player) sender;
                        player.setGameMode(GameMode.ADVENTURE);
                        player.sendMessage("已经设置为冒险模式");
                        return true;
                    }
                }else {
                    Player player = (Player) sender;
                    player.sendMessage("unknown command");
                }
            }
            return false;

    }
}

