package fansyr.protect_core;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


public final class Protect_core extends JavaPlugin implements Listener {



    private FileConfiguration config;
    @Override
    public void onEnable() {

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

        this.getCommand("gm").setExecutor(new CommandGM());
        this.getCommand("ki").setExecutor(new CommandKI());
        this.getCommand("nv").setExecutor(new CommandPO());

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

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action act = event.getAction();
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock != null && clickedBlock.getType() == Material.RESPAWN_ANCHOR) {
            if(act == Action.RIGHT_CLICK_BLOCK) {
                // 取消重生锚的使用
                event.setCancelled(true);
            }
        }
    }

    //玩家加入事件
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();


//        event.setJoinMessage("§e" + name + "加入了游戏");
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

}

