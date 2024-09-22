package fansyr.protect_core.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class say implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof BlockCommandSender) {
            sender.sendMessage(ChatColor.RED + "此命令不能由命令方块执行。");
            return true;
        }
        // 检查参数数量
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "请提供要广播的消息内容！");
            return false;
        }

        // 将所有的参数拼接成一条消息
        StringBuilder messageBuilder = new StringBuilder();
        for (String arg : args) {
            messageBuilder.append(arg).append(" ");
        }
        String message = ChatColor.translateAlternateColorCodes('&', messageBuilder.toString().trim());

        // 构建带有发送者名字的消息
        String broadcastMessage;
        if (sender instanceof Player) {
            Player player = (Player) sender;
            broadcastMessage = ChatColor.AQUA + "[" + player.getName() + "] " + ChatColor.RESET + message;
        } else {
            // 如果是控制台发送的广播
            broadcastMessage = ChatColor.GOLD + "[服务器] " + ChatColor.RESET + message;
        }

        // 给每个在线玩家发送消息
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendMessage(broadcastMessage);
        }

        // 可选：给命令发送者反馈
        sender.sendMessage(ChatColor.GREEN + "消息已成功广播给所有在线玩家。");

        return true;
    }
}
