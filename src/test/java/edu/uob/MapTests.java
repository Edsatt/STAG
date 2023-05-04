package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class MapTests {

    GameServer testServer;
    Map map;
    HashMap<String, PlayerCharacter> players;

    public MapTests(){}
    @BeforeEach
    void setup() {
        File entitiesFile = Paths.get("config" + File.separator + "extended-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config" + File.separator + "basic-actions.xml").toAbsolutePath().toFile();
        testServer = new GameServer(entitiesFile, actionsFile);
        map = testServer.getMap();
        players = map.getPlayers();
    }

    @Test
    void startRoomTests(){
        testServer.handleCommand("ed: inventory");
        PlayerCharacter ed = players.get("ed");
        assertEquals(ed.getLocation(), map.getCurrentLocation());
        assertEquals(ed.getLocation().getId(),"cabin");
        testServer.handleCommand("ed: look");
        assertEquals(ed.getLocation(), map.getCurrentLocation());
        assertEquals(ed.getLocation().getId(),"cabin");

        //map.currentLocation is dependent on the location of the active player
        ed.setLocation(map.getLocation("forest"));
        assertEquals(ed.getLocation().getId(), "forest");
        assertNotEquals(map.getCurrentLocation().getId(), "forest");
        testServer.handleCommand("ed: look");
        assertEquals(map.getCurrentLocation().getId(), "forest");
    }

    @Test
    void killPlayerTests(){
        testServer.handleCommand("ed: get axe");
        testServer.handleCommand("ed: goto forest");
        PlayerCharacter ed = map.getCurrentPlayer();
        assertFalse(ed.getInventory().isEmpty(), "inventory should have contain the axe");
        assertEquals("forest", ed.getLocation().getId(), "player should be in forest");
        map.killPlayer();
        assertTrue(ed.getInventory().isEmpty(),"inventory should reset");
        assertTrue(map.getLocation("forest").getArtefacts().containsKey("axe"),"forest should now contain the axe");
        assertEquals("cabin", ed.getLocation().getId(), "player should have reset at the cabin");
        assertFalse(map.getLocation("forest").getCharacters().containsKey("ed"), "player ed should be removed from location when dead");
    }

    @ParameterizedTest
    @ValueSource(strings = {"cellar","cabin","forest","clearing","riverbank"})
    void isLocationTests(String location){
        assertTrue(map.isLocation(location),location+" should return true");
    }

    @Test
    void roomsContainPlayersTests(){
        testServer.handleCommand("ed: look");
        testServer.handleCommand("isobel: look");
        assertTrue(map.getLocation("cabin").getCharacters().containsKey("ed"), "cabin should contain player ed");
        assertTrue(map.getLocation("cabin").getCharacters().containsKey("isobel"), "cabin should contain player isobel");
        assertEquals(2, map.getLocation("cabin").getCharacters().size(), "cabin should contain 2 players");
        testServer.handleCommand("ed: goto forest");
        assertTrue(map.getLocation("cabin").getCharacters().containsKey("isobel"), "cabin should still contain player isobel");
        assertEquals(1, map.getLocation("cabin").getCharacters().size(),"cabin should only contain one player");

    }
}


