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

        player.spigot().sendMessage(new ComponentBuilder("感谢使用ProtectCore插件!").
                color(ChatColor.GOLD).
                bold(true).
                append(" [更新日志]").//使用append进行分段
                        color(ChatColor.AQUA).
                        event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                "/pup update")).
                append(" [更新计划]").//使用append进行分段
                        color(ChatColor.BLUE).
                        event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                "(不想写了，自己期待吧!)"))
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

    private String SubChoose;
    //处理玩家名
    public static String SubPlayer(String input,Player sender) {
        // 查找第一个 '@' 符号的位置
        int atIndex = input.indexOf('@');
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

            String pn = mention.substring(1);
            Player targetPlayer = Bukkit.getPlayer(pn);

            if (targetPlayer != null && targetPlayer.isOnline()) {
                // 播放音效
                targetPlayer.playSound(targetPlayer.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 2.0f, 1.0f);
                sender.playSound(sender.getLocation(), Sound.ENTITY_ARROW_HIT, 2.0f, 1.0f);

                // 发送消息给发送者
                sender.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(org.bukkit.ChatColor.AQUA + "已经向" + mention + "发送通知!"));

                // 将子字符串改为金色
                String goldMention = org.bukkit.ChatColor.GOLD + mention + ChatColor.RESET;

                // 重新构建整个字符串
                return input.substring(0, atIndex) + goldMention + input.substring(spaceIndex);
            } else {
                // 将子字符串改为蓝色
                String goldMention = org.bukkit.ChatColor.BLUE + mention + ChatColor.RESET;

                // 重新构建整个字符串
                return input.substring(0, atIndex) + goldMention + input.substring(spaceIndex);
            }

    }

}

