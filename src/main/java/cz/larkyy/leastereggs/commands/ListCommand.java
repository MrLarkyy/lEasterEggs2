package cz.larkyy.leastereggs.commands;

import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.utils.DataUtils;
import cz.larkyy.leastereggs.utils.StorageUtils;
import cz.larkyy.leastereggs.utils.Utils;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ListCommand {

    private Utils utils;
    private DataUtils cfg;
    private StorageUtils storageUtils;
    private java.util.List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15);

    public ListCommand(Leastereggs main, Player p) {
        this.utils = main.utils;
        this.cfg = main.getCfg();
        this.storageUtils = main.storageUtils;
        for (int i : list) {
            p.sendMessage(i + "");
            if (i == 5)
                break;
        }
    }
}
