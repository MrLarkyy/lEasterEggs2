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

    private Leastereggs main;
    private StorageUtils storageUtils;

    public PlayerJoinListener(Leastereggs main) {
        this.main = main;
        this.storageUtils = main.storageUtils;
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!storageUtils.getPlayers().containsKey(p.getUniqueId())) {
            storageUtils.addPlayer(p.getUniqueId(),new EggPlayer(p.getUniqueId(),new ArrayList<>()));
        }
    }
}
