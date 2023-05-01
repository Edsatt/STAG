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
        assertEquals(testServer.handleCommand("ed: get key axe"), "Command cannot contain more than one subject");
    }

    @Test
    void testInventoryCommand(){
        assertEquals(testServer.handleCommand("ed: inventory"), "Your inventory is empty");
        PlayerCharacter ed = map.getCurrentPlayer();
        assertTrue(ed.getInventory().isEmpty());
        Artefact key = new Artefact();
        key.setDescription("A key");
        Artefact axe = new Artefact();
        axe.setDescription("A shiny axe");
        ed.addItemToInventory("key", key);
        ed.addItemToInventory("axe", axe);
        assertEquals(ed.getInventory().size(), 2);
        String response = testServer.handleCommand("ed: Inventory");
        assertTrue(response.contains("A key"));
        assertTrue(response.contains("A shiny axe"));
    }

    @Test
    void testLookCommand(){
        //System.out.println(testServer.handleCommand("ed: look"));
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
//        System.out.println(testServer.handleCommand("ed: look"));
//        System.out.println(testServer.map.getCurrentLocation().getId());
//        System.out.println(testServer.handleCommand("ed: goto forest"));
//        System.out.println(testServer.map.getCurrentLocation().getId());
//        System.out.println(testServer.handleCommand("ed: look"));
//        System.out.println(testServer.map.getCurrentLocation().getId());
    }
}
