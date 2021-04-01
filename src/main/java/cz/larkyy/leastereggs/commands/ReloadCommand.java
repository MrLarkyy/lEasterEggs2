package cz.larkyy.leastereggs.commands;

import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand {

    public ReloadCommand(MainCommand mainCommand, CommandSender sender) {
        Leastereggs main = mainCommand.getMain();
        Utils utils = main.utils;

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (!p.hasPermission(main.getCfg().getString("settings.permissions.reload","eastereggs.reload"))) {
                utils.sendMsg(p, main.getCfg().getString("messages.noPermission","&cYou have no permission to do that!"));
                return;
            }

            main.reloadCfg();
            utils.sendMsg(p, main.getCfg().getString("messages.reload", "&ePlugin has been reloaded!"));

        } else {
            main.reloadCfg();
            utils.sendConsoleMsg(main.getCfg().getString("messages.reload", "&ePlugin has been reloaded!"));
        }
    }
}
