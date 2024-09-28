package fansyr.protect_core.Commands.disable;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class ExecuteCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        sender.sendMessage(ChatColor.DARK_RED + "" + ChatColor.MAGIC + "12343" + ChatColor.RESET + "你的指令被神秘力量拦截了!" + ChatColor.DARK_RED + ChatColor.MAGIC + "34321");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return List.of();
    }
}
