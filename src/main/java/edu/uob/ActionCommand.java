package edu.uob;

import java.util.ArrayList;
import java.util.List;

public class ActionCommand extends GameCommand{
    private final ArrayList<String> triggerList;
    private ArrayList<String> subjectList;
    private String trigger;
    private final Location storeroom;
    private String producedEntity;
    private String consumedEntity;
    private String narration;

    public ActionCommand(ArrayList<String> commandWords, Map map){
        super(commandWords, map);
        this.triggerList = map.getTriggersList();
        this.storeroom = map.getLocation("storeroom");
    }

    public String handleActionCommand(){
        String response;
        if(!checkActionCommand()) {
            response = "Error: Command must contain one trigger phrase and at least one subject";
        }else if(!subjectsInScope()){
            response = "Error: Subjects must be either in your inventory or your current location";
        }else if(!findGameAction()){
            response = "Error: Ambiguous command not recognised";
        }else response = consumeEntity();
        return response;
    }

    //checks that command contains one trigger and at least one subject
    public boolean checkActionCommand(){
        return(checkTriggers() && !isSubjectListEmpty());
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


    public boolean isSubjectListEmpty(){
        return subjectList.isEmpty();
    }

    public boolean findGameAction(){
        GameAction gameAction = map.getGameAction(trigger, subjectList);
        if(gameAction ==null) return false;
        else {
            this.consumedEntity = gameAction.getConsumed();
            this.producedEntity = gameAction.getProduced();
            this.narration = gameAction.getNarration();
            return true;
        }
    }

    public String consumeEntity(){
        if(notHealthOrNull("consume")){
            String entityType = entityInLocation(consumedEntity);
            if(entityType!=null){
                GameEntity entity = currentLocation.takeEntity(entityType, consumedEntity);
                storeroom.addEntity(entityType,consumedEntity,entity);

            }else if(entityInInventory(consumedEntity)){
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
                Location entityLocation = getEntityLocation(producedEntity);
                String entityType = entityLocation.containsEntity(producedEntity);
                GameEntity entity = entityLocation.takeEntity(entityType, producedEntity);
                currentLocation.addEntity(entityType, producedEntity, entity);
            }
        }
        return narration;
    }

    public boolean notHealthOrNull(String entityFate){
        String entity = switch (entityFate){
            case "consume" -> consumedEntity;
            case "produce" -> producedEntity;
            default -> null;
        };
        if(entity==null) return false;
        else if(entity.equals("health")){
            handleHealth(entityFate);
            return false;
        }
        return true;
    }
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

    public Location getEntityLocation(String entityKey){
        Location entityLocation = null;
        for(Location location: map.getLocations().values()){
            if(location.containsEntity(entityKey)!=null){
                entityLocation = location;
            }
        }
        return entityLocation;
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
