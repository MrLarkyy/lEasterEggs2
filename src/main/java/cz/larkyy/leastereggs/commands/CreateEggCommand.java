package cz.larkyy.leastereggs.commands;

import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class CreateEggCommand {

    private Leastereggs main;
    private Utils utils;

    public CreateEggCommand(MainCommand mainCommand, CommandSender sender) {
        this.main = mainCommand.getMain();
        this.utils = main.utils;

        if (mainCommand.isPlayerSender()) {
            Player p = (Player) sender;

            if (!p.getInventory().getItemInMainHand().getType().equals(Material.AIR) && p.getInventory().getItemInMainHand().getType().isBlock()) {
                mkItemFromHand(p);
                main.utils.sendMsg(p,main.getCfg().getString("messages.eggBlockCreated","&eYou have created the Easter Egg Block!"));
            } else {
                main.utils.sendMsg(p,main.getCfg().getString("messages.noBlockInHand","&cYou are not holding any block!"));
            }

        }
    }

    public void mkItemFromHand(Player p) {
        ItemStack is = p.getInventory().getItemInMainHand();

        ItemMeta im = is.getItemMeta();
        im.setDisplayName(utils.format(main.getCfg().getString("eggItem.displayName", "&dEaster Egg &fBlock &7(Place)")));
        im.setLore(utils.formatLore(main.getCfg().getStringList("eggItem.lore", Arrays.asList("", "&7Place to create a new Egg!"))));
        im.setLocalizedName("EasterEgg-Block");
        is.setItemMeta(im);
    }
}
