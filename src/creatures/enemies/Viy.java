package creatures.enemies;

import creatures.Creature;
import graphics.GameWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Viy extends Creature {

    private static final int DRAW_RATE = 20;
    private static BufferedImage closed_fst, closed_snd/*, opened_fst, opened_snd*/;
    private int healthPoints;
    private boolean takeDamage = false;
    private int countOfChorts;

    static {
        loadImage();
    }

    public Viy(int x, int y) {
        super(x, y);
        super.solidArea = new Rectangle(1, 1, 4*GameWindow.blockSize - 2, 4*GameWindow.blockSize - 2);
        this.healthPoints = 100;
        spawnChorts();
    }

    /**
     * Method to put chorts on the map
     */
    private void spawnChorts() {
    }

    @Override
    public void update() {
        if(countOfChorts == 0 && healthPoints > 0){
            openEyes();
            takeDamage = true;
            //таймер
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    spawnChorts();
                    closeEyes();
                }
            };
            java.util.Timer timer = new Timer();
            timer.schedule(timerTask, 4500);


        }else if (healthPoints == 0){
            die();
        }
    }

    private void closeEyes() {
    }

    @Override
    public void die() {
    }

    @Override
    protected void wake() {

    }

    private void openEyes() {

    }

    public void getDamage(){
        healthPoints -= 10;

    }

    public boolean canTakeDamage(){
        return takeDamage;
    }

    @Override
    public int getFigureWidth() {
        return 4*GameWindow.blockSize;
    }

    @Override
    public int getFigureHeight() {
        return 4*GameWindow.blockSize;
    }

    @Override
    protected BufferedImage getImage() {
        //if (this.velocityX < 0) {
            if (this.draw_counter < getDrawRate())
                return Viy.closed_fst;
            else
                return Viy.closed_snd;
        /*} else {
            if (this.draw_counter < getDrawRate())
                return Viy.opened_fst;
            else
                return Viy.opened_snd;
        }*/
    }

    @Override
    protected int getDrawRate() {
        return DRAW_RATE;
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

    private static void loadImage() {
        try {
            Viy.closed_fst = ImageIO.read(new File("heroes/ViyC_1.png"));
            Viy.closed_snd = ImageIO.read(new File("heroes/ViyC_2.png"));
            /*Viy.opened_fst = ImageIO.read(new File("heroes/ViyC_1.png"));
            Viy.opened_snd = ImageIO.read(new File("heroes/ViyC_2.png"));*/
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error while loading images for viy",
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
