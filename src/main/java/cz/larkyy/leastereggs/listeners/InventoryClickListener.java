package cz.larkyy.leastereggs.listeners;

import cz.larkyy.leastereggs.ActionType;
import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.inventory.ActionListGUIHolder;
import cz.larkyy.leastereggs.inventory.EditorGUIHolder;
import cz.larkyy.leastereggs.inventory.GUIUtils;
import cz.larkyy.leastereggs.inventory.ListGUIHolder;
import cz.larkyy.leastereggs.objects.Egg;
import cz.larkyy.leastereggs.utils.StorageUtils;
import cz.larkyy.leastereggs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;

public class InventoryClickListener implements Listener {

    private final GUIUtils guiUtils;
    private final Leastereggs main;
    private final StorageUtils storageUtils;
    private final Utils utils;

    public InventoryClickListener(Leastereggs main) {
        this.utils = main.utils;
        this.guiUtils = main.guiUtils;
        this.main = main;
        this.storageUtils = main.storageUtils;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (e.getCurrentItem() == null || e.getCurrentItem().getItemMeta() == null) return;

        InventoryHolder nholder = e.getInventory().getHolder();
        ItemStack is = e.getCurrentItem();
        ItemMeta im = is.getItemMeta();
        String locName = im.getLocalizedName();

        if (nholder instanceof ListGUIHolder) {
            ListGUIHolder holder = (ListGUIHolder) nholder;

            switch (locName) {
                case "nextPage":
                    if (guiUtils.hasNextPage(holder.getPage() + 1, "main", "eggs")) {
                        holder.setPage(holder.getPage() + 1);
                        p.openInventory(holder.getInventory());
                    }
                    break;
                case "prevPage":
                    if (holder.getPage() != 0) {
                        holder.setPage(holder.getPage() - 1);
                        p.openInventory(holder.getInventory());
                    }
                    break;
                case "close":
                    p.closeInventory();
                    break;
                default:
                    if (locName.contains("Egg")) {
                        Egg egg = main.storageUtils.getEggs().get(Integer.parseInt(im.getLocalizedName().substring(4)));
                        p.openInventory(new EditorGUIHolder(main, p, egg).getInventory());
                    }
                    break;
            }
            e.setCancelled(true);

        } else if (nholder instanceof EditorGUIHolder) {
            EditorGUIHolder holder = (EditorGUIHolder) nholder;
            Egg egg = holder.getEgg();

            switch (locName) {
                case "teleport":
                    utils.sendMsg(p, main.getCfg().getString("messages.teleported", "&eYou have been teleported to the Egg!"));
                    p.teleport(egg.getLoc().clone().add(0.5, 0.5, 0.5));
                    break;
                case "back":
                    p.openInventory(new ListGUIHolder(main, p, 0).getInventory());
                    break;
                case "delete":
                    try {
                        storageUtils.delEgg(egg);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    egg.getLoc().getBlock().setType(Material.AIR);
                    utils.sendMsg(p, main.getCfg().getString("messages.eggRemoved", "&cYou just &4Removed&c the Easter Egg!"));
                    p.openInventory(new ListGUIHolder(main, p, 0).getInventory());
                    break;
                case "actions":
                    p.openInventory(new ActionListGUIHolder(main, p, egg, 0).getInventory());
                    break;
                default:
                    break;
            }
            e.setCancelled(true);

            // ACTIONS LIST GUI
        } else if (nholder instanceof ActionListGUIHolder) {
            ActionListGUIHolder holder = (ActionListGUIHolder) nholder;
            Egg egg = holder.getEgg();
            ClickType clickType = e.getClick();

            switch (locName) {
                case "addCommand":
                    addTyping(p, egg, "add", ActionType.CMD, 0);
                    break;
                case "addMessage":
                    addTyping(p, egg, "add", ActionType.MSG, 0);
                    break;
                case "nextPage":
                    if (guiUtils.hasNextPage(holder.getPage() + 1, "actionslist", "actions")) {
                        holder.setPage(holder.getPage() + 1);
                        p.openInventory(holder.getInventory());
                    }
                    break;
                case "prevPage":
                    if (holder.getPage() != 0) {
                        holder.setPage(holder.getPage() - 1);
                        p.openInventory(holder.getInventory());
                    }
                    break;
                case "back":
                    p.openInventory(new EditorGUIHolder(main, p, egg).getInventory());
                    break;
                default:
                    if (locName.contains("actionlist")) {
                        int id = Integer.parseInt(locName.substring(11));

                        if (clickType.isRightClick() && clickType.isShiftClick()) {
                            egg.remAction(id);
                            utils.sendMsg(p, main.getCfg().getString("messages.actionRemoved", "&cYou just &4Removed&c the action!"));
                            p.openInventory(holder.getInventory());

                        } else if (clickType.isLeftClick()) {
                            addTyping(p, egg, "edit", utils.readAction(egg.getActions().get(id)), id);
                        }
                    }
                    break;
            }
            e.setCancelled(true);
        }
    }

    private void addTyping(Player p, Egg egg, String editType, ActionType actionType, int id) {
        storageUtils.addTyping(p, egg, editType, actionType, id);
        utils.sendMsg(p, main.getCfg().getString("messages.typeAction", "&7Type the action into the chat please...\n&7Type &ocancel&7 to cancel the action."));
        p.closeInventory();
    }
}
