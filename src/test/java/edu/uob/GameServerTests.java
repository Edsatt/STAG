package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class GameServerTests {
    GameServer testServer;

    @BeforeEach
    void setup(){
        File entitiesFile = Paths.get("config" + File.separator + "test1-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config" + File.separator + "test1-actions.xml").toAbsolutePath().toFile();
        testServer = new GameServer(entitiesFile, actionsFile);
    }

    @Test
    void usernameTests(){
       assertTrue(testServer.usernameCheck("ed"));
       assertFalse(testServer.usernameCheck("ed3"));
    }

    @Test
    void handleCommandTests(){
        assertTrue(testServer.handleCommand("").contains("Error"));
        assertTrue(testServer.handleCommand("ed: ").contains("Error"));
        assertTrue(testServer.handleCommand("ed: look").contains("cabin"));
    }
}
