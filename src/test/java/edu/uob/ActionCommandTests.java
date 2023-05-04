package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ActionCommandTests {
    GameServer testServer;
    Map map;
    ActionCommand actionCommand;
    public ActionCommandTests(){}
    @BeforeEach
    void setup() {
        File entitiesFile = Paths.get("config" + File.separator + "test2-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config" + File.separator + "test2-actions.xml").toAbsolutePath().toFile();
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
        testServer.handleCommand("ed: cut chop cut down chop tree");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertTrue(actionCommand.checkTriggers());
    }

    @Test
    void triggerTests3(){
        testServer.handleCommand("ed: cut down tree fight");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertFalse(actionCommand.checkTriggers());
    }

    @Test
    void setSubjectListTest(){
        testServer.handleCommand("ed: cut tree");
        ArrayList<String> commandWords = new ArrayList<>(List.of("axe", "tree"));
        actionCommand = testServer.getCommandHandler().getActionCommand();
        actionCommand.setSubjectList(commandWords);
        assertEquals("[axe, tree]",actionCommand.getSubjectList().toString());
    }

    @Test
    void subjectListTests(){
        testServer.handleCommand("ed: unlock trapdoor with key");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertFalse(actionCommand.actionCommandInvalid(), "should be false");
        List<String> subjectList = actionCommand.getSubjectList();
        assertEquals(2, subjectList.size(), "should contain 2 subjects");
        assertTrue(subjectList.contains("trapdoor") && subjectList.contains("key"));
        testServer.handleCommand("ed: unlock door with hammer");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertTrue(actionCommand.actionCommandInvalid(), "should be true as no subjects");
    }

    @Test
    void checkActionCommandTests(){
        testServer.handleCommand("ed: unlock trapdoor with key");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertFalse(actionCommand.actionCommandInvalid(), "should be true");
        testServer.handleCommand("ed: unlock door with hammer");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertTrue(actionCommand.actionCommandInvalid(), "should be false as invalid subject");
        testServer.handleCommand("ed: lift trapdoor");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertTrue(actionCommand.actionCommandInvalid(), "should be false as invalid trigger");
    }

    @Test
    void subjectNotInScopeTest(){
        testServer.handleCommand("ed: get coin");
        testServer.handleCommand("ed: open hole with elf");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertTrue(actionCommand.subjectsNotInScope());
        testServer.handleCommand("ed: open trapdoor with coin");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertFalse(actionCommand.subjectsNotInScope());
        testServer.handleCommand("ed: get potion");
        testServer.handleCommand("ed: drink potion with coin");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertFalse(actionCommand.subjectsNotInScope());
        testServer.handleCommand("ed: open trapdoor with axe");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertFalse(actionCommand.subjectsNotInScope());
        testServer.handleCommand("ed: open trapdoor with elf");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertTrue(actionCommand.subjectsNotInScope());
        testServer.handleCommand("ed: open hole with axe");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertTrue(actionCommand.subjectsNotInScope());
    }

    @Test
    void findGameActionTests(){
        testServer.handleCommand("ed: unlock trapdoor with key");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertFalse(actionCommand.gameActionNotFound());
        testServer.handleCommand("ed: unlock trapdoor with axe");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertTrue(actionCommand.gameActionNotFound());
        testServer.handleCommand("ed: unlock trapdoor with key and axe");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertTrue(actionCommand.gameActionNotFound());
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
        assertTrue(map.getCurrentLocation().hasPath("cellar"));
    }

    @Test
    void producedEntityTests2(){
        testServer.handleCommand("ed: get axe");
        testServer.handleCommand("ed: goto forest");
        testServer.handleCommand("ed: cut tree");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        actionCommand.produceEntity();
        assertEquals("log", actionCommand.getProducedEntity(), "produces log");
    }

    @Test
    void producedNullTest(){
        testServer.handleCommand("ed: fight elf");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertFalse(actionCommand.notHealthOrNull(""));
    }

    @Test
    void entityTests(){
        testServer.handleCommand("ed: goto forest");
        testServer.handleCommand("ed: get key");
        testServer.handleCommand("ed: goto cabin");
        testServer.handleCommand("ed: open trapdoor");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        assertTrue(actionCommand.getNarration().contains("door"), "should return narration");
        assertEquals("key",actionCommand.getConsumedEntity(),"key should be consumed");
        assertEquals("cellar",actionCommand.getProducedEntity(),"cellar should be produced");
    }

    @Test
    void handleHealthTests(){
        testServer.handleCommand("ed: cut tree");
        actionCommand = testServer.getCommandHandler().getActionCommand();
        PlayerCharacter ed = actionCommand.player;
        ed.decreaseHealth();
        ed.decreaseHealth();
        actionCommand.handleHealth("produce");
        assertEquals(2, ed.getHealth(),"ed should have 2 health");
        ed.decreaseHealth();
        actionCommand.handleHealth("consume");
        assertTrue(actionCommand.getNarration().contains("died"),"player should have died");
    }
}
