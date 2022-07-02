package creatures.enemies;

import graphics.GameWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Knife extends HorizontalEnemy {

    private static final int SIZE = GameWindow.blockSize/2;
    private static final int DRAW_RATE = 10;
    private static final int HORIZONTAL_SPEED = 3;
    private boolean exist;
    private static BufferedImage fst;

    static {
        loadImage();
    }

    public Knife(int x, int y) {
        super(x, y);
        super.solidArea = new Rectangle(1, 1, SIZE - 2, SIZE - 2);
        exist = true;
    }

    @Override
    public void leftCollision() {
        fallApart();
    }

    public void fallApart() {
        exist = false;
    }

    public boolean isExist() {
        return exist;
    }

    @Override
    public void rightCollision() {
        fallApart();
    }

    @Override
    public int getFigureWidth() {
        return SIZE;
    }

    @Override
    public int getFigureHeight() {
        return SIZE;
    }

    @Override
    protected BufferedImage getImage() {
        return fst;
    }

    @Override
    protected int getDrawRate() {
        return DRAW_RATE;
    }

    @Override
    protected int getHorizontalSpeed() {
        return HORIZONTAL_SPEED;
    }

    @Override
    public boolean isAlive() {
        return false;
    }

    private static void loadImage() {
        try {
            Knife.fst = ImageIO.read(new File("images/Knife.png"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error while loading images for knife",
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
