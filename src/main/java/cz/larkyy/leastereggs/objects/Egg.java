package cz.larkyy.leastereggs.objects;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Egg {

    private Location loc;
    private List<String> actions;

    public Egg (Location loc, List<String> actions){
        this.loc = loc;
        this.actions = actions;
    }

    public Location getLoc() {
        return loc;
    }

    public List<String> getActions() {
        return actions;
    }

    public void addAction(String action) {
        if (actions==null) {
            actions = new ArrayList<>();
        }
        actions.add(action);
    }

    public void remAction(int id) {
        this.actions.remove(id);
    }

    public void setAction(int id,String action) {
        this.actions.remove(id);
        this.actions.add(action);
    }
}
