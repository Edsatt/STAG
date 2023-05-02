package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameCommandTests {

    GameCommand command;
    GameServer testServer;
    Map map;
    HashMap<String, PlayerCharacter> players;
    PlayerCharacter player;
    @BeforeEach
    void setup() {
        File entitiesFile = Paths.get("config" + File.separator + "basic-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config" + File.separator + "basic-actions.xml").toAbsolutePath().toFile();
        testServer = new GameServer(entitiesFile, actionsFile);
        map = testServer.map;
    }

    @Test
    void basicCommandCountTests(){
        map.selectPlayer("ed");
        command = new GameCommand("ed: inventory inv");
        command.handleCommand(map);
        assertEquals(command.basicCommandCount(), 2);
        command = new GameCommand("ed: Inventory test");
        command.handleCommand(map);
        assertEquals(command.basicCommandCount(), 1);
        command = new GameCommand("ed: get goto drop");
        command.handleCommand(map);
        assertEquals(command.basicCommandCount(), 3);
        command = new GameCommand("ed: droop");
        command.handleCommand(map);
        assertEquals(command.basicCommandCount(), 0);
        assertEquals(testServer.handleCommand("ed: inventory inv"), "Input cannot contain more than one command");
    }

    @Test
    void subjectCountTests(){
        map.selectPlayer("ed");
        command = new GameCommand("ed: get key key");
        command.handleCommand(map);
        assertEquals(command.subjectCount(), 2);
        command = new GameCommand("ed: get key axe");
        command.handleCommand(map);
        assertEquals(command.subjectCount(), 2);
        command = new GameCommand("ed: get key");
        command.handleCommand(map);
        assertEquals(command.subjectCount(), 1);
        assertEquals(command.getSubject(), "key");
        command = new GameCommand("ed: inventory");
        command.handleCommand(map);
        assertEquals(command.subjectCount(), 0);
        command = new GameCommand("ed: get bronze key");
        command.handleCommand(map);
        assertEquals(command.getSubject(), "key");
        assertEquals(command.subjectCount(), 1);
        String response = testServer.handleCommand("ed: key axe");
        assertTrue(response.contains("Error"));
    }

    @Test
    void testInventoryCommand(){
        assertEquals(testServer.handleCommand("ed: inventory"), "Your inventory is empty");
        PlayerCharacter ed = map.getCurrentPlayer();
        assertTrue(ed.getInventory().isEmpty());
        assertEquals(testServer.handleCommand("ed: get axe"), "You pick up the Shiny axe");
        assertTrue(ed.getInventory().containsKey("axe"));
        assertFalse(map.getCurrentLocation().getArtefacts().containsKey("axe"));
        String response1 = testServer.handleCommand("ed: inventory potion");
        assertTrue(response1.contains("Error"));
        String response2 = testServer.handleCommand("ed: inventory please");
        assertTrue(response2.contains("You are holding the following items:"));
    }

    @Test
    void testLookCommand(){
        String response1 = testServer.handleCommand("ed: look");
        assertTrue(response1.contains("cabin"));
        String response2 = testServer.handleCommand("ed: look forest");
        assertTrue(response2.contains("Error"));
        String response3 = testServer.handleCommand("ed: look around");
        assertTrue(response3.contains("cabin"));
        testServer.handleCommand("ed: goto forest");
        String response4 = testServer.handleCommand("ed: look");
        assertTrue(response4.contains("forest"));
    }

    @Test
    void testGetCommand(){
        testServer.handleCommand("ed: inv");
        PlayerCharacter ed = map.getCurrentPlayer();
        assertTrue(ed.getInventory().isEmpty());
        testServer.handleCommand("ed: get axe");
        assertTrue(ed.getInventory().containsKey("axe"));
        assertFalse(map.getCurrentLocation().getArtefacts().containsKey("axe"));
        assertEquals(testServer.handleCommand("ed: get"),"Error: Get must be followed by an item in the current location");
        assertEquals(testServer.handleCommand("ed: get key"),"Error: Get must be followed by an item in the current location");
    }

    @Test
    void testDropCommand(){
        testServer.handleCommand("ed: look");
        PlayerCharacter ed = map.getCurrentPlayer();
        testServer.handleCommand("ed: get axe");
        assertTrue(ed.getInventory().containsKey("axe"));
        testServer.handleCommand("ed: goto forest");
        testServer.handleCommand("ed: look");
        assertEquals(map.getCurrentLocation().getId(),"forest");
        assertFalse(map.getCurrentLocation().getArtefacts().containsKey("axe"));
        assertEquals(testServer.handleCommand("ed: drop axe"),"You drop the Shiny axe");
        assertFalse(ed.getInventory().containsKey("axe"));
        assertTrue(map.getCurrentLocation().getArtefacts().containsKey("axe"));
    }

    @Test
    void testGotoCommand(){
        testServer.handleCommand("ed: look");
        String response1 = testServer.handleCommand("ed: goto forest");
        assertTrue(response1.contains("forest"));
        String response2 = testServer.handleCommand("ed: goto cellar");
        assertTrue(response2.contains("Error"));
        String response3 = testServer.handleCommand("ed: goto cabin axe");
        assertTrue(response3.contains("Error"));
    }
}
