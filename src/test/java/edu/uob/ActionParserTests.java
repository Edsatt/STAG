package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ActionParserTests {
    ActionsList actionsList;
    ActionParser actionParser;
    NodeList actions;
    ArrayList<String> subjectList;
    Map map;
    GameAction gameAction;

    //The following tests all use values from an actionFile
    @BeforeEach
    void setup() {
        File actionsFile = Paths.get("config" + File.separator + "test1-actions.xml").toAbsolutePath().toFile();
        map = new Map();
        actionsList = map.getActionList();
        actionParser = new ActionParser(map, actionsFile);
        actionParser.parse();
        actions = actionParser.getActions();
        subjectList = new ArrayList<>();
    }

    @Test
    void parsingTests(){
        assertEquals(9, actionParser.getNumActions(), "Unexpected number of actions");
        assertNotNull(actionParser.getActions(), "action parser should not be null");
    }

    @Test
    void getTriggersTests(){
        actionParser.setActionElement((Element)actionParser.getActions().item(1));
        String [] response1 = actionParser.getElements("triggers", "keyphrase");
        assertEquals("open", response1[0], "unexpected response");
        assertEquals("unlock", response1[1], "unexpected response");
        actionParser.setActionElement((Element)actionParser.getActions().item(3));
        String [] response2 = actionParser.getElements("triggers", "keyphrase");
        assertEquals("chop", response2[0], "unexpected response");
        assertEquals("cut", response2[1], "unexpected response");
        assertEquals("cut down", response2[2], "unexpected response");
    }

    @Test
    void getSubjectsTests(){
        actionParser.setActionElement((Element)actionParser.getActions().item(5));
        String [] response1 = actionParser.getElements("subjects", "entity");
        assertEquals("potion", response1[0], "unexpected response");
        actionParser.setActionElement((Element)actionParser.getActions().item(7));
        String [] response2 = actionParser.getElements("subjects", "entity");
        assertEquals("elf", response2[0], "unexpected response");
    }

    @Test
    void getConsumedTests(){
        actionParser.setActionElement((Element)actionParser.getActions().item(3));
        String response1 = actionParser.getElement("consumed");
        assertEquals("tree", response1, "unexpected response");
        actionParser.setActionElement((Element)actionParser.getActions().item(7));
        String response2 = actionParser.getElement("consumed");
        assertEquals("health", response2, "unexpected response");
    }

    @Test
    void getProducedTests(){
        actionParser.setActionElement((Element)actionParser.getActions().item(1));
        String response1 = actionParser.getElement("produced");
        assertEquals("cellar", response1, "unexpected response");
        actionParser.setActionElement((Element)actionParser.getActions().item(7));
        String response2 = actionParser.getElement("produced");
        assertNull(response2, "response should be null as nothing is produced");
    }

    @Test
    void actionsListTests1(){
        assertTrue(actionsList.getActions().isEmpty(), "actionsList should be empty before creating actions");
        actionParser.createActions();
        assertFalse(actionsList.getActions().isEmpty(), "actionsList should not be empty");
        subjectList.addAll(List.of("trapdoor", "key"));
        gameAction = actionsList.getGameAction("open", subjectList);
        assertNotNull(gameAction, "gameAction should not be null");
        assertEquals(gameAction.getConsumed(), "key", "key should be consumed by this action");
    }

    @Test
    void actionsListTests2(){
        actionParser.createActions();
        assertFalse(actionsList.getActions().isEmpty(), "actionsList should not be empty");
        subjectList.addAll(List.of("door", "key"));
        gameAction = actionsList.getGameAction("open", subjectList);
        assertNull(gameAction, "gameAction should be null");
    }

    @Test
    void addTriggersToMapTests(){
        ArrayList<String> triggers = map.getTriggersList();
        assertTrue(triggers.isEmpty(), "triggers should be empty before creating actions");
        actionParser.createActions();
        triggers = map.getTriggersList();
        assertEquals(9, triggers.size(), "there should only be 9 trigger phrases in this XML");
        assertTrue(triggers.contains("unlock") && triggers.contains("cut down"), "expecting all keyphrases in triggers");
    }
}
