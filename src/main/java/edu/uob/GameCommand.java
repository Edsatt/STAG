package edu.uob;
import java.util.ArrayList;

public class GameCommand {
    String command;
    ArrayList<Token> commandTokens;

    public GameCommand(String input) {
        this.command = input;
        this.commandTokens = new ArrayList<>();
        handleCommand();
    }

    public void handleCommand() {
        String[] commandWords = command.split(" ");
        for (String word : commandWords) {
            tokenize(word);
        }
        for (Token token : commandTokens) {
            interpretCommand(token);
        }
    }

    private void tokenize(String word) {
        Token token = new Token(word);
        commandTokens.add(token);
    }

    private void interpretCommand(Token token) {
        switch (token.tokenType) {
            case INVENTORY -> {
                handleInventoryCommand();
            }
            case GET -> {
                handleGetCommand();
            }
            case DROP -> {
                handleDropCommand();
            }
            case GOTO -> {
                handleGotoCommand();
            }
            case LOOK -> {
                handleLookCommand();
            }
        }
    }

    private void handleInventoryCommand() {System.out.println("inventory");}

    private void handleGetCommand() {System.out.println("get");}

    private void handleDropCommand() {System.out.println("drop");}

    private void handleGotoCommand(){System.out.println("goto");}

    private void handleLookCommand(){System.out.println("look");}

}
