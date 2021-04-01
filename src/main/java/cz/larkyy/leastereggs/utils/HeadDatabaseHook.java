package cz.larkyy.leastereggs.utils;

import me.arcaniax.hdb.api.HeadDatabaseAPI;

public class HeadDatabaseHook {

    private final HeadDatabaseAPI headDatabaseAPI;

    public HeadDatabaseHook() {
        headDatabaseAPI = new HeadDatabaseAPI();
    }

    public HeadDatabaseAPI getHeadDatabaseAPI() {
        return headDatabaseAPI;
    }
}
