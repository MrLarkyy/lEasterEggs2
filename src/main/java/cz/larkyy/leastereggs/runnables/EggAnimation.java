package cz.larkyy.leastereggs.runnables;

import cz.larkyy.leastereggs.Leastereggs;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;

public class EggAnimation extends BukkitRunnable {

    private int frame = 0;
    private ArmorStand as;
    private Leastereggs main;

    public EggAnimation(ArmorStand as, Leastereggs main) {
        this.as = as;
        this.main = main;
    }

    @Override
    public void run() {
        Location loc = as.getLocation();
        if (frame<40) {
            loc.add(0, 0.03, 0);
            loc.setYaw(loc.getYaw() + 9);

            as.teleport(loc);
        }
        else if (frame==49) {
            main.delArmorStand(as);
            as.remove();
            loc.getWorld().spawnParticle(Particle.SNOW_SHOVEL,loc.add(0,1.1,0),7,0.18,0.18,0.18);
            loc.getWorld().playSound(loc, Sound.ENTITY_CHICKEN_EGG,4,1);
            cancel();
        }
        frame++;
    }
}
