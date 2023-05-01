package edu.uob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class GameCommand {
    private final String newLine = System.lineSeparator();
    private Map map;
    private PlayerCharacter player;
    private final String command;
    private String subject;
    private final ArrayList<String> commandWords;
    private Location currentLocation;
    private HashMap<String, Artefact> inventory;

    public GameCommand(String input) {
        this.command = input.toLowerCase();
        this.commandWords = new ArrayList<>();
    }

    public String handleCommand(Map map) {
        this.map = map;
        this.player = map.getCurrentPlayer();
        this.currentLocation = player.getLocation();
        commandWords.addAll(Arrays.asList(command.split(" ")));

        return interpretCommand();
    }

    private String interpretCommand(){
        return subjectCheck();
    }

    public String subjectCheck(){
        String response = "";
        switch (subjectCount()){
            case 0,1 -> response = basicCommandCheck();
            case 2 -> response = "Command cannot contain more than one subject";
        }
        return response;
    }

    public int subjectCount(){
        int count = 0;
        StringBuilder subjects = new StringBuilder();
        for(String subject: map.getSubjects()){
            subjects.append(subject).append("|");
        }
        subjects.deleteCharAt(subjects.length()-1);
        String regex = "\\b("+subjects+")\\b";

        for(String word: commandWords){
            if(word.matches(regex)){
                count ++;
                setSubject(word);
            }
        }
        return count;
    }

    private void setSubject(String subject){
        this.subject = subject;
    }

    public String getSubject(){
        return subject;
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

    public int basicCommandCount() {
        String regex = "\\b(inventory|inv|get|drop|goto|look)\\b";
        int count = 0;
        for (String word : commandWords) {
            if (word.matches(regex)) {
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
        Artefact item = currentLocation.getArtefactByKey(subject);
        if(subject == null || item==null){
            return ("Error: Get must be followed by an item in the current location");
        }else{
            player.addItemToInventory(subject, item);
            return ("You pick up the "+item.getDescription());
        }
    }

    private String handleDropCommand() {
        Artefact item = player.dropItem(subject);
        if(subject == null || item==null){
            return ("Error: Cannot drop an item is not in your inventory");
        }else{
            currentLocation.addArtefact(subject, item);
            return ("You drop the "+item.getDescription());
        }
    }

    private String handleGotoCommand(){
        if(currentLocation.checkPaths(subject)){
            currentLocation = map.getLocation(subject);
            player.setLocation(currentLocation);
            return handleLookCommand();
        }else return "Error: goto command must be followed by a location you can travel to";
    }

    private String handleLookCommand(){
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
