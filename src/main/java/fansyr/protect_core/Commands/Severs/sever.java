package fansyr.protect_core.Commands.Severs;

import org.bukkit.ChatColor;
import org.bukkit.command.*;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.text.DecimalFormat;
import java.util.List;



public class sever implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (sender instanceof BlockCommandSender) {
            sender.sendMessage(ChatColor.RED + "此命令不能由命令方块执行。");
            return true;
        }
        // 获取系统信息
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();

        // 获取CPU使用率
        double cpuUsage = osBean.getSystemLoadAverage() * 100.0;

        // 获取内存使用情况
        MemoryUsage heapMemoryUsage = memoryBean.getHeapMemoryUsage();
        long totalMemory = heapMemoryUsage.getMax();
        long usedMemory = heapMemoryUsage.getUsed();
        long freeMemory = heapMemoryUsage.getMax() - heapMemoryUsage.getUsed();

        // 发送信息给玩家或控制台
        sender.sendMessage(ChatColor.GREEN + "系统信息:");
        sender.sendMessage(ChatColor.AQUA + "CPU 使用率: " + formatCpuUsage(cpuUsage));
        sender.sendMessage(ChatColor.AQUA + "总内存: " + formatBytes(totalMemory));
        sender.sendMessage(ChatColor.AQUA + "已用内存: " + formatBytes(usedMemory));
        sender.sendMessage(ChatColor.AQUA + "可用内存: " + formatBytes(freeMemory));


        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return List.of();
    }

    private String formatBytes(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
        } else {
            return String.format("%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0));
        }
    }
    private String formatCpuUsage(double cpuUsage) {
        if (cpuUsage < -1.0) {
            return "不可用";
        } else {
            DecimalFormat df = new DecimalFormat("#.##");
            return df.format(cpuUsage) + "%";
        }
    }
}
