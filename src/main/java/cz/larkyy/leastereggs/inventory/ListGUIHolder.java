package cz.larkyy.leastereggs.inventory;

import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.inventory.GUIUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class ListGUIHolder implements InventoryHolder {

    private GUIUtils guiUtils;
    private int page;
    private Player p;
    private Leastereggs main;

    public ListGUIHolder(Leastereggs main, Player p, int page) {
        this.main = main;
        this.guiUtils = main.guiUtils;
        this.p = p;
        this.page = page;
    }

    @Override
    public @NotNull Inventory getInventory() {

        Inventory gui = Bukkit.createInventory(this,main.getCfg().getInt("inventories.main.size",45),main.utils.format(main.getCfg().getString("inventories.main.title","&d&lEE &8| Eggs List (%page%)").replace("%page%",String.valueOf(page+1))));

        guiUtils.loadItems(gui,"main",page);
        return gui;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
