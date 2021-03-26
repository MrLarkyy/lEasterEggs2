package cz.larkyy.leastereggs.inventory;

import cz.larkyy.leastereggs.ActionType;
import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.objects.Actions;
import cz.larkyy.leastereggs.objects.Egg;
import cz.larkyy.leastereggs.utils.DataUtils;
import cz.larkyy.leastereggs.utils.StorageUtils;
import cz.larkyy.leastereggs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GUIUtils {

    private Leastereggs main;
    private StorageUtils storageUtils;
    private Utils utils;

    public GUIUtils(Leastereggs main) {
        this.main = main;
        this.storageUtils = main.storageUtils;
        this.utils = main.utils;

    }

    public void loadItems(Inventory gui,String invType, int page) {
        for (String itemType : main.getCfg().getConfiguration().getConfigurationSection("inventories."+invType+".items").getKeys(false)) {

                // EGG ITEMS
            if (!itemType.equals("action") && !invType.equals("actionlist")) {
                if (itemType.equals("eggs")) {
                    List<ItemStack> eggItems = new ArrayList<>();
                    for (Map.Entry<Integer, Egg> pair : storageUtils.getEggs().entrySet()) {
                        eggItems.add(utils.mkItem(
                                //MATERIAL
                                Material.valueOf(main.getCfg().getConfiguration().getString("inventories." + invType + ".items." + itemType + ".material", "STONE")),
                                //NAME
                                utils.format(main.getCfg().getConfiguration().getString("inventories." + invType + ".items." + itemType + ".name", "&dEaster Egg &7#%id%").replace("%id%", pair.getKey().toString())),
                                //LOCALIZEDNAME
                                "Egg " + pair.getKey(),
                                //LORE
                                utils.formatLore(main.getCfg().getConfiguration().getStringList("inventories." + invType + ".items." + itemType + ".lore")),
                                //TEXTURE
                                main.getCfg().getConfiguration().getString("inventories." + invType + ".items." + itemType + ".texture", null)
                        ));
                    }
                    loadEggItems(gui, eggItems, page, "inventories.main.items.eggs.slots");

                    // PREVIOUS PAGE ITEM
                } else if (itemType.equals("prevPage")) {
                    gui.setItem(main.getCfg().getConfiguration().getInt("inventories." + invType + ".items." + itemType + ".slot"), utils.mkItem(
                            //MATERIAL
                            Material.valueOf(main.getCfg().getConfiguration().getString("inventories." + invType + ".items." + itemType + ".material", "ARROW")),
                            //NAME
                            utils.format(main.getCfg().getConfiguration().getString("inventories." + invType + ".items." + itemType + ".name", "&ePrevious Page")),
                            //LOCALIZEDNAME
                            "prevPage",
                            //LORE
                            utils.formatLore(main.getCfg().getConfiguration().getStringList("inventories." + invType + ".items." + itemType + ".lore")),
                            //TEXTURE
                            main.getCfg().getConfiguration().getString("inventories." + invType + ".items." + itemType + ".texture", null)));

                    // NEXT PAGE ITEM
                } else if (itemType.equals("nextPage")) {
                    gui.setItem(main.getCfg().getConfiguration().getInt("inventories." + invType + ".items." + itemType + ".slot"), utils.mkItem(
                            //MATERIAL
                            Material.valueOf(main.getCfg().getConfiguration().getString("inventories." + invType + ".items." + itemType + ".material", "ARROW")),
                            //NAME
                            utils.format(main.getCfg().getConfiguration().getString("inventories." + invType + ".items." + itemType + ".name", "&eNext Page")),
                            //LOCALIZEDNAME
                            "nextPage",
                            //LORE
                            utils.formatLore(main.getCfg().getConfiguration().getStringList("inventories." + invType + ".items." + itemType + ".lore")),
                            //TEXTURE
                            main.getCfg().getConfiguration().getString("inventories." + invType + ".items." + itemType + ".texture", null)));

                } else if (itemType.equals("teleport")) {
                    gui.setItem(main.getCfg().getConfiguration().getInt("inventories." + invType + ".items." + itemType + ".slot"), utils.mkItem(
                            //MATERIAL
                            Material.valueOf(main.getCfg().getConfiguration().getString("inventories." + invType + ".items." + itemType + ".material", "ENDER_PEARL")),
                            //NAME
                            utils.format(main.getCfg().getConfiguration().getString("inventories." + invType + ".items." + itemType + ".name", "&eTeleport to the Egg")),
                            //LOCALIZEDNAME
                            "teleport",
                            //LORE
                            utils.formatLore(main.getCfg().getConfiguration().getStringList("inventories." + invType + ".items." + itemType + ".lore")),
                            //TEXTURE
                            main.getCfg().getConfiguration().getString("inventories." + invType + ".items." + itemType + ".texture", null)));

                } else if (itemType.equals("delete")) {
                    gui.setItem(main.getCfg().getConfiguration().getInt("inventories." + invType + ".items." + itemType + ".slot"), utils.mkItem(
                            //MATERIAL
                            Material.valueOf(main.getCfg().getConfiguration().getString("inventories." + invType + ".items." + itemType + ".material", "BARRIER")),
                            //NAME
                            utils.format(main.getCfg().getConfiguration().getString("inventories." + invType + ".items." + itemType + ".name", "&cDelete the Egg")),
                            //LOCALIZEDNAME
                            "delete",
                            //LORE
                            utils.formatLore(main.getCfg().getConfiguration().getStringList("inventories." + invType + ".items." + itemType + ".lore")),
                            //TEXTURE
                            main.getCfg().getConfiguration().getString("inventories." + invType + ".items." + itemType + ".texture", null)));

                } else if (itemType.equals("back")) {
                    gui.setItem(main.getCfg().getConfiguration().getInt("inventories." + invType + ".items." + itemType + ".slot"), utils.mkItem(
                            //MATERIAL
                            Material.valueOf(main.getCfg().getConfiguration().getString("inventories." + invType + ".items." + itemType + ".material", "OAK_DOOR")),
                            //NAME
                            utils.format(main.getCfg().getConfiguration().getString("inventories." + invType + ".items." + itemType + ".name", "&eBack")),
                            //LOCALIZEDNAME
                            "back",
                            //LORE
                            utils.formatLore(main.getCfg().getConfiguration().getStringList("inventories." + invType + ".items." + itemType + ".lore")),
                            //TEXTURE
                            main.getCfg().getConfiguration().getString("inventories." + invType + ".items." + itemType + ".texture", null)));

                } else if (itemType.equals("close")) {
                    gui.setItem(main.getCfg().getConfiguration().getInt("inventories." + invType + ".items." + itemType + ".slot"), utils.mkItem(
                            //MATERIAL
                            Material.valueOf(main.getCfg().getConfiguration().getString("inventories." + invType + ".items." + itemType + ".material", "IRON_DOOR")),
                            //NAME
                            utils.format(main.getCfg().getConfiguration().getString("inventories." + invType + ".items." + itemType + ".name", "&cClose")),
                            //LOCALIZEDNAME
                            "close",
                            //LORE
                            utils.formatLore(main.getCfg().getConfiguration().getStringList("inventories." + invType + ".items." + itemType + ".lore")),
                            //TEXTURE
                            main.getCfg().getConfiguration().getString("inventories." + invType + ".items." + itemType + ".texture", null)));

                } else if (itemType.equals("actions")) {
                    gui.setItem(main.getCfg().getConfiguration().getInt("inventories." + invType + ".items." + itemType + ".slot"), utils.mkItem(
                            //MATERIAL
                            Material.valueOf(main.getCfg().getConfiguration().getString("inventories." + invType + ".items." + itemType + ".material", "PAPER")),
                            //NAME
                            utils.format(main.getCfg().getConfiguration().getString("inventories." + invType + ".items." + itemType + ".name", "&eActions")),
                            //LOCALIZEDNAME
                            "actions",
                            //LORE
                            utils.formatLore(main.getCfg().getConfiguration().getStringList("inventories." + invType + ".items." + itemType + ".lore")),
                            //TEXTURE
                            main.getCfg().getConfiguration().getString("inventories." + invType + ".items." + itemType + ".texture", null)));

                } else if (itemType.equals("messageAction")) {
                    gui.setItem(main.getCfg().getConfiguration().getInt("inventories." + invType + ".items." + itemType + ".slot"), utils.mkItem(
                            //MATERIAL
                            Material.valueOf(main.getCfg().getConfiguration().getString("inventories." + invType + ".items." + itemType + ".material", "WHITE_STAINED_GLASS_PANE")),
                            //NAME
                            utils.format(main.getCfg().getConfiguration().getString("inventories." + invType + ".items." + itemType + ".name", "&eAdd Message Action")),
                            //LOCALIZEDNAME
                            "addMessage",
                            //LORE
                            utils.formatLore(main.getCfg().getConfiguration().getStringList("inventories." + invType + ".items." + itemType + ".lore")),
                            //TEXTURE
                            main.getCfg().getConfiguration().getString("inventories." + invType + ".items." + itemType + ".texture", null)));
                } else if (itemType.equals("commandAction")) {
                    gui.setItem(main.getCfg().getConfiguration().getInt("inventories." + invType + ".items." + itemType + ".slot"), utils.mkItem(
                            //MATERIAL
                            Material.valueOf(main.getCfg().getConfiguration().getString("inventories." + invType + ".items." + itemType + ".material", "ORANGE_STAINED_GLASS_PANE")),
                            //NAME
                            utils.format(main.getCfg().getConfiguration().getString("inventories." + invType + ".items." + itemType + ".name", "&eAdd Command Action")),
                            //LOCALIZEDNAME
                            "addCommand",
                            //LORE
                            utils.formatLore(main.getCfg().getConfiguration().getStringList("inventories." + invType + ".items." + itemType + ".lore")),
                            //TEXTURE
                            main.getCfg().getConfiguration().getString("inventories." + invType + ".items." + itemType + ".texture", null)));
                }
                else {
                    if (isMoreSlots(invType, itemType)) {
                        for (int i : main.getCfg().getConfiguration().getIntegerList("inventories." + invType + ".items." + itemType + ".slots")) {
                            gui.setItem(i, utils.mkItem(
                                    //MATERIAL
                                    Material.valueOf(main.getCfg().getConfiguration().getString("inventories." + invType + ".items." + itemType + ".material", "STONE")),
                                    //NAME
                                    utils.format(main.getCfg().getConfiguration().getString("inventories." + invType + ".items." + itemType + ".name", null)),
                                    //LOCALIZEDNAME
                                    null,
                                    //LORE
                                    utils.formatLore(main.getCfg().getConfiguration().getStringList("inventories." + invType + ".items." + itemType + ".lore")),
                                    //TEXTURE
                                    main.getCfg().getConfiguration().getString("inventories." + invType + ".items." + itemType + ".texture", null)
                            ));
                        }
                    } else {
                        gui.setItem(main.getCfg().getConfiguration().getInt("inventories." + invType + ".items." + itemType + ".slot"), utils.mkItem(
                                //MATERIAL
                                Material.valueOf(main.getCfg().getConfiguration().getString("inventories." + invType + ".items." + itemType + ".material", "STONE")),
                                //NAME
                                utils.format(main.getCfg().getConfiguration().getString("inventories." + invType + ".items." + itemType + ".name", null)),
                                //LOCALIZEDNAME
                                null,
                                //LORE
                                utils.formatLore(main.getCfg().getConfiguration().getStringList("inventories." + invType + ".items." + itemType + ".lore")),
                                //TEXTURE
                                main.getCfg().getConfiguration().getString("inventories." + invType + ".items." + itemType + ".texture", null)
                        ));
                    }
                }
            }
        }
    }

    public boolean isMoreSlots(String invType,String itemType) {
        return main.getCfg().getConfiguration().getConfigurationSection("inventories." + invType + ".items." + itemType).getKeys(true).contains("slots");
    }

    public boolean hasNextPage(int page){
        List<Integer> slots = main.getCfg().getConfiguration().getIntegerList("inventories.main.items.eggs.slots");
        int first = slots.size() * page;
        return first < storageUtils.getEggs().size();
    }

    public void loadEggItems(Inventory gui,List<ItemStack> eggItems,int page, String slotsPath){
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
    public void loadActions (Inventory gui,Egg egg, int page) {
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
        loadEggItems(gui,actionItems,page,"inventories.actionslist.items.actions.slots");
    }


}
