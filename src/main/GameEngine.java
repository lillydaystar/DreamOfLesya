package main;

import graphics.GameWindow;

class GameEngine implements Runnable {

    private static final int CLOCK_RATE = 60;
    private static final long NANOS_IN_SEC = 1000000000L;

    private GameWindow window;

    private Thread gameThread;

    GameEngine() {
        this.window = new GameWindow();
        createGameThread();
    }

    @Override
    public void run() {
        double drawInterval = NANOS_IN_SEC/CLOCK_RATE;
        double delta = 0;//delta is more then 1 if we need to repaint
        long lastTime = System.nanoTime();
        long currentTime;
        /*double timer = 0;
        int count = 0;*/
        while (this.gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            /*timer += currentTime - lastTime;*/
            lastTime = currentTime;
            if (delta >= 1) {
                update();
                repaint();
                delta--;
                /*count++;*/
            }
            /*if (timer >= NANOS_IN_SEC) {
                System.out.println("FPS "+count);
                count = 0;
                timer = 0;
            }*/
            /*try {
                Thread.sleep(CLOCK_RATE);
            } catch (InterruptedException e) {
                System.out.println("ERROR: Interrupted thread");
            }*/
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