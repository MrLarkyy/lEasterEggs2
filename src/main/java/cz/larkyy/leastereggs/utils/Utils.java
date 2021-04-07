package cz.larkyy.leastereggs.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import cz.larkyy.leastereggs.ActionType;
import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.objects.Actions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private final Leastereggs main;
    private final Pattern pattern = Pattern.compile("#[a-fA-f0-9]{6}");

    private static final List<Actions> actions = Arrays.asList(
            new Actions(Pattern.compile("(^(command|cmd):\\s?)(.*$)"), ActionType.CMD),
            new Actions(Pattern.compile("(^(msg|message|tell):\\s?)(.*$)"), ActionType.MSG),
            new Actions(Pattern.compile("(^(sound|playsound):\\s?)(.*$)"), ActionType.SOUND)
    );

    public Utils(Leastereggs main) {
        this.main = main;
    }

    // CHAT UTILS

    public void sendMsg(Player p, String msg) {
        p.sendMessage(format(msg));
    }

    public String format(String msg) {
        if (msg != null) {
            if (Bukkit.getVersion().contains("1.16")) {
                Matcher match = pattern.matcher(msg);
                while (match.find()) {
                    String color = msg.substring(match.start(), match.end());
                    msg = msg.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
                    match = pattern.matcher(msg);
                }
            }
            return ChatColor.translateAlternateColorCodes('&', msg);
        }
        return msg;
    }

    public void sendConsoleMsg(String msg) {
        main.getServer().getConsoleSender().sendMessage(format(msg));
    }

    // ITEM UTILS

    public ItemStack mkItem(Material material, String displayName, String localizedName, List<String> lore, String texture) {
        ItemStack is = new ItemStack(material);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(format(displayName));
        im.setLocalizedName(localizedName);
        if (lore != null)
            im.setLore(formatLore(lore));
        is.setItemMeta(im);

        if (texture != null && material.equals(Material.PLAYER_HEAD))
            setSkullItemSkin(is, texture);

        return is;
    }

    public ItemStack modifyItem(ItemStack is, String displayName, String localizedName, List<String> lore, String texture) {
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(format(displayName));
        im.setLocalizedName(localizedName);
        if (lore != null)
            im.setLore(formatLore(lore));
        is.setItemMeta(im);

        if (texture != null && is.getType().equals(Material.PLAYER_HEAD))
            setSkullItemSkin(is, texture);
        return is;
    }

    public List<String> formatLore(List<String> lore) {
        List<String> result = new ArrayList<>();
        for (String str : lore) {
            result.add(format(str));
        }
        return result;
    }

    public void setSkullItemSkin(ItemStack is, String texture) {

        ItemMeta meta = is.getItemMeta();

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", texture));
        Field field;
        try {
            field = meta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(meta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException x) {
            x.printStackTrace();
        }
        is.setItemMeta(meta);

    }

    // ACTIONS UTILS

    public void sendActions(List<String> actionsList, Player p) {
        for (String input : actionsList) {
            for (Actions action : actions) {
                final Matcher matcher = action.getPattern().matcher(input);
                if (matcher.matches()) {
                    ActionType actionType = action.getActionType();
                    if (actionType.equals(ActionType.MSG)) {
                        sendMsg(p, matcher.group(3).replace("%player%", p.getName()));
                    }
                    if (actionType.equals(ActionType.CMD)) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), matcher.group(3).replace("%player%", p.getName()));
                    }
                }
            }
        }
    }

    public ActionType readAction(String input) {
        for (Actions action : actions) {
            final Matcher matcher = action.getPattern().matcher(input);
            if (matcher.matches()) {
                return action.getActionType();
            }
        }
        return null;
    }

    public String stringOfAction(String input) {
        for (Actions action : actions) {
            final Matcher matcher = action.getPattern().matcher(input);
            if (matcher.matches()) {
                return matcher.group(3);
            }
        }
        return null;
    }


    public List<String> replaceInLore(List<String> lore, List<String> operator, List<String> replacement) {
        List<String> newLore = new ArrayList<>();

        if (lore != null) {
            int count;
            for (String lore1 : lore) {
                count = 0;

                for (String operator1 : operator) {

                    newLore.add(lore1.replace(operator1, replacement.get(count)));
                    count++;

                }
            }
        }
        return newLore;
    }

    public void sendTitleMsg(Player p, String title, String subTitle, int fadein, int stay, int fadeout) {
        p.sendTitle(format(title), format(subTitle), fadein, stay, fadeout);
    }


}
