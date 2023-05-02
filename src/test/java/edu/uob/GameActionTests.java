package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameActionTests {
    GameAction gameAction;
    ArrayList<String> list;
    @BeforeEach
    void setup() {
        gameAction = new GameAction();
        list = new ArrayList<>();
    }

    @Test
    void setSubjectsTests(){
        gameAction.setSubjects(new String[]{"door", "key"});
        list.add(0, "door");
        assertTrue(gameAction.checkSubjects(list));
        list.set(0, "key");
        assertTrue(gameAction.checkSubjects(list));
        list.add("door");
        assertTrue(gameAction.checkSubjects(list));
        list.add("axe");
        assertFalse(gameAction.checkSubjects(list));
    }

    @Test
    void setConsumedTests(){
        gameAction.setConsumed("door");
        assertNotNull(gameAction.getConsumed(), "consumed should not be null");
        assertEquals("door", gameAction.getConsumed(), "Unexpected value");
    }

    @Test
    void setProducedTests(){
        gameAction.setProduced("cellar");
        assertNotNull(gameAction.getProduced(), "produced should not be null");
        assertEquals("cellar", gameAction.getProduced(), "Unexpected value");
    }

    @Test
    void setNarrationTests(){
        gameAction.setNarration("You open the door");
        assertNotNull(gameAction.getNarration(), "narration should not be null");
        assertEquals("You open the door", gameAction.getNarration(), "Unexpected value");
    }

}
