package creatures.enemies;

import graphics.GameWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Lisovyk extends VerticalEnemy {

    private static final int DRAW_RATE = 10;
    private static final int VERTICAL_SPEED = 3;
    private static BufferedImage left_fst/*, left_snd*/, right_fst/*, right_snd*/;

    static {
        loadImage();
    }

    public Lisovyk(int x, int y) {
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
        if (this.velocityY < 0) {
            if (this.draw_counter < getDrawRate())
                return Lisovyk.left_fst;
            else
                return Lisovyk.left_fst;
        } else {
            if (this.draw_counter < getDrawRate())
                return Lisovyk.right_fst;
            else
                return Lisovyk.right_fst;
        }
    }

    @Override
    protected int getDrawRate() {
        return DRAW_RATE;
    }

    @Override
    protected int getVerticalSpeed() {
        return VERTICAL_SPEED;
    }

    private static void loadImage() {
        try {
            Lisovyk.left_fst = ImageIO.read(new File("heroes/LisovykL.png"));
//            Lisovyk.left_snd = ImageIO.read(new File("heroes/LisovykL_2.png"));
            Lisovyk.right_fst = ImageIO.read(new File("heroes/LisovykR.png"));
//            Lisovyk.right_snd = ImageIO.read(new File("heroes/LisovykR_2.png"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error while loading images for lisovyk",
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
