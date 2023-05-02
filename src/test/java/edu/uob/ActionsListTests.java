package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ActionsListTests {

    ActionsList actionsList;
    GameAction gameAction;
    ArrayList<String> list;
    @BeforeEach
    void setup() {
        actionsList = new ActionsList();
        list = new ArrayList<>();
    }

    @Test
    void addActionTests(){
        gameAction = new GameAction();
        assertTrue(actionsList.getActions().isEmpty(), "actionsList should be empty");
        actionsList.addAction("open", gameAction);
        assertEquals(1,actionsList.getActions().size(),"size should be one");
        gameAction = new GameAction();
        actionsList.addAction("open", gameAction);
        assertEquals(1,actionsList.getActions().size(),"size should still be one");
        assertEquals(2, actionsList.getAllActionsForTrigger("open").size(), "should hold 2 values");
        actionsList.addAction("unlock", gameAction);
        assertEquals(2,actionsList.getActions().size(),"size should be two");
    }

}
