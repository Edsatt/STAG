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

    public EntityParserTests(){}
    @BeforeEach
    void setup() {
        File entitiesFile = Paths.get("config" + File.separator + "test2-entities.dot").toAbsolutePath().toFile();
        map = new Map();
        locations = map.getLocations();
        EntityParser entityParser = new EntityParser(map, entitiesFile);
        entityParser.parse();
        entityParser.createLocations();
    }

    @Test
    void mapTests(){
        //Check that map contains all locations from the entity file
        assertEquals(6, locations.size());
        assertTrue(locations.containsKey("cabin"));
        assertTrue(locations.containsKey("forest"));
        assertTrue(locations.containsKey("cellar"));
        assertTrue(locations.containsKey("storeroom"));
        assertTrue(locations.containsKey("clearing"));
        assertTrue(locations.containsKey("riverbank"));
    }

    //Check that each location contains the correct description, entities and paths
    @Test
    void cellarTests(){
        Location cellar = locations.get("cellar");
        assertEquals(cellar.getDescription(), "A dusty cellar");
        assertTrue(cellar.getArtefacts().isEmpty());
        assertTrue(cellar.getFurniture().isEmpty());
        assertTrue(cellar.getCharacters().containsKey("elf"));
        assertEquals(cellar.getCharacters().get("elf").getDescription(), "An angry looking Elf");
        assertTrue(cellar.getPaths().contains("cabin"));
    }

    @Test
    void cabinTests(){
        Location cabin = locations.get("cabin");
        assertEquals(cabin.getDescription(), "A log cabin in the woods");
        assertTrue(cabin.getCharacters().isEmpty());
        assertTrue(cabin.getArtefacts().containsKey("potion"));
        assertEquals(cabin.getArtefacts().get("potion").getDescription(), "A bottle of magic potion");
        assertTrue(cabin.getArtefacts().containsKey("axe"));
        assertEquals(cabin.getArtefacts().get("axe").getDescription(), "A razor sharp axe");
        assertTrue(cabin.getFurniture().containsKey("trapdoor"));
        assertEquals(cabin.getFurniture().get("trapdoor").getDescription(), "A locked wooden trapdoor in the floor");
        assertTrue(cabin.getPaths().contains("forest"));
    }

    @Test
    void forestTests(){
        Location forest = locations.get("forest");
        assertEquals(forest.getDescription(), "A deep dark forest");
        assertTrue(forest.getCharacters().isEmpty());
        assertTrue(forest.getArtefacts().containsKey("key"));
        assertEquals(forest.getArtefacts().get("key").getDescription(), "A rusty old key");
        assertTrue(forest.getFurniture().containsKey("tree"));
        assertEquals(forest.getFurniture().get("tree").getDescription(), "A tall pine tree");
        assertTrue(forest.getPaths().contains("cabin"));
    }

    @Test
    void containsEntityTests(){
        Location cabin = locations.get("cabin");
        Location cellar = locations.get("cellar");
        assertEquals("artefacts", cabin.containsEntity("potion"));
        assertEquals("artefacts", cabin.containsEntity("axe"));
        assertEquals("artefacts", cabin.containsEntity("coin"));
        assertEquals("furniture", cabin.containsEntity("trapdoor"));
        assertNull(cabin.containsEntity("elf"));
        assertEquals("characters", cellar.containsEntity("elf"));
    }

}
