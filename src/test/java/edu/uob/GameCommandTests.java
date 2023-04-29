package edu.uob;

import com.sun.source.tree.AssertTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameCommandTests {

    GameCommand command;
    Map map;
    PlayerCharacter player;
    @BeforeEach
    void setup(){
        map = new Map();
        map.addPlayer("ed");
        player = map.getCurrentPlayer();
    }
    @Test
    void testInventoryToken(){
        command = new GameCommand("inventory inv INVENTORY INV Inventory Inv");
        command.handleCommand(map);
        for (Token token: command.commandTokens) {
            assertEquals(token.getTokenType(), TokenType.INVENTORY);
        }
    }

    @Test
    void testInventoryCommand(){
        command = new GameCommand("Inventory");
        assertTrue(player.getInventory().isEmpty());
        assertEquals(command.handleCommand(map), "Your inventory is empty");
        Artefact key = new Artefact();
        key.setDescription("A key");
        Artefact axe = new Artefact();
        axe.setDescription("A shiny axe");
        player.addItemToInventory("key", key);
        player.addItemToInventory("axe", axe);
        assertEquals(player.getInventory().size(), 2);
        command = new GameCommand("Inventory");
        String response2 = command.handleCommand(map);
        assertTrue(response2.contains("A key"));
        assertTrue(response2.contains("A shiny axe"));
    }

    @Test
    void testGetToken(){
        command = new GameCommand("get Get GET gEt");
        command.handleCommand(map);
        for (Token token: command.commandTokens) {
            assertEquals(token.getTokenType(), TokenType.GET);
        }
    }

    @Test
    void testDrop(){
        command = new GameCommand("drop Drop DROP dRop");
        command.handleCommand(map);
        for (Token token: command.commandTokens) {
            assertEquals(token.getTokenType(), TokenType.DROP);
        }
    }

    @Test
    void testGoto(){
        command = new GameCommand("goto Goto GOTO goTo");
        command.handleCommand(map);
        for (Token token: command.commandTokens) {
            assertEquals(token.getTokenType(), TokenType.GOTO);
        }
    }

    @Test
    void testLook(){
        command = new GameCommand("look Look LOOK loOk");
        command.handleCommand(map);
        for (Token token: command.commandTokens) {
            assertEquals(token.getTokenType(), TokenType.LOOK);
        }
    }
}
