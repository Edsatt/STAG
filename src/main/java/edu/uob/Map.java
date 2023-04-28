package edu.uob;

import java.util.HashMap;

public class Map {
    HashMap<String, Location> locations;

    public Map(){
        this.locations = new HashMap<>();
    }

    public void addLocation(String id, Location location){
        locations.put(id, location);
    }

    public Location getLocation(String id){
        return locations.get(id);
    }
}
