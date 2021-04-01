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

public class EditorGUIHolder implements InventoryHolder {

    private GUIUtils guiUtils;
    private Player p;
    private Egg egg;
    private Leastereggs main;
    private StorageUtils storageUtils;
    private Inventory gui;
    private Utils utils;

    public EditorGUIHolder(Leastereggs main, Player p, Egg egg) {
        this.guiUtils = main.guiUtils;
        this.storageUtils = main.storageUtils;
        this.main = main;
        this.p = p;
        this.egg = egg;
        this.utils = main.utils;
    }

    @Override
    public @NotNull Inventory getInventory() {

        gui = Bukkit.createInventory(this,main.getCfg().getInt("inventories.editor.size",45),main.utils.format(main.getCfg().getString("inventories.editor.title","&d&lEE &8| Egg Editor (ID #%id%)").replace("%id%",storageUtils.getEggID(egg)+"")));

        solveItems();
        return gui;
    }

    public Egg getEgg() {
        return egg;
    }

    private void solveItems() {
        for (String itemType : main.getCfg().getConfiguration().getConfigurationSection("inventories.editor.items").getKeys(false)) {

            switch (itemType) {
                case "teleport":
                    loadItem(itemType,"teleport");
                    break;
                case "delete":
                    loadItem(itemType,"delete");
                    break;
                case "back":
                    loadItem(itemType,"back");
                    break;
                case "actions":
                    loadItem(itemType,"actions");
                    break;
                default:
                    loadItem(itemType,null);
                    break;
            }
        }
    }
    private void loadItem(String itemType,String localizedName){
        if (!isMoreSlots(itemType)) {
            int slot = main.getCfg().getConfiguration().getInt("inventories.editor.items." + itemType + ".slot");
            if (slot!=-1) {
                gui.setItem(slot, mkItem(itemType, localizedName));
            }

        } else {
            ItemStack is = mkItem(itemType, localizedName);
            for (int i : main.getCfg().getConfiguration().getIntegerList("inventories.editor.items." + itemType + ".slots")) {
                gui.setItem(i,is);
            }
        }
    }

    private ItemStack mkItem(String itemType,String localizedName) {
        return main.utils.mkItem(
                //MATERIAL
                Material.valueOf(main.getCfg().getConfiguration().getString("inventories.editor.items." + itemType + ".material", "STONE")),
                //NAME
                utils.format(main.getCfg().getConfiguration().getString("inventories.editor.items." + itemType + ".name", null)),
                //LOCALIZEDNAME
                localizedName,
                //LORE
                utils.formatLore(main.getCfg().getConfiguration().getStringList("inventories.editor.items." + itemType + ".lore")),
                //TEXTURE
                main.getCfg().getConfiguration().getString("inventories.editor.items." + itemType + ".texture", null));
    }

    private boolean isMoreSlots(String itemType) {
        return guiUtils.isMoreSlots("editor",itemType);
    }

}
