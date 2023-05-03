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

    public boolean notHealthOrNull(String entityFate) {
        switch (entityFate) {
            case "consume" -> {
                if (consumedEntity == null) return false;
                if (consumedEntity.equals("health")) {
                    handleHealth();
                    return false;
                }
            }
            case "produce" -> {
                if (producedEntity == null) return false;
                if (producedEntity.equals("health")) {
                    handleHealth();
                    return false;
                }
            }
        }
        return true;
    }

    public String consumeEntity(){
        if(notHealthOrNull("consume")){
            String entityType = entityInLocation(consumedEntity);
            if(entityType!=null){
                GameEntity entity = currentLocation.takeEntity(entityType, consumedEntity);
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
        }
        return narration;
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

    public void handleHealth(){
        System.out.println("Helth");
    }

    //checks that command contains one trigger and at least one subject
    public boolean checkActionCommand(){
        return(checkTriggers() && !isSubjectListEmpty());
    }

    public boolean checkTriggers(){
        int count=0;
        for(String word: commandWords){
            if(word.matches(GameServer.generateRegex(triggerList))){
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
