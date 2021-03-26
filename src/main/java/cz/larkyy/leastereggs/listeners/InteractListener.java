package cz.larkyy.leastereggs.listeners;

import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.objects.Egg;
import cz.larkyy.leastereggs.objects.EggPlayer;
import cz.larkyy.leastereggs.utils.DataUtils;
import cz.larkyy.leastereggs.utils.StorageUtils;
import cz.larkyy.leastereggs.utils.Utils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class InteractListener implements Listener {

    private Leastereggs main;
    private DataUtils cfg;
    private DataUtils data;
    private Utils utils;
    private StorageUtils storageUtils;

    public InteractListener(Leastereggs main){
        this.main = main;
        this.utils = main.utils;
        this.cfg = main.getCfg();
        this.data = main.getData();
        this.storageUtils = main.storageUtils;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Block b = e.getClickedBlock();
        if (
                e.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                && e.getHand().equals(EquipmentSlot.HAND)
                && storageUtils.isEgg(b.getLocation())
        ) {
            e.setCancelled(true);
            Egg egg = storageUtils.getEgg(b.getLocation());
            EggPlayer eggPlayer = storageUtils.getPlayer(p.getUniqueId());

            if (!eggPlayer.getEggs().contains(egg)) {
                utils.sendActions(egg.getActions(), p);
                eggPlayer.addEgg(egg);
            } else {
                utils.sendMsg(p,cfg.getString("messages.found","&cYou have already found this Easter Egg!"));
            }

        }
    }
}
