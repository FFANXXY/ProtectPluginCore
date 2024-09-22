package fansyr.protect_core.Commands.Severs;

import fansyr.protect_core.Commands.Target_selector;
import fansyr.protect_core.Protect_core;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static fansyr.protect_core.Protect_core.whiteList;

public class HideCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 3) {
            sender.sendMessage(ChatColor.RED + "用法: /hide <玩家名> for <目标选择器> | /hide <玩家名> show <目标选择器>");
            return false;
        }

        // 检查是否是命令方块
        if (sender instanceof BlockCommandSender) {
            sender.sendMessage(ChatColor.RED + "命令方块不能执行此命令");
            return false;
        }

        // 检查是否是控制台或白名单中的玩家
        if (!(sender instanceof ConsoleCommandSender)) {
            if (!whiteList.isWhiteList((Player) sender)) {
                sender.sendMessage(ChatColor.RED + "你没有权限!");
                return false;
            }
        }

        String playerName = args[0];
        String action = args[1].toLowerCase();
        String targetSelector = args[2];

        Player playerToHide = Bukkit.getPlayer(playerName);
        if (playerToHide == null) {
            sender.sendMessage(ChatColor.RED + "找不到玩家: " + playerName);
            return false;
        }

        List<Entity> targetEntities = Target_selector.seletor(targetSelector, sender);
        List<Player> targetPlayers = targetEntities.stream()
                .filter(entity -> entity instanceof Player)
                .map(entity -> (Player) entity)
                .collect(Collectors.toList());

        if (targetPlayers.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "找不到目标玩家");
            return false;
        }

        if (action.equals("for")) {
            hidePlayerForTargets(playerToHide, targetPlayers, sender);
        } else if (action.equals("show")) {
            showPlayerForTargets(playerToHide, targetPlayers, sender);
        } else {
            sender.sendMessage(ChatColor.RED + "无效的操作: " + action);
            return false;
        }

        return true;
    }

    private void hidePlayerForTargets(Player playerToHide, List<Player> targetPlayers, CommandSender sender) {
        StringBuilder targetNames = new StringBuilder();
        for (Player target : targetPlayers) {
            target.hidePlayer(playerToHide);
            if (targetNames.length() > 0) {
                targetNames.append(", ");
            }
            targetNames.append(target.getName());
        }

        // 设置元数据
        playerToHide.setMetadata("hide_for", new FixedMetadataValue(this.getPlugin(), targetNames.toString()));

        // 通知被隐藏的玩家
        playerToHide.sendMessage(ChatColor.YELLOW + "你已经被隐藏对以下玩家: " + targetNames.toString());

        // 通知命令发送者
        sender.sendMessage(ChatColor.GREEN + "已对 " + targetPlayers.size() + " 个玩家隐藏 " + playerToHide.getName() + ": " + targetNames.toString());
    }

    private void showPlayerForTargets(Player playerToHide, List<Player> targetPlayers, CommandSender sender) {
        StringBuilder targetNames = new StringBuilder();
        for (Player target : targetPlayers) {
            target.showPlayer(playerToHide);
            if (targetNames.length() > 0) {
                targetNames.append(", ");
            }
            targetNames.append(target.getName());
        }

        // 移除元数据
        playerToHide.removeMetadata("hide_for", this.getPlugin());

        // 通知被隐藏的玩家
        playerToHide.sendMessage(ChatColor.YELLOW + "你已经对以下玩家显示: " + targetNames.toString());

        // 通知命令发送者
        sender.sendMessage(ChatColor.GREEN + "已对 " + targetPlayers.size() + " 个玩家显示 " + playerToHide.getName() + ": " + targetNames.toString());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            // 提供在线玩家名的建议
            completions.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
        } else if (args.length == 2) {
            // 提供操作（for 或 show）的建议
            completions.add("for");
            completions.add("show");
        } else if (args.length == 3 || args.length == 4) {
            // 提供目标选择器的建议
            completions.add("@a");
            completions.add("@p");
            completions.add("@p[self=true]");
            completions.add("@p[self=false]");
            completions.add("@r");
        }

        return completions.stream()
                .filter(completion -> completion.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                .collect(Collectors.toList());
    }

    private Protect_core getPlugin() {
        return (Protect_core) Bukkit.getPluginManager().getPlugin("protect_core");
    }


}
