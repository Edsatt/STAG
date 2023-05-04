package edu.uob;

import java.util.List;

public class GameCommand {
    String newLine = System.lineSeparator();
    List<String> commandWords;
    Map map;
    PlayerCharacter player;
    Location currentLocation;

    public GameCommand(List<String> commandWords, Map map) {
        this.map = map;
        this.player = map.getCurrentPlayer();
        this.commandWords = commandWords;
        this.currentLocation = player.getLocation();
    }
}

