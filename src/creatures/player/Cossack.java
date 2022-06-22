package creatures.player;

import creatures.enemies.Creature;
import graphics.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Cossack extends Creature {

    private int xCord;
    private int yCord;

    private int xMap;
    private int yMap;

    private int xVel, yVel;

    private boolean leftCommand, rightCommand, jumpCommand;
    private static BufferedImage left_fst, left_snd, right_fst, right_snd, on_place, jump_left, jump_right;

    private int counter;
    private static final int STATE_RATE = 20;
    private static final int JUMP_SPEED = -27;
    private static final int GRAVITY = 2;
    private static final int HORIZONTAL_SPEED = 3;
    private static final int SENTINEL_PLAYER_LEFT = 8 * GameWindow.blockSize;
    private static final int SENTINEL_PLAYER_RIGHT = GamePanel.worldWidth - 9 * GameWindow.blockSize;
    private static final int INITIAL_PLAYER_ABSCISSE = 8 * GameWindow.blockSize;
    private static final int INITIAL_PLAYER_ORDINATE = GameWindow.screenHeight - 3*GameWindow.blockSize;
    private static final int FIGURE_HEIGHT = 2*GameWindow.blockSize;
    private static final int FIGURE_WIDTH = GameWindow.blockSize;
    public boolean collision = false;
    public boolean fall = false;

    static {
        loadImage();
    }

    public Cossack() {
        this.xMap = INITIAL_PLAYER_ABSCISSE;
        this.yMap = INITIAL_PLAYER_ORDINATE;
        this.xVel = this.yVel = 0;
        this.xCord = xMap;
        this.yCord = yMap;
    }

    public void draw(Graphics2D graphics2D) {
        BufferedImage image;
        int width, height;
        height = 2*GameWindow.blockSize;
        if ((leftCommand && rightCommand) || (!leftCommand && !rightCommand)) {
            image = Cossack.on_place;
            width = GameWindow.blockSize;
            if(!onGround()){
                image = Cossack.jump_right;
                width = 72;
            }
        } else if (leftCommand) {
            if(!onGround()){
                image = Cossack.jump_left;
                width = 72;
            }
            else {
                if (this.counter < STATE_RATE)
                    image = Cossack.left_fst;
                else
                    image = Cossack.left_snd;
                width = GameWindow.blockSize;
            }
        } else {
            if(!onGround()){
                image = Cossack.jump_right;
                width = 72;
            }
            else {
                if (this.counter >= STATE_RATE)
                    image = Cossack.right_fst;
                else
                    image = Cossack.right_snd;
                width = GameWindow.blockSize;
            }
        }
        graphics2D.drawImage(image, getScreenX(), getScreenY(),
                width, height, null);
    }

    @Override
    public int getFigureWidth() {
        return FIGURE_WIDTH;
    }

    @Override
    public int getFigureHeight() {
        return FIGURE_HEIGHT;
    }

    public void rightPressed() {
        if (!this.rightCommand) {
            this.rightCommand = true;
        }
    }

    public void leftPressed() {
        if (!this.leftCommand) {
            this.leftCommand = true;
        }
    }

    public void rightReleased() {
        if (this.rightCommand) {
            this.rightCommand = false;
        }
    }

    public void leftReleased() {
        if (this.leftCommand) {
            this.leftCommand = false;
        }
    }

    public void jump() {
        if (!this.jumpCommand) {
            this.jumpCommand = true;
        }
    }

    public void jumpRelease() {
        if (this.jumpCommand) {
            this.jumpCommand = false;
        }
    }

    public int getX() {
        return this.xCord;
    }

    public int getY() {
        return this.yCord;
    }
    
    public void setY(int y) {
        this.yMap = y;
        this.yCord = y;

    }

    public void setVelocityY(int yVel) {
        this.yVel = yVel;
    }

    public int getVelocityY() {
        return yVel;
    }

    public int getWorldX() {
        return this.xMap;
    }

    public int getWorldY() {
        return this.yMap;
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
        return this.yMap;
    }

    public void update() {
        if (this.yMap >= GameWindow.screenHeight - 3*GameWindow.blockSize) {
            this.yVel = 0;
            this.yMap = GameWindow.screenHeight - 3*GameWindow.blockSize;
        }
        if (!onGround() || fall)
            this.yVel += GRAVITY;
        else if (this.jumpCommand)
            this.yVel = JUMP_SPEED;
        this.counter++;
        if (this.counter == 2*STATE_RATE)
            this.counter = 0;

        //якщо козак доходить до краю карти, він мусить дойти до кутка
        //В інших випадках козак знаходиться тільки в центрі екрану
        if ((this.leftCommand && this.rightCommand) || (!this.leftCommand && !this.rightCommand) || collision)
            this.xVel = 0;
        else if (this.leftCommand && xMap >= 0) {
            this.xVel = -HORIZONTAL_SPEED;
        }
        else if (this.rightCommand && xMap <= GamePanel.worldWidth - GameWindow.blockSize) {
            this.xVel = HORIZONTAL_SPEED;
        } else {
            this.xVel = 0;
        }
        this.xMap += this.xVel;
        this.yMap += this.yVel;
        this.xCord = getScreenX();
        this.yCord = getScreenY();
    }

    private static void loadImage() {
        try {
            Cossack.left_fst = ImageIO.read(new File("heroes/CossackL_1.png"));
            Cossack.left_snd = ImageIO.read(new File("heroes/CossackL(move2).png"));
            Cossack.right_fst = ImageIO.read(new File("heroes/CossackR_1.png"));
            Cossack.right_snd = ImageIO.read(new File("heroes/Cossack(move2).png"));
            Cossack.on_place = ImageIO.read(new File("heroes/CossackS.png"));
            Cossack.jump_left = ImageIO.read(new File("heroes/CossackJL.png"));
            Cossack.jump_right = ImageIO.read(new File("heroes/CossackJR.png"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error while loading images for cossack",
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public boolean onGround() {
        return this.yVel == 0;
    }

    public boolean isRightCommand() {
        return this.rightCommand;
    }

    public boolean isLeftCommand() {
        return this.leftCommand;
    }

    public boolean isJumpCommand() {
        return this.jumpCommand;
    }
}