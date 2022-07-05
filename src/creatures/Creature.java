package creatures;

import graphics.GameWindow;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public abstract class Creature {

    protected int velocityX;
    protected int velocityY;

    protected int abscissa;
    protected int ordinate;

    protected int playerXmap = Cossack.INITIAL_PLAYER_ABSCISSE;
    protected int playerYmap = Cossack.INITIAL_PLAYER_ORDINATE;

    protected boolean top_collision, bottom_collision, left_collision, right_collision;

    protected Rectangle solidArea;

    //counts while we do not have to change character's image
    protected int draw_counter;

    //counts while we have to show character while it is disappearing
    private int dead_draw_counter;

    //shows how many times to draw character while it is disappearing
    protected final int DEAD_DRAWS = 15;

    protected Random random;
    protected CreatureState state;

    Creature() {}

    public Creature(int x, int y) {
        this.abscissa = x;
        this.ordinate = y;
        this.state = CreatureState.Alive;
        this.random = new Random();
    }

    public abstract void update();

    public void draw(Graphics2D graph, int playerAbscissa, int playerOrdinate,
                     int playerScreenX, int playerScreenY) {
        int enemyScreenAbscissa = getScreenAbscissa(playerAbscissa, playerScreenX);
        int enemyScreenOrdinate = getScreenOrdinate(playerOrdinate, playerScreenY);
        this.playerXmap = playerAbscissa;
        this.playerYmap = playerOrdinate;
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
//            System.out.println(dead_draw_counter);
            int diff = dead_draw_counter*getFigureHeight()/DEAD_DRAWS;
            if(diff <= getFigureHeight()) {
                graph.drawImage(getImage(), enemyScreenAbscissa,
                        enemyScreenOrdinate + diff, getFigureWidth(),
                        getFigureHeight() - diff, null);
            }
        }
    }

    public void die() {
        this.state = CreatureState.Dead;
    }

    public boolean isFullyDead() {
        return dead_draw_counter >= DEAD_DRAWS ||
                this instanceof Harakternyk && this.state == CreatureState.Dead;
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

    protected abstract int getSpeed();

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
        if(this.state == CreatureState.ClosedEyes || this.state == CreatureState.OpenedEyes)
            return true;
        return this.state == CreatureState.Alive;
    }
}