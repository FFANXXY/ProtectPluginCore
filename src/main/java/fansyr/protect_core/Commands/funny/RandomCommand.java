package fansyr.protect_core.Commands.funny;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomCommand implements CommandExecutor, TabCompleter {

    private final Random random = new Random();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof BlockCommandSender) {
            sender.sendMessage(ChatColor.RED + "此命令不能由命令方块执行。");
            return true;
        }
        if (args.length < 2 || args.length > 3) {
            sender.sendMessage(ChatColor.RED + "用法: /random <min> <max> [show=false]");
            return false;
        }

        try {
            int min = Integer.parseInt(args[0]);
            int max = Integer.parseInt(args[1]);
            boolean show = args.length == 3 && args[2].equalsIgnoreCase("true");

            if (min > max) {
                sender.sendMessage(ChatColor.RED + "最小值不能大于最大值");
                return false;
            }

            int randomNumber = random.nextInt(max - min + 1) + min;
            sender.sendMessage(ChatColor.GREEN + "你生成的随机数是: " + randomNumber);

            if (show) {
                Command.broadcastCommandMessage(sender," 生成的随机数是: " + randomNumber + " ( " + min + "到" + max + ")");
            }

            return true;
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "请输入有效的整数");
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            // 提供一些常见的最小值建议
            completions.add("0");
            completions.add("1");
            completions.add("10");
            completions.add("100");
        } else if (args.length == 2) {
            // 提供一些常见的最大值建议
            completions.add("10");
            completions.add("100");
            completions.add("1000");
            completions.add("10000");
        } else if (args.length == 3) {
            // 提供 show 参数的建议
            completions.add("true");
            completions.add("false");
        }

        return completions;
    }
}
