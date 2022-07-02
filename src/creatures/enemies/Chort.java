package creatures.enemies;

import creatures.Creature;
import graphics.GameWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Chort extends Creature {

    private static final int DRAW_RATE = 10;
    private static final int MAX_HORIZONTAL_SPEED = 4;
    private static final int MAX_VERTICAL_SPEED = 4;

    private static BufferedImage left_fst, left_snd, right_fst, right_snd;
    private static Random random = new Random();

    //shows how long we do not need to change direction
    private static final int CHANGE_DIRECTION_RATE = 20;

    //counts while we do not need to change direction;
    private int direction_count = CHANGE_DIRECTION_RATE;

    static {
        loadImage();
    }

    public Chort(int x, int y) {
        super(x, y);
        this.solidArea = new Rectangle(2, 2, GameWindow.blockSize - 4, GameWindow.blockSize - 4);
    }

    @Override
    public void update() {
        if (direction_count == CHANGE_DIRECTION_RATE) {
            direction_count = -1;
            //reset velocities so that they are in range [-MAX_<>_SPEED, +MAX_<>_SPEED]
            this.velocityX = random.nextInt(2*MAX_HORIZONTAL_SPEED + 1) - MAX_HORIZONTAL_SPEED;
            this.velocityY = random.nextInt(2*MAX_VERTICAL_SPEED + 1) - MAX_VERTICAL_SPEED;
        }
        System.out.printf("%d %d\n", velocityX, velocityY);
        this.abscissa += this.velocityX;
        this.ordinate += this.velocityY;
        direction_count++;
    }

    @Override
    protected void wake() {

    }

    @Override
    public int getFigureWidth() {
        return GameWindow.blockSize;
    }

    @Override
    public int getFigureHeight() {
        return GameWindow.blockSize;
    }

    @Override
    protected BufferedImage getImage() {
        if (this.velocityX < 0) {
            if (this.draw_counter < getDrawRate())
                return Chort.left_fst;
            else
                return Chort.left_snd;
        } else {
            if (this.draw_counter < getDrawRate())
                return Chort.right_fst;
            else
                return Chort.right_snd;
        }
    }

    @Override
    protected int getDrawRate() {
        return  DRAW_RATE;
    }

    @Override
    public void leftCollision() {
        this.collideHorizontally();
    }

    @Override
    public void rightCollision() {
        this.collideHorizontally();
    }

    @Override
    public void upCollision() {
        this.collideVertically();
    }

    @Override
    public void downCollision() {
        this.collideVertically();
    }

    @Override
    protected int getVerticalSpeed() {
        return 0;
    }

    @Override
    protected int getHorizontalSpeed() {
        return 0;
    }

    private static void loadImage() {
        try {
            Chort.left_fst = ImageIO.read(new File("heroes/ChortL_1.png"));
            Chort.left_snd = ImageIO.read(new File("heroes/ChortL_2.png"));
            Chort.right_fst = ImageIO.read(new File("heroes/ChortR_1.png"));
            Chort.right_snd = ImageIO.read(new File("heroes/ChortR_2.png"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error while loading images for chort",
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}