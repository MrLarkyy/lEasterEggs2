package cz.larkyy.leastereggs.commands;

import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.objects.Egg;
import cz.larkyy.leastereggs.utils.StorageUtils;
import cz.larkyy.leastereggs.utils.Utils;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SetEggCommand {

    private Leastereggs main;
    private Utils utils;
    private StorageUtils storageUtils;

    public SetEggCommand(MainCommand mainCommand, CommandSender sender) {
        this.main = mainCommand.getMain();
        this.utils = main.utils;
        this.storageUtils = main.storageUtils;

        if (mainCommand.isPlayerSender()) {
            Player p = (Player) sender;

            Block b = p.getTargetBlock(20);
            if (b!=null && b.getType().isBlock()) {
                storageUtils.addEgg(new Egg(
                        b.getLocation(),
                        main.getCfg().getStringList("settings.defaultActions", Arrays.asList("msg: &eYou have found an Easter Egg!","cmd: give %player% minecraft:diamond 1"))));
                utils.sendMsg(p,main.getCfg().getString("messages.eggBlockSet","&eYou have set an Easter Egg to the looked block!"));
            } else
                utils.sendMsg(p,main.getCfg().getString("messages.mustLookAtBlock","&cYou are not looking at block!"));


        }
    }
}
