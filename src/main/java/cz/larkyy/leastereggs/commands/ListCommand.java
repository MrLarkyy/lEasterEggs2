package cz.larkyy.leastereggs.commands;

import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.objects.Egg;
import cz.larkyy.leastereggs.utils.StorageUtils;
import cz.larkyy.leastereggs.utils.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListCommand {

    private Utils utils;
    private StorageUtils storageUtils;
    private Leastereggs main;

    public ListCommand(MainCommand mainCommand, CommandSender sender, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            this.main = mainCommand.getMain();
            this.utils = main.utils;
            this.storageUtils = main.storageUtils;

            if (!p.hasPermission(main.getCfg().getString("settings.permissions.eggList","eastereggs.list"))) {
                utils.sendMsg(p, main.getCfg().getString("messages.noPermission","&cYou have no permission to do that!"));
                return;
            }

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

                if (storageUtils.getEggs().size()-first<1) {
                    main.utils.sendMsg(p,main.getCfg().getString("messages.noEggs","&cNo eggs found..."));
                    return;
                }
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

            } catch (NumberFormatException ex) {
                main.utils.sendMsg(p, main.getCfg().getString("messages.mustBeNumber", "&cYou must type a number!"));
            }


        } else
            mainCommand.sendOnlyInGameMsg();
    }

    private TextComponent mkComponents(int id) {
        TextComponent eggmsg = mkComponent(
                main.getCfg().getString("messages.list.syntaxe","&d#%id% &7- ")
                        .replace("%id%",id+""),
                null,
                null);

        TextComponent teleport = mkComponent(
                main.getCfg().getString("messages.list.teleport","&f[Teleport]"),
                main.getCfg().getString("messages.list.teleportClick","&eClick to teleport"),
                "ee tp "+id);

        TextComponent edit = mkComponent(
                main.getCfg().getString("messages.list.edit","&e[Edit]"),
                main.getCfg().getString("messages.list.editClick","&eClick to edit"),
                "ee edit "+id);

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
            backArrow = mkComponent(
                    main.getCfg().getString("messages.list.footer.noPrevArrow","&8<<< "),
                    main.getCfg().getString("messages.list.footer.noPrevPageHover","&7No previous page"),
                    null);
        } else {
            backArrow = mkComponent(
                    main.getCfg().getString("messages.list.footer.prevArrow","&7<<< "),
                    main.getCfg().getString("messages.list.footer.prevPageHover","&eClick to open"),
                    "ee list "+(page-1));
        }

        if (!hasNextPage(page)) {
            nextArrow = mkComponent(
                    main.getCfg().getString("messages.list.footer.noNextArrow","&8 >>>"),
                    main.getCfg().getString("messages.list.footer.noNextPageHover","&7No next page"),
                    null);
        } else {
            nextArrow = mkComponent(
                    main.getCfg().getString("messages.list.footer.nextArrow","&7 >>>"),
                    main.getCfg().getString("messages.list.footer.nextPageHover","&eClick to open"),
                    "ee list "+(page+1));
        }

        info = mkComponent(
                main.getCfg().getString("messages.list.footer.pageInfo","&7(%current%/%max%)")
                    .replace("%current%",page+"")
                    .replace("%max%",maxPage()+""),
                main.getCfg().getString("messages.list.footer.pageInfoHover","&fTotal eggs amount: &e%total%")
                        .replace("%total%",storageUtils.getEggs().size()+""),
                null);

        backArrow.addExtra(info);
        backArrow.addExtra(nextArrow);

        return backArrow;
    }

    private TextComponent mkComponent(String msg,String hover,String cmd) {
        TextComponent component = new TextComponent(utils.format(msg));
        if (hover!=null)
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(utils.format(hover)).create()));
        if (cmd!=null)
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/"+cmd));

        return component;
    }
}
