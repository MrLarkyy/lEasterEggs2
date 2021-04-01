package cz.larkyy.leastereggs.commands;

import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.objects.Egg;
import cz.larkyy.leastereggs.utils.StorageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TabComplete implements TabCompleter {

    private StorageUtils storageUtils;
    private Leastereggs main;

    public TabComplete(Leastereggs main) {
        this.main = main;
        this.storageUtils = main.storageUtils;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (strings!=null) {
            switch (strings.length) {
                case 1:
                    return Arrays.asList("help","give","reload","list","menu","create","set","tp","edit");
                case 2:
                    switch (strings[0]) {
                        case "give":
                            return Arrays.asList("random","hdb:<id>","texture");
                        case "tp":
                        case "edit":
                            return getEggIDs();
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        }

        return null;
    }

    private List<String> getEggIDs(){
        List<String> eggs = new ArrayList<>();
        for (Map.Entry<Integer, Egg> pair : storageUtils.getEggs().entrySet()) {
            eggs.add(pair.getKey()+"");
        }
        return eggs;
    }
}
