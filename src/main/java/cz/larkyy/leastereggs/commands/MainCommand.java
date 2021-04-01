package cz.larkyy.leastereggs.commands;

import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.inventory.ListGUIHolder;
import cz.larkyy.leastereggs.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

public class MainCommand implements CommandExecutor {

    private final Leastereggs main;
    private final Utils utils;
    private CommandSender sender;

    public MainCommand (Leastereggs main) {
        this.main = main;
        this.utils = main.utils;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.sender = sender;

        if (args == null || args.length < 1) {
                sendHelpMsg();
                return false;
        }

        if (sender instanceof Player && main.storageUtils.getTyping().containsKey(sender)) {
            Player p = (Player) sender;
            utils.sendMsg(p,main.getCfg().getString("messages.alreadyEditing","&cYou are editing an Easter Egg right now!\n&cType &4cancel&c to cancel the action..."));
            return false;
        }

        if (sender instanceof Player || sender instanceof ConsoleCommandSender) {

            switch (args[0].toLowerCase()) {
                case "give":
                    new GiveCommand(this, sender,args);
                    break;
                case "reload":
                    new ReloadCommand(this, sender);
                    break;
                case "list":
                    new ListCommand(this, sender, args);
                    break;
                case "menu":
                    if (isPlayerSender()) {
                        Player p = (Player) sender;

                        if (!p.hasPermission(main.getCfg().getString("settings.permissions.menu","eastereggs.menu"))) {
                            utils.sendMsg(p, main.getCfg().getString("messages.noPermission","&cYou have no permission to do that!"));
                            return false;
                        }

                        p.openInventory(new ListGUIHolder(main, p, 0).getInventory());
                    } else
                        sendOnlyInGameMsg();
                    break;
                case "create":
                    new CreateEggCommand(this, sender);
                    break;
                case "set":
                    new SetEggCommand(this, sender);
                    break;
                case "tp":
                    new TeleportCommand(this, sender, args);
                    break;
                case "edit":
                    new EditCommand(this,sender,args);
                    break;
                case "found":
                    new FoundCommand(this, sender);
                    break;
                default:
                    sendHelpMsg();
            }
        }
        return false;
    }

    private void sendHelpMsg(){
        if (isPlayerSender())
            for (String str : main.getCfg().getStringList("messages.help", Collections.singletonList("&cMessage is missing in the config!"))){
                utils.sendMsg((Player) sender,str);
            }
        else
            for (String str : main.getCfg().getStringList("messages.help", Collections.singletonList("&cMessage is missing in the config!"))) {
                utils.sendConsoleMsg(str);
            }
    }

    public boolean isPlayerSender() {
        return (sender instanceof Player);
    }

    public Leastereggs getMain() {
        return main;
    }

    public void sendOnlyInGameMsg() {
        utils.sendConsoleMsg(main.getCfg().getString("messages.onlyPlayer", "&cThis command can be sent only ingame!"));
    }

}
