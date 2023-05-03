package edu.uob;

import java.util.HashMap;

public class PlayerCharacter extends GameCharacter {
    private final String username;
    private final HashMap<String, Artefact> inventory;
    private Location location;
    private int health;
    private boolean dead;
    
    public PlayerCharacter(String username){
        this.username = username;
        this.inventory = new HashMap<>();
        this.health = 3;
        this.dead = false;
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

    public void resetHealth(){
        this.health =3;
        this.dead = false;
    }

    public void addHealth(){
        if(health<3){
            health++;
        }
    }

    public void decreaseHealth(){
        health --;
        if(health ==0) this.dead = true;
    }

    public int getHealth(){
        return health;
    }

    public boolean isDead(){
        return this.dead;
    }

    public boolean isItemInventory(String id){
        return(inventory.containsKey(id));
    }

    public Artefact takeItemFromInventory(String id){
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
