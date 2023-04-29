package edu.uob;

import java.util.HashMap;

public class PlayerCharacter extends Character{
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

    public boolean removeItemFromInventory(String id){
        if(inventory.containsKey(id)){
            inventory.remove(id);
            return true;
        } else return false;
    }
    public HashMap<String, Artefact> getInventory() {
        return inventory;
    }
    
    
}
