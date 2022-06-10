package main;

import graphics.GameWindow;

class GameEngine implements Runnable {

    private GameWindow window;

    private Thread gameThread;

    GameEngine() {
        this.window = new GameWindow();
        createGameThread();
    }

    @Override
    public void run() {
        while (this.gameThread != null) {
            update();
            repaint();
        }
    }

    private void createGameThread() {
        this.gameThread = new Thread(this);
        this.gameThread.start();
    }

    private void update() {
        this.window.update();
    }

    private void repaint() {
        this.window.repaint();
    }
}