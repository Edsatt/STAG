package edu.uob;

import java.util.ArrayList;
import java.util.HashMap;

public class BasicCommand extends GameCommand{
    private String subject;

    public BasicCommand(ArrayList<String> commandWords, Map map){
        super(commandWords, map);
    }

    public void setSubject(String subject){
        this.subject = subject;
    }

    public String handleBasicCommand(){
        String response = "";
        for(String word: commandWords){
            switch(word){
                case "inventory", "inv" -> response = handleInventoryCommand();
                case "drop" -> response = handleDropCommand();
                case "goto" -> response = handleGotoCommand();
                case "get" -> response = handleGetCommand();
                case "look" -> response = handleLookCommand();
                case "health" -> response = handleHealthCommand();
            }
        }
        return response;
    }

    private String handleInventoryCommand() {
        HashMap<String, Artefact> inventory = player.getInventory();
        String response;
        if(subject!=null) {
            response = "Error: Inventory commands cannot contain any subjects";
        } else if(inventory.isEmpty()){
            response = "Your inventory is empty";
        }else{
            response = "You are holding the following items:";
            for(Artefact item: inventory.values()){
                response = response.concat(newLine+item.getDescription());
            }
        }
        return response;
    }

    private String handleGetCommand() {
        Artefact item = (Artefact)currentLocation.takeEntity("artefacts", subject);
        if(subject == null || item==null){
            return ("Error: Get must be followed by an item in the current location");
        }else{
            player.addItemToInventory(subject, item);
            return ("You pick up the "+item.getDescription());
        }
    }

    private String handleDropCommand() {
        Artefact item = player.takeItemFromInventory(subject);
        if(subject == null || item==null){
            return ("Error: Cannot drop an item is not in your inventory");
        }else{
            currentLocation.addEntity("artefacts",subject, item);
            return ("You drop the "+item.getDescription());
        }
    }

    private String handleGotoCommand(){
        if(currentLocation.hasPath(subject)){
            currentLocation.removePlayerCharacter(player);
            currentLocation = map.getLocation(subject);
            player.setLocation(currentLocation);
            return "You travel to the " +currentLocation.getId();
        }else return "Error: goto command must be followed by a location you can travel to";
    }

    private String handleLookCommand(){
        if(subject!=null){
            return "Error: The look command cannot contain any subjects";
        }
        StringBuilder response = new StringBuilder("You are in a ");
        response.append(currentLocation.getDescription()).append(". You can see:").append(newLine);

        for(Artefact item: currentLocation.getArtefacts().values()){
            response.append(item.getDescription()).append(newLine);
        }
        for(Furniture item: currentLocation.getFurniture().values()){
            response.append(item.getDescription()).append(newLine);
        }
        for(GameCharacter character: currentLocation.getCharacters().values()){
            response.append(character.getDescription()).append(newLine);
        }
        response.append("You can access from here: ").append(newLine);
        for(String path: currentLocation.getPaths()){
            response.append(path).append(newLine);
        }
        return response.toString();
    }

    private String handleHealthCommand() {
        if(subject!=null) return "Error: Health command cannot contain any subjects";
        return "Your health is "+player.getHealth()+"/3";
    }
}
