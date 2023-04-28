package edu.uob;

import java.util.ArrayList;

public class Location {
    private final String id;
    private String description;
    private ArrayList<String> characters;
    private ArrayList<Artefact> artefacts;
    private ArrayList<String> furniture;
    private String[] paths;
    public Location(String id){
        this.id = id;
    }
}
