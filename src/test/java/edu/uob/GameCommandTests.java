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

//    @Test
//    void basicCommandCountTests(){
//        command = new GameCommand("inventory inv");
//        command.handleCommand(map);
//        assertEquals(command.basicCommandCount(), 2);
//        command = new GameCommand("Inventory test");
//        command.handleCommand(map);
//        assertEquals(command.basicCommandCount(), 1);
//        command = new GameCommand("get goto drop");
//        command.handleCommand(map);
//        assertEquals(command.basicCommandCount(), 3);
//        command = new GameCommand("droop");
//        command.handleCommand(map);
//        assertEquals(command.basicCommandCount(), 0);
//    }

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
}
