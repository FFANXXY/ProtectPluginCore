package fansyr.protect_core;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CommandKI implements CommandExecutor {
    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c只有玩家可以使用此命令.");
            return true;
        }
        if (sender instanceof Player) {
            if (args.length == 0) {
                Player player = (Player) sender;
                GameMode gameMode = player.getGameMode();
                player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                player.setGameMode(GameMode.SURVIVAL);
                player.damage(2147483647);
                player.setGameMode(gameMode);
            } else {
                Player player = (Player) sender;
                player.sendMessage("unknown command");
            }
        }

        return false;
    }
}

