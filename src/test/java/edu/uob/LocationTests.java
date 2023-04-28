package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;

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
        assertEquals(room.getArtefacts().get("key"), "An old key");
        assertTrue(room.getArtefacts().containsKey("fish"));
        assertEquals(room.getArtefacts().get("fish"), "A rotten fish");
        assertEquals(room.getCharacters().get("dwarf"), "A bulky dwarf");
        assertEquals(room.getCharacters().get("dragon"), "An angry dragon");
        assertEquals(room.getFurniture().get("door"), "A locked door");
        assertEquals(room.getFurniture().get("window"), "An ornate window");
    }
}

