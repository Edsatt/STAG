package edu.uob;

import java.util.*;

public class Map {
    private String startLocationKey;
    private PlayerCharacter currentPlayer;
    private Location currentLocation;
    private final LinkedHashMap<String, Location> locations;
    private final HashMap<String, PlayerCharacter> players;
    private final String [] commands;
    private final List<String> subjects;
    private final List<String> triggers;
    private final HashSet<List<String>> triggerGroups;
    private final ActionsList actionsList;

    public Map(){
        this.locations = new LinkedHashMap<>();
        this.players = new HashMap<>();
        this.commands = new String []{"inventory", "inv", "look", "get", "goto", "drop", "health"};
        this.subjects = new ArrayList<>();
        this.triggers = new ArrayList<>();
        this.triggerGroups = new HashSet<>();
        this.actionsList = new ActionsList();
    }

    public void setStartLocationKey(String key){
        this.startLocationKey = key;
    }

    public void addSubjectKey(String key){
        subjects.add(key);
    }

    public void addTrigger(String trigger){
        triggers.add(trigger);
    }

    public void addTriggerGroup(String... triggerGroup){
        triggerGroups.add(Arrays.stream(triggerGroup).toList());
    }

    public List<String> getTriggerGroup(String trigger){
        for(List<String> triggerGroup: triggerGroups){
            if(triggerGroup.contains(trigger)){
                return triggerGroup;
            }
        }
        return null;
    }

    public void selectPlayer(String username){
        if(!players.containsKey(username)){
            setupNewPlayer(username);
        }
        this.currentPlayer = players.get(username);
    }

    public void setupNewPlayer(String username){
        PlayerCharacter player = new PlayerCharacter(username);
        players.put(username, player);
        player.setLocation(locations.get(startLocationKey));
        player.setId("player");
    }

    public void loadLocation(){
        this.currentLocation = currentPlayer.getLocation();
    }

    public void addLocation(String id, Location location){
        locations.put(id, location);
    }

    public void killPlayer(){
        HashMap<String, Artefact> inventory = currentPlayer.getInventory();
        for(String key: inventory.keySet()){
            GameEntity item = currentPlayer.takeItemFromInventory(key);
            currentPlayer.getLocation().addEntity("artefacts",key,item);
        }
        currentPlayer.getLocation().removePlayerCharacter(currentPlayer);
        currentPlayer.setLocation(locations.get(startLocationKey));
        currentPlayer.resetHealth();
    }

    public PlayerCharacter getCurrentPlayer(){
        return this.currentPlayer;
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

    public List<String> getSubjectsList() {
        return subjects;
    }

    public List<String> getTriggersList(){
        return triggers;
    }

    public String[] getCommands() {
        return commands;
    }

    public GameAction getGameAction(String trigger, List<String> subjects){
        return actionsList.getGameAction(trigger, subjects);
    }

    public ActionsList getActionList(){
        return this.actionsList;
    }

    public boolean isLocation(String key){
        return (locations.containsKey(key));
    }

    public Location getEntityLocation(String entityKey){
        Location entityLocation = null;
        for(Location location: locations.values()){
            if(location.containsEntity(entityKey)!=null){
                entityLocation = location;
            }
        }
        return entityLocation;
    }
}
