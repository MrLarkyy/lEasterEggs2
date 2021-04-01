package cz.larkyy.leastereggs.inventory;

import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.objects.Egg;
import cz.larkyy.leastereggs.utils.StorageUtils;
import cz.larkyy.leastereggs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ActionListGUIHolder implements InventoryHolder {

    private GUIUtils guiUtils;
    private Player p;
    private Egg egg;
    private Leastereggs main;
    private Utils utils;
    private Inventory gui;
    private int page;

    public ActionListGUIHolder(Leastereggs main, Player p, Egg egg, int page) {
        this.guiUtils = main.guiUtils;
        this.main = main;
        this.page = page;
        this.p = p;
        this.egg = egg;
        this.utils = main.utils;
    }

    @Override
    public @NotNull Inventory getInventory() {

        gui = Bukkit.createInventory(this,main.getCfg().getInt("inventories.actionslist.size",45),main.utils.format(main.getCfg().getString("inventories.actionslist.title","&d&lEE &8| Actions List (Page #%page%)").replace("%page%",String.valueOf(page+1))));

        solveItems();
        return gui;
    }

    public Egg getEgg() {
        return egg;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    private void solveItems() {
        for (String itemType : main.getCfg().getConfiguration().getConfigurationSection("inventories.actionslist.items").getKeys(false)) {

            switch (itemType) {
                case "prevPage":
                    loadItem(itemType,"prevPage");
                    break;
                case "nextPage":
                    loadItem(itemType,"nextPage");
                    break;
                case "back":
                    loadItem(itemType,"back");
                    break;
                case "messageAction":
                    loadItem(itemType,"addMessage");
                    break;
                case "commandAction":
                    loadItem(itemType,"addCommand");
                    break;
                case "actions":
                    guiUtils.loadActions(gui,egg,page);
                    break;
                default:
                    loadItem(itemType,null);
                    break;
            }
        }
    }
    private void loadItem(String itemType,String localizedName){
        if (!isMoreSlots(itemType)) {
            int slot = main.getCfg().getConfiguration().getInt("inventories.actionslist.items." + itemType + ".slot");
            if (slot!=-1) {
                gui.setItem(slot, mkItem(itemType, localizedName));
            }

        } else {
            ItemStack is = mkItem(itemType, localizedName);
            for (int i : main.getCfg().getConfiguration().getIntegerList("inventories.actionslist.items." + itemType + ".slots")) {
                gui.setItem(i,is);
            }
        }
    }

    private ItemStack mkItem(String itemType,String localizedName) {
        return main.utils.mkItem(
                //MATERIAL
                Material.valueOf(main.getCfg().getConfiguration().getString("inventories.actionslist.items." + itemType + ".material", "STONE")),
                //NAME
                utils.format(main.getCfg().getConfiguration().getString("inventories.actionslist.items." + itemType + ".name", null)),
                //LOCALIZEDNAME
                localizedName,
                //LORE
                utils.formatLore(main.getCfg().getConfiguration().getStringList("inventories.actionslist.items." + itemType + ".lore")),
                //TEXTURE
                main.getCfg().getConfiguration().getString("inventories.actionslist.items." + itemType + ".texture", null));
    }

    private boolean isMoreSlots(String itemType) {
        return guiUtils.isMoreSlots("actionslist",itemType);
    }
}
