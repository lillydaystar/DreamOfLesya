package graphics.bonus;

import creatures.Cossack;
import graphics.GameWindow;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Bonus {
    private boolean active;
    private final int type;
    private final Cossack cossack;
    private BufferedImage image;
    private int worldX, worldY;
    private int yVel, xVel;
    private boolean fall;
    private Thread bonusThread;

    public Bonus(int type, Cossack cossack, int blockCol, int blockRow){
        /*
        We will have 5 types of bonus, there will be:
            1. add hp - борщ у колбі
            2. speed+  -  магічні черевики
            3. extra-life  -  українське серце
            4. super-power  -  цвіт папороті
            5. jump higher  -  кропива
         */
        try {
            switch (type) {
                case 1:
                    image = ImageIO.read(new File("images/sunflower.png"));
                    break;
                case 2:
                    image = ImageIO.read(new File("images/SpeedUp.png"));
                    break;
                case 3:
                    image = ImageIO.read(new File("images/JumpHigher.png"));
                    break;
                case 4:
                    image = ImageIO.read(new File("images/Extra-life.png"));
                    break;
                case 5:
                    image = ImageIO.read(new File("images/AddHP.png"));
                    break;
                case 6:
                    image = ImageIO.read(new File("images/SuperPower.png"));
                    break;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        this.type = type;
        this.cossack = cossack;
        fallOut(blockCol, blockRow);
    }

    private void fallOut(int blockCol, int blockRow) {
        fall = true;
        yVel = -15;
        xVel = 5;
        worldX = blockCol * GameWindow.blockSize;
        worldY = (blockRow-1) * GameWindow.blockSize;
    }

    public boolean isFall() {
        return fall;
    }

    public void setFall(boolean fall) {
        this.fall = fall;
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getWorldX(){
        return this.worldX;
    }

    public int getWorldY() {
        return worldY;
    }

    public int getyVel() {
        return yVel;
    }

    public int getxVel() {
        return xVel;
    }

    public void setWorldX(int worldX) {
        this.worldX = worldX;
    }

    public void setWorldY(int worldY) {
        this.worldY = worldY;
    }

    public void setyVel(int yVel) {
        this.yVel = yVel;
    }

    public void setxVel(int xVel) {
        this.xVel = xVel;
    }

    public void activateBonus(){
        this.active = true;
        switchType();
    }

    public void inactivateBonus(){
        this.active = false;
        switchType();
    }

    /*Не працює на рівнях 1-3*/
    private void addHP(){
       this.cossack.health.addHP();
    }

    /*Працює на всіх рівнях*/
    private void speedUp(){
        if(active){
            this.cossack.setSpeed(1);
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    inactivateBonus();
                }
            };
            Timer timer = new Timer();
            timer.schedule(timerTask, 3000);
        }
        else{
            this.cossack.setSpeed(-1);
        }
    }

    /*Не працює на рівнях 1-2*/
    private void extraLife(){
        cossack.health.addExtraLife();
    }

    /*Не працює на рівнях 1-3*/
    private void superPower(){
        if(active){
            /*Бонус, який триває певний час. За цей час козак зможе збивати будь-якого ворога*/
            //метод для збивання (потрібні пророблені вороги)
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    inactivateBonus();
                }
            };
            Timer timer = new Timer();
            timer.schedule(timerTask, 4500);
        }
        else{
            //метод деактивації бонуса
        }
    }

    /*Не працює на рівні 1*/
    private void jumpHigher(){
        if(active){
            this.cossack.setJump(1);
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    inactivateBonus();
                }
            };
            Timer timer = new Timer();
            timer.schedule(timerTask, 4000);
        }
        else{
            this.cossack.setJump(-1);
        }
    }

    private void dropCoin(){
        cossack.coins++;
    }

    private void switchType(){
        switch (type){
            case 1:
                dropCoin();
                break;
            case 2:
                this.bonusThread = new Thread(this::speedUp);
                speedUp();
                break;
            case 3:
                this.bonusThread = new Thread(this::jumpHigher);
                jumpHigher();
                break;
            case 4:
                this.bonusThread = new Thread(this::extraLife);
                extraLife();
                break;
            case 5:
                this.bonusThread = new Thread(this::addHP);
                addHP();
                break;
            case 6:
                this.bonusThread = new Thread(this::superPower);
                superPower();
                break;
        }
    }
}
