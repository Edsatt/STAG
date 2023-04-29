package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        File entitiesFile = Paths.get("config" + File.separator + "basic-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config" + File.separator + "basic-actions.xml").toAbsolutePath().toFile();
        testServer = new GameServer(entitiesFile, actionsFile);
        map = testServer.map;
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
}


