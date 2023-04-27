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
    @Test
    void testInventory(){
        command = new GameCommand("inventory inv INVENTORY INV Inventory Inv");
        for (Token token: command.commandTokens) {
            assertEquals(TokenType.INVENTORY, token.tokenType);
        }
    }

    @Test
    void testGet(){
        command = new GameCommand("get Get GET gEt");
        for (Token token: command.commandTokens) {
            assertEquals(TokenType.GET, token.tokenType);
        }
    }

    @Test
    void testDrop(){
        command = new GameCommand("drop Drop DROP dRop");
        for (Token token: command.commandTokens) {
            assertEquals(TokenType.DROP, token.tokenType);
        }
    }

    @Test
    void testGoto(){
        command = new GameCommand("goto Goto GOTO goTo");
        for (Token token: command.commandTokens) {
            assertEquals(TokenType.GOTO, token.tokenType);
        }
    }

    @Test
    void testLook(){
        command = new GameCommand("look Look LOOK loOk");
        for (Token token: command.commandTokens) {
            assertEquals(TokenType.LOOK, token.tokenType);
        }
    }
}
