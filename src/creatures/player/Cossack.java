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
//||||||| merged common ancestors
//    private int xVel = 3, yVel = 0;
//    private boolean flight;
//=======
//    private int xVel = 3, yVel = 0;
//    public boolean flight;
//>>>>>>> master

    private boolean leftCommand, rightCommand, jumpCommand;
    private static BufferedImage left_fst, left_snd, right_fst, right_snd, on_place;

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
            if (this.counter < STATE_RATE)
                image = Cossack.left_fst;
            else
                image = Cossack.left_snd;
        } else {
            if (this.counter >= STATE_RATE)
                image = Cossack.right_fst;
            else
                image = Cossack.right_snd;
        }
        graphics2D.drawImage(image, xCord, yCord,
                GameWindow.blockSize, 2*GameWindow.blockSize, null);
//        System.out.printf("speed (%d, %d) coordinates (%d, %d), on map %d\n", xVel, yVel, xCord, yCord, xMap);
    }

    public void rightPressed() {
        if (!this.rightCommand) {
            this.rightCommand = true;
            System.out.println("right press");
        }
    }

    public void leftPressed() {
        if (!this.leftCommand) {
            this.leftCommand = true;
            System.out.println("left press");
        }
    }

    public void rightReleased() {
        if (this.rightCommand) {
            this.rightCommand = false;
            System.out.println("right release");
        }
    }

    public void leftReleased() {
        if (this.leftCommand) {
            this.leftCommand = false;
            System.out.println("left release");
        }
    }

    public void jump() {
        if (!this.jumpCommand) {
            this.jumpCommand = true;
            System.out.println("jump press");
        }
    }

    public void jumpRelease() {
        if (this.jumpCommand) {
            this.jumpCommand = false;
            System.out.println("jump release");
        }
    }

    public int getX() {
        return this.xCord;
    }

    public int getY() {
        return this.yCord;
    }

    public void stayOnSurface() {
        this.yVel = 0;
    }
    
    public void setY(int y) {
        this.yCord = y;
    }

    public void setVelocityY(int yVel){
        this.yVel = yVel;
    }

    public int getVelocityY() {
        return yVel;
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
        if (this.yCord >= GameWindow.screenHeight - FIGURE_HEIGHT) {
            stayOnSurface();
            this.yCord = GameWindow.screenHeight - FIGURE_HEIGHT;
        }
        if (!onGround())
            this.yVel += GRAVITY;
        else if (this.jumpCommand)
            this.yVel = JUMP_SPEED;
        this.counter++;
        if (this.counter == 2*STATE_RATE)
            this.counter = 0;

        //якщо козак доходить до краю карти, він мусить дойти до кутка
        //В інших випадках козак знаходиться тільки в центрі екрану
        if ((this.leftCommand && this.rightCommand) || (!this.leftCommand && !this.rightCommand))
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
        this.yCord += this.yVel;
        this.xCord = getScreenX();
    }

    private static void loadImage() {
        try {
            Cossack.left_fst = ImageIO.read(new File("heroes/CossackL_1.png"));
            Cossack.left_snd = ImageIO.read(new File("heroes/CossackL(move2).png"));
            Cossack.right_fst = ImageIO.read(new File("heroes/CossackR_1.png"));
            Cossack.right_snd = ImageIO.read(new File("heroes/Cossack(move2).png"));
            Cossack.on_place = ImageIO.read(new File("heroes/CossackS.png"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error while loading images for cossack",
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private boolean onGround() {
        return this.yVel == 0;
    }
}