package creatures.enemies;

import graphics.GameWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Perelesnyk extends HorizontalEnemy {

    private static final int DRAW_RATE = 10;
    private static final int HORIZONTAL_SPEED = 5;
    private static BufferedImage left_fst, left_snd, right_fst, right_snd;

    static {
        loadImage();
    }

    public Perelesnyk(int x, int y) {
        super(x, y);
        super.solidArea = new Rectangle(1, 1, GameWindow.blockSize - 2, GameWindow.blockSize - 2);
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
                return Perelesnyk.left_fst;
            else
                return Perelesnyk.left_snd;
        } else {
            if (this.draw_counter < getDrawRate())
                return Perelesnyk.right_fst;
            else
                return Perelesnyk.right_snd;
        }
    }

    @Override
    protected int getDrawRate() {
        return DRAW_RATE;
    }

    @Override
    protected int getSpeed() {
        return HORIZONTAL_SPEED;
    }

    private static void loadImage() {
        try {
            Perelesnyk.left_fst = ImageIO.read(new File("heroes/PerelesnykL_1.png"));
            Perelesnyk.left_snd = ImageIO.read(new File("heroes/PerelesnykL_2.png"));
            Perelesnyk.right_fst = ImageIO.read(new File("heroes/PerelesnykR_1.png"));
            Perelesnyk.right_snd = ImageIO.read(new File("heroes/PerelesnykR_2.png"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error while loading images for perelesnyk",
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
