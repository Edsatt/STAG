package edu.uob;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Map {
    private String startLocationKey;
    private PlayerCharacter currentPlayer;
    private Location currentLocation;
    private final LinkedHashMap<String, Location> locations;
    private final HashMap<String, PlayerCharacter> players;

    public Map(){
        this.locations = new LinkedHashMap<>();
        this.players = new HashMap<>();
    }

    public void setStartLocationKey(String key){
        this.startLocationKey = key;
    }

    public void selectPlayer(String username){
        if(!players.containsKey(username)){
            setupNewPlayer(username);
        }
        this.currentPlayer = players.get(username);
    }

    public void setupNewPlayer(String username){
        PlayerCharacter player = new PlayerCharacter(username);
        player.setAliveStatus(true);
        players.put(username, player);
        player.setLocation(locations.get(startLocationKey));
    }

    public void loadLocation(){
        this.currentLocation = currentPlayer.getLocation();
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
