package creatures;

import graphics.GameWindow;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Creature {

    protected int velocityX;
    protected int velocityY;

    protected int abscissa;
    protected int ordinate;

    protected Rectangle solidArea;
    protected int draw_counter;
    protected CreatureState state;

    Creature() {}

    public Creature(int x, int y) {
        this.abscissa = x;
        this.ordinate = y;
        this.state = CreatureState.Alive;
    }

    public abstract void update();

    public void draw(Graphics2D graph, int playerAbscissa, int playerOrdinate,
                     int playerScreenX, int playerScreenY) {
        int enemyScreenAbscissa = getScreenAbscissa(playerAbscissa, playerScreenX);
        int enemyScreenOrdinate = getScreenOrdinate(playerOrdinate, playerScreenY);
        graph.drawImage(getImage(), enemyScreenAbscissa, enemyScreenOrdinate,
                getFigureWidth(), getFigureHeight(), null);
        ++draw_counter;
        if (this.draw_counter == 2*getDrawRate())
            this.draw_counter = 0;
        if (enemyScreenAbscissa >= 0 && enemyScreenAbscissa < GameWindow.screenWidth &&
                enemyScreenOrdinate >= 0 && enemyScreenOrdinate < GameWindow.screenHeight)
            wake();
    }

    public void die() {
        this.state = CreatureState.Dead;
    }

    protected abstract void wake();

    protected void collideHorizontally() {
        this.velocityX *= -1;
    }

    protected void collideVertically() {
        this.velocityY *= -1;
    }

    public abstract int getFigureWidth();

    public abstract int getFigureHeight();


    /*This method returns correct image depending on this
    * creature and its state so each creature class must
    * redefine DRAW_RATE and implement this method
    * corresponding to its animation. The method getDrawRate()
    * also has to be implemented*/
    protected abstract BufferedImage getImage();

    protected abstract int getDrawRate();


    public abstract void leftCollision();

    public abstract void rightCollision();

    public abstract void upCollision();

    public abstract void downCollision();

    protected abstract int getVerticalSpeed();

    protected abstract int getHorizontalSpeed();

    private int getScreenAbscissa(int playerAbscissa, int playerScreenX) {
        return playerScreenX + abscissa - playerAbscissa;
    }

    private int getScreenOrdinate(int playerOrdinate, int playerScreenY) {
        return playerScreenY + ordinate - playerOrdinate;
    }

    public Rectangle getSolidArea() {
        return this.solidArea;
    }

    public int getVelocityX() {
        return this.velocityX;
    }

    public int getVelocityY() {
        return this.velocityY;
    }

    public int getAbscissa() {
        return this.abscissa;
    }

    public int getOrdinate() {
        return this.ordinate;
    }
}