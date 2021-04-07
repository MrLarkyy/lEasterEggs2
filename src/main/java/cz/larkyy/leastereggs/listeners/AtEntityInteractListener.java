package cz.larkyy.leastereggs.listeners;

import cz.larkyy.leastereggs.Leastereggs;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class AtEntityInteractListener implements Listener {

    private final Leastereggs main;

    public AtEntityInteractListener(Leastereggs main) {
        this.main = main;
    }

    @EventHandler
    public void onEntityClick(PlayerInteractAtEntityEvent e) {
        if (e.getRightClicked() instanceof ArmorStand) {
            ArmorStand as = (ArmorStand) e.getRightClicked();
            if (main.getArmorStands().contains(as)) {
                e.setCancelled(true);
            }
        }
    }
}
