package edu.uob;
import javax.swing.text.Document;
import java.util.ArrayList;
import java.util.HashMap;

public class GameCommand {
    Map map;
    PlayerCharacter player;
    String command;
    ArrayList<Token> commandTokens;

    public GameCommand(String input) {
        this.command = input;
        this.commandTokens = new ArrayList<>();
    }

    public String handleCommand(Map map) {
        this.map = map;
        this.player = map.getCurrentPlayer();
        String[] commandWords = command.split(" ");
        for (String word : commandWords) {
            tokenize(word);
        }
        for (Token token : commandTokens) {
            return interpretCommand(token);
        }
        return "";
    }

    private void tokenize(String word) {
        Token token = new Token(word);
        commandTokens.add(token);
    }

    private String interpretCommand(Token token) {
        switch (token.tokenType) {
            case INVENTORY -> {
                return handleInventoryCommand();
            }
            case GET -> {
                return handleGetCommand();
            }
            case DROP -> {
                return handleDropCommand();
            }
            case GOTO -> {
                return handleGotoCommand();
            }
            case LOOK -> {
                return handleLookCommand();
            }
        }
        return "";
    }

    private String handleInventoryCommand() {
        String response = "";
        HashMap<String, Artefact> inventory = player.getInventory();
        if(inventory.isEmpty()){
            return "Your inventory is empty";
        }else{
            for(Artefact item: inventory.values()){
                response = response.concat(item.getDescription()+System.lineSeparator());
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
        return "look";
    }

}
