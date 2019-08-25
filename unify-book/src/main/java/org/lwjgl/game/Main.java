package org.lwjgl.game;


import org.lwjgl.engine.GameEngine;
import org.lwjgl.engine.IGameLogic;

public class Main {

    public static void main(String[] args) {
        try {
            IGameLogic gameLogic = new DummyGame();
            GameEngine gameEng = new GameEngine("GAME", 600, 480, true, gameLogic);
            gameEng.start();
        } catch (Exception exp) {
            exp.printStackTrace();
            System.exit(-1);
        }
    }
}
