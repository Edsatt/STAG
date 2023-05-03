package edu.uob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ActionsList {

    private final HashMap<String, HashSet<GameAction>> actions;

    public ActionsList(){
        actions = new HashMap<>();
    }

    public void addAction(String keyphrase, GameAction action){
        if(actions.containsKey(keyphrase)){
            actions.get(keyphrase).add(action);
        }else{
            HashSet<GameAction> newAction = new HashSet<>();
            newAction.add(action);
            actions.put(keyphrase, newAction);
        }
    }

    public HashMap<String, HashSet<GameAction>> getActions() {
        return actions;
    }

    public GameAction getGameAction(String trigger, ArrayList<String> subjects){
        HashSet<GameAction> gameActions = actions.get(trigger);
        if(gameActions!=null){
            for(GameAction gameAction: gameActions){
                if(gameAction.checkSubjects(subjects)){
                    return gameAction;
                }
            }
        }
        return null;
    }

    //for testing
    public HashSet<GameAction> getAllActionsForTrigger(String trigger){
        return actions.get(trigger);
    }
}
