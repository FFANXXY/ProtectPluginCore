package fansyr.protect_core.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static fansyr.protect_core.Protect_core.whiteList;
import static org.bukkit.Bukkit.getPlayer;

public class CommandGM implements CommandExecutor, TabCompleter {

    // 游戏模式
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // 检查命令发送者是否为命令方块
        if (sender instanceof BlockCommandSender) {
            sender.sendMessage(ChatColor.RED + "此命令不能由命令方块执行。");
            return true;
        }

        // 检查命令发送者是否为控制台
        if (sender instanceof ConsoleCommandSender) {
            if (args.length == 2) {
                Player targetPlayer = getPlayerOn(args[1]);
                if (targetPlayer != null) {
                    GameMode gameMode = getGameModeFromInput(args[0]);
                    if (gameMode != null) {
                        targetPlayer.setGameMode(gameMode);
                        targetPlayer.sendMessage(ChatColor.GREEN + "服务器 已将你的游戏模式设置为 " + gameMode.name().toLowerCase() + " 模式。");
                        sender.sendMessage(ChatColor.GREEN + "已将 " + targetPlayer.getName() + " 的游戏模式设置为 " + gameMode.name().toLowerCase() + " 模式。");
                    } else {
                        sender.sendMessage(ChatColor.RED + "无效的游戏模式，请使用 0（生存）、1（创造）、2（旁观）、3（冒险）或对应的英文名。");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "没有找到名为 " + args[1] + " 的在线玩家。");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "命令语法错误: /gm <0/1/2/3/survival/creative/spectator/adventure> <玩家名>");
            }
            return true;
        }

        // 检查命令发送者是否为玩家
        if (sender instanceof Player) {
            Player player = (Player) sender;

            // 检查参数数量
            if (args.length == 1) {
                // 更改自己的游戏模式
                GameMode gameMode = getGameModeFromInput(args[0]);
                if (gameMode != null) {
                    player.setGameMode(gameMode);
                    player.sendMessage(ChatColor.WHITE + "已将自己的游戏模式设置为 " + gameMode.name().toLowerCase() + " 模式。");
                } else {
                    player.sendMessage(ChatColor.RED + "无效的游戏模式，请使用 0（生存）、1（创造）、2（旁观）、3（冒险）或对应的英文名。");
                }
            } else if (args.length == 2 && whiteList.isWhiteList(player)) {
                // 更改其他玩家的游戏模式
                Player targetPlayer = getPlayerOn(args[1]);
                if (targetPlayer != null) {
                    GameMode gameMode = getGameModeFromInput(args[0]);
                    if (gameMode != null) {
                        targetPlayer.setGameMode(gameMode);
                        targetPlayer.sendMessage(ChatColor.GREEN + player.getName() + " 已将你的游戏模式设置为 " + gameMode.name().toLowerCase() + " 模式。");
                        player.sendMessage(ChatColor.GREEN + "已将 " + targetPlayer.getName() + " 的游戏模式设置为 " + gameMode.name().toLowerCase() + " 模式。");
                    } else {
                        player.sendMessage(ChatColor.RED + "无效的游戏模式，请使用 0（生存）、1（创造）、2（旁观）、3（冒险）或对应的英文名。");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "没有找到名为 " + args[1] + " 的在线玩家。");
                }
            } else {
                player.sendMessage(ChatColor.RED + "命令语法错误: /gm <0/1/2/3/survival/creative/spectator/adventure> [玩家名]");
            }
        }

        return true;
    }

    // 获取指定名字的在线玩家
    public Player getPlayerOn(String name) {
        return Bukkit.getPlayerExact(name);
    }

    // 根据输入获取游戏模式
    private GameMode getGameModeFromInput(String input) {
        try {
            int gameModeNumber = Integer.parseInt(input);
            switch (gameModeNumber) {
                case 0:
                    return GameMode.SURVIVAL;
                case 1:
                    return GameMode.CREATIVE;
                case 2:
                    return GameMode.SPECTATOR;
                case 3:
                    return GameMode.ADVENTURE;
                default:
                    return null;
            }
        } catch (NumberFormatException e) {
            // 如果不是数字，则尝试匹配字符串
            switch (input.toLowerCase()) {
                case "survival":
                    return GameMode.SURVIVAL;
                case "creative":
                    return GameMode.CREATIVE;
                case "spectator":
                    return GameMode.SPECTATOR;
                case "adventure":
                    return GameMode.ADVENTURE;
                default:
                    return null;
            }
        }
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) { // 是否为玩家
            List<String> gameModes = new ArrayList<>();
            gameModes.add("0");
            gameModes.add("1");
            gameModes.add("2");
            gameModes.add("3");
            gameModes.add("survival");
            gameModes.add("creative");
            gameModes.add("spectator");
            gameModes.add("adventure");

            // 如果没有输入参数或只有一个参数，则返回所有可能的游戏模式
            if (args.length == 1 ) {
                // 过滤出以输入与字符串开头匹配的参数
                List<String> matchingParams = new ArrayList<>();
                for (String mode : gameModes) {
                    if (mode.toLowerCase().startsWith(args[0].toLowerCase())) {
                        matchingParams.add(mode);
                    }
                }
                return matchingParams;
            } else if (args.length == 2) {
                // 如果是第二个参数，尝试补全玩家名
                List<String> playerNames = new ArrayList<>();
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (onlinePlayer.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                        playerNames.add(onlinePlayer.getName());
                    }
                }
                return playerNames;
            }
        }

        return null;
    }
}

