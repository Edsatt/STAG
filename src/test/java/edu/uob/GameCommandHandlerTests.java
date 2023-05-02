package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameCommandHandlerTests {

    GameCommandHandler command;
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
        command = new GameCommandHandler("ed: inventory inv");
        command.handleCommand(map);
        assertEquals(command.basicCommandCount(), 2);
        command = new GameCommandHandler("ed: Inventory test");
        command.handleCommand(map);
        assertEquals(command.basicCommandCount(), 1);
        command = new GameCommandHandler("ed: get goto drop");
        command.handleCommand(map);
        assertEquals(command.basicCommandCount(), 3);
        command = new GameCommandHandler("ed: droop");
        command.handleCommand(map);
        assertEquals(command.basicCommandCount(), 0);
        assertEquals(testServer.handleCommand("ed: inventory inv"), "Input cannot contain more than one command");
    }

    @Test
    void subjectCountTests(){
        map.selectPlayer("ed");
        command = new GameCommandHandler("ed: get key key");
        command.handleCommand(map);
        assertEquals(command.subjectCount(), 2);
        command = new GameCommandHandler("ed: get key axe");
        command.handleCommand(map);
        assertEquals(command.subjectCount(), 2);
        command = new GameCommandHandler("ed: get key");
        command.handleCommand(map);
        assertEquals(command.subjectCount(), 1);
        assertEquals(command.getSubject(), "key");
        command = new GameCommandHandler("ed: inventory");
        command.handleCommand(map);
        assertEquals(command.subjectCount(), 0);
        command = new GameCommandHandler("ed: get bronze key");
        command.handleCommand(map);
        assertEquals(command.getSubject(), "key");
        assertEquals(command.subjectCount(), 1);
        String response = testServer.handleCommand("ed: key axe");
        assertTrue(response.contains("Error"));
    }
}
