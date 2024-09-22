package fansyr.protect_core.Commands.funny;

import fansyr.protect_core.Commands.Target_selector;
import fansyr.protect_core.Commands.interfaces.getEntity;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RanPlayer implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof BlockCommandSender) {
            sender.sendMessage(ChatColor.RED + "此命令不能由命令方块执行。");
            return true;
        }
        if(args.length == 1) {
            List<Entity> choose = Target_selector.seletor(args[0],sender);
            if(choose.isEmpty()) {
                sender.sendMessage(ChatColor.RED + "没有找到玩家");
                return false;
            }
            if(!args[0].equals("@r")) {
                Command.broadcastCommandMessage(sender, "选择了" + getEntity.combinePlayerNames(choose));
            } else {
                Command.broadcastCommandMessage(sender, "随机选择到了" + getEntity.combinePlayerNames(choose));
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            // 获取当前输入的参数
            String currentArg = args.length > 0 ? args[0] : "";

            // 根据当前输入的参数进行不同的补全
            if (currentArg.equals("@p")) {
                // 用户输入了 @p，提示 [
                return List.of("@p[");
            } else if (currentArg.startsWith("@p[")) {
                // 用户输入了 @p[，提示 self
                if (currentArg.endsWith("[")) {
                    return List.of("@p[self=");
                } else if (currentArg.endsWith("[self=")) {
                    // 用户输入了 @p[self=，提示 true 和 false
                    return List.of("@p[self=true", "@p[self=false");
                } else if (currentArg.endsWith("[self=true") ) {
                    // 用户输入了 @p[self=true 或 @p[self=false，提示 ]
                    return List.of("[self=true]");
                } else if  (currentArg.endsWith("[self=false")) {
                    // 用户输入了 @p[self=true 或 @p[self=false，提示 ]
                    return List.of("[self=false]");
                }
            } else {
                // 如果不是 @p 选择器，返回其他可能的选择器
                List<String> Params = new ArrayList<>();
                Params.add("@r");
                Params.add("@a");
                Params.add("@e");
                Params.add("@s");
                Params.add("@p");
                Params.add("@f");

                // 过滤出以输入与字符串开头匹配的参数
                List<String> LastParams = new ArrayList<>();
                for (String shows : Params) {
                    if (shows.toLowerCase().startsWith(currentArg.toLowerCase())) {
                        LastParams.add(shows);
                    }
                }
                return LastParams;
            }
        }
        return null;
    }
}
