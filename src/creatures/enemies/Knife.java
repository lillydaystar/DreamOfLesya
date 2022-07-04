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
    private static final int DRAW_RATE = 5;
    private static final int HORIZONTAL_SPEED = 8;
    private boolean exist;
    private static BufferedImage fst, snd, trd, frh;

    static {
        loadImage();
    }

    public Knife(int x, int y, int path) {
        super(x, y);
        super.solidArea = new Rectangle(1, 1, SIZE - 2, SIZE - 2);
        this.velocityX = path*this.getSpeed();
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

        if (this.draw_counter < getDrawRate()/4)
            return Knife.fst;
        else if (this.draw_counter < getDrawRate()/2)
            return Knife.snd;
        else if (this.draw_counter < getDrawRate())
            return Knife.trd;
        else
            return Knife.frh;

    }

    @Override
    protected int getDrawRate() {
        return DRAW_RATE;
    }

    @Override
    protected int getSpeed() {
        return HORIZONTAL_SPEED;
    }

    @Override
    public boolean isAlive() {
        return false;
    }

    private static BufferedImage rotateImage(BufferedImage buffImage, double angle) {
        double radian = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(radian));
        double cos = Math.abs(Math.cos(radian));

        int width = buffImage.getWidth();
        int height = buffImage.getHeight();

        int nWidth = (int) Math.floor((double) width * cos + (double) height * sin);
        int nHeight = (int) Math.floor((double) height * cos + (double) width * sin);

        BufferedImage rotatedImage = new BufferedImage(
                nWidth, nHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = rotatedImage.createGraphics();

        graphics.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        graphics.translate((nWidth - width) / 2, (nHeight - height) / 2);
        graphics.rotate(radian, (double) (width / 2), (double) (height / 2));
        graphics.drawImage(buffImage, 0, 0, null);
        graphics.dispose();

        return rotatedImage;
    }

    private static void loadImage() {
        try {
            Knife.fst = ImageIO.read(new File("images/Knife.png"));
            Knife.snd = rotateImage(Knife.fst, 90);
            Knife.trd = rotateImage(Knife.fst, 180);
            Knife.frh = rotateImage(Knife.fst, 270);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error while loading images for knife",
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
