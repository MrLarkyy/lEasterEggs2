package cz.larkyy.leastereggs.runnables;

import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.utils.StorageUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

public class SaveDatabase extends BukkitRunnable {

    private Leastereggs main;
    private StorageUtils storageUtils;

    public SaveDatabase(Leastereggs main) {
        this.main = main;
        storageUtils = main.storageUtils;
    }

    @Override
    public void run() {
        try {
            storageUtils.saveEggs();
            storageUtils.savePlayers();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
