package fansyr.protect_core.Commands;

import fansyr.protect_core.Commands.interfaces.getEntity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.*;

import static fansyr.protect_core.Commands.interfaces.getEntity.*;
import static org.bukkit.Bukkit.getPlayer;

public class Target_selector {

    //目标选择器
    public static List<Entity> seletor(String arg, CommandSender sender) {
        if (arg.startsWith("@")) {
            return seletor_at(arg, sender);
        } else {
            // 尝试通过玩家名称获取玩家
            Player player = getPlayer(arg);
            if (player != null) {
                List<Entity> entityList = new ArrayList<>();
                entityList.add(player);
                return entityList;
            } else {
                // 如果不是有效的玩家名称，尝试通过UUID获取实体
                boolean isUUID = getEntity.isValidUUID(arg);
                if (isUUID) {
                    Entity entity = getEntity.getEntityByUUID(getEntity.toUUID(arg));
                    if (entity != null) {
                        List<Entity> entityList = new ArrayList<>();
                        entityList.add(entity);
                        return entityList;
                    }
                }
            }
        }
        return new ArrayList<>(); // 如果都没有找到，返回空列表
    }

    //@目标选择器
    private static List<Entity> seletor_at(String arg,CommandSender sender){
        if(arg.startsWith("@a")) {
            List<Entity> e = Bukkit.getWorlds().stream()
                    .flatMap(world -> world.getEntities().stream())
                    //.filter(entity -> entity instanceof !!!) !!!处写上类型
                    .filter(entity -> entity instanceof Player)
                    .toList();
            return e;
        }else if(arg.startsWith("@p")) {
            Location senderLocation = getLocation(sender);
            if (senderLocation == null) {
                return new ArrayList<>(); // 如果没有位置信息，返回空列表
            }

            // 解析 [self=false] 参数
            List<String> params = parseParameters(arg);
            boolean excludeSelf = Upchoose(params, sender);

            // 获取最近的玩家
            Player nearestPlayer = Bukkit.getOnlinePlayers().stream()
                    .filter(player -> !excludeSelf || !player.equals(sender))
                    .min(Comparator.comparingDouble(player -> player.getLocation().distance(senderLocation)))
                    .orElse(null);

            // 创建一个包含最近玩家的 List<Entity>
            List<Entity> nearestPlayerList = new ArrayList<>();
            if (nearestPlayer != null) {
                nearestPlayerList.add(nearestPlayer);
            }

            return nearestPlayerList;
        }else if(arg.startsWith("@f")) {
            Location senderLocation = getLocation(sender);
            if (senderLocation == null) {
                return new ArrayList<>(); // 如果没有位置信息，返回空列表
            }

            // 解析 [self=false] 参数
            List<String> params = parseParameters(arg);
            boolean excludeSelf = Upchoose(params, sender);

            // 获取最远的玩家
            Player nearestPlayer = Bukkit.getOnlinePlayers().stream()
                    .filter(player -> !excludeSelf || !player.equals(sender))
                    .max(Comparator.comparingDouble(player -> player.getLocation().distance(senderLocation)))
                    .orElse(null);

            // 创建一个包含最近玩家的 List<Entity>
            List<Entity> nearestPlayerList = new ArrayList<>();
            if (nearestPlayer != null) {
                nearestPlayerList.add(nearestPlayer);
            }

            return nearestPlayerList;
        }else if(arg.startsWith("@e[type=item]")) {
            List<Entity> e = Bukkit.getWorlds().stream()
                    .flatMap(world -> world.getEntities().stream())
                    .filter(entity -> entity instanceof Item)
                    .toList();
            return e;

        }
        else if(arg.startsWith("@e")) {
            List<Entity> e = Bukkit.getWorlds().stream()
                    .flatMap(world -> world.getEntities().stream())
                    //.filter(entity -> entity instanceof !!!) !!!处写上类型
                    .filter(entity -> entity instanceof LivingEntity)
                    .toList();
            return e;

        }else if(arg.startsWith("@s")) {
            List<Entity> entityList = new ArrayList<>();
            // 将 Player 添加到 List<Entity> 中
            if (sender instanceof BlockCommandSender) {
                sender.sendMessage(ChatColor.RED + "找不到目标:@s");
                return null;
            }else if(sender instanceof ConsoleCommandSender) {
                sender.sendMessage(ChatColor.RED + "无法选择服务器!");
                return null;
            } else if (sender instanceof Player) {
                entityList.add((Player) sender);
            }
            return entityList;
        } else if(arg.startsWith("@r")) {
            Random random = new Random();
            List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());

            if (onlinePlayers.isEmpty()) {
                return getEntity.optionalEntityToList(Optional.empty());
            }

            int index = random.nextInt(onlinePlayers.size());
            return getEntity.optionalEntityToList((Optional.of(onlinePlayers.get(index))));
        }else {
            return null;
        }


    }

    private static List<String> parseParameters(String arg) {
        int start = arg.indexOf('[');
        int end = arg.indexOf(']');
        if (start == -1 || end == -1) {
            return new ArrayList<>();
        }
        String paramsStr = arg.substring(start + 1, end);
        return List.of(paramsStr.split(","));
    }

    private static boolean Upchoose(List<String> params, CommandSender sender) {
        for (String param : params) {
            if (param.trim().startsWith("self=")) {
                String value = param.trim().substring(param.trim().indexOf("=") + 1).toLowerCase();
                if (value.equals("false")) {
                    return true;
                } else if (value.equals("true") && sender instanceof Player) {
                    return false;
                }
            }
        }
        return false; // 默认不排除自己
    }


}
