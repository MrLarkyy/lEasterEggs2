package cz.larkyy.leastereggs.listeners;

import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.inventory.ActionListGUIHolder;
import cz.larkyy.leastereggs.objects.Egg;
import cz.larkyy.leastereggs.objects.TypingPlayer;
import cz.larkyy.leastereggs.utils.StorageUtils;
import cz.larkyy.leastereggs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private Leastereggs main;
    private StorageUtils storageUtils;
    private Utils utils;

    public ChatListener(Leastereggs main) {
        this.main = main;
        utils = main.utils;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (main.storageUtils.getTyping().containsKey(p)) {
            storageUtils = main.storageUtils;

            TypingPlayer typingPlayer = storageUtils.getTyping().get(p);
            Egg egg = typingPlayer.getEgg();

            storageUtils.delTyping(p);
            if (!isCancelWord(e.getMessage())) {
                if (typingPlayer.getEditType().equals("edit")) {
                    utils.sendMsg(p, main.getCfg().getString("messages.actionAdded", "&eYou have edited the action!"));
                    egg.setAction(typingPlayer.getId(), typingPlayer.getActionTypeString()+e.getMessage());
                } else {
                    utils.sendMsg(p, main.getCfg().getString("messages.actionAdded", "&eYou have added a new action!"));
                    egg.addAction(typingPlayer.getActionTypeString()+e.getMessage());
                }
            } else
                utils.sendMsg(p, main.getCfg().getString("messages.actionCancelled", "&cYou have &4Cancelled&c adding the action!"));
                openBackInv(p,egg);
            e.setCancelled(true);

        }
    }
    private void openBackInv(Player p, Egg egg) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> p.openInventory(new ActionListGUIHolder(main,p,egg,0).getInventory()));
    }

    private boolean isCancelWord(String msg) {
        for (String str : main.getCfg().getConfiguration().getStringList("settings.cancellationWords")) {
            if (msg.equalsIgnoreCase(str))
                return true;
        }
        return false;
    }

}
