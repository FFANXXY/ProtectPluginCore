package fansyr.protect_core.designs;

import org.bukkit.entity.Player;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

public class WhiteList {
    private final List<String> whiteList;

    public WhiteList() {
        // 使用 CopyOnWriteArrayList 以确保线程安全
        whiteList = new CopyOnWriteArrayList<>();
        registered();
    }

    public void registered() {
        whiteList.add("Fansyr");
        whiteList.add("shenshui6");
    }

    public boolean temporary_add(String name) {
        if (!whiteList.contains(name)) {
            whiteList.add(name);
            return true;
        } else {
            return false;
        }
    }

    public boolean temporary_remove(String name) {
        try {
            whiteList.remove(name);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isWhiteList(Player player) {
        return whiteList.contains(player.getName());
    }

    public List<String> getWhiteList() {
        return whiteList;
    }
}