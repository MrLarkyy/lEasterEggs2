package cz.larkyy.leastereggs.commands;

import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.inventory.EditorGUIHolder;
import cz.larkyy.leastereggs.objects.Egg;
import cz.larkyy.leastereggs.utils.DataUtils;
import cz.larkyy.leastereggs.utils.StorageUtils;
import cz.larkyy.leastereggs.utils.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListCommand {

    private Leastereggs main;
    private Utils utils;
    private StorageUtils storageUtils;

    public ListCommand(MainCommand mainCommand, CommandSender sender, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            this.main = mainCommand.getMain();
            this.utils = main.utils;
            this.storageUtils = main.storageUtils;

            try {

                int page;
                if (args.length > 1) {
                    page = Integer.parseInt(args[1]);

                    if (page < 1)
                        page = 1;
                }
                else
                    page = 1;

                int maxcfg = 5;

                int first = maxcfg*(page-1);
                int loaded = 0;
                int max = maxcfg*page;

                if (storageUtils.getEggs().size()-first>0) {
                    for (int i = 0; i < Integer.MAX_VALUE; i++) {
                        Egg egg = storageUtils.getEggs().get(i);
                        if (egg != null) {
                            loaded++;
                            if (loaded > first)
                                p.spigot().sendMessage(mkComponents(i));
                        }

                        if (loaded >= max || loaded == storageUtils.getEggs().size()) {
                            p.spigot().sendMessage(mkArrowComponents(page));
                            break;
                        }
                    }
                }
                else p.sendMessage("No eggs found");



            } catch (NumberFormatException ex) {
                main.utils.sendMsg(p, main.getCfg().getString("messages.mustBeNumber", "&cYou must type a number!"));
            }


        }
    }

    private TextComponent mkComponents(int id) {
        TextComponent eggmsg = new TextComponent(utils.format("&dEgg #%id% &7- ").replace("%id%",id+""));

        TextComponent teleport = new TextComponent(utils.format("&f[Teleport]"));
        teleport.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ee tp "+id));
        teleport.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(utils.format("&eClick to teleport")).create()));

        TextComponent edit = new TextComponent(utils.format("&e[Edit]"));
        edit.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ee edit "+id));
        edit.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(utils.format("&eClick to edit")).create()));

        eggmsg.addExtra(teleport);
        eggmsg.addExtra(" ");
        eggmsg.addExtra(edit);

        return eggmsg;
    }

    private boolean hasNextPage(int page) {
        return (page < maxPage());
    }

    private int maxPage() {
        float amount = storageUtils.getEggs().size();
        int max = 5;

        if (amount>0) {
            amount += max;
            double pages = Math.floor(amount / max);
            return (int) pages;

        } else
            return 0;
    }

    private TextComponent mkArrowComponents(int page) {

        TextComponent backArrow;
        TextComponent info;
        TextComponent nextArrow;

        if (page==1) {
            backArrow = new TextComponent(utils.format("&8<<< "));
            backArrow.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(utils.format("&7No previous page")).create()));
        } else {
            backArrow = new TextComponent(utils.format("&7<<< "));
            backArrow.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(utils.format("&eClick to open")).create()));
            backArrow.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/ee list "+(page-1)));
        }

        if (!hasNextPage(page)) {
            nextArrow = new TextComponent(utils.format("&8 >>>"));
            nextArrow.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(utils.format("&7No next page")).create()));
        } else {
            nextArrow = new TextComponent(utils.format("&7 >>>"));
            nextArrow.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(utils.format("&eClick to open")).create()));
            nextArrow.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/ee list "+(page+1)));
        }

        info = new TextComponent(utils.format("&7(%current%/%max%)")
                .replace("%current%",page+"")
                .replace("%max%",maxPage()+""));
        info.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(utils.format("&fTotal eggs amount: &e%total%")
                .replace("%total%",storageUtils.getEggs().size()+"")).create()));

        backArrow.addExtra(info);
        backArrow.addExtra(nextArrow);

        return backArrow;
    }
}
