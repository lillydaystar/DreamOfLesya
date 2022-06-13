package creatures.player;

import graphics.GameWindow;

import java.awt.*;

public class Cossack {

    private int xCord, yCord;

    private int xVel = 3, yVel = 2;

    private boolean leftCommand, rightCommand, upCommand, downCommand;

    public Cossack() {

    }

    public Image getImage() {
        return null;
    }

    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(Color.white);
        graphics2D.fillRect(xCord, yCord, GameWindow.blockSize, GameWindow.blockSize);
    }

    public void rightReleased() {
        this.rightCommand = false;
    }

    public void leftReleased() {
        this.leftCommand = false;
    }

    public void upReleased() {
        this.upCommand = false;
    }

    public void downReleased() {
        this.downCommand = false;
    }

    public void rightPressed() {
        this.rightCommand = true;
    }

    public void leftPressed() {
        this.leftCommand = true;
    }

    public void upPressed() {
        this.upCommand = true;
    }

    public void downPressed() {
        this.downCommand = true;
    }

    public int getX() {
        return this.xCord;
    }

    public int getY() {
        return this.yCord;
    }

    public void update() {
        if (this.downCommand) {
            this.yCord += this.yVel;
        }
        if (this.leftCommand) {
            this.xCord -= this.xVel;
        }
        if (this.upCommand) {
            this.yCord -= this.yVel;
        }
        if (this.rightCommand) {
            this.xCord += this.xVel;
        }
    }
}