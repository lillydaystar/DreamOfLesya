package creatures.player;

import graphics.GameWindow;

import java.awt.*;

public class Cossack {

    private int xCord, yCord;

    private int xVel = 2, yVel = 3;

    private boolean leftCommand, rightCommand, upCommand, downCommand;

    public Cossack() {

    }

    public Image getImage() {
        return null;
    }

    public void add(Graphics2D graphics2D) {
        correctPosition();
//        System.out.printf("%d %d", xCord, yCord);
        graphics2D.setColor(Color.white);
        graphics2D.fillRect(xCord, yCord, GameWindow.tileSize, GameWindow.tileSize);
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

    private void correctPosition() {
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