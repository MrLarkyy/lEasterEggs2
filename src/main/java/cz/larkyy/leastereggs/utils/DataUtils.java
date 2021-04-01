package cz.larkyy.leastereggs.utils;

import cz.larkyy.leastereggs.Leastereggs;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DataUtils {
    private final File file;
    private FileConfiguration config;

    private final Leastereggs main;
    private final Utils utils;

    public DataUtils(Leastereggs main, String path) {
        this.main = main;
        this.file = new File(main.getDataFolder(), path);
        this.utils = main.utils;
    }

    public void load() {
        if (!file.exists())
            try {
                main.saveResource(file.getName(), false);
            } catch (IllegalArgumentException e) {
                try {
                    file.createNewFile();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }


        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfiguration() {
        if (config == null)
            load();

        return config;
    }

    public void save() throws IOException {
        config.save(file);
    }


    public String getString(String path, String defValue) {
        if (config.getString(path)!=null) {
            return config.getString(path);
        } else {
            utils.sendConsoleMsg("&cPath "+path+" in "+file.getName()+" wasn't found! Using default value...");
            return defValue;
        }
    }
    public Integer getInt(String path, Integer defValue) {
        if (config.getString(path)!=null) {
            return config.getInt(path);
        } else {
            utils.sendConsoleMsg("&cPath "+path+" in "+file.getName()+" wasn't found! Using default value...");
            return defValue;
        }
    }


    public List<String> getStringList(String path, List<String> defValue) {
        if (!config.getStringList(path).isEmpty()) {
            return config.getStringList(path);
        } else {
            utils.sendConsoleMsg("&cPath "+path+" in "+file.getName()+" wasn't found! Using default value...");
            return defValue;
        }
    }

    public boolean hasPermission(Player p, String path, String defValue) {
        return p.hasPermission(getString("settings.permissions."+path, defValue));
    }
}

