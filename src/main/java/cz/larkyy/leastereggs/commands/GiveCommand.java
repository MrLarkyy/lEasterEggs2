package cz.larkyy.leastereggs.commands;

import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.utils.DataUtils;
import cz.larkyy.leastereggs.utils.StorageUtils;
import cz.larkyy.leastereggs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class GiveCommand {

    private Utils utils;
    private DataUtils cfg;
    private StorageUtils storageUtils;

    public GiveCommand(Player p, Leastereggs main) {
        this.utils = main.utils;
        this.cfg = main.getCfg();
        this.storageUtils = main.storageUtils;

        p.getInventory().addItem(utils.mkItem(
                Material.valueOf(cfg.getString("eggItem.block","PLAYER_HEAD")),
                cfg.getString("eggItem.displayName","&dEaster Egg &fBlock &7(Place)"),
                "EasterEgg-Block",
                cfg.getStringList("eggItem.lore",Arrays.asList("","&7Place to create a new Egg!")),
                cfg.getString("eggItem.texture","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjU2ZjdmM2YzNTM2NTA2NjI2ZDVmMzViNDVkN2ZkZjJkOGFhYjI2MDA4NDU2NjU5ZWZlYjkxZTRjM2E5YzUifX19")
        ));


    }

}
