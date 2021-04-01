package cz.larkyy.leastereggs.commands;

import cz.larkyy.leastereggs.Leastereggs;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FoundCommand {

    private final MainCommand mainCommand;
    private final CommandSender sender;
    private final Leastereggs main;

    public FoundCommand(MainCommand mainCommand, CommandSender sender) {
        this.mainCommand = mainCommand;
        this.sender = sender;
        this.main = mainCommand.getMain();

        if (mainCommand.isPlayerSender()) {
            Player p = (Player) sender;

            if (!p.hasPermission(main.getCfg().getString("settings.permissions.eggFound","eastereggs.found"))) {
                main.utils.sendMsg(p, main.getCfg().getString("messages.noPermission","&cYou have no permission to do that!"));
                return;
            }

            main.utils.sendMsg(p,main.getCfg().getString("messages.foundAmount","&eYou have found &6%found%/%total%&e Easter Eggs!")
                    .replace("%found%",main.storageUtils.getPlayer(p.getUniqueId()).getEggs().size()+"")
                    .replace("%total%",main.storageUtils.getEggs().size()+""));
        } else
            mainCommand.sendOnlyInGameMsg();
    }
}
