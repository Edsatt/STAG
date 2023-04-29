package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class EntityParserTests {

    Map map;
    HashMap<String, Location> locations;
    @BeforeEach
    void setup() {
        File entitiesFile = Paths.get("config" + File.separator + "basic-entities.dot").toAbsolutePath().toFile();
        map = new Map();
        locations = map.getLocations();
        EntityParser entityParser = new EntityParser(map, entitiesFile);
        entityParser.parse();
        entityParser.createLocations();
    }

    @Test
    void mapTests(){
        //Check that map contains all locations from the entity file
        assertEquals(locations.size(), 4);
        assertTrue(locations.containsKey("cabin"));
        assertTrue(locations.containsKey("forest"));
        assertTrue(locations.containsKey("cellar"));
        assertTrue(locations.containsKey("storeroom"));
    }

    @Test
    void locationTests(){
        //Check that each location contains the correct description, entities and paths
        Location cellar = locations.get("cellar");
        assertEquals(cellar.getDescription(), "A dusty cellar");
        assertTrue(cellar.getArtefacts().isEmpty());
        assertTrue(cellar.getFurniture().isEmpty());
        assertTrue(cellar.getCharacters().containsKey("elf"));
        assertEquals(cellar.getCharacters().get("elf").getDescription(), "Angry Elf");
        assertTrue(cellar.getPaths().contains("cabin"));

        Location cabin = locations.get("cabin");
        assertEquals(cabin.getDescription(), "A log cabin in the woods");
        assertTrue(cabin.getCharacters().isEmpty());
        assertTrue(cabin.getArtefacts().containsKey("potion"));
        assertEquals(cabin.getArtefacts().get("potion").getDescription(), "Magic potion");
        assertTrue(cabin.getArtefacts().containsKey("axe"));
        assertEquals(cabin.getArtefacts().get("axe").getDescription(), "Shiny axe");
        assertTrue(cabin.getFurniture().containsKey("trapdoor"));
        assertEquals(cabin.getFurniture().get("trapdoor").getDescription(), "Wooden trapdoor");
        assertTrue(cabin.getPaths().contains("forest"));

        Location forest = locations.get("forest");
        assertEquals(forest.getDescription(), "A dark forest");
        assertTrue(forest.getCharacters().isEmpty());
        assertTrue(forest.getArtefacts().containsKey("key"));
        assertEquals(forest.getArtefacts().get("key").getDescription(), "Brass key");
        assertTrue(forest.getFurniture().containsKey("tree"));
        assertEquals(forest.getFurniture().get("tree").getDescription(), "A big tree");
        assertTrue(forest.getPaths().contains("cabin"));

        Location storeroom = locations.get("storeroom");
        assertEquals(storeroom.getDescription(), "Storage for any entities not placed in the game");
        assertTrue(storeroom.getCharacters().isEmpty());
        assertTrue(storeroom.getFurniture().isEmpty());
        assertTrue(storeroom.getArtefacts().containsKey("log"));
        assertEquals(storeroom.getArtefacts().get("log").getDescription(), "A heavy wooden log");
        assertTrue(storeroom.getPaths().isEmpty());
    }
}
