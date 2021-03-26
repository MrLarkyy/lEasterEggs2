package cz.larkyy.leastereggs.commands;

import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.utils.DataUtils;
import cz.larkyy.leastereggs.utils.Utils;
import org.bukkit.entity.Player;

public class ReloadCommand {

    private Leastereggs main;
    private Utils utils;
    private DataUtils cfg;

    public ReloadCommand(Leastereggs main, Player p) {
        this.main = main;
        this.utils = main.utils;
        this.cfg = main.getCfg();

        if (cfg.hasPermission(p,"reload","easteregss.reload")) {
            main.reloadCfg();

            utils.sendMsg(p, cfg.getString("messages.reload","&ePlugin has been reloaded!"));
        }
    }
}
