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
        File entitiesFile = Paths.get("config" + File.separator + "extended-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config" + File.separator + "extended-actions.xml").toAbsolutePath().toFile();
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
    void triggerTests2(){
        testServer.handleCommand("ed: cut down tree");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertEquals("cut down", actionCommand.getTrigger(),"trigger should be cut down");
    }

    @Test
    void subjectListTests(){
        testServer.handleCommand("ed: unlock trapdoor with key");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertFalse(actionCommand.isSubjectListEmpty(), "should be false");
        ArrayList<String> subjectList = actionCommand.getSubjectList();
        assertEquals(2, subjectList.size(), "should contain 2 subjects");
        assertTrue(subjectList.contains("trapdoor") && subjectList.contains("key"));
        testServer.handleCommand("ed: unlock door with hammer");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertTrue(actionCommand.isSubjectListEmpty(), "should be true as no subjects");
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

    @Test
    void subjectInScopeTest(){
        testServer.handleCommand("ed: get coin");
        testServer.handleCommand("ed: open hole with elf");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertFalse(actionCommand.subjectsInScope());
        testServer.handleCommand("ed: open trapdoor with coin");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertTrue(actionCommand.subjectsInScope());
        testServer.handleCommand("ed: get potion");
        testServer.handleCommand("ed: drink potion with coin");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertTrue(actionCommand.subjectsInScope());
        testServer.handleCommand("ed: open trapdoor with axe");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertTrue(actionCommand.subjectsInScope());
        testServer.handleCommand("ed: open trapdoor with elf");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertFalse(actionCommand.subjectsInScope());
        testServer.handleCommand("ed: open hole with axe");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertFalse(actionCommand.subjectsInScope());
    }

    @Test
    void findGameActionTests(){
        testServer.handleCommand("ed: unlock trapdoor with key");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertTrue(actionCommand.findGameAction());
        testServer.handleCommand("ed: unlock trapdoor with axe");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertFalse(actionCommand.findGameAction());
        testServer.handleCommand("ed: unlock trapdoor with key and axe");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertFalse(actionCommand.findGameAction());
    }

    @Test
    void consumedEntityTests(){
        testServer.handleCommand("ed: look");
        assertNull(map.getLocation("storeroom").containsEntity("potion"), "storeroom should not contain potion");
        testServer.handleCommand("ed: drink potion");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertEquals("potion",actionCommand.getConsumedEntity(), "potion should be stored as consumed entity");
        assertNull(map.getCurrentLocation().containsEntity("potion"), "current location should no longer contain the potion");
        assertEquals("artefacts", map.getLocation("storeroom").containsEntity("potion"),"storeroom should now contain potion");
    }

    @Test
    void producedEntityTests(){
        testServer.handleCommand("ed: goto forest");
        testServer.handleCommand("ed: get key");
        testServer.handleCommand("ed: goto cabin");
        testServer.handleCommand("ed: open trapdoor with key");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        actionCommand.produceEntity();
        assertTrue(map.getCurrentLocation().checkPaths("cellar"));
    }
}
