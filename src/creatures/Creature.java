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

    //counts while we do not have to change character's image
    protected int draw_counter;

    //counts while we have to show character while it is disappearing
    private int dead_draw_counter;

    //shows how many times to draw character while it is disappearing
    protected final int DEAD_DRAWS = 15;

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
        if (this.state != CreatureState.Dead) {
            graph.drawImage(getImage(), enemyScreenAbscissa, enemyScreenOrdinate,
                    getFigureWidth(), getFigureHeight(), null);
            ++draw_counter;
            if (this.draw_counter == 2 * getDrawRate())
                this.draw_counter = 0;
            if (enemyScreenAbscissa >= 0 && enemyScreenAbscissa < GameWindow.screenWidth &&
                    enemyScreenOrdinate >= 0 && enemyScreenOrdinate < GameWindow.screenHeight)
                wake();
        } else {
            ++dead_draw_counter;
            int diff = dead_draw_counter*getFigureHeight()/DEAD_DRAWS;
            graph.drawImage(getImage(), enemyScreenAbscissa,
                    enemyScreenOrdinate + diff, getFigureWidth(),
                    getFigureHeight() - diff, null);
        }
    }

    public void die() {
        this.state = CreatureState.Dead;
    }

    public boolean isFullyDead() {
        return dead_draw_counter == DEAD_DRAWS;
    }

    protected abstract void wake();

    protected void collideHorizontally() {
        this.velocityX *= -1;
    }

    public void collideVertically() {
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

    public CreatureState getState() {
        return state;
    }

    public boolean isAlive() {
        return this.state == CreatureState.Alive;
    }
}