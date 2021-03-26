package cz.larkyy.leastereggs.listeners;

import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.objects.Egg;
import cz.larkyy.leastereggs.objects.EggPlayer;
import cz.larkyy.leastereggs.utils.DataUtils;
import cz.larkyy.leastereggs.utils.StorageUtils;
import cz.larkyy.leastereggs.utils.Utils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class BlockBreakListener implements Listener {

    private Leastereggs main;
    private DataUtils cfg;
    private DataUtils data;
    private Utils utils;
    private StorageUtils storageUtils;

    public BlockBreakListener(Leastereggs main){
        this.main = main;
        this.utils = main.utils;
        this.cfg = main.getCfg();
        this.data = main.getData();
        this.storageUtils = main.storageUtils;
    }

    @EventHandler (ignoreCancelled = true)
    public void onBreak(BlockBreakEvent e) throws IOException {
        Player p = e.getPlayer();
        Block b = e.getBlock();
        Location loc = b.getLocation();

        if (storageUtils.isEgg(loc)) {
            if (p.isSneaking() && cfg.hasPermission(p,"eggBreak","eastereggs.break")) {

                for (Map.Entry<UUID, EggPlayer> pair : storageUtils.getPlayers().entrySet()) {
                    pair.getValue().getEggs().remove(storageUtils.getEgg(loc));
                }
                storageUtils.savePlayers();

                storageUtils.delEgg(storageUtils.getEgg(loc));
                utils.sendMsg(p,cfg.getString("messages.eggBreak","&eYou have &6Broken &ethe Easter Egg!"));
                e.setDropItems(false);



            } else
                e.setCancelled(true);
        }
    }
}
