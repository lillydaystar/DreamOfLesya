package creatures;

import java.awt.image.BufferedImage;

public class Harakternyk extends Creature {

    public Harakternyk(int x, int y) {
        super(x, y);
    }

    @Override
    public void update() {

    }

    @Override
    public int getFigureWidth() {
        return 0;
    }

    @Override
    public int getFigureHeight() {
        return 0;
    }

    @Override
    protected BufferedImage getImage() {
        return null;
    }

    @Override
    protected int getDrawRate() {
        return 0;
    }

    @Override
    public void leftCollision() {

    }

    @Override
    public void rightCollision() {

    }

    @Override
    public void upCollision() {

    }

    @Override
    public void downCollision() {

    }

    @Override
    protected int getVerticalSpeed() {
        return 0;
    }

    @Override
    protected int getHorizontalSpeed() {
        return 0;
    }
}
