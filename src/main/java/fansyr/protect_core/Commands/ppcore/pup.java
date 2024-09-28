package fansyr.protect_core.Commands.ppcore;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class pup implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(ChatColor.RED + "语法: /pup <update>");
        } else if(args.length == 1) {
            if(args[0].equals("update")) {
                sender.sendMessage(ChatColor.GREEN + "ProtectPluginCore 更新日志");
                sender.sendMessage(ChatColor.GREEN + "Meta 9.0.0:");
                sender.sendMessage(ChatColor.GREEN + "1. 修复了一些bug");
                sender.sendMessage(ChatColor.GREEN + "2. 修复一些漏洞");
                sender.sendMessage(ChatColor.GREEN + "3. 修改了一些代码");
                sender.sendMessage(ChatColor.GREEN + "4. 修改了一些配置文件");
                sender.sendMessage(ChatColor.GREEN + "5. 修改了一些文件");
                sender.sendMessage(ChatColor.GREEN + "6. 修改了一些注释");
                sender.sendMessage(ChatColor.AQUA + " ! 7. 新增@效果 @player + <空格> 或 @ + <空格> 可以直接代指所有玩家");
                sender.sendMessage(ChatColor.AQUA + " | --使用@时可以提醒玩家");
                sender.sendMessage(ChatColor.GREEN + "8. 修改更新日志机制");
                sender.sendMessage(ChatColor.AQUA + "！9.添加pup指令实现实用操作 (目前只有update)");
                sender.sendMessage(ChatColor.GREEN + "10 无");
                sender.sendMessage(ChatColor.GREEN + "11 无");
            }
        }
        return true;
    }
}
