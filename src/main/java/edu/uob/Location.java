package edu.uob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Location {
    private final String id;
    private String description;
    private final HashMap<String, String> characters;
    private final HashMap<String, String> artefacts;
    private final HashMap<String, String> furniture;
    private HashSet<String> paths;
    public Location(String id){
        this.id = id;
        this.characters = new HashMap<>();
        this.artefacts = new HashMap<>();
        this.furniture = new HashMap<>();
        this.paths = new HashSet<>();
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void addValuePair(String entityType, String id, String description){
        //System.out.println(entityType+" "+id+" "+description);
        switch (entityType){
            case "characters" -> {
                characters.put(id, description);
            }
            case "artefacts" -> {
                artefacts.put(id, description);
            }
            case "furniture" -> {
                furniture.put(id, description);
            }
        }
    }

    public void addPath(String path){
        paths.add(path);
    }

    public String getDescription() {
        return description;
    }

    public HashMap<String, String> getArtefacts() {
        return artefacts;
    }

    public HashMap<String, String> getCharacters() {
        return characters;
    }

    public HashMap<String, String> getFurniture() {
        return furniture;
    }

    public HashSet<String> getPaths() {
        return paths;
    }

    public boolean checkPaths(String path){
        return paths.contains(path);
    }


}
