package cz.larkyy.leastereggs.objects;

import cz.larkyy.leastereggs.ActionType;
import org.bukkit.entity.Player;

public class TypingPlayer {

    private ActionType actionType;
    private String editType;
    private int id;
    private Player p;
    private Egg egg;

    public TypingPlayer(String editType, ActionType actionType, Player p, Egg egg, int id) {
        this.editType = editType;
        this.actionType = actionType;
        this.p = p;
        this.egg = egg;
        this.id = id;
    }

    public Egg getEgg() {
        return egg;
    }

    public String getEditType() {
        return editType;
    }

    public int getId() {
        return id;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public String getActionTypeString() {
        if (actionType.equals(ActionType.MSG)) {
            return "msg: ";
        } else if (actionType.equals(ActionType.CMD)) {
            return "cmd: ";
        }
        return null;
    }
}
