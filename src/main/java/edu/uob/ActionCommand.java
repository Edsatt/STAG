package edu.uob;

import java.util.ArrayList;
import java.util.List;

public class ActionCommand extends GameCommand{
    private final List<String> triggerList;
    private List<String> subjectList;
    private String trigger;
    private final Location storeroom;
    private String producedEntity;
    private String consumedEntity;
    private String narration;

    public ActionCommand(List<String> commandWords, Map map){
        super(commandWords, map);
        this.triggerList = map.getTriggersList();
        this.storeroom = map.getLocation("storeroom");
    }

    public void setSubjectList(List<String> subjectList){
        this.subjectList = subjectList;
    }

    public String handleActionCommand(){
        String response;
        if(actionCommandInvalid()) {
            response = "Error: Command must contain one trigger phrase and at least one subject";
        }else if(subjectsNotInScope()){
            response = "Error: Subjects must be either in your inventory or your current location";
        }else if(gameActionNotFound()){
            response = "Error: Ambiguous command not recognised";
        }else response = consumeEntity();
        return response;
    }

    //checks that command contains one trigger and at least one subject
    public boolean actionCommandInvalid(){
        return (!checkTriggers() || subjectList.isEmpty());
    }

    public boolean checkTriggers(){
        List<String> triggers = new ArrayList<>();
        for(String word: commandWords){
            if(triggerList.contains(word)){
                triggers.add(word);
            }
        }
        if(triggers.isEmpty()){
            return false;
        }
        return checkTriggersMatchAction(triggers);
    }

    public boolean checkTriggersMatchAction(List<String> triggers){
        List<String> triggerGroup = map.getTriggerGroup(triggers.get(0));
        for(String trigger: triggers){
            if(!triggerGroup.contains(trigger)){
                return false;
            }
        }
        this.trigger = triggers.get(0);
        return true;
    }

    //returns false if no action is found matching the trigger and subjects
    public boolean gameActionNotFound(){
        GameAction gameAction = map.getGameAction(trigger, subjectList);
        if(gameAction ==null) return true;
        else {
            this.consumedEntity = gameAction.getConsumed();
            this.producedEntity = gameAction.getProduced();
            this.narration = gameAction.getNarration();
            return false;
        }
    }

    public String consumeEntity(){
        if(notHealthOrNull("consume")){
            String entityType = currentLocation.containsEntity(consumedEntity);
            if(entityType!=null){
                GameEntity entity = currentLocation.takeEntity(entityType, consumedEntity);
                storeroom.addEntity(entityType,consumedEntity,entity);

            }else if(player.isItemInInventory(consumedEntity)){
                Artefact item = player.takeItemFromInventory(consumedEntity);
                storeroom.addEntity("artefacts", consumedEntity, item);

            }else{
                return "Error: entity not found in current scope";
            }
        }
        return produceEntity();
    }

    public String produceEntity(){
        if(notHealthOrNull("produce")){
            if(map.isLocation(producedEntity)){
                currentLocation.addPath(producedEntity);
            }else{
                Location entityLocation = map.getEntityLocation(producedEntity);
                String entityType = entityLocation.containsEntity(producedEntity);
                GameEntity entity = entityLocation.takeEntity(entityType, producedEntity);
                currentLocation.addEntity(entityType, producedEntity, entity);
            }
        }
        return narration;
    }

    //used to check if either the produced or consumed entity are null or health
    public boolean notHealthOrNull(String entityFate){
        String entity = switch (entityFate){
            case "consume" -> consumedEntity;
            case "produce" -> producedEntity;
            default -> null;
        };
        if(entity==null) return false;
        if(entity.equals("health")){
            handleHealth(entityFate);
            return false;
        }
        return true;
    }

    //if consumed or produced entity is health then this is dealt with
    public void handleHealth(String effect){
        switch(effect){
            case "consume" -> player.decreaseHealth();
            case "produce" -> player.addHealth();
        }
        if(player.isDead()){
            map.killPlayer();
            narration = "You have died and lost all your items. You must return to the game's start location";
        }
    }

    //Checks that each subject is in either the inventory or location
    public boolean subjectsNotInScope(){
        for (String subject: subjectList) {
            if(currentLocation.containsEntity(subject)==null && !player.isItemInInventory(subject)){
                return true;
            }
        }
        return false;
    }

    //for testing
    public String getTrigger(){
        return trigger;
    }

    public List<String> getSubjectList(){
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
