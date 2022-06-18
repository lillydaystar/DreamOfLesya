package creatures.enemies;

import java.awt.*;

public abstract class Creature {

    private int velocityX;
    private int velocityY;

    private int abscissa;
    private int ordinate;

    abstract int getDrawRate();

    public void update() {

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