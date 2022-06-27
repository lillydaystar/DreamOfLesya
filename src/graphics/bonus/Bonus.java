package graphics.bonus;

import creatures.Cossack;
import graphics.GameWindow;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Bonus {
    private boolean active;
    private final int type;
    private final Cossack cossack;
    private BufferedImage image;
    private int worldX, worldY;
    private int yVel, xVel;
    private boolean fall;

    public Bonus(int type, Cossack cossack, int blockCol, int blockRow){
        /*
        We will have 5 types of bonus, there will be:
            1. Half-hp mode - борщ у колбі
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
                    image = ImageIO.read(new File("images/HalfHPMode.png"));
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

    /*Не працює на рівнях 1-2 і 5*/
    private void halfHP(){
        /*this.cossack.setHalfHPMode();*/
    }

    /*Працює на всіх рівнях*/
    private void speedUp(){
        /*if(active){
            this.cossack.setSpeed(1);
        }
        else{
            this.cossack.setSpeed(-1);
        }*/
    }

    /*Не працює на рівнях 1-2*/
    private void invulnerable(){
        if(active){
            /*Не отримує ушкоджень 1 раз*/
        }
        else{

        }
    }

    /*Не працює на рівнях 1-3*/
    private void superPower(){
        if(active){
            /*Бонус, який триває певний час. За цей час козак зможе збивати будь-якого ворога*/
        }
        else{

        }
    }

    /*Не працює на рівні 1*/
    private void jumpHigher(){
        if(active){

        }
        else{

        }
    }

    private void dropCoin(){

    }

    private void switchType(){
        switch (type){
            case 1:
                dropCoin();
                break;
            case 2:
                speedUp();
                break;
            case 3:
                jumpHigher();
                break;
            case 4:
                invulnerable();
                break;
            case 5:
                halfHP();
                break;
            case 6:
                superPower();
                break;
        }
    }
}
