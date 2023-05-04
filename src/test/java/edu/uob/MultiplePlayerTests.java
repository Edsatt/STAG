package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class MultiplePlayerTests {

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
        assertFalse(players.get("ed").isDead());
        assertFalse(players.get("isobel").isDead());
        assertTrue(players.get("ed").getInventory().isEmpty());
    }

    @Test
    void playerLookTests(){
        testServer.handleCommand("ed: inventory");
        testServer.handleCommand("isobel: inventory");
        String response1 = testServer.handleCommand("ed: look");
        System.out.println(response1);
        assertTrue(response1.contains("isobel"), "one player can see the other");
        assertFalse(response1.contains(" ed "), "player does not see themselves");
    }

    @Test
    void playerItemTests(){
        testServer.handleCommand("ed: goto forest");
        testServer.handleCommand("ed: get key");
        testServer.handleCommand("ed: goto cabin");
        testServer.handleCommand("ed: drop key");
        String response1 = testServer.handleCommand("isobel: look");
        assertTrue(response1.contains("key"), "key should now be in cabin");
    }
}


