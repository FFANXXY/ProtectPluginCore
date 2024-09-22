package fansyr.protect_core.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandKI implements CommandExecutor {
    //自杀
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof BlockCommandSender) {
            sender.sendMessage(ChatColor.RED + "此命令不能由命令方块执行。");
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c只有玩家可以使用此命令.");
            return true;
        }
        if (sender instanceof Player) {
            if (args.length == 0) {
                Player player = (Player) sender;
                player.playSound(player, "entity.player.hurt", 1, 1);
                player.setHealth(0);
                return true;
            } else {
                Player player = (Player) sender;
                player.sendMessage("unknown command");
                return true;
            }
        }

        return false;
    }
}

