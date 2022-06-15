package creatures.player;

import graphics.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Cossack {

    private int xCord;
    private int yCord;

    private int xMap;
    private int yMap;

    private int xVel, yVel;
    private boolean flight;

    private boolean leftCommand, rightCommand;

    private int counter;

    private static final int STATE_RATE = 20;
    private static final int JUMP_SPEED = -20;
    private static final int HORIZONTAL_SPEED = 3;
    private static final int SENTINEL_PLAYER_LEFT = 8 * GameWindow.blockSize;
    private static final int SENTINEL_PLAYER_RIGHT = GamePanel.worldWidth - 9 * GameWindow.blockSize;
    private static final int INITIAL_PLAYER_ABSCISSE = 8 * GameWindow.blockSize;
    private static final int INITIAL_PLAYER_ORDINATE = GameWindow.screenHeight - 3*GameWindow.blockSize;
    private static final int GRAVITY = 1;

    private static BufferedImage left_fst, left_snd, right_fst, right_snd, on_place;

    static {
        loadImage();
    }

    public Cossack() {
        this.xCord = INITIAL_PLAYER_ABSCISSE;
        this.yCord = INITIAL_PLAYER_ORDINATE;
        this.xVel = this.yVel = 0;
        this.xVel = 3;
        this.xMap = xCord;
    }

    public void draw(Graphics2D graphics2D) {
        BufferedImage image;
        if ((leftCommand && rightCommand) || (!leftCommand && !rightCommand)) {
            image = Cossack.on_place;
        } else if (leftCommand) {
            if (this.counter < STATE_RATE / 2)
                image = Cossack.left_fst;
            else
                image = Cossack.left_snd;
        } else {
            if (this.counter < STATE_RATE / 2)
                image = Cossack.right_fst;
            else
                image = Cossack.right_snd;
        }
        graphics2D.drawImage(image, xCord, yCord,
                GameWindow.blockSize, 2*GameWindow.blockSize, null);
    }

    public void rightPressed() {
        this.rightCommand = true;
    }

    public void leftPressed() {
        this.leftCommand = true;
    }

    public void rightReleased() {
        this.rightCommand = false;
    }

    public void leftReleased() {
        this.leftCommand = false;
    }

    public void jump() {
        if (!this.flight) {
            this.yVel = JUMP_SPEED;
            this.flight = true;
        }
    }

    public int getX() {
        return this.xCord;
    }

    public int getY() {
        return this.yCord;
    }

    public void stayOnSurface() {
        this.flight = false;
        this.yVel = 0;
    }

    public int getWorldX(){
        return this.xMap;
    }

    private int getScreenX() {
        if (this.xMap >= SENTINEL_PLAYER_LEFT && this.xMap <= SENTINEL_PLAYER_RIGHT)
            return INITIAL_PLAYER_ABSCISSE;
        else if (this.xMap < SENTINEL_PLAYER_LEFT)
            return this.xMap;
        else
            return GameWindow.screenWidth - GamePanel.worldWidth + this.xMap;
    }

    private int getScreenY() {
        return this.yCord;
    }

    public void update() {
        if (this.yCord >= GameWindow.screenHeight) {
            stayOnSurface();
            this.yCord = GameWindow.screenHeight - 2*GameWindow.blockSize;
        }
        this.yCord += this.yVel;
        if (this.flight) {
            this.yVel += GRAVITY;
        }
        this.counter++;
        if (this.counter >= STATE_RATE)
            this.counter -= STATE_RATE;

        //якщо козак доходить до краю карти, він мусить дойти до кутка
        //В інших випадках козак знаходиться тільки в центрі екрану
        if (this.leftCommand) {
            if(xMap >= 0) {
                this.xMap -= this.xVel;
            }
        }
        if (this.rightCommand) {
            if (xMap <= GamePanel.worldWidth - GameWindow.blockSize) {
                this.xMap += this.xVel;
            }
        }
        this.xCord = getScreenX();
    }

    private static void loadImage() {
        try {
            Cossack.left_fst = ImageIO.read(new File("heroes/CossackL_1.png"));
            Cossack.left_snd = ImageIO.read(new File("heroes/CossackL_2.png"));
            Cossack.right_fst = ImageIO.read(new File("heroes/CossackR_1.png"));
            Cossack.right_snd = ImageIO.read(new File("heroes/CossackR_2.png"));
            Cossack.on_place = ImageIO.read(new File("heroes/CossackS.png"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error while loading images for cossack",
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}