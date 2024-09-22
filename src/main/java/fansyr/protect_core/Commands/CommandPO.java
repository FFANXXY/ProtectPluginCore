package fansyr.protect_core.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class CommandPO implements CommandExecutor, TabCompleter {

    // 给予夜视效果
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
                Player player = (Player) sender;
                if(args.length == 1) {
                    if (args[0].equals("true")) {
                        give_night_vision_effect(player);
                    } else if (args[0].equals("false")) {
                        clear_night_vision_effect(player);
                    }
                }else if(args.length == 0){
                    if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                        clear_night_vision_effect(player);
                    } else {
                        give_night_vision_effect(player);
                    }
                } else {
                    player.sendMessage("§c请输入正确的参数.");
                }
            }
            return true;

    }

    private void give_night_vision_effect(Player player) {
        PotionEffect nightVisionEffect = new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false);
        player.addPotionEffect(nightVisionEffect);
        player.sendMessage("已给予夜视效果");
    }
    private void clear_night_vision_effect(Player player) {
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        player.sendMessage("已移除夜视效果");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {//是否为玩家
            List<String> Params = new ArrayList<>();
            Params.add("true");
            Params.add("false");
            // 过滤出以输入与字符串开头匹配的参数
            List<String> LastParams = new ArrayList<>();
            for (String shows : Params) {
                if (shows.toLowerCase().startsWith(args[0].toLowerCase())) {
                    LastParams.add(shows);
                }
            }
            return LastParams;
        }
        return null;
    }
}

