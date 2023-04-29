package edu.uob;

import java.util.HashMap;

public class Map {
    private PlayerCharacter currentPlayer;
    private final HashMap<String, Location> locations;
    private final HashMap<String, PlayerCharacter> players;

    public Map(){
        this.locations = new HashMap<>();
        this.players = new HashMap<>();
    }

    public void addPlayer(String username){
        if(!players.containsKey(username)){
            PlayerCharacter player = new PlayerCharacter(username);
            player.setAliveStatus(true);
            players.put(username, player);
        }
        this.currentPlayer = players.get(username);
    }

    public void addLocation(String id, Location location){
        locations.put(id, location);
    }

    public PlayerCharacter getCurrentPlayer(){
        return this.currentPlayer;
    }

    public PlayerCharacter getPlayerByUsername(String username){
        return players.get(username);
    }

    public Location getLocation(String id){
        return locations.get(id);
    }

    public HashMap<String, PlayerCharacter> getPlayers() {
        return players;
    }

    public HashMap<String, Location> getLocations() {
        return locations;
    }
}
