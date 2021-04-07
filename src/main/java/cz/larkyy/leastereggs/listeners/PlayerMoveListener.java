package cz.larkyy.leastereggs.listeners;

import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.objects.Egg;
import cz.larkyy.leastereggs.objects.EggPlayer;
import cz.larkyy.leastereggs.runnables.EggAnimation;
import cz.larkyy.leastereggs.utils.DataUtils;
import cz.larkyy.leastereggs.utils.StorageUtils;
import cz.larkyy.leastereggs.utils.Utils;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;

public class PlayerMoveListener implements Listener {

    private final Leastereggs main;
    private final Utils utils;

    public PlayerMoveListener(Leastereggs main) {
        this.main = main;
        this.utils = main.utils;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {

        if (getCfg().getString("settings.claimingMethod","CLICK").equalsIgnoreCase("CLICK"))
            return;

        if (getStorageUtils().getEggs()==null || getStorageUtils().getPlayers()==null)
            return;

        Player p = e.getPlayer();
        Location loc = e.getTo().clone();
        loc.add(0,-1,0);

        loc.setYaw(0);
        loc.setPitch(0);

        Location prevLoc = e.getFrom().clone();
        prevLoc.add(0,-1,0);

        prevLoc.setYaw(0);
        prevLoc.setPitch(0);

        loc.setX(Math.floor(loc.getX()));
        loc.setY(Math.floor(loc.getY()));
        loc.setZ(Math.floor(loc.getZ()));

        prevLoc.setX(Math.floor(prevLoc.getX()));
        prevLoc.setY(Math.floor(prevLoc.getY()));
        prevLoc.setZ(Math.floor(prevLoc.getZ()));

        if (loc.equals(prevLoc))
            return;

        if (!(getStorageUtils().isEgg(loc) || getStorageUtils().isEgg(loc.add(0,-1,0))))
            return;

        Egg egg = getStorageUtils().getEgg(loc);
        EggPlayer eggPlayer = getStorageUtils().getPlayer(p.getUniqueId());

        // ADDING PLAYER INTO CACHE, IF DOES NOT EXIST
        if (eggPlayer == null) {
            getStorageUtils().addPlayer(p.getUniqueId(),new EggPlayer(p.getUniqueId(),new ArrayList<>()));
            return;
        }

        if (!eggPlayer.getEggs().contains(egg)) {

            // ADDING EGG INTO CACHE AND DB
            eggPlayer.addEgg(egg);

            // SENDING ACTIONS
            utils.sendActions(egg.getActions(), p);

            // ANIMATION
            if (getCfg().getConfiguration().getBoolean("settings.animation.enabled", true))
                playAnimation(loc);

            // SENDING TITLE
            utils.sendTitleMsg(
                    p,
                    getCfg().getString("settings.titles.eggFound.title", "&d&lEASTER EGG"),
                    getCfg().getString("settings.titles.eggFound.subtitle", "&fYou found an easter egg! &7(%found%/%total%)")
                            .replace("%found%", eggPlayer.getEggs().size() + "")
                            .replace("%total%", getStorageUtils().getEggs().size() + ""),
                    getCfg().getInt("settings.titles.eggFound.fadeIn", 0),
                    getCfg().getInt("settings.titles.eggFound.stay", 50),
                    getCfg().getInt("settings.titles.eggFound.fadeOut", 0));

            // SPAWNING A FIREWORK
            if (getCfg().getConfiguration().getBoolean("settings.firework.enabled", true))
                spawnFirework(loc);

            // PLAYING A SOUND
            if (getCfg().getConfiguration().getBoolean("settings.sounds.notFound.enabled", true)) {
                p.playSound(
                        p.getLocation(),
                        Sound.valueOf(getCfg().getString("settings.sounds.notFound.type", "UI_TOAST_CHALLENGE_COMPLETE")),
                        getCfg().getInt("settings.sounds.notFound.volume", 10),
                        getCfg().getConfiguration().getInt("settings.sounds.notFound.pitch", 2));
            }

            if (getStorageUtils().getPlayers().get(p.getUniqueId()).getEggs().size() == getStorageUtils().getEggs().size()) {
                utils.sendActions(getCfg().getStringList("settings.foundAllEggsActions", new ArrayList<>()), p);
            }

        }

    }

    private StorageUtils getStorageUtils() {
        return main.storageUtils;
    }

    private DataUtils getCfg() {
        return main.getCfg();
    }

    private void playAnimation(Location loc) {

        ArmorStand as = (ArmorStand) loc.getWorld().spawnEntity(loc.add(0.5, -0.25, 0.5), EntityType.ARMOR_STAND);
        main.addArmorStand(as);
        as.setGravity(false);
        as.setInvulnerable(true);
        as.setVisible(false);
        as.setBasePlate(false);
        as.setSmall(true);
        as.setHelmet(new ItemStack(utils.mkItem(
                Material.valueOf(getCfg().getString("settings.animation.material", "PLAYER_HEAD")),
                "Easter Egg",
                null,
                null,
                getCfg().getString("settings.animation.texture", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjU2ZjdmM2YzNTM2NTA2NjI2ZDVmMzViNDVkN2ZkZjJkOGFhYjI2MDA4NDU2NjU5ZWZlYjkxZTRjM2E5YzUifX19"))));

        new EggAnimation(as, main).runTaskTimer(main, 0, 1);
    }

    private void spawnFirework(Location loc) {
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc.add(0.5, 0, 0.5), EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        FireworkEffect effect = FireworkEffect.builder().flicker(true).withColor(Color.FUCHSIA).withFade(Color.AQUA).build();
        fwm.addEffect(effect);
        fwm.setPower(0);

        fw.setFireworkMeta(fwm);
    }
}
