package cz.larkyy.leastereggs.commands;

import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class GiveCommand {

    private final Leastereggs main;
    private final Utils utils;

    public GiveCommand(MainCommand mainCommand, CommandSender sender,String[] args) {
        this.main = mainCommand.getMain();
        this.utils = main.utils;

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (!p.hasPermission(main.getCfg().getString("settings.permissions.giveEgg","eastereggs.give"))) {
                main.utils.sendMsg(p, main.getCfg().getString("messages.noPermission","&cYou have no permission to do that!"));
                return;
            }

            if (args.length > 1) {

                if (args[1].equalsIgnoreCase("random")) {
                    p.getInventory().addItem(utils.mkItem(
                            Material.valueOf(main.getCfg().getString("eggItem.block", "PLAYER_HEAD")),
                            main.getCfg().getString("eggItem.displayName", "&dEaster Egg &fBlock &7(Place)"),
                            "EasterEgg-Block Random",
                            main.getCfg().getStringList("eggItem.lore", Arrays.asList("", "&7Place to create a new Egg!")),
                            main.getCfg().getString("eggItem.texture", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjU2ZjdmM2YzNTM2NTA2NjI2ZDVmMzViNDVkN2ZkZjJkOGFhYjI2MDA4NDU2NjU5ZWZlYjkxZTRjM2E5YzUifX19")
                    ));
                }
                else if (args[1].startsWith("hdb:")) {
                    if (main.getHeadDatabaseHook()==null) {
                        p.sendMessage("HeadDatabase plugin is missing");
                        return;
                    }

                    String id = args[1].split("hdb:")[1];

                    if (!main.getHeadDatabaseHook().getHeadDatabaseAPI().isHead(id)) {
                        p.sendMessage("Invalid HeadDatabase ID "+id);
                        return;
                    }
                    ItemStack is = main.getHeadDatabaseHook().getHeadDatabaseAPI().getItemHead(id);
                    utils.modifyItem(
                            is,
                            main.getCfg().getString("eggItem.displayName", "&dEaster Egg &fBlock &7(Place)"),
                            "EasterEgg-Block",
                            main.getCfg().getStringList("eggItem.lore", Arrays.asList("", "&7Place to create a new Egg!")),
                            null
                    );
                    p.getInventory().addItem(is);


                } else {
                    p.getInventory().addItem(utils.mkItem(
                            Material.valueOf(main.getCfg().getString("eggItem.block", "PLAYER_HEAD")),
                            main.getCfg().getString("eggItem.displayName", "&dEaster Egg &fBlock &7(Place)"),
                            "EasterEgg-Block",
                            main.getCfg().getStringList("eggItem.lore", Arrays.asList("", "&7Place to create a new Egg!")),
                            args[1]
                            ));
                }

            } else {
                p.getInventory().addItem(utils.mkItem(
                        Material.valueOf(main.getCfg().getString("eggItem.block", "PLAYER_HEAD")),
                        main.getCfg().getString("eggItem.displayName", "&dEaster Egg &fBlock &7(Place)"),
                        "EasterEgg-Block",
                        main.getCfg().getStringList("eggItem.lore", Arrays.asList("", "&7Place to create a new Egg!")),
                        main.getCfg().getString("eggItem.texture", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjU2ZjdmM2YzNTM2NTA2NjI2ZDVmMzViNDVkN2ZkZjJkOGFhYjI2MDA4NDU2NjU5ZWZlYjkxZTRjM2E5YzUifX19")
                ));
            }
            sendGiveMsg(p);
        } else
            mainCommand.sendOnlyInGameMsg();
    }

    private void sendGiveMsg(Player p) {
        utils.sendMsg(p, main.getCfg().getString("messages.eggGive", "&eYou have been given the Easter Egg block!"));
    }

}
