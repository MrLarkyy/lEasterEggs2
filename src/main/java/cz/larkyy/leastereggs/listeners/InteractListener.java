package cz.larkyy.leastereggs.listeners;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.objects.Egg;
import cz.larkyy.leastereggs.objects.EggPlayer;
import cz.larkyy.leastereggs.runnables.EggAnimation;
import cz.larkyy.leastereggs.utils.DataUtils;
import cz.larkyy.leastereggs.utils.StorageUtils;
import cz.larkyy.leastereggs.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

public class InteractListener implements Listener {

    private final Leastereggs main;
    private final Utils utils;

    public InteractListener(Leastereggs main) {
        this.main = main;
        this.utils = main.utils;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Block b = e.getClickedBlock();

        if (getCfg().getString("settings.claimingMethod","CLICK").equalsIgnoreCase("WALK"))
            return;

        if (getStorageUtils().getEggs()==null || getStorageUtils().getPlayers()==null)
            return;

        if (
                e.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                        && b != null
                        && e.getHand() != null
                        && e.getHand().equals(EquipmentSlot.HAND)
                        && getStorageUtils().isEgg(b.getLocation())
        ) {

            e.setCancelled(true);
            Egg egg = getStorageUtils().getEgg(b.getLocation());
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
                    playAnimation(b.getLocation());

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
                    spawnFirework(b.getLocation());

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

            } else {
                utils.sendMsg(p, getCfg().getString("messages.found", "&cYou have already found this Easter Egg!"));

                // PLAYING A SOUND
                if (getCfg().getConfiguration().getBoolean("settings.sounds.alreadyFound.enabled", true)) {
                    p.playSound(
                            p.getLocation(),
                            Sound.valueOf(getCfg().getString("settings.sounds.alreadyFound.type", "ENTITY_VILLAGER_NO")),
                            getCfg().getInt("settings.sounds.alreadyFound.volume", 10),
                            getCfg().getConfiguration().getInt("settings.sounds.alreadyFound.pitch", 1));
                }

                // SPAWNING A PARTICLE
                if (getCfg().getConfiguration().getBoolean("settings.particles.alreadyFound.enabled", true)) {
                    p.spawnParticle(
                            Particle.valueOf(getCfg().getString("settings.particles.alreadyFound.type", "VILLAGER_ANGRY")),
                            egg.getLoc().clone().add(0.5, 0.5, 0.5),
                            getCfg().getInt("settings.particles.alreadyFound.count", 7));
                }
            }

        }
    }

    private void playAnimation(Location loc) {

        String texture = getSkin(loc.getBlock());
        if (texture==null) {
            texture = getCfg().getString("settings.animation.texture", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjU2ZjdmM2YzNTM2NTA2NjI2ZDVmMzViNDVkN2ZkZjJkOGFhYjI2MDA4NDU2NjU5ZWZlYjkxZTRjM2E5YzUifX19");
        }

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
                texture)));

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

    private StorageUtils getStorageUtils() {
        return main.storageUtils;
    }

    private DataUtils getCfg(){
        return main.getCfg();
    }

    private String getSkin(Block b) {
        if (b.getType() != Material.PLAYER_HEAD) return null;
        final Skull skull = (Skull)b.getState();
        try {
            Field profileField = skull.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            if (profileField.get(skull) == null){
                return null;
            } else {
                Object gameProfile = profileField.get(skull);
                if (gameProfile instanceof GameProfile){
                    GameProfile gm = (GameProfile) gameProfile;
                    Collection<Property> a = gm.getProperties().get("textures");
                    for (Property property : a) {
                        return property.getValue();
                    }
                }
            }
        }catch (NoSuchFieldException | IllegalAccessException e) { e.printStackTrace(); }
        return null;
    }

}
