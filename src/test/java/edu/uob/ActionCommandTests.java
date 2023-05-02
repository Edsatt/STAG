package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ActionCommandTests {
    GameServer testServer;
    Map map;
    ActionCommand actionCommand;
    @BeforeEach
    void setup() {
        File entitiesFile = Paths.get("config" + File.separator + "basic-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config" + File.separator + "basic-actions.xml").toAbsolutePath().toFile();
        testServer = new GameServer(entitiesFile, actionsFile);
        map = testServer.getMap();
    }

    @Test
    void triggerTests(){
        testServer.handleCommand("ed: unlock trapdoor");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertEquals("unlock", actionCommand.getTrigger(),"trigger should be unlock");
        String response = (testServer.handleCommand("ed: destroy trapdoor"));
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertNull(actionCommand.getTrigger(),"trigger should not be set");
        assertTrue(response.contains("Error"), "response should contain an error");
    }

    @Test
    void subjectListTests(){
        testServer.handleCommand("ed: unlock trapdoor with key");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertTrue(actionCommand.checkSubjectList(), "should be true");
        ArrayList<String> subjectList = actionCommand.getSubjectList();
        assertEquals(2, subjectList.size(), "should contain 2 subjects");
        assertTrue(subjectList.contains("trapdoor") && subjectList.contains("key"));
        testServer.handleCommand("ed: unlock door with hammer");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertFalse(actionCommand.checkSubjectList(), "should be false as no subjects");
    }

    @Test
    void checkActionCommandTests(){
        testServer.handleCommand("ed: unlock trapdoor with key");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertTrue(actionCommand.checkActionCommand(), "should be true");
        testServer.handleCommand("ed: unlock door with hammer");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertFalse(actionCommand.checkActionCommand(), "should be false as invalid subject");
        testServer.handleCommand("ed: lift trapdoor");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertFalse(actionCommand.checkActionCommand(), "should be false as invalid trigger");
    }
}
