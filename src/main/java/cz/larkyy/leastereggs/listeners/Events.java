package cz.larkyy.leastereggs.listeners;

import cz.larkyy.leastereggs.Leastereggs;

public class Events {



    public Events(Leastereggs main) {

        main.getServer().getPluginManager().registerEvents(new BlockPlaceListener(main),main);
        main.getServer().getPluginManager().registerEvents(new InteractListener(main),main);;
        main.getServer().getPluginManager().registerEvents(new BlockBreakListener(main),main);
        main.getServer().getPluginManager().registerEvents(new PlayerJoinListener(main),main);
        main.getServer().getPluginManager().registerEvents(new InventoryClickListener(main),main);
        main.getServer().getPluginManager().registerEvents(new ChatListener(main),main);
        main.getServer().getPluginManager().registerEvents(new AtEntityInteractListener(main),main);

    }

}
