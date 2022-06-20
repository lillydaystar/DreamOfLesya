package creatures.enemies;

import java.awt.*;

public abstract class Creature {

    private int velocityX;
    private int velocityY;

    private int abscissa;
    private int ordinate;

    public Creature(int x, int y) {
        this.abscissa = x;
        this.ordinate = y;
    }

    abstract int getDrawRate();

    public void update() {
        this.abscissa += velocityX;
        this.ordinate += velocityY;
    }

    public void draw(Graphics2D graph) {

    }

    public void collideHorizontally() {
        this.velocityX *= -1;
    }

    public void collideVertically() {
        this.velocityY *= -1;
    }
}