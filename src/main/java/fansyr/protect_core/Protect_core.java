package fansyr.protect_core;

import fansyr.protect_core.Commands.*;
import fansyr.protect_core.Commands.Severs.HideCommand;
import fansyr.protect_core.Commands.Severs.sever;
import fansyr.protect_core.Commands.Severs.whiteLists;
import fansyr.protect_core.Commands.difficult.ExecuteCommand;
import fansyr.protect_core.Commands.funny.RanPlayer;
import fansyr.protect_core.Commands.funny.RandomCommand;
import fansyr.protect_core.designs.WhiteList;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
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
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;


public final class Protect_core extends JavaPlugin implements Listener {

    public static WhiteList whiteList = new WhiteList();
    @Override
    public void onEnable() {

        whiteList.registered();
        //启动插件tips
        System.out.println("""
                            ===============================================
                            |  PPcore is loaded
                            |  Version : 1.0.0
                            |  Create By Fansyr
                            |  Github : https://github.com/ffanxxy
                            |  Sever For : ShenuiCity Sever
                            """ + "===============================================");

        //注册事件
        getServer().getPluginManager().registerEvents(this, this);

        //================================================================
        OfflinePlayer player = Bukkit.getOfflinePlayer("Fansyr");
        // 检查玩家是否已经被加入白名单
        if (!player.isWhitelisted()) {
            // 添加玩家到白名单
            player.setWhitelisted(true);
        }
        //================================================================

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

        player.spigot().sendMessage(new ComponentBuilder("感谢使用ProtectCore插件!").
                color(ChatColor.GOLD).
                bold(true).
                append(" [更新日志]").//使用append进行分段
                        color(ChatColor.AQUA).
                        event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                "/tellraw @s [{\"text\":\"[PPC] Beta 0.6.2\"}," +
                                        "{\"text\":\"\\n| 1./gamemode简单化\"}," +
                                        "{\"text\":\"\\n| 2./help告诉语法\"}," +
                                        "{\"text\":\"\\n| 3.修复指令返回与效果\"}," +
                                        "{\"text\":\"\\n| 4.添加命令补全\"}," +
                                        "{\"text\":\"\\n| 5.支持服务端输入更多指令\"}," +
                                        "{\"text\":\"\\n| 6.???\"}," +
                                        "{\"text\":\"\\n| 7.???\"}" + "]")).
                append(" [更新计划]").//使用append进行分段
                        color(ChatColor.BLUE).
                        event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                "/tellraw @s [{\"text\":\"[更新计划] Beta 0.8.0\"}," +
                                        "{\"text\":\"\\n| 1./kill指令支持更多参数\"}," +
                                        "{\"text\":\"\\n| 2./random指令\"}," +
                                        "{\"text\":\"\\n| 3.修复bug\"}," +
                                        "{\"text\":\"\\n| 4.添加更多命令补全\"}," +
                                        "{\"text\":\"\\n| 5.优化性能\"}," +
                                        "{\"text\":\"\\n| 6.更多趣味指令\"}," +
                                        "{\"text\":\"\\n| 7.添加报错日志\"}" + "]"))
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
        System.out.println("Sever may be closed.Report is spawning...");
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
        if(msg.startsWith("/execute")) {
            event.setCancelled(true);
            player.sendMessage("§c你不能使用这个指令");
        }
    }
}

