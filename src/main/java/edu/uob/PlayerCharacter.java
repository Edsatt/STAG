package edu.uob;

import java.util.HashMap;

public class PlayerCharacter extends GameCharacter {
    private final String username;
    private final HashMap<String, Artefact> inventory;
    private Location location;
    
    public PlayerCharacter(String username){
        this.username = username;
        this.inventory = new HashMap<>();
    }

    public void setLocation(Location location){
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public String getUsername() {
        return username;
    }
    
    public void addItemToInventory(String id, Artefact item){
        inventory.put(id, item);
    }

    public Artefact dropItem(String id){
        Artefact item = null;
        if(inventory.containsKey(id)){
            item = inventory.get(id);
            inventory.remove(id);
        }
        return item;
    }

    public HashMap<String, Artefact> getInventory() {
        return inventory;
    }
    
    
}
