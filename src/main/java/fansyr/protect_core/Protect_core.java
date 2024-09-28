package fansyr.protect_core;

import fansyr.protect_core.Commands.*;
import fansyr.protect_core.Commands.Severs.HideCommand;
import fansyr.protect_core.Commands.Severs.sever;
import fansyr.protect_core.Commands.Severs.whiteLists;
import fansyr.protect_core.Commands.disable.ExecuteCommand;
import fansyr.protect_core.Commands.disable.Me;
import fansyr.protect_core.Commands.funny.RanPlayer;
import fansyr.protect_core.Commands.funny.RandomCommand;
import fansyr.protect_core.Commands.ppcore.pup;
import fansyr.protect_core.designs.WhiteList;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.ServerError;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public final class Protect_core extends JavaPlugin implements Listener {

    public static WhiteList whiteList = new WhiteList();
    @Override
    public void onEnable() {
        //清空报告文件
        clearCloseLogsFolder();

        whiteList.registered();
        //启动插件tips
        System.out.println(ChatColor.AQUA + symbols.STAR + "-------------------------" + symbols.STAR);
        System.out.println(ChatColor.LIGHT_PURPLE + symbols.List_1);
        System.out.println(ChatColor.BLUE + "作者: " + ChatColor.GOLD + "Fansyr\n" + ChatColor.BLUE + " 插件名: " + ChatColor.GOLD + "ProtectCore" + ChatColor.BLUE + " 版本: " + ChatColor.GOLD + "META-0.0.9.34b");
        System.out.println(ChatColor.LIGHT_PURPLE + symbols.List_2);
        System.out.println(ChatColor.AQUA + symbols.STAR + "制作时间 : 2024.10.1" + symbols.STAR + "   " + ChatColor.DARK_AQUA + "1.20.1-META-0.0.9.34b");
        System.out.println(ChatColor.LIGHT_PURPLE + symbols.List_3);
        System.out.println(ChatColor.AQUA + symbols.STAR + "-------------------------" + symbols.STAR);
        //注册事件
        getServer().getPluginManager().registerEvents(this, this);
        

        new BukkitRunnable() {
            @Override
            public void run() {
                removeEntities();
            }
        }.runTaskTimerAsynchronously(this, 0L, 1L); // 每刻执行一次

        new BukkitRunnable() {
            @Override
            public void run() {
                hidefor();
            }
        }.runTaskTimerAsynchronously(this, 100L, 10L);

        Objects.requireNonNull(this.getCommand("gamemode")).setExecutor(new CommandGM());
        Objects.requireNonNull(this.getCommand("ki")).setExecutor(new CommandKI());
        Objects.requireNonNull(this.getCommand("nv")).setExecutor(new CommandPO());
        Objects.requireNonNull(this.getCommand("kill")).setExecutor(new Kill());
        Objects.requireNonNull(this.getCommand("sever")).setExecutor(new sever());
        Objects.requireNonNull(this.getCommand("wl")).setExecutor(new whiteLists());
        Objects.requireNonNull(this.getCommand("say")).setExecutor(new say());
        Objects.requireNonNull(this.getCommand("ranplayer")).setExecutor(new RanPlayer());
        Objects.requireNonNull(this.getCommand("execute")).setExecutor(new ExecuteCommand());
        Objects.requireNonNull(this.getCommand("random")).setExecutor(new RandomCommand());
        Objects.requireNonNull(this.getCommand("hide")).setExecutor(new HideCommand());
        Objects.requireNonNull(this.getCommand("me")).setExecutor(new Me());
        Objects.requireNonNull(this.getCommand("pup")).setExecutor(new pup());




    }

    //删除实体
    private void removeEntities() {
        // 删除所有TNT Primed实体
        for (Entity entity : Bukkit.getWorlds().get(0).getEntities()) {
            if (entity instanceof TNTPrimed) {
                entity.remove();
            } else if (entity instanceof Minecart) {
                if(((Minecart) entity).getDisplayBlockData().getMaterial() == Material.TNT) {
                    entity.remove();
                }
            } else if (entity instanceof EnderCrystal) {
                entity.remove();
            }
        }
    }
    //提示隐藏
    private void hidefor() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            List<MetadataValue> metadata = player.getMetadata("hide_for");
            if (!metadata.isEmpty()) {
                String hiddenFor = metadata.get(0).asString();
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(org.bukkit.ChatColor.GOLD + "你已对 " + hiddenFor + " 隐藏"));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action act = event.getAction();
        Block clickedBlock = event.getClickedBlock();
        Player player = event.getPlayer();
        ItemStack item = player.getItemInUse();
        if (clickedBlock != null && clickedBlock.getType() == Material.RESPAWN_ANCHOR) {
            if(act == Action.RIGHT_CLICK_BLOCK) {
                if(clickedBlock.getLightLevel() > 0 && !player.isSneaking()) {
                    player.spawnParticle(Particle.SMOKE_NORMAL, clickedBlock.getLocation(), 10, 0.5, 0.5, 0.5, 0.05);
                    event.setCancelled(true);
                }
            }
        }
    }

    //玩家加入事件
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();

        player.spigot().sendMessage(new ComponentBuilder(ChatColor.GOLD + ChatColor.BOLD.toString() +  "感谢使用ProtectCore插件" + ChatColor.RESET +
                ChatColor.GOLD  + "<V.2024.10.1 2253>!").
                append(" [更新日志]").//使用append进行分段
                        color(ChatColor.AQUA).
                        event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                "/pup update")).
                append(" [更新计划]").//使用append进行分段
                        color(ChatColor.BLUE).
                        event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                "/pup future"))
                .create());


    }

    //玩家退出事件
    @EventHandler
    public void onPlayerLeft(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
//        event.setQuitMessage("§e" + name + "退出了游戏");
    }


    //底层阻止爆炸
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.getEntity() instanceof TNTPrimed) {
            TNTPrimed tnt = (TNTPrimed) event.getEntity();
                event.setCancelled(true);
                tnt.remove();
        } else if (event.getEntity() instanceof EnderCrystal) {
            EnderCrystal tnt = (EnderCrystal) event.getEntity();
            event.setCancelled(true);
            tnt.remove();
        } else if (event.getEntity() instanceof Minecart) {
            Minecart tnt = (Minecart) event.getEntity();
            if (tnt.getDisplayBlockData().getMaterial() == Material.TNT) {
                event.setCancelled(true);
                tnt.remove();
            }
        }
    }

    //关闭时执行
    @Override
    public void onDisable() {

        //生成报告文件
        generateReportFile();

            try {
                getServer().getPluginManager().enablePlugin(this);
                getLogger().info("Plugin re-enabled because the server is not shutting down.");
            } catch (Exception ignored) {

            }
    }

    //LOG
    public static void LOG(String msg) {
        System.out.println(msg);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        String msg = event.getMessage();
        event.setMessage(SubPlayer(msg,player));
    }

    private static final String MENTION_PREFIX = "@";
    private static final String GOLD_MENTION_FORMAT = ChatColor.GOLD + "%s" + ChatColor.RESET;
    private static final String BLUE_MENTION_FORMAT = ChatColor.BLUE + "%s" + ChatColor.RESET;

    public static String SubPlayer(String input, Player sender) {
        if (input == null || input.isEmpty()) {
            return input; // 如果输入为空，直接返回
        }

        // 查找第一个 '@' 符号的位置
        int atIndex = input.indexOf(MENTION_PREFIX);
        if (atIndex == -1) {
            return input; // 如果没有找到 '@'，直接返回原字符串
        }

        // 查找从 '@' 开始到下一个空格之间的子字符串
        int spaceIndex = input.indexOf(' ', atIndex);
        if (spaceIndex == -1) {
            spaceIndex = input.length(); // 如果没有找到空格，设置为空格位置为字符串长度
        }

        // 提取需要格式化的子字符串
        String mention = input.substring(atIndex, spaceIndex);

        // 去掉 '@' 符号，获取玩家名
        String playerName = mention.substring(1);

        // 获取目标玩家
        Player targetPlayer = Bukkit.getPlayer(playerName);

        if (targetPlayer != null && targetPlayer.isOnline()) {
            // 播放音效
            targetPlayer.playSound(targetPlayer.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 2.0f, 1.0f);
            sender.playSound(sender.getLocation(), Sound.ENTITY_ARROW_HIT, 2.0f, 1.0f);

            // 发送消息给发送者
            sender.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.AQUA + "已经向" + mention + "发送通知!"));

            // 将子字符串改为金色
            String goldMention = String.format(GOLD_MENTION_FORMAT, mention);
            return input.substring(0, atIndex) + goldMention + input.substring(spaceIndex);
        } else {
            // 将子字符串改为蓝色
            String blueMention = String.format(BLUE_MENTION_FORMAT, mention);
            return input.substring(0, atIndex) + blueMention + input.substring(spaceIndex);
        }
    }

    //报告生成
    private static final String LOGS_FOLDER = "CloseLogs";

    private void generateReportFile() {
        // 获取数据文件夹
        File dataFolder = getDataFolder();

        // 确保 CloseLogs 文件夹存在
        File logsFolder = new File(dataFolder, LOGS_FOLDER);
        if (!logsFolder.exists()) {
            logsFolder.mkdirs();
        }

        // 生成文件名
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String fileName = "report_" + dateFormat.format(new Date()) + ".txt";
        File reportFile = new File(logsFolder, fileName);

        try (FileWriter writer = new FileWriter(reportFile)) {
            // 写入报告内容
            writer.write("插件关闭报告\n");
            writer.write("时间: " + new Date() + "\n");
            // 可以在这里添加更多的报告信息
            writer.write("=====================================");
            writer.write("功能暂未实现，敬请期待");
        } catch (IOException e) {
            getLogger().severe("无法生成报告文件: " + e.getMessage());
        }
    }

    private void clearCloseLogsFolder() {
        // 获取数据文件夹
        File dataFolder = getDataFolder();

        // 获取 CloseLogs 文件夹
        File logsFolder = new File(dataFolder, LOGS_FOLDER);

        // 如果文件夹存在，删除其所有内容
        if (logsFolder.exists() && logsFolder.isDirectory()) {
            for (File file : logsFolder.listFiles()) {
                if (file.isFile()) {
                    file.delete();
                }
            }
        }
    }
}

