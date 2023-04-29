package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameCommandTests {

    GameCommand command;
    Map map = new Map();
    @Test
    void testInventory(){
        command = new GameCommand("inventory inv INVENTORY INV Inventory Inv");
        command.handleCommand(map);
        for (Token token: command.commandTokens) {
            assertEquals(token.getTokenType(), TokenType.INVENTORY);
        }
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
