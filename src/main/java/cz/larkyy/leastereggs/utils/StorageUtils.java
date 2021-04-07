package cz.larkyy.leastereggs.utils;

import cz.larkyy.leastereggs.ActionType;
import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.objects.Egg;
import cz.larkyy.leastereggs.objects.EggPlayer;
import cz.larkyy.leastereggs.objects.TypingPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.*;

public class StorageUtils {

    private Leastereggs main;
    private DataUtils data;

    private HashMap<Integer, Egg> eggs;
    private HashMap<UUID, EggPlayer> players;

    private HashMap<Player, TypingPlayer> typing;

    public StorageUtils(Leastereggs main) {
        this.main = main;
        this.data = main.getData();

        typing = new HashMap<>();
    }


    public void loadEggs() {
        HashMap<Integer, Egg> loadedEggs = new HashMap<>();
        if (data.getConfiguration().getConfigurationSection("eggs") != null) {
            for (String str : data.getConfiguration().getConfigurationSection("eggs").getKeys(false)) {

                if (Bukkit.getWorld(getDataFile().getString("eggs." + str + ".world")) != null) {
                    try {
                        double x = getDataFile().getDouble("eggs." + str + ".x");
                        double y = getDataFile().getDouble("eggs." + str + ".y");
                        double z = getDataFile().getDouble("eggs." + str + ".z");
                        World w = Bukkit.getWorld(getDataFile().getString("eggs." + str + ".world"));
                        Location loc = new Location(w, x, y, z);
                        List<String> actions = getDataFile().getStringList("eggs." + str + ".actions");

                        Egg egg = new Egg(loc, actions);
                        loadedEggs.put(Integer.parseInt(str), egg);
                    } catch (IllegalArgumentException ex) {
                        main.getServer().getConsoleSender().sendMessage("Egg #" + str + " has unknown location!");
                    }
                }

            }
        }
        this.eggs = loadedEggs;
    }

    public void loadPlayers() {
        HashMap<UUID, EggPlayer> loadedPlayers = new HashMap<>();
        if (getDataFile().getConfigurationSection("players") != null) {
            for (String uuidStr : getDataFile().getConfigurationSection("players").getKeys(false)) {

                UUID uuid = UUID.fromString(uuidStr);
                List<Egg> eggList = new ArrayList<>();

                for (Integer i : getDataFile().getIntegerList("players." + uuidStr)) {
                    eggList.add(eggs.get(i));
                }

                loadedPlayers.put(uuid, new EggPlayer(uuid, eggList));

            }
        }
        players = loadedPlayers;
    }

    public void saveEggs() throws IOException {
        if (eggs != null)
            for (Map.Entry<Integer, Egg> pair : eggs.entrySet()) {
                Egg egg = pair.getValue();

                getDataFile().set("eggs." + pair.getKey() + ".world", egg.getLoc().getWorld().getName());
                getDataFile().set("eggs." + pair.getKey() + ".x", egg.getLoc().getX());
                getDataFile().set("eggs." + pair.getKey() + ".y", egg.getLoc().getY());
                getDataFile().set("eggs." + pair.getKey() + ".z", egg.getLoc().getZ());
                getDataFile().set("eggs." + pair.getKey() + ".actions", egg.getActions());
            }
        data.save();
    }

    public void savePlayers() throws IOException {
        for (Map.Entry<UUID, EggPlayer> pair : players.entrySet()) {
            List<Integer> ids = new ArrayList<>();

            EggPlayer eggPlayer = pair.getValue();

            for (Egg egg : eggPlayer.getEggs()) {
                ids.add(getEggID(egg));
            }

            getDataFile().set("players." + pair.getKey(), ids);
        }
        data.save();
    }

    public void addEgg(Egg egg) {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            if (!eggs.containsKey(i)) {
                eggs.put(i, egg);
                break;
            }
        }
    }

    public void delEgg(Egg egg) throws IOException {
        if (egg != null) {
            for (Map.Entry<Integer, Egg> pair : eggs.entrySet()) {
                if (pair.getValue().equals(egg)) {
                    eggs.remove(pair.getKey());
                    getDataFile().set("eggs." + pair.getKey(), null);
                    break;
                }
            }
            saveEggs();
        }
    }

    public Egg getEgg(Location loc) {
        for (Map.Entry<Integer, Egg> pair : eggs.entrySet()) {
            if (pair.getValue().getLoc().equals(loc))
                return pair.getValue();
        }
        return null;
    }

    public HashMap<Integer, Egg> getEggs() {
        return eggs;
    }

    public boolean isEgg(Location loc) {
        for (Map.Entry<Integer, Egg> pair : eggs.entrySet()) {
            Location eggLoc = pair.getValue().getLoc().clone();

            eggLoc.setYaw(0);
            eggLoc.setPitch(0);

            if (eggLoc.equals(loc))
                return true;
        }
        return false;
    }

    public int getEggID(Egg egg) {
        for (Map.Entry<Integer, Egg> pair2 : eggs.entrySet()) {
            if (egg.equals(pair2.getValue())) {
                return pair2.getKey();
            }
        }
        return 0;
    }

    public void addPlayer(UUID uuid, EggPlayer eggPlayer) {
        players.put(uuid, eggPlayer);
    }

    public HashMap<UUID, EggPlayer> getPlayers() {
        return players;
    }

    public EggPlayer getPlayer(UUID uuid) {
        return players.get(uuid);
    }

    public HashMap<Player, TypingPlayer> getTyping() {
        return typing;
    }

    public void addTyping(Player p, Egg egg, String editType, ActionType actionType, int id) {
        typing.put(p, new TypingPlayer(editType, actionType, p, egg, id));
    }

    public void delTyping(Player p) {
        typing.remove(p);
    }

    private FileConfiguration getDataFile() {
        return main.getData().getConfiguration();
    }
}
