package fansyr.protect_core.Commands;

import fansyr.protect_core.Protect_core;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static fansyr.protect_core.Protect_core.whiteList;

public class Kill implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof BlockCommandSender) {
            sender.sendMessage(ChatColor.RED + "此命令不能由命令方块执行。");
            return true;
        }

        if ((sender instanceof Player)) {
            Player player = (Player) sender;
            if (args.length == 0) {
                player.playSound(player, "entity.player.hurt", 1, 1);
                player.setHealth(0);
                Command.broadcastCommandMessage(sender, "我紫砂了");
                return true;
            } else if (args.length == 1) {
                //白名单
                if (whiteList.isWhiteList(player)) {
                    if(args[0].equals("items")) {
                        player.sendMessage("已清除" + killItems() + "个掉落物");
                    } else if(args[0].equals("@a")) {
                        player.sendMessage("已清除" + killPlayers() + "个玩家");
                    } else if(args[0].equals("@e")) {
                        player.sendMessage("已清除" + killLivings() + "个实体");
                    }

                } else {
                    player.sendMessage("§c你没有权限!");
                }
                return true;
            }
        } else {
            try {
                if (args.length == 1) {
                    if (args[0].equals("items")) {
                        sender.sendMessage("已清除" + killItems() + "个物品");
                    } else if (args[0].equals("@a")) {
                        sender.sendMessage("已清除" + killPlayers() + "个玩家");
                    } else if (args[0].equals("@e")) {
                        sender.sendMessage("已清除" + killLivings() + "个实体");
                    }

                    return true;
                }
            } catch (Exception e2) {
                Protect_core.LOG("§c服务器使用指令时发生错误!");
                e2.printStackTrace();
            }
        }
            return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {//是否为玩家
            List<String> Params = new ArrayList<>();
            Params.add("items");
            Params.add("@a");
            Params.add("@e");
            // 过滤出以输入与字符串开头匹配的参数
            List<String> LastParams = new ArrayList<>();
            for (String shows : Params) {
                if (shows.toLowerCase().startsWith(args[0].toLowerCase())) {
                    LastParams.add(shows);
                }
            }
            return LastParams;
        }
        return null;
    }

    //kill items
    public static int killItems() {
        List<Entity> e = Bukkit.getWorlds().stream()
                .flatMap(world -> world.getEntities().stream())
                .filter(entity -> entity instanceof Item)
                .toList();
        int n = e.size();
        for (Entity entity : e) {
            entity.remove();
        }
        return n;
    }
    //kill @a
    public static int killPlayers() {
        List<Entity> e = Bukkit.getWorlds().stream()
                .flatMap(world -> world.getEntities().stream())
                //.filter(entity -> entity instanceof !!!) !!!处写上类型
                .filter(entity -> entity instanceof Player)
                .toList();
        int n = e.size();
        for (Entity entity : e) {
            Player player = (Player) entity;
            player.playSound(player, "entity.player.hurt", 1, 1);
            player.setHealth(0);
        }
        return n;
    }
    //kill @e
    public static int killLivings() {
        List<Entity> e = Bukkit.getWorlds().stream()
                .flatMap(world -> world.getEntities().stream())
                //.filter(entity -> entity instanceof !!!) !!!处写上类型
                .filter(entity -> entity instanceof LivingEntity)
                .toList();
        int n = e.size();
        for (Entity entity : e) {
            try {
                Player player = (Player) entity;
                player.playSound(player, "entity.player.hurt", 1, 1);
                player.setHealth(0);
            } catch(Exception e1) {
                entity.remove();
            }
        }
        return n + killItems();
    }

}





