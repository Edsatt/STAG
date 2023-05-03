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
    @BeforeEach
    void setup() {
        File entitiesFile = Paths.get("config" + File.separator + "extended-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config" + File.separator + "basic-actions.xml").toAbsolutePath().toFile();
        testServer = new GameServer(entitiesFile, actionsFile);
        map = testServer.getMap();
        players = map.getPlayers();
    }

    @Test
    void newPlayerTests(){
        //tests adding new players to the game
        testServer.handleCommand("ed: inventory");
        assertEquals(map.getCurrentPlayer().getUsername(), "ed");
        testServer.handleCommand("isobel: inventory");
        assertEquals(map.getCurrentPlayer().getUsername(), "isobel");
        assertTrue(players.containsKey("ed") && players.containsKey("isobel"));
        assertEquals(players.size(), 2);
        testServer.handleCommand("ed: inventory");
        assertTrue(players.containsKey("ed") && players.containsKey("isobel"));
        assertEquals(players.size(), 2);
        assertTrue(players.get("ed").isAlive());
        assertTrue(players.get("isobel").isAlive());
        assertTrue(players.get("ed").getInventory().isEmpty());
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

    @ParameterizedTest
    @ValueSource(strings = {"cellar","cabin","forest","clearing","riverbank"})
    void isLocationTests(String location){
        assertTrue(map.isLocation(location),location+" should return true");
    }
}


