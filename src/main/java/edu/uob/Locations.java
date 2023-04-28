package edu.uob;

import java.util.HashMap;

public class Locations {
    HashMap<String, Location> locations;

    public Locations(){
        this.locations = new HashMap<>();
    }

    public void addLocation(String id){
        Location newLocation = new Location(id);
        locations.put(id, newLocation);
    }

    public Location getLocation(String id){
        return locations.get(id);
    }
}
