package creatures.enemies;

import java.awt.*;

public abstract class Creature {

    private int velocityX;
    private int velocityY;

    private int abscissa;
    private int ordinate;

    private Rectangle solidArea;
    private boolean collisionOn;
    private boolean isAlive;

    protected Creature() {

    }

    public Creature(int x, int y) {
        this.abscissa = x;
        this.ordinate = y;
    }

    public void update() {
        if (isAlive) {
            this.abscissa += velocityX;
            this.ordinate += velocityY;
        }
    }

    public void draw(Graphics2D graph) {

    }

    public void collideHorizontally() {
        this.velocityX *= -1;
    }

    public void collideVertically() {
        this.velocityY *= -1;
    }

    public abstract int getFigureWidth();

    public abstract int getFigureHeight();
}