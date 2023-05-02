package edu.uob;

import java.util.HashMap;
import java.util.HashSet;

public class ActionsList {

    HashMap<String, HashSet<GameAction>> actions;

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
}
