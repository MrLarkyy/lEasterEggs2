package cz.larkyy.leastereggs.runnables;

import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.objects.Egg;
import cz.larkyy.leastereggs.utils.DataUtils;
import cz.larkyy.leastereggs.utils.StorageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class EggParticles extends BukkitRunnable {

    private Leastereggs main;
    private StorageUtils storageUtils;
    private DataUtils cfg;

    public EggParticles (Leastereggs main) {
        this.main = main;
        this.storageUtils = main.storageUtils;
        this.cfg = main.getCfg();
    }

    @Override
    public void run() {
        if (main.getCfg().getConfiguration().getBoolean("settings.particles.notFound.enabled")) {
            for (Map.Entry<Integer, Egg> pair : storageUtils.getEggs().entrySet()) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    Egg egg = pair.getValue();
                    if (!storageUtils.getPlayers().get(p.getUniqueId()).getEggs().contains(egg)) {
                        Location loc = egg.getLoc().clone();
                        loc.add(0.5, 0.5, 0.5);
                        if (p.getLocation().getWorld()!=null && !p.getLocation().getWorld().equals(egg.getLoc().getWorld()))
                            return;

                        double distance = loc.distance(p.getLocation());

                        if (main.getCfg().getInt("settings.particles.notFound.maxDistance", 0) == 0) {
                            spawnParticle(p, loc);
                        } else if (distance <= main.getCfg().getInt("settings.particles.notFound.maxDistance", 0)) {

                            spawnParticle(p, loc);
                        }
                    }
                }
            }
        }
    }

    private void spawnParticle(Player p, Location loc) {
        p.spawnParticle(
                Particle.valueOf(main.getCfg().getString("settings.particles.notFound.type", "VILLAGER_HAPPY")),
                loc,
                cfg.getInt("settings.particles.notFound.count", 7),
                0.325, 0.325, 0.325
        );
    }
}
