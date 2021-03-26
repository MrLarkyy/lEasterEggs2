package cz.larkyy.leastereggs.commands;

import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand {

    private Leastereggs main;
    private Utils utils;

    public ReloadCommand(MainCommand mainCommand, CommandSender sender) {
        this.main = mainCommand.getMain();
        this.utils = main.utils;

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (main.getCfg().hasPermission(p, "reload", "easteregss.reload")) {
                main.reloadCfg();

                utils.sendMsg(p, main.getCfg().getString("messages.reload", "&ePlugin has been reloaded!"));
            }
        }
    }
}
