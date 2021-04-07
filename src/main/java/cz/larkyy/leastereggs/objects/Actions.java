package cz.larkyy.leastereggs.objects;

import cz.larkyy.leastereggs.ActionType;

import java.util.regex.Pattern;

public class Actions {

    private Pattern pattern;
    private ActionType actionType;

    public Actions(Pattern pattern, ActionType actionType) {
        this.pattern = pattern;
        this.actionType = actionType;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public ActionType getActionType() {
        return actionType;
    }
}
