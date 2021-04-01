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

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BlockPlaceListener implements Listener {

    private Leastereggs main;
    private DataUtils cfg;
    private DataUtils data;
    private Utils utils;
    private StorageUtils storageUtils;

    private List<String> textures;

    public BlockPlaceListener(Leastereggs main){
        this.main = main;
        this.utils = main.utils;
        this.cfg = main.getCfg();
        this.data = main.getData();
        this.storageUtils = main.storageUtils;

        textures = main.getCfg().getStringList(
                "settings.randomEggTextures",
                Arrays.asList(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjU2ZjdmM2YzNTM2NTA2NjI2ZDVmMzViNDVkN2ZkZjJkOGFhYjI2MDA4NDU2NjU5ZWZlYjkxZTRjM2E5YzUifX19",
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNThiOWUyOWFiMWE3OTVlMmI4ODdmYWYxYjFhMzEwMjVlN2NjMzA3MzMzMGFmZWMzNzUzOTNiNDVmYTMzNWQxIn19fQ==",
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTViOGRjYmVhMjdmNDJmNWFlOTEwNDQ1ZTA1ZGFjODlkMzEwYWFmMjM2YTZjMjEyM2I4NTI4MTIwIn19fQ==",
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTg4OWYxMWM4ODM4YzA5ZTFlY2YyZjgzNDM5ZWJjYjlmMzI0ZTU2N2IwZTlkYzRiN2MyNWQ5M2U1MGZmMmIifX19",
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjNkNjliMjNhZTU5MmM2NDdlYjhkY2ViOWRhYWNlNDQxMzlmNzQ4ZTczNGRjODQ5NjI2MTNjMzY2YTA4YiJ9fX0=",
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzc2NTk1ZWZmY2M1NjI3ZTg1YjE0YzljODgyNDY3MWI1ZWMyOTY1NjU5YzhjNDE3ODQ5YTY2Nzg3OGZhNDkwIn19fQ==",
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjY0NDMwZTQ5M2ZlYjVlYWExNDU1ODJlNTRlNzYxYTg2MDNmYjE2Y2MwZmYxMjY4YTVkMWU4NjRlNmY0NzlmNiJ9fX0="
                ));
    }

    @EventHandler (ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();

        ItemStack is = e.getItemInHand();

        if (is.getItemMeta().getLocalizedName().contains("EasterEgg-Block")) {

            if (!p.hasPermission(main.getCfg().getString("settings.permissions.eggPlace","eastereggs.place"))) {
                utils.sendMsg(p, main.getCfg().getString("messages.noPermission","&cYou have no permission to do that!"));
                return;
            }

            if (is.getItemMeta().getLocalizedName().contains("Random")) {
                Collections.shuffle(textures);
                utils.setSkullItemSkin(is,textures.get(1));
            }

            storageUtils.addEgg(new Egg(
                    e.getBlock().getLocation(),
                    cfg.getStringList("settings.defaultActions",Arrays.asList("msg: &eYou have found an Easter Egg!","cmd: give %player% minecraft:diamond 1"))
            ));
            utils.sendMsg(p, cfg.getString("messages.eggPlace","&dYou have placed a new Easter Egg!"));
        }
    }
}
