package creatures.enemies;

import graphics.GameWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Potercha extends HorizontalEnemy {

    private static final int DRAW_RATE = 10;
    private static final int HORIZONTAL_SPEED = 4;
    private static BufferedImage left_fst, left_snd, right_fst, right_snd;

    static {
        loadImage();
    }

    public Potercha(int x, int y) {
        super(x, y);
        super.solidArea = new Rectangle(1, 1, GameWindow.blockSize - 2, GameWindow.blockSize - 2);
    }

    public int getDrawRate() {
        return DRAW_RATE;
    }

    @Override
    protected int getSpeed() {
        return HORIZONTAL_SPEED;
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
                return Potercha.left_fst;
            else
                return Potercha.left_snd;
        } else {
            if (this.draw_counter < getDrawRate())
                return Potercha.right_fst;
            else
                return Potercha.right_snd;
        }
    }

    private static void loadImage() {
        try {
            Potercha.left_fst = ImageIO.read(new File("heroes/PoterchaL_1.png"));
            Potercha.left_snd = ImageIO.read(new File("heroes/PoterchaL_2.png"));
            Potercha.right_fst = ImageIO.read(new File("heroes/PoterchaR_1.png"));
            Potercha.right_snd = ImageIO.read(new File("heroes/PoterchaR_2.png"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error while loading images for potercha",
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
