package cz.larkyy.leastereggs.inventory;

import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.objects.Egg;
import cz.larkyy.leastereggs.utils.StorageUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class EditorGUIHolder implements InventoryHolder {

    private GUIUtils guiUtils;
    private Player p;
    private Egg egg;
    private Leastereggs main;
    private StorageUtils storageUtils;

    public EditorGUIHolder(Leastereggs main, Player p, Egg egg) {
        this.guiUtils = main.guiUtils;
        this.storageUtils = main.storageUtils;
        this.main = main;
        this.p = p;
        this.egg = egg;
    }

    @Override
    public @NotNull Inventory getInventory() {

        Inventory gui = Bukkit.createInventory(this,main.getCfg().getInt("inventories.editor.size",45),main.utils.format(main.getCfg().getString("inventories.editor.title","&d&lEE &8| Egg Editor (ID #%id%)").replace("%id%",storageUtils.getEggID(egg)+"")));

        guiUtils.loadItems(gui,"editor",0);
        return gui;
    }

    public Egg getEgg() {
        return egg;
    }
}
