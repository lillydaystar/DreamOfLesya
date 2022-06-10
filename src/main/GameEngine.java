package main;

import graphics.GameWindow;

class GameEngine implements Runnable {

    private GameWindow window;

    private Thread gameThread;

    GameEngine() {
        this.window = new GameWindow();
    }

    @Override
    public void run() {
        while (this.gameThread != null) {
            update();
            paint();
        }
    }

    private void createGameThread() {
        this.gameThread = new Thread(this);
        this.gameThread.start();
    }

    private void update() {

    }

    private void paint() {

    }
}