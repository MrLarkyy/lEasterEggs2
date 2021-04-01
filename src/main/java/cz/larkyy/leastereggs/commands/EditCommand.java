package cz.larkyy.leastereggs.commands;

import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.inventory.EditorGUIHolder;
import cz.larkyy.leastereggs.objects.Egg;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EditCommand {

    private final Leastereggs main;

    public EditCommand(MainCommand mainCommand, CommandSender sender, String[] args) {
        this.main = mainCommand.getMain();

        if (mainCommand.isPlayerSender()){
            Player p = (Player) sender;

            if (!p.hasPermission(main.getCfg().getString("settings.permissions.editEgg","eastereggs.edit"))) {
                main.utils.sendMsg(p, main.getCfg().getString("messages.noPermission","&cYou have no permission to do that!"));
                return;
            }

            if (args.length>1) {
                try {
                    int id = Integer.parseInt(args[1]);

                    if (main.storageUtils.getEggs().get(id)!=null) {
                        Egg egg = main.storageUtils.getEggs().get(id);

                        p.openInventory(new EditorGUIHolder(main,p,egg).getInventory());
                    } else {
                        main.utils.sendMsg(p,main.getCfg().getString("messages.invalidID","&cThere is no Easter Egg with this ID!"));
                    }
                } catch (NumberFormatException ex) {
                    main.utils.sendMsg(p,main.getCfg().getString("messages.mustBeNumber","&cYou must type a number!"));
                }

            } else {
                main.utils.sendMsg(p, main.getCfg().getString("messages.usage.syntaxe", "&cInvalid Command Usage! &7Usage: &f/ee %arguments%")
                        .replace("%arguments%", "edit "
                                + main.getCfg().getString("messages.usage.argument", "<%arg%>").replace("%arg%", "ID")));
            }
        } else
            mainCommand.sendOnlyInGameMsg();
    }
}
