package cz.larkyy.leastereggs.listeners;

import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.objects.Egg;
import cz.larkyy.leastereggs.utils.DataUtils;
import cz.larkyy.leastereggs.utils.StorageUtils;
import cz.larkyy.leastereggs.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class BlockPlaceListener implements Listener {

    private Leastereggs main;
    private DataUtils cfg;
    private DataUtils data;
    private Utils utils;
    private StorageUtils storageUtils;

    public BlockPlaceListener(Leastereggs main){
        this.main = main;
        this.utils = main.utils;
        this.cfg = main.getCfg();
        this.data = main.getData();
        this.storageUtils = main.storageUtils;
    }

    @EventHandler (ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        ItemStack is = e.getItemInHand();
        if (is.getItemMeta().getLocalizedName().equals("EasterEgg-Block")) {
            storageUtils.addEgg(new Egg(
                    e.getBlock().getLocation(),
                    cfg.getStringList("settings.defaultActions",Arrays.asList("msg: &eYou have found an Easter Egg!","cmd: give %player% minecraft:diamond 1"))
            ));
            utils.sendMsg(p, cfg.getString("messages.eggPlace","&dYou have placed a new Easter Egg!"));
        }
    }
}
