package creatures.enemies;

import graphics.GameWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Poludnytsia extends WalkingEnemy {

    private static final int DRAW_RATE = 10;
    private static final int HORIZONTAL_SPEED = 3;
    private static BufferedImage left_fst, left_snd, right_fst, right_snd;

    static {
        loadImage();
    }

    public Poludnytsia(int x, int y) {
        super(x, y);
        super.solidArea = new Rectangle(1, 1, GameWindow.blockSize - 2, 2*GameWindow.blockSize - 2);
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
        return 2*GameWindow.blockSize;
    }

    @Override
    protected BufferedImage getImage() {
        if (this.velocityX < 0) {
            if (this.draw_counter < getDrawRate())
                return Poludnytsia.left_fst;
            else
                return Poludnytsia.left_snd;
        } else {
            if (this.draw_counter < getDrawRate())
                return Poludnytsia.right_fst;
            else
                return Poludnytsia.right_snd;
        }
    }

    private static void loadImage() {
        try {
            Poludnytsia.left_fst = ImageIO.read(new File("heroes/PoludnytsiaL_1.png"));
            Poludnytsia.left_snd = ImageIO.read(new File("heroes/PoludnytsiaL_2.png"));
            Poludnytsia.right_fst = ImageIO.read(new File("heroes/PoludnytsiaR_1.png"));
            Poludnytsia.right_snd = ImageIO.read(new File("heroes/PoludnytsiaR_2.png"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error while loading images for poludnytsia",
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}