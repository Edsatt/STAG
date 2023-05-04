package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocationTests {

    Location room;
    @BeforeEach
    void setup() {
        room = new Location("Room");
        room.setDescription("A room for testing");
        room.addPath("Forest");
        room.addPath("Cabin");
        room.addValuePair("artefacts", "key", "An old key");
        room.addValuePair("artefacts", "fish", "A rotten fish");
        room.addValuePair("characters", "dwarf", "A bulky dwarf");
        room.addValuePair("characters", "dragon", "An angry dragon");
        room.addValuePair("furniture", "door", "A locked door");
        room.addValuePair("furniture", "window", "An ornate window");
    }

    @Test
    void basicLocationTests(){
        //Tests all the methods in the Location class
        assertEquals(room.getDescription(), "A room for testing");
        assertFalse(room.getPaths().isEmpty());
        assertFalse(room.getFurniture().isEmpty());
        assertFalse(room.getArtefacts().isEmpty());
        assertFalse(room.getCharacters().isEmpty());
        assertTrue(room.getPaths().contains("Forest") && room.getPaths().contains("Cabin"));
        assertTrue(room.getArtefacts().containsKey("key"));
        assertEquals(room.getArtefacts().get("key").getDescription(), "An old key");
        assertTrue(room.getArtefacts().containsKey("fish"));
        assertEquals(room.getArtefacts().get("fish").getDescription(), "A rotten fish");
        assertEquals(room.getCharacters().get("dwarf").getDescription(), "A bulky dwarf");
        assertEquals(room.getCharacters().get("dragon").getDescription(), "An angry dragon");
        assertEquals(room.getFurniture().get("door").getDescription(), "A locked door");
        assertEquals(room.getFurniture().get("window").getDescription(), "An ornate window");
    }

    @Test
    void locationHasPathsTests(){
        room.addPath("cabin");
        room.addPath("forest");
        assertTrue(room.hasPath("cabin"), "should have cabin path");
        assertTrue(room.hasPath("forest"), "should have forest path");
    }

    @Test
    void addEntityTests(){
        GameEntity axe = new Artefact();
        GameEntity door = new Furniture();
        GameEntity troll = new NonPlayerCharacter();
        room.addEntity("artefacts", "axe", axe);
        room.addEntity("furniture", "door", door);
        room.addEntity("characters", "troll", troll);
        assertEquals("artefacts", room.containsEntity("axe"), "axe stored as artefact");
        assertEquals("furniture", room.containsEntity("door"), "door stored as furniture");
        assertEquals("characters", room.containsEntity("troll"), "troll stored as character");
        assertNull(room.containsEntity("book"), "no book entity");
    }

    @Test
    void takeEntityTests(){
        GameEntity axe = new Artefact();
        GameEntity door = new Furniture();
        GameEntity troll = new NonPlayerCharacter();
        room.addEntity("artefacts", "axe", axe);
        room.addEntity("furniture", "door", door);
        room.addEntity("characters", "troll", troll);
        assertNotNull(room.takeEntity("artefacts", "axe"), "returns axe");
        assertNotNull(room.takeEntity("furniture", "door"), "returns door");
        assertNotNull(room.takeEntity("characters", "troll"), "returns troll");
        assertNull(room.takeEntity("characters", "fish"), "no fish entity");
    }
}

