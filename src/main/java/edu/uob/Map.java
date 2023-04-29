package edu.uob;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Map {
    private PlayerCharacter currentPlayer;
    private Location currentLocation;
    private final LinkedHashMap<String, Location> locations;
    private final HashMap<String, PlayerCharacter> players;

    public Map(){
        this.locations = new LinkedHashMap<>();
        this.players = new HashMap<>();
    }

    public void setCurrentLocation(Location location){
        currentLocation = location;
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

    public Location getCurrentLocation(){
        return currentLocation;
    }

    public Location getLocation(String id){
        return locations.get(id);
    }

    public HashMap<String, PlayerCharacter> getPlayers() {
        return players;
    }

    public LinkedHashMap<String, Location> getLocations() {
        return locations;
    }
}
