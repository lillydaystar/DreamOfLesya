package main;

import graphics.GameWindow;

class GameEngine implements Runnable {

    private static final int CLOCK_RATE = 60;
    private static final long NANOSECOND_IN_SECOND = 1000000000L;

    private GameWindow window;

    private Thread gameThread;

    GameEngine() {
        this.window = new GameWindow();
        createGameThread();
    }

    @Override
    public void run() {
        double drawInterval = NANOSECOND_IN_SECOND / CLOCK_RATE;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        long count = 0;
        while (this.gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += currentTime - lastTime;
            lastTime = currentTime;
            if (delta >= 1) {
                update();
                repaint();
                delta--;
                count++;
            }
            if (timer >= NANOSECOND_IN_SECOND) {
                System.out.printf("%d FPS\n", count);
                count = 0;
                timer = 0;
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