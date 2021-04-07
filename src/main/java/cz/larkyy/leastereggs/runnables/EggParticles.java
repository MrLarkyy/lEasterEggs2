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

import java.util.Map;

public class EggParticles extends BukkitRunnable {

    private final Leastereggs main;

    public EggParticles(Leastereggs main) {
        this.main = main;
    }

    @Override
    public void run() {

        if (getCfg().getConfiguration().getBoolean("settings.particles.notFound.enabled")) {

            if (getStorageUtils().getEggs()==null) {
                return;
            }

            for (Map.Entry<Integer, Egg> pair : getStorageUtils().getEggs().entrySet()) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    Egg egg = pair.getValue();
                    Location loc = egg.getLoc().clone();
                    loc.add(0.5, 0.5, 0.5);

                    if (p.getLocation().getWorld()==null) {
                        break;
                    }

                    if (egg.getLoc().getWorld()==null) {
                        break;
                    }

                    if (!p.getLocation().getWorld().getName().equals(egg.getLoc().getWorld().getName())) {
                        break;
                    }

                    if (getStorageUtils().getPlayers().get(p.getUniqueId()).getEggs() == null) {
                        spawnParticle(p, loc);
                    }

                    if (!getStorageUtils().getPlayers().get(p.getUniqueId()).getEggs().contains(egg)) {
                        spawnParticle(p, loc);
                    }
                }
            }
        }
    }

    private void spawnParticle(Player p, Location loc) {
        double distance = loc.distance(p.getLocation());

        if (distance <= getCfg().getInt("settings.particles.notFound.maxDistance", 0) || getCfg().getInt("settings.particles.notFound.maxDistance", 0) == 0) {
            p.spawnParticle(
                    Particle.valueOf(getCfg().getString("settings.particles.notFound.type", "VILLAGER_HAPPY")),
                    loc,
                    getCfg().getInt("settings.particles.notFound.count", 7),
                    0.325, 0.325, 0.325
            );
        }
    }

    private StorageUtils getStorageUtils() {
        return main.storageUtils;
    }

    private DataUtils getCfg() {
        return main.getCfg();
    }
}
