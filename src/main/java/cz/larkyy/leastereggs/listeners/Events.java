package cz.larkyy.leastereggs.listeners;

import cz.larkyy.leastereggs.Leastereggs;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Events {

    /*private Leastereggs main;
    private DataUtils cfg;
    private DataUtils data;
    private Utils utils;*/

    public Events(Leastereggs main) {

        main.getServer().getPluginManager().registerEvents(new BlockPlaceListener(main),main);
        main.getServer().getPluginManager().registerEvents(new InteractListener(main),main);;
        main.getServer().getPluginManager().registerEvents(new BlockBreakListener(main),main);
        main.getServer().getPluginManager().registerEvents(new PlayerJoinListener(main),main);
        main.getServer().getPluginManager().registerEvents(new InventoryClickListener(main),main);
        main.getServer().getPluginManager().registerEvents(new ChatListener(main),main);

    }

}
