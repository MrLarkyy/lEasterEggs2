package cz.larkyy.leastereggs.objects;

import java.util.List;
import java.util.UUID;

public class EggPlayer {

    private UUID uuid;
    private List<Egg> eggs;

    public EggPlayer(UUID uuid, List<Egg> eggs) {
        this.uuid = uuid;
        this.eggs = eggs;
    }

    public UUID getUuid() {
        return uuid;
    }

    public List<Egg> getEggs() {
        return eggs;
    }

    public void addEgg(Egg egg) {
        eggs.add(egg);
    }

    public void delEgg(Egg egg) {
        eggs.remove(egg);
    }
}
