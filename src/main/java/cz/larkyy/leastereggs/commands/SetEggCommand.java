package cz.larkyy.leastereggs.commands;

import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.objects.Egg;
import cz.larkyy.leastereggs.utils.StorageUtils;
import cz.larkyy.leastereggs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SetEggCommand {

    public SetEggCommand(MainCommand mainCommand, CommandSender sender) {
        Leastereggs main = mainCommand.getMain();
        Utils utils = main.utils;
        StorageUtils storageUtils = main.storageUtils;

        if (mainCommand.isPlayerSender()) {
            Player p = (Player) sender;

            if (!p.hasPermission(main.getCfg().getString("settings.permissions.setEgg", "eastereggs.set"))) {
                utils.sendMsg(p, main.getCfg().getString("messages.noPermission", "&cYou have no permission to do that!"));
                return;
            }

            Block b = p.getTargetBlock(null, 20);
            if (b != null && b.getType().isBlock() && !b.getType().equals(Material.AIR)) {
                storageUtils.addEgg(new Egg(
                        b.getLocation(),
                        main.getCfg().getStringList("settings.defaultActions", Arrays.asList("msg: &eYou have found an Easter Egg!", "cmd: give %player% minecraft:diamond 1"))));
                utils.sendMsg(p, main.getCfg().getString("messages.eggBlockSet", "&eYou have set an Easter Egg to the looked block!"));
            } else
                utils.sendMsg(p, main.getCfg().getString("messages.mustLookAtBlock", "&cYou are not looking at block!"));


        } else
            mainCommand.sendOnlyInGameMsg();
    }
}
