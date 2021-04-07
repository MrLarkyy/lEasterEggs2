package cz.larkyy.leastereggs.inventory;

import cz.larkyy.leastereggs.ActionType;
import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.objects.Egg;
import cz.larkyy.leastereggs.utils.StorageUtils;
import cz.larkyy.leastereggs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUIUtils {

    private Leastereggs main;
    private StorageUtils storageUtils;
    private Utils utils;

    public GUIUtils(Leastereggs main) {
        this.main = main;
        this.storageUtils = main.storageUtils;
        this.utils = main.utils;

    }

    public boolean isMoreSlots(String invType, String itemType) {
        return main.getCfg().getConfiguration().getConfigurationSection("inventories." + invType + ".items." + itemType).getKeys(true).contains("slots");
    }

    public boolean hasNextPage(int page, String invType, String itemType) {
        List<Integer> slots = main.getCfg().getConfiguration().getIntegerList("inventories.main.items." + itemType + ".slots");
        int first = slots.size() * page;
        return first < storageUtils.getEggs().size();
    }

    public void loadEggItems(Inventory gui, List<ItemStack> eggItems, int page, String slotsPath) {
        List<Integer> slots = main.getCfg().getConfiguration().getIntegerList(slotsPath);
        int first = slots.size() * page;
        for (int i : slots) {
            try {
                gui.setItem(i, eggItems.get(first));
            } catch (IndexOutOfBoundsException ex) {
                continue;
            }
            first++;
        }
    }

    public void loadActions(Inventory gui, Egg egg, int page) {
        List<ItemStack> actionItems = new ArrayList<>();
        int id = 0;
        for (String str : egg.getActions()) {
            if (utils.readAction(str).equals(ActionType.CMD)) {
                actionItems.add(utils.mkItem(
                        //MATERIAL
                        Material.valueOf(main.getCfg().getConfiguration().getString("inventories.actionslist.items.actions.command.material", "COMMAND_BLOCK")),
                        //NAME
                        utils.format(main.getCfg().getConfiguration().getString("inventories.actionslist.items.actions.command.name", "&eAction %id% &7(Command)").replace("%id%", id + "")),
                        //LOCALIZEDNAME
                        "actionlist " + id,
                        //LORE
                        utils.replaceInLore(utils.formatLore(main.getCfg().getConfiguration().getStringList("inventories.actionslist.items.actions.command.lore")), Arrays.asList("%string%"), Arrays.asList(utils.stringOfAction(str))),
                        //TEXTURE
                        main.getCfg().getConfiguration().getString("inventories.actionslist.items.actions.command.texture", null)
                ));
            } else if (utils.readAction(str).equals(ActionType.MSG)) {
                actionItems.add(utils.mkItem(
                        //MATERIAL
                        Material.valueOf(main.getCfg().getConfiguration().getString("inventories.actionslist.items.actions.message.material", "PAPER")),
                        //NAME
                        utils.format(main.getCfg().getConfiguration().getString("inventories.actionslist.items.actions.message.name", "&eAction %id% &7(Message)").replace("%id%", id + "")),
                        //LOCALIZEDNAME
                        "actionlist " + id,
                        //LORE
                        utils.replaceInLore(utils.formatLore(main.getCfg().getConfiguration().getStringList("inventories.actionslist.items.actions.message.lore")), Arrays.asList("%string%"), Arrays.asList(utils.stringOfAction(str))),
                        //TEXTURE
                        main.getCfg().getConfiguration().getString("inventories.actionslist.items.actions.message.texture", null)
                ));
            }
            id++;
        }
        loadEggItems(gui, actionItems, page, "inventories.actionslist.items.actions.slots");
    }


}
