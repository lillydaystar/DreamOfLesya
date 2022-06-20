package creatures.enemies;

import java.awt.*;

public class Mavka extends Creature {

    private int velocityX = 1;
    private int velocityY = 0;

    private static final int DRAW_RATE = 10;

    public Mavka(int x, int y) {
        super(x, y);
    }

    @Override
    public int getDrawRate() {
        return DRAW_RATE;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics2D graph) {

    }
}
