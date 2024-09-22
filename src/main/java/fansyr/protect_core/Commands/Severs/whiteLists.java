package fansyr.protect_core.Commands.Severs;

import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static fansyr.protect_core.Protect_core.whiteList;

public class whiteLists implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // 检查命令发送者是否为命令方块
        if (sender instanceof BlockCommandSender) {
            sender.sendMessage(ChatColor.RED + "此命令不能由命令方块执行。");
            return true;
        }

        // 检查命令发送者是否为控制台
        if (sender instanceof ConsoleCommandSender) {
            // 检查参数数量
            if (args.length == 2) {
                // 检查子命令
                if (args[1].equalsIgnoreCase("add")) {
                    sender.sendMessage(ChatColor.GREEN + "正在添加...");
                    try {
                        whiteList.temporary_add(args[0]); // 这里应该是 args[0] 而不是 args[1]
                    } catch (Exception e) {
                        sender.sendMessage(ChatColor.RED + "发生错误！");
                        e.printStackTrace(); // 使用 printStackTrace 来记录错误信息
                    }
                } else if (args[1].equalsIgnoreCase("remove")) {
                    sender.sendMessage(ChatColor.GREEN + "正在移除...");
                    try {
                        whiteList.temporary_remove(args[0]); // 这里应该是 args[0] 而不是 args[1]
                    } catch (Exception e) {
                        sender.sendMessage(ChatColor.RED + "发生错误！");
                        e.printStackTrace(); // 使用 printStackTrace 来记录错误信息
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "未知的子命令: " + args[1]);
                }
            } else if (args.length == 1 && args[0].equalsIgnoreCase("get")) {
                sender.sendMessage(ChatColor.GREEN + "当前白名单:");
                List<String> whitelist = whiteList.getWhiteList();
                for (String player : whitelist) {
                    sender.sendMessage(ChatColor.AQUA + "- " + player);
                }
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "参数错误！使用方法: /wl <玩家名> add|remove|get");
                return true;
            }
        } else {
            sender.sendMessage(ChatColor.RED + "此命令只能由控制台执行。");
            return true;
        }


        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        // 只有当发送者是控制台时才提供自动补全
        if (sender instanceof ConsoleCommandSender) {
            // 提供的子命令选项
            List<String> subCommands = new ArrayList<>();
            subCommands.add("add");
            subCommands.add("remove");
            subCommands.add("get");

            // 如果已经有两个参数，则不需要补全
            if (args.length == 2) {
                // 过滤出以输入与字符串开头匹配的子命令
                List<String> matchingSubCommands = new ArrayList<>();
                for (String subCommand : subCommands) {
                    if (subCommand.toLowerCase().startsWith(args[1].toLowerCase())) {
                        matchingSubCommands.add(subCommand);
                    }
                }
                return matchingSubCommands;
            }
        }

        return null;
    }
}