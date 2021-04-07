package cz.larkyy.leastereggs.runnables;

import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.utils.StorageUtils;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

public class SaveDatabase extends BukkitRunnable {

    private final Leastereggs main;

    public SaveDatabase(Leastereggs main) {
        this.main = main;
    }

    @Override
    public void run() {
        if (getStorageUtils().getEggs()==null || getStorageUtils().getPlayers()==null)
            return;

        try {
            getStorageUtils().saveEggs();
            getStorageUtils().savePlayers();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private StorageUtils getStorageUtils() {
        return main.storageUtils;
    }
}
