package creatures.player;

import graphics.GameWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Cossack {

    private int xCord = 8 * GameWindow.blockSize;
    private int yCord = GameWindow.screenHeight - 3*GameWindow.blockSize;

    private int xMap = xCord;

    private int xVel = 3, yVel = 0;
    public boolean flight;

    public boolean leftCommand, rightCommand/*, upCommand, downCommand*/;
    private BufferedImage left_fst, left_snd, right_fst, right_snd, on_place;

    private int counter;
    private static final int STATE_RATE = 20;
    private static final int JUMP_SPEED = -30;
    private static final int GRAVITY = 2;
    public boolean collision = false;

    public Cossack() {
        loadImage();
    }

    public void draw(Graphics2D graphics2D) {
        BufferedImage image = null;
        if ((leftCommand && rightCommand) || (!leftCommand && !rightCommand)) {
            image = this.on_place;
        } else if (leftCommand) {
            if (this.counter < STATE_RATE / 2)
                image = this.left_fst;
            else
                image = this.left_snd;
        } else {
            if (this.counter < STATE_RATE / 2)
                image = this.right_fst;
            else
                image = this.right_snd;
        }
        graphics2D.drawImage(image, xCord, yCord, GameWindow.blockSize, 2*GameWindow.blockSize, null);
        /*graphics2D.setColor(Color.white);
        graphics2D.fillRect(xCord, yCord, GameWindow.blockSize, GameWindow.blockSize);*/
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
        this.yVel = JUMP_SPEED;
        this.flight = true;
    }

    /*public void upPressed() {
        this.upCommand = true;
    }

    public void downPressed() {
        this.downCommand = true;
    }

    public void upReleased() {
        this.upCommand = false;
    }

    public void downReleased() {
        this.downCommand = false;
    }*/

    public int getX() {
        return this.xCord;
    }

    public int getY() {
        return this.yCord;
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

    public void stayOnSurface() {
        this.flight = false;
        this.yVel = 0;
    }

    public int getWorldX(){
        return this.xMap;
    }

    public void update() {
        if(!collision) {
            if (this.leftCommand) {
                if (xMap >= 0) {
                    if (xMap <= 8 * GameWindow.blockSize || xMap >= GameWindow.worldWidth - 10 * GameWindow.blockSize) {
                        this.xCord -= this.xVel;
                    }
                    this.xMap -= this.xVel;
                }
            }
            if (this.rightCommand) {
                if (xMap <= GameWindow.worldWidth - GameWindow.blockSize) {
                    if (xMap <= 8 * GameWindow.blockSize || xMap >= GameWindow.worldWidth - 10 * GameWindow.blockSize) {
                        this.xCord += this.xVel;  //якщо козак доходить до краю карти, він мусить дойти до кутка
                        //В інших випадках козак знаходиться тільки в центрі екрану
                    }
                    this.xMap += this.xVel;
                }
            }
        }
        /*if (this.downCommand) {
            if(yCord <= GameWindow.screenHeight - GameWindow.blockSize) {
                this.yCord += this.yVel;
            }
        }
        if (this.upCommand) {
            if(yCord >= 0) {
                this.yCord -= this.yVel;
            }
        }*/
        /*if (this.yCord >= GameWindow.screenHeight - 3*GameWindow.blockSize) {
            stayOnSurface();
            System.out.println("succes");
            this.yCord = GameWindow.screenHeight - 3*GameWindow.blockSize;
        }*/
        this.yCord += this.yVel;
        if (this.flight) {
            this.yVel += GRAVITY;
        }
        this.counter++;
        if (this.counter >= STATE_RATE)
            this.counter -= STATE_RATE;
    }

    private void loadImage() {
        try {
            this.left_fst = ImageIO.read(new File("heroes/CossackL_1.png"));
            this.left_snd = ImageIO.read(new File("heroes/CossackL(move2).png"));
            this.right_fst = ImageIO.read(new File("heroes/CossackR_1.png"));
            this.right_snd = ImageIO.read(new File("heroes/Cossack(move2).png"));
            this.on_place = ImageIO.read(new File("heroes/CossackS.png"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error while loading images for cossack",
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}