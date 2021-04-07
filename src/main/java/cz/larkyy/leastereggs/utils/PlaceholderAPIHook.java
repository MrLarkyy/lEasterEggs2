package cz.larkyy.leastereggs.utils;

import cz.larkyy.leastereggs.Leastereggs;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPIHook extends PlaceholderExpansion {

    private final Leastereggs main;

    public PlaceholderAPIHook(Leastereggs main) {
        this.main = main;
    }

    public String replaceMsg(Player p, String msg) {
        return PlaceholderAPI.setPlaceholders(p, msg);
    }

    @Override
    public @NotNull String getIdentifier() {
        return "eastereggs";
    }

    @Override
    public @NotNull String getAuthor() {
        return "MrLarkyy_";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {

        if (params.equalsIgnoreCase("found"))
            return main.storageUtils.getPlayer(player.getUniqueId()).getEggs().size() + "";

        if (params.equalsIgnoreCase("total"))
            return main.storageUtils.getEggs().size() + "";

        return null;
    }

    @Override
    public boolean register() {
        return super.register();
    }
}
