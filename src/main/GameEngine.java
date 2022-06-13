package main;

import graphics.GameWindow;

class GameEngine implements Runnable {

    private static final int CLOCK_RATE = 1000;

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
            try {
                Thread.sleep(CLOCK_RATE);
            } catch (InterruptedException e) {
                System.out.println("ERROR: Interrupted thread");
            }
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