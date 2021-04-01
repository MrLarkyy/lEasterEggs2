package cz.larkyy.leastereggs;

import cz.larkyy.leastereggs.commands.MainCommand;
import cz.larkyy.leastereggs.commands.TabComplete;
import cz.larkyy.leastereggs.inventory.GUIUtils;
import cz.larkyy.leastereggs.listeners.Events;
import cz.larkyy.leastereggs.runnables.EggParticles;
import cz.larkyy.leastereggs.runnables.SaveDatabase;
import cz.larkyy.leastereggs.utils.DataUtils;
import cz.larkyy.leastereggs.utils.HeadDatabaseHook;
import cz.larkyy.leastereggs.utils.StorageUtils;
import cz.larkyy.leastereggs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class Leastereggs extends JavaPlugin {

    public Utils utils = new Utils(this);
    private List<ArmorStand> armorStands = new ArrayList<>();

    private DataUtils config = new DataUtils(this,"config.yml");
    private final DataUtils data = new DataUtils(this,"data.yml");

    public StorageUtils storageUtils;
    public GUIUtils guiUtils;

    private HeadDatabaseHook headDatabaseHook;

    @Override
    public void onEnable() {

        if (!getDataFolder().exists())
            getDataFolder().mkdir();

        config.load();
        data.load();

        storageUtils = new StorageUtils(this);
        guiUtils = new GUIUtils(this);

        getCommand("leastereggs").setExecutor(new MainCommand(this));
        getCommand("leastereggs").setTabCompleter(new TabComplete(this));
        new Events(this);

        storageUtils.loadEggs();
        storageUtils.loadPlayers();
        new SaveDatabase(this).runTaskTimerAsynchronously(this,40,config.getInt("settings.autosave",100));
        new EggParticles(this).runTaskTimerAsynchronously(this,60,config.getInt("settings.particles.notFound.cooldown",20));

        if(Bukkit.getPluginManager().getPlugin("HeadDatabase") != null) {
            headDatabaseHook =  new HeadDatabaseHook();
        }

    }

    @Override
    public void onDisable() {

        try {
            storageUtils.saveEggs();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public DataUtils getCfg() {
        return config;
    }

    public DataUtils getData() {
        return data;
    }

    public void reloadCfg() {
        config.load();
    }

    public HeadDatabaseHook getHeadDatabaseHook() {
        return headDatabaseHook;
    }

    public List<ArmorStand> getArmorStands() {
        return armorStands;
    }

    public void addArmorStand(ArmorStand as) {
        armorStands.add(as);
    }

    public void delArmorStand(ArmorStand as) {
        armorStands.remove(as);
    }
}
