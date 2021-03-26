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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;

public class InventoryClickListener implements Listener {

    private GUIUtils guiUtils;
    private Leastereggs main;
    private StorageUtils storageUtils;
    private Utils utils;

    public InventoryClickListener(Leastereggs main) {
        this.utils = main.utils;
        this.guiUtils = main.guiUtils;
        this.main = main;
        this.storageUtils = main.storageUtils;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        // MAIN GUI
        if (e.getInventory().getHolder() instanceof ListGUIHolder) {
            ListGUIHolder holder = (ListGUIHolder) e.getInventory().getHolder();

            if (e.getCurrentItem() !=null) {

                ItemStack is = e.getCurrentItem();
                ItemMeta im = is.getItemMeta();

                if (im.getLocalizedName().equals("nextPage")) {

                    if (guiUtils.hasNextPage(holder.getPage() + 1)) {
                        holder.setPage(holder.getPage() + 1);
                        p.openInventory(holder.getInventory());
                    }

                } else if (im.getLocalizedName().equals("prevPage")) {
                    if (holder.getPage()!=0) {
                        holder.setPage(holder.getPage()-1);
                        p.openInventory(holder.getInventory());
                    }
                } else if (im.getLocalizedName().contains("Egg")) {
                    p.openInventory(new EditorGUIHolder(main,p,main.storageUtils.getEggs().get(Integer.parseInt(im.getLocalizedName().substring(4)))).getInventory());
                } else if (im.getLocalizedName().equals("close")) {
                    p.closeInventory();
                }
            }

            e.setCancelled(true);

        // EDITOR GUI
        } else if (e.getInventory().getHolder() instanceof EditorGUIHolder) {
            EditorGUIHolder holder = (EditorGUIHolder) e.getInventory().getHolder();

            if (e.getCurrentItem()!=null) {

                ItemStack is = e.getCurrentItem();
                ItemMeta im = is.getItemMeta();
                Egg egg = holder.getEgg();

                if (im.getLocalizedName().equals("teleport")) {
                    utils.sendMsg(p,main.getCfg().getString("messages.teleported","&eYou have been teleported to the Egg!"));
                    p.teleport(egg.getLoc().clone().add(0.5,0.5,0.5));

                } else if (im.getLocalizedName().equals("back")) {
                    p.openInventory(new ListGUIHolder(main,p,0).getInventory());

                } else if (im.getLocalizedName().equals("delete")) {
                    try {
                        storageUtils.delEgg(egg);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    egg.getLoc().getBlock().setType(Material.AIR);
                    utils.sendMsg(p,main.getCfg().getString("messages.eggRemoved","&cYou just &4Removed&c the Easter Egg!"));
                    p.openInventory(new ListGUIHolder(main,p,0).getInventory());
                } else if (im.getLocalizedName().equals("actions")) {
                    p.openInventory(new ActionListGUIHolder(main, p, egg,0).getInventory());
                }

            }

            e.setCancelled(true);

        // ACTIONS LIST GUI
        } else if (e.getInventory().getHolder() instanceof ActionListGUIHolder) {
            ActionListGUIHolder holder = (ActionListGUIHolder) e.getInventory().getHolder();

            if (e.getCurrentItem()!=null) {

                ItemStack is = e.getCurrentItem();
                ItemMeta im = is.getItemMeta();
                Egg egg = holder.getEgg();

                if (im.getLocalizedName().contains("actionlist")) {
                    int id = Integer.parseInt(im.getLocalizedName().substring(11));
                    if (e.getClick().isRightClick() && e.getClick().isShiftClick()) {
                        egg.remAction(id);
                        utils.sendMsg(p,main.getCfg().getString("messages.actionRemoved","&cYou just &4Removed&c the action!"));
                        p.openInventory(holder.getInventory());
                    } else if(e.getClick().isLeftClick()) {
                        utils.sendMsg(p,main.getCfg().getString("messages.typeAction","&7Type the action into the chat please...\n&7Type &ocancel&7 to cancel the action."));
                        storageUtils.addTyping(p,egg,"edit",utils.readAction(egg.getActions().get(id)),id);
                        p.closeInventory();
                    }
                }
                else if (im.getLocalizedName().equals("addCommand")) {

                    storageUtils.addTyping(p,egg,"add", ActionType.CMD,0);
                    p.closeInventory();
                    utils.sendMsg(p,main.getCfg().getString("messages.typeAction","&7Type the action into the chat please...\n&7Type &ocancel&7 to cancel the action."));
                }
                else if (im.getLocalizedName().equals("addMessage")) {

                    storageUtils.addTyping(p,egg,"add", ActionType.MSG,0);
                    p.closeInventory();
                    utils.sendMsg(p,main.getCfg().getString("messages.typeAction","&7Type the action into the chat please...\n&7Type &ocancel&7 to cancel the action."));
                } else if (im.getLocalizedName().equals("back")) {
                    p.openInventory(new EditorGUIHolder(main,p,egg).getInventory());
                }
            }
            e.setCancelled(true);
        }
    }
}
