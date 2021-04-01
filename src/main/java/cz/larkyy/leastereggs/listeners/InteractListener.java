package cz.larkyy.leastereggs.listeners;

import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.objects.Egg;
import cz.larkyy.leastereggs.objects.EggPlayer;
import cz.larkyy.leastereggs.runnables.EggAnimation;
import cz.larkyy.leastereggs.utils.StorageUtils;
import cz.larkyy.leastereggs.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;

public class InteractListener implements Listener {

    private Leastereggs main;
    private Utils utils;
    private StorageUtils storageUtils;

    public InteractListener(Leastereggs main){
        this.main = main;
        this.utils = main.utils;
        this.storageUtils = main.storageUtils;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Block b = e.getClickedBlock();
        if (
                e.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                && b!=null
                && e.getHand()!=null
                && e.getHand().equals(EquipmentSlot.HAND)
                && storageUtils.isEgg(b.getLocation())
        ) {
            e.setCancelled(true);
            Egg egg = storageUtils.getEgg(b.getLocation());
            EggPlayer eggPlayer = storageUtils.getPlayer(p.getUniqueId());

            if (!eggPlayer.getEggs().contains(egg)) {

                // ADDING EGG INTO CACHE AND DB
                eggPlayer.addEgg(egg);

                // SENDING ACTIONS
                utils.sendActions(egg.getActions(), p);

                // ANIMATION
                if (main.getCfg().getConfiguration().getBoolean("settings.animation.enabled",true))
                    playAnimation(b.getLocation());

                // SENDING TITLE
                utils.sendTitleMsg(
                        p,
                        main.getCfg().getString("settings.titles.eggFound.title","&d&lEASTER EGG"),
                        main.getCfg().getString("settings.titles.eggFound.subtitle","&fYou found an easter egg! &7(%found%/%total%)")
                                .replace("%found%",eggPlayer.getEggs().size()+"")
                                .replace("%total%",storageUtils.getEggs().size()+""),
                        main.getCfg().getInt("settings.titles.eggFound.fadeIn",0),
                        main.getCfg().getInt("settings.titles.eggFound.stay",50),
                        main.getCfg().getInt("settings.titles.eggFound.fadeOut",0));

                // SPAWNING A FIREWORK
                if (main.getCfg().getConfiguration().getBoolean("settings.firework.enabled",true))
                    spawnFirework(b.getLocation());

                // PLAYING A SOUND
                if (main.getCfg().getConfiguration().getBoolean("settings.sounds.notFound.enabled",true)) {
                    p.playSound(
                            p.getLocation(),
                            Sound.valueOf(main.getCfg().getString("settings.sounds.notFound.type","UI_TOAST_CHALLENGE_COMPLETE")),
                            main.getCfg().getInt("settings.sounds.notFound.volume",10),
                            main.getCfg().getConfiguration().getInt("settings.sounds.notFound.pitch",2));
                }

                if (storageUtils.getPlayers().get(p.getUniqueId()).getEggs().size()==storageUtils.getEggs().size()) {
                    utils.sendActions(main.getCfg().getStringList("settings.foundAllEggsActions",new ArrayList<>()),p);
                }

            } else {
                utils.sendMsg(p,main.getCfg().getString("messages.found","&cYou have already found this Easter Egg!"));

                // PLAYING A SOUND
                if (main.getCfg().getConfiguration().getBoolean("settings.sounds.alreadyFound.enabled",true)) {
                    p.playSound(
                            p.getLocation(),
                            Sound.valueOf(main.getCfg().getString("settings.sounds.alreadyFound.type","ENTITY_VILLAGER_NO")),
                            main.getCfg().getInt("settings.sounds.alreadyFound.volume",10),
                            main.getCfg().getConfiguration().getInt("settings.sounds.alreadyFound.pitch",1));
                }

                // SPAWNING A PARTICLE
                if (main.getCfg().getConfiguration().getBoolean("settings.particles.alreadyFound.enabled",true)) {
                    p.spawnParticle(
                            Particle.valueOf(main.getCfg().getString("settings.particles.alreadyFound.type","VILLAGER_ANGRY")),
                            egg.getLoc().clone().add(0.5,0.5,0.5),
                            main.getCfg().getInt("settings.particles.alreadyFound.count",7));
                }
            }

        }
    }

    private void playAnimation(Location loc) {

        ArmorStand as = (ArmorStand) loc.getWorld().spawnEntity(loc.add(0.5,-0.25,0.5), EntityType.ARMOR_STAND);
        main.addArmorStand(as);
        as.setGravity(false);
        as.setInvulnerable(true);
        as.setVisible(false);
        as.setBasePlate(false);
        as.setSmall(true);
        as.setHelmet(new ItemStack(utils.mkItem(
                Material.valueOf(main.getCfg().getString("settings.animation.material","PLAYER_HEAD")),
                "Easter Egg",
                null,
                null,
                main.getCfg().getString("settings.animation.texture","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjU2ZjdmM2YzNTM2NTA2NjI2ZDVmMzViNDVkN2ZkZjJkOGFhYjI2MDA4NDU2NjU5ZWZlYjkxZTRjM2E5YzUifX19"))));

        new EggAnimation(as,main).runTaskTimerAsynchronously(main,0,1);
    }

    private void spawnFirework(Location loc) {
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc.add(0.5,0,0.5),EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        FireworkEffect effect = FireworkEffect.builder().flicker(true).withColor(Color.FUCHSIA).withFade(Color.AQUA).build();
        fwm.addEffect(effect);
        fwm.setPower(0);

        fw.setFireworkMeta(fwm);
    }
}
