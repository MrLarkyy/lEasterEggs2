package cz.larkyy.leastereggs.commands;

import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class GiveCommand {

    private Leastereggs main;
    private Utils utils;

    public GiveCommand(MainCommand mainCommand, CommandSender sender) {
        this.main = mainCommand.getMain();
        this.utils = main.utils;

        if (sender instanceof Player) {
            Player p = (Player) sender;

            p.getInventory().addItem(utils.mkItem(
                    Material.valueOf(main.getCfg().getString("eggItem.block", "PLAYER_HEAD")),
                    main.getCfg().getString("eggItem.displayName", "&dEaster Egg &fBlock &7(Place)"),
                    "EasterEgg-Block",
                    main.getCfg().getStringList("eggItem.lore", Arrays.asList("", "&7Place to create a new Egg!")),
                    main.getCfg().getString("eggItem.texture", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjU2ZjdmM2YzNTM2NTA2NjI2ZDVmMzViNDVkN2ZkZjJkOGFhYjI2MDA4NDU2NjU5ZWZlYjkxZTRjM2E5YzUifX19")
            ));

        }
    }

}
