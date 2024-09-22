package fansyr.protect_core.Commands.interfaces;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

//这不是一个接口
public class getEntity {
    // 检测字符串是否是有效的 UUID
    public static boolean isValidUUID(String uuidString) {
        try {
            // 尝试将字符串解析为 UUID
            UUID.fromString(uuidString);
            return true;
        } catch (IllegalArgumentException e) {
            // 如果解析失败，说明字符串不是有效的 UUID
            return false;
        }
    }
    public static UUID toUUID(String uuidString) {
        try {
            // 尝试将字符串解析为 UUID
            return UUID.fromString(uuidString);
        } catch (IllegalArgumentException e) {
            // 如果解析失败，说明字符串不是有效的 UUID
            return null;
        }
    }

    // 通过 UUID 获取指定世界的实体
    public static Entity getEntityByUUID(World world, UUID uuid) {
        for (Entity entity : world.getEntities()) {
            if (entity.getUniqueId().equals(uuid)) {
                return entity;
            }
        }
        return null; // 如果没有找到匹配的实体，返回 null
    }

    // 通过 UUID 获取所有世界中的实体
    public static Entity getEntityByUUID(UUID uuid) {
        for (World world : Bukkit.getWorlds()) {
            Entity entity = getEntityByUUID(world, uuid);
            if (entity != null) {
                return entity;
            }
        }
        return null; // 如果没有找到匹配的实体，返回 null
    }

    // 将 Optional<Player> 转化为 List<Entity>
    public static List<Entity> optionalPlayerToList(Optional<Player> optionalPlayer) {
        List<Entity> entityList = new ArrayList<>();
        optionalPlayer.ifPresent(player -> entityList.add(player));
        return entityList;
    }

    // 将 Optional<Entity> 转化为 List<Entity>
    public static List<Entity> optionalEntityToList(Optional<Entity> optionalEntity) {
        List<Entity> entityList = new ArrayList<>();
        optionalEntity.ifPresent(entity -> entityList.add(entity));
        return entityList;
    }

    // 获取 CommandSender 的坐标
    public static Location getLocation(CommandSender sender) {
        if (sender instanceof Player player) {
            return player.getLocation();
        } else if (sender instanceof BlockCommandSender blockSender) {
            Block block = blockSender.getBlock();
            return block.getLocation();
        }
        return null; // 如果不是玩家或命名方块，返回 null
    }

    // 将 List<Entity> 中的玩家名字连接成一个字符串，并用逗号隔开
    public static String combinePlayerNames(List<Entity> entities) {
        return entities.stream()
                .filter(entity -> entity instanceof Player)  // 过滤出玩家实体
                .map(entity -> ((Player) entity).getName())  // 获取玩家的名字
                .collect(Collectors.joining(", "));          // 用逗号和空格连接名字
    }
}
