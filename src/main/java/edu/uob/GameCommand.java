package edu.uob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class GameCommand {
    private final String newLine = System.lineSeparator();
    private final Integer programCount;
    private Map map;
    private PlayerCharacter player;
    private final String command;
    private final ArrayList<String> commandWords;
    private Location currentLocation;
    private HashMap<String, Artefact> inventory;

    public GameCommand(String input) {
        this.command = input.toLowerCase();
        this.commandWords = new ArrayList<>();
        this.programCount=0;
    }

    public String handleCommand(Map map) {
        this.map = map;
        this.player = map.getCurrentPlayer();
        commandWords.addAll(Arrays.asList(command.split(" ")));

        return interpretCommand();
    }

    private String interpretCommand(){
        return basicCommandCheck();
    }

    private String basicCommandCheck() {
        String response = "";
        switch(basicCommandCount()){
            case 0 -> response = null;
            case 1 -> response = handleBasicCommand();
            case 2 -> response = "Input cannot contain more than one command";
        }
        return response;
    }

    public int basicCommandCount(){
        String regex = "\\b(inventory|inv|get|drop|goto|look)\\b";
        int count = 0;
        for(String word: commandWords){
            if(word.matches(regex)){
                count++;
            }
        }
        return count;
    }

    private String handleBasicCommand(){
        String response = "";
        for(String word: commandWords){
            switch(word){
                case "inventory", "inv" -> response = handleInventoryCommand();
                case "drop" -> response = handleDropCommand();
                case "goto" -> response = handleGotoCommand();
                case "get" -> response = handleGetCommand();
                case "look" -> response = handleLookCommand();
            }
        }
        return response;
    }

    private String handleInventoryCommand() {
        String response = "";
        this.inventory = player.getInventory();
        if(inventory.isEmpty()){
            return "Your inventory is empty";
        }else{
            for(Artefact item: inventory.values()){
                response = response.concat(item.getDescription()+newLine);
            }
            return response;
        }
    }


    private String handleGetCommand() {
        return "get";
    }

    private String handleDropCommand() {
        return "drop";
    }

    private String handleGotoCommand(){
        return "goto";
    }

    private String handleLookCommand(){
        this.currentLocation = player.getLocation();
        StringBuilder response = new StringBuilder("You are in a ");
        response.append(currentLocation.getDescription()).append(". You can see:").append(newLine);

        for(Artefact item: currentLocation.getArtefacts().values()){
            response.append(item.getDescription()).append(newLine);
        }
        for(Furniture item: currentLocation.getFurniture().values()){
            response.append(item.getDescription()).append(newLine);
        }
        for(NonPlayerCharacter character: currentLocation.getCharacters().values()){
            response.append(character.getDescription()).append(newLine);
        }
        response.append("You can access from here: ").append(newLine);
        for(String path: currentLocation.getPaths()){
            response.append(path).append(newLine);
        }
        return response.toString();
    }

}
