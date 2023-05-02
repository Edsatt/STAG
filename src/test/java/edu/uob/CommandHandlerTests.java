package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommandHandlerTests {

    CommandHandler command;
    GameServer testServer;
    Map map;
    @BeforeEach
    void setup() {
        File entitiesFile = Paths.get("config" + File.separator + "basic-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config" + File.separator + "basic-actions.xml").toAbsolutePath().toFile();
        testServer = new GameServer(entitiesFile, actionsFile);
        map = testServer.getMap();
    }

    @Test
    void basicCommandCountTests(){
        map.selectPlayer("ed");
        command = new CommandHandler("ed: inventory inv");
        command.handleCommand(map);
        assertEquals(command.basicCommandCount(), 2);
        command = new CommandHandler("ed: Inventory test");
        command.handleCommand(map);
        assertEquals(command.basicCommandCount(), 1);
        command = new CommandHandler("ed: get goto drop");
        command.handleCommand(map);
        assertEquals(command.basicCommandCount(), 3);
        command = new CommandHandler("ed: droop");
        command.handleCommand(map);
        assertEquals(command.basicCommandCount(), 0);
        assertEquals(testServer.handleCommand("ed: inventory inv"), "Input cannot contain more than one command");
    }

    @Test
    void subjectCountTests(){
        map.selectPlayer("ed");
        command = new CommandHandler("ed: get key key");
        command.handleCommand(map);
        assertEquals(command.subjectCount(), 2);
        command = new CommandHandler("ed: get key axe");
        command.handleCommand(map);
        assertEquals(command.subjectCount(), 2);
        command = new CommandHandler("ed: get key");
        command.handleCommand(map);
        assertEquals(command.subjectCount(), 1);
        assertEquals(command.getSubjectList().get(0), "key");
        command = new CommandHandler("ed: inventory");
        command.handleCommand(map);
        assertEquals(command.subjectCount(), 0);
        command = new CommandHandler("ed: get bronze key");
        command.handleCommand(map);
        assertEquals(command.getSubjectList().get(0), "key");
        assertEquals(command.subjectCount(), 1);
        String response = testServer.handleCommand("ed: key axe");
        assertTrue(response.contains("Error"));
    }
}
