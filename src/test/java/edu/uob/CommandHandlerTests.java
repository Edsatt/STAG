package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommandHandlerTests {

    CommandHandler commandHandler;
    GameServer testServer;
    Map map;

    public CommandHandlerTests(){}

    @BeforeEach
    void setup() {
        File entitiesFile = Paths.get("config" + File.separator + "test1-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config" + File.separator + "test1-actions.xml").toAbsolutePath().toFile();
        testServer = new GameServer(entitiesFile, actionsFile);
        map = testServer.getMap();
    }

    @Test
    void basicCommandCountTests1(){
        map.selectPlayer("ed");
        commandHandler = new CommandHandler("ed: inventory inv");
        commandHandler.handleCommand(map);
        assertEquals(2,commandHandler.basicCommandCount());
        commandHandler = new CommandHandler("ed: Inventory test");
        commandHandler.handleCommand(map);
        assertEquals(1,commandHandler.basicCommandCount());
        commandHandler = new CommandHandler("ed: get goto drop");
        commandHandler.handleCommand(map);
        assertEquals(3,commandHandler.basicCommandCount());
        commandHandler = new CommandHandler("ed: droop");
        commandHandler.handleCommand(map);
        assertEquals(0,commandHandler.basicCommandCount());
        assertEquals(testServer.handleCommand("ed: inventory inv"), "Input cannot contain more than one command");
    }

    @Test
    void tokenizeCommandTest1(){
        testServer.handleCommand("ed: show me the inventory");
        commandHandler = testServer.getCommandHandler();
        assertEquals(1, commandHandler.getCommandWords().size(), "should only contain inventory");
        assertTrue(commandHandler.getCommandWords().contains("inventory"), "should contain inventory");
    }

    @Test
    void tokenizeCommandTest2(){
        testServer.handleCommand("ed: get the shiny axe");
        commandHandler = testServer.getCommandHandler();
        assertEquals(2, commandHandler.getCommandWords().size(), "should only get and axe");
        assertTrue(commandHandler.getCommandWords().contains("axe"), "should contain axe");
    }

    @Test
    void tokenizeCommandTest3(){
        testServer.handleCommand("ed: unlock the trapdoor please");
        commandHandler = testServer.getCommandHandler();
        assertEquals(2, commandHandler.getCommandWords().size(), "should only contain unlock and trapdoor");
        assertTrue(commandHandler.getCommandWords().contains("unlock"), "should contain unlock");
    }

    @Test
    void subjectCountTests(){
        map.selectPlayer("ed");
        commandHandler = new CommandHandler("ed: get key key");
        commandHandler.handleCommand(map);
        assertEquals(commandHandler.subjectCount(), 2);
        commandHandler = new CommandHandler("ed: get key axe");
        commandHandler.handleCommand(map);
        assertEquals(commandHandler.subjectCount(), 2);
        commandHandler = new CommandHandler("ed: get key");
        commandHandler.handleCommand(map);
        assertEquals(commandHandler.subjectCount(), 1);
        assertEquals(commandHandler.getCommandSubjects().get(0), "key");
        commandHandler = new CommandHandler("ed: inventory");
        commandHandler.handleCommand(map);
        assertEquals(commandHandler.subjectCount(), 0);
        commandHandler = new CommandHandler("ed: get bronze key");
        commandHandler.handleCommand(map);
        assertEquals(commandHandler.getCommandSubjects().get(0), "key");
        assertEquals(commandHandler.subjectCount(), 1);
        String response = testServer.handleCommand("ed: key axe");
        assertTrue(response.contains("Error"));
    }
}
