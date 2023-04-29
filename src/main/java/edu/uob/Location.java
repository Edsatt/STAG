package edu.uob;

import java.util.HashMap;
import java.util.HashSet;

public class Location {
    private boolean startLocation;
    private boolean storeroom;
    private final String id;
    private String description;
    private final HashMap<String, NonPlayerCharacter> characters;
    private final HashMap<String, Artefact> artefacts;
    private final HashMap<String, Furniture> furniture;
    private final HashSet<String> paths;
    public Location(String id){
        this.id = id;
        this.characters = new HashMap<>();
        this.artefacts = new HashMap<>();
        this.furniture = new HashMap<>();
        this.paths = new HashSet<>();
        this.startLocation = false;
        this.storeroom = false;
    }

    public void setStartLocation(boolean isStart){
        this.startLocation = isStart;
    }

    public void setStoreroom(boolean storeroom) {
        this.storeroom = storeroom;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void addValuePair(String entityType, String id, String description){
        switch (entityType){
            case "characters" -> {
                NonPlayerCharacter npc = new NonPlayerCharacter();
                npc.setDescription(description);
                npc.setAliveStatus(true);
                characters.put(id, npc);
            }
            case "artefacts" -> {
                Artefact artefact = new Artefact();
                artefact.setDescription(description);
                artefacts.put(id, artefact);
            }
            case "furniture" -> {
                Furniture furnitureItem = new Furniture();
                furnitureItem.setDescription(description);
                furniture.put(id, furnitureItem);
            }
        }
    }

    public void addPath(String path){
        paths.add(path);
    }

    public String getId(){
        return id;
    }

    public boolean isStartLocation() {
        return startLocation;
    }

    public boolean isStoreroom() {
        return storeroom;
    }

    public String getDescription() {
        return description;
    }

    public HashMap<String, Artefact> getArtefacts() {
        return artefacts;
    }

    public HashMap<String, NonPlayerCharacter> getCharacters() {
        return characters;
    }

    public HashMap<String, Furniture> getFurniture() {
        return furniture;
    }

    public HashSet<String> getPaths() {
        return paths;
    }

    public boolean checkPaths(String path){
        return paths.contains(path);
    }


}
