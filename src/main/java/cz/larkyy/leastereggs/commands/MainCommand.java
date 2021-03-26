package cz.larkyy.leastereggs.commands;

import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.inventory.ListGUIHolder;
import cz.larkyy.leastereggs.utils.DataUtils;
import cz.larkyy.leastereggs.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.Console;
import java.util.Arrays;

public class MainCommand implements CommandExecutor {

    private final Leastereggs main;
    private final Utils utils;
    private final DataUtils data;

    public MainCommand (Leastereggs main) {
        this.main = main;
        this.utils = main.utils;
        this.data = main.getData();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label,  String[] args) {

        if (args.length>0) {
            if (sender instanceof Player && !main.storageUtils.getTyping().containsKey(sender) || sender instanceof ConsoleCommandSender) {

                // HELP COMMAND
                if (args[0].equalsIgnoreCase("help"))
                    sendHelpMsg(sender);

                // GIVE COMMAND
                if (args[0].equalsIgnoreCase("give"))
                    new GiveCommand(this, sender);

                // RELOAD COMMAND
                if (args[0].equalsIgnoreCase("reload"))
                    new ReloadCommand(this, sender);

                // LIST COMMAND
                if (args[0].equalsIgnoreCase("list"))
                    new ListCommand(this, sender);

                // MENU COMMAND
                if (args[0].equalsIgnoreCase("menu"))
                    if (isPlayerSender(sender)) {
                        Player p = (Player) sender;
                        p.openInventory(new ListGUIHolder(main, p, 0).getInventory());
                    } else
                        utils.sendConsoleMsg(main.getCfg().getString("messages.onlyPlayer", "&cThis command can be sent only ingame!"));

            }
        } else {
            sendHelpMsg(sender);
        }

        return false;
    }

    private void sendHelpMsg(CommandSender sender){
        if (isPlayerSender(sender))
            for (String str : main.getCfg().getStringList("messages.help", Arrays.asList("&cMessage is missing in the config!"))){
                utils.sendMsg((Player) sender,str);
            }
        else
            for (String str : main.getCfg().getStringList("messages.help", Arrays.asList("&cMessage is missing in the config!"))) {
                utils.sendConsoleMsg(str);
            }
    }

    public boolean isPlayerSender(CommandSender sender) {
        return (sender instanceof Player);
    }

    public Leastereggs getMain() {
        return main;
    }

}
