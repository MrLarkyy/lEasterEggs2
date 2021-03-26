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

public class MainCommand implements CommandExecutor {

    private final Leastereggs main;
    private final Utils utils;
    private final DataUtils cfg;
    private final DataUtils data;

    public MainCommand (Leastereggs main) {
        this.main = main;
        this.utils = main.utils;
        this.cfg = main.getCfg();
        this.data = main.getData();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label,  String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (args!=null && args.length>0 && !main.storageUtils.getTyping().containsKey(p)) {

                // HELP COMMAND
                if (args[0].equalsIgnoreCase("help"))
                    sendHelpMsg(p);

                // GIVE COMMAND
                if (args[0].equalsIgnoreCase("give"))
                    new GiveCommand(p,main);

                // RELOAD COMMAND
                if (args[0].equalsIgnoreCase("reload"))
                    new ReloadCommand(main,p);

                // LIST COMMAND
                if (args[0].equalsIgnoreCase("list"))
                    new ListCommand(main,p);

                // MENU COMMAND
                if (args[0].equalsIgnoreCase("menu"))
                    p.openInventory(new ListGUIHolder(main,p,0).getInventory());


            } else {
                sendHelpMsg(p);
            }
        } else if (sender instanceof ConsoleCommandSender) {
            if (args!=null && args.length>0) {
                // RELOAD COMMAND
                if (args[0].equalsIgnoreCase("reload"))
                    new ReloadCommand(main,null);
            }
        }

        return false;
    }

    private void sendHelpMsg(Player p){
        utils.sendMsg(p,cfg.getString("messages.help","&eDefault message"));
    }

}
