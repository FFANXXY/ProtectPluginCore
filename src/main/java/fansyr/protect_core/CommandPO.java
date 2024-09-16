package fansyr.protect_core;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CommandPO implements CommandExecutor {

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c只有玩家可以使用此命令.");
            return true;
        }

            if (sender instanceof Player) {
                Player player = (Player) sender;
                if(args.length == 1) {
                    if (args[0].equals("1")) {
                        PotionEffect nightVisionEffect = new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false);
                        player.addPotionEffect(nightVisionEffect);
                    } else if (args[0].equals("0")) {
                        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                    }
                }else {

                }
            }
            return false;

    }
}

