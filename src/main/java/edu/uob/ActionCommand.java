package edu.uob;

import java.util.ArrayList;

public class ActionCommand extends GameCommand{
    private final ArrayList<String> triggerList;
    private ArrayList<String> subjectList;
    private String trigger;
    private GameAction gameAction;
    private Location storeroom;
    private String producedEntity;
    private String consumedEntity;
    private String narration;

    public ActionCommand(ArrayList<String> commandWords, Map map){
        super(commandWords, map);
        this.triggerList = map.getTriggersList();
        this.storeroom = map.getLocation("storeroom");
    }

    public String handleActionCommand(){
        String response = "";
        if(!checkActionCommand()) {
            response = "Error: Command must contain one trigger phrase and at least one subject";
        }else if(!subjectsInScope()){
            response = "Error: Subjects must be either in your inventory or your current location";
        }else if(!findGameAction()){
            response = "Error: Ambiguous command not recognised";
        }
        return consumeEntity();
    }

    public boolean findGameAction(){
        this.gameAction = map.getGameAction(trigger, subjectList);
        if(gameAction==null) return false;
        else {
            this.consumedEntity = gameAction.getConsumed();
            this.producedEntity = gameAction.getProduced();
            this.narration = gameAction.getNarration();
            return true;
        }
    }

    public String consumeEntity(){
        String entityType = entityInLocation(consumedEntity);
        if(entityType!=null){
            GameEntity entity = currentLocation.consumeEntity(entityType, consumedEntity);
            switch(entityType){
                case "artefacts" ->{
                    storeroom.addArtefact(consumedEntity, (Artefact)entity);
                }
                case "furniture" ->{
                    storeroom.addFurniture(consumedEntity, (Furniture)entity);
                }
                case "characters" ->
                    storeroom.addNPC(consumedEntity, (NonPlayerCharacter)entity);
            }
        }else if(entityInInventory(consumedEntity)){
            Artefact item = player.dropItem(consumedEntity);
            storeroom.addArtefact(consumedEntity, item);
        }else{
            return "Error: entity not found in current scope";
        }
        return produceEntity();
    }

    public String produceEntity(){
        if(map.isLocation(producedEntity)){
            currentLocation.addPath(producedEntity);
        }else{
            String entityType = storeroom.containsEntity(producedEntity);
            GameEntity entity = storeroom.consumeEntity(entityType, producedEntity);
            switch(entityType){
                case "artefacts" ->{
                    currentLocation.addArtefact(producedEntity, (Artefact)entity);
                }
                case "furniture" ->{
                    currentLocation.addFurniture(producedEntity, (Furniture)entity);
                }
                case "characters" ->
                        currentLocation.addNPC(producedEntity, (NonPlayerCharacter)entity);
            }
        }
        return narration;
    }

    //checks that command contains one trigger and at least one subject
    public boolean checkActionCommand(){
        return(checkTriggers() && !isSubjectListEmpty());
    }

    public boolean checkTriggers(){
        int count=0;
        StringBuilder sb = new StringBuilder();
        for(String trigger: triggerList){
            sb.append(trigger).append("|");
        }
        sb.deleteCharAt(sb.length()-1);
        String regex = "\\b("+sb+")\\b";

        for(String word: commandWords){
            if(word.matches(regex)){
                count++;
                this.trigger = word;
            }
        }
        return count==1;
    }

    public boolean isSubjectListEmpty(){
        return subjectList.isEmpty();
    }

    //Checks that each subject is in either the inventory or location
    public boolean subjectsInScope(){
        for (String subject: subjectList) {
            if(entityInLocation(subject)==null){
                if(!entityInInventory(subject)){
                    return false;
                }
            }
        }
        return true;
    }

    public String entityInLocation(String entityKey){
        return currentLocation.containsEntity(entityKey);
    }

    public boolean entityInInventory(String entityKey){
        return player.isItemInventory(entityKey);
    }

    public void setSubjectList(ArrayList<String> subjectList){
        this.subjectList = subjectList;
    }

    //for testing
    public String getTrigger(){
        return trigger;
    }

    public ArrayList<String> getSubjectList(){
        return subjectList;
    }

    public String getConsumedEntity() {
        return consumedEntity;
    }

    public String getProducedEntity() {
        return producedEntity;
    }

    public String getNarration() {
        return narration;
    }
}
