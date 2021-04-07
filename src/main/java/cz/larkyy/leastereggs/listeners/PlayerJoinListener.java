package cz.larkyy.leastereggs.listeners;

import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.objects.EggPlayer;
import cz.larkyy.leastereggs.utils.StorageUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;

public class PlayerJoinListener implements Listener {

    private final Leastereggs main;

    public PlayerJoinListener(Leastereggs main) {
        this.main = main;
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (getStorageUtils().getEggs()==null || getStorageUtils().getPlayers()==null)
            return;

        if (!getStorageUtils().getPlayers().containsKey(p.getUniqueId())) {
            getStorageUtils().addPlayer(p.getUniqueId(), new EggPlayer(p.getUniqueId(), new ArrayList<>()));
        }
    }

    private StorageUtils getStorageUtils() {
        return main.storageUtils;
    }
}
