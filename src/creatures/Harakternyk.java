package creatures;

import graphics.GameWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Harakternyk extends Creature {

    private static final int DRAW_RATE = 60;

    private static final float DISAPPEAR_PROBABILITY = 1f/2;

    private static Random rand;
    private static BufferedImage image_0, image_1, image_2, image_3, image_4, image_5, image_6, image_7;

    //counts when (and if) charakternyk is disappearing
    private int character_count;

    static {
        loadImage();
        rand = new Random();
    }

    public Harakternyk(int x, int y) {
        super(x, y);
        super.solidArea = new Rectangle(0, 0, 2*GameWindow.blockSize, 2*GameWindow.blockSize);
        super.state = CreatureState.Wait;
    }

    @Override
    public void update() {
        if (state == CreatureState.Disappearing) {
            if (draw_counter == DRAW_RATE) {
                draw_counter = 0;
                character_count++;
            }
            if (character_count == 8) {
                state = CreatureState.Wait;
                character_count = 0;
                move();
            }
        }
    }

    @Override
    public int getFigureWidth() {
        return 2*GameWindow.blockSize;
    }

    @Override
    public int getFigureHeight() {
        return 2*GameWindow.blockSize;
    }

    @Override
    protected BufferedImage getImage() {
        switch (character_count) {
            case 0:
                return image_0;
            case 1:
                return image_1;
            case 2:
                return image_2;
            case 3:
                return image_3;
            case 4:
                return image_4;
            case 5:
                return image_5;
            case 6:
                return image_6;
            case 7:
                return image_7;
            default:
                return null;
        }
    }

    @Override
    protected int getDrawRate() {
        return DRAW_RATE;
    }

    @Override
    public void leftCollision() { }

    @Override
    public void rightCollision() { }

    @Override
    public void upCollision() { }

    @Override
    public void downCollision() { }

    @Override
    protected int getVerticalSpeed() {
        return 0;
    }

    @Override
    protected int getHorizontalSpeed() {
        return 0;
    }

    @Override
    protected void wake() {
        if (state == CreatureState.Wait) {
            float choise = rand.nextFloat();
            if (choise <= DISAPPEAR_PROBABILITY)
                this.state = CreatureState.Disappearing;
            else
                this.state = CreatureState.Appearing;
            System.out.println(state);
            character_count = 0;
            draw_counter = 0;
        }
    }

    private void move() {

    }

    private static void loadImage() {
        try {
            Harakternyk.image_0 = ImageIO.read(new File("heroes/Harakternyk1.png"));
            Harakternyk.image_1 = ImageIO.read(new File("heroes/Harakternyk2.png"));
            Harakternyk.image_2 = ImageIO.read(new File("heroes/Harakternyk3.png"));
            Harakternyk.image_3 = ImageIO.read(new File("heroes/Harakternyk4.png"));
            Harakternyk.image_4 = ImageIO.read(new File("heroes/Harakternyk5.png"));
            Harakternyk.image_5 = ImageIO.read(new File("heroes/Harakternyk6.png"));
            Harakternyk.image_6 = ImageIO.read(new File("heroes/Harakternyk7.png"));
            Harakternyk.image_7 = ImageIO.read(new File("heroes/Harakternyk8.png"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error while loading images for haracternyk",
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}