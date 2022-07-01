package creatures;

import creatures.Creature;
import graphics.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Cossack extends Creature {

    private int xCord;
    private int yCord;

    private int xMap;
    private int yMap;

    private int xVel, yVel;

    private boolean leftCommand, rightCommand, jumpCommand, fightShCommand, fightKCommand;
    private static BufferedImage left_fst, left_snd, right_fst, right_snd, on_place, jump_left, jump_right, dead, sh_left, sh_right;

    private int counter;
    public boolean alive;
    private int WORLD_WIDTH;
    private int WORLD_HEIGHT;
    private static final int STATE_RATE = 20;
    private static int JUMP_SPEED = -27;
    private static final int GRAVITY = 2;
    private static int HORIZONTAL_SPEED = 3;
    private static final int SENTINEL_PLAYER_LEFT = 8 * GameWindow.blockSize;
    private int SENTINEL_PLAYER_RIGHT = WORLD_WIDTH - 9 * GameWindow.blockSize;
    private static final int INITIAL_PLAYER_ABSCISSE = 8 * GameWindow.blockSize;
    private static final int INITIAL_PLAYER_ORDINATE = GameWindow.screenHeight - 3*GameWindow.blockSize;
    private static final int FIGURE_HEIGHT = 2*GameWindow.blockSize;
    private static final int FIGURE_WIDTH = GameWindow.blockSize;
    public boolean collision = false;
    public boolean fall = false;
    public int coins = 0;

    public Health health;
    private int fightMode = 0;

    static {
        loadImage();
    }

    public Cossack() {
        setDefaultCoordinates();
        this.alive = true;
    }

    public void draw(Graphics2D graphics2D) {
        BufferedImage image;
        int width, height;
        int x = getScreenX(), y = getScreenY();
        height = 2*GameWindow.blockSize;
        if(!alive){
            image = Cossack.dead;
            width = GameWindow.blockSize;
        }
        else if ((leftCommand && rightCommand) || (!leftCommand && !rightCommand)) {
            image = Cossack.on_place;
            width = GameWindow.blockSize;
            if(!onGround() || fall){
                image = Cossack.jump_right;
                width = 7*GameWindow.blockSize/4;
            }
            else if(fightShCommand){
                image = Cossack.sh_right;
                width = 7*GameWindow.blockSize/4;
            }
        } else if (leftCommand) {
            if(!onGround() || fall){
                image = Cossack.jump_left;
                width = 7*GameWindow.blockSize/4;
            }
            else if(fightShCommand){
                image = Cossack.sh_left;
                width = 7*GameWindow.blockSize/4;
                x -= GameWindow.blockSize;
            }
            else {
                if (this.counter < STATE_RATE)
                    image = Cossack.left_fst;
                else
                    image = Cossack.left_snd;
                width = GameWindow.blockSize;
            }
        } else {
            if(!onGround() || fall){
                image = Cossack.jump_right;
                width = 7*GameWindow.blockSize/4;
            }
            else if(fightShCommand){
                image = Cossack.sh_right;
                width = 7*GameWindow.blockSize/4;
            }
            else {
                if (this.counter >= STATE_RATE)
                    image = Cossack.right_fst;
                else
                    image = Cossack.right_snd;
                width = GameWindow.blockSize;
            }
        }
        graphics2D.drawImage(image, x, y,
                width, height, null);
        health.drawHP(graphics2D);
        if(!alive) {
            BufferedImage img = null;
            try{
                img = ImageIO.read(new File("images/GameOver.png"));
            }catch (IOException e){
                e.printStackTrace();
            }
            graphics2D.drawImage(img, (GameWindow.screenWidth-550)/2, (GameWindow.screenHeight-100)/2, 550, 100, null);
            die();
        }
    }

    private void die() {
        alive = false;
        /*add music*/
    }

    @Override
    public int getFigureWidth() {
        return FIGURE_WIDTH;
    }

    @Override
    public int getFigureHeight() {
        return FIGURE_HEIGHT;
    }

    public void rightPressed() {
        if (!this.rightCommand) {
            this.rightCommand = true;
        }
    }

    public void leftPressed() {
        if (!this.leftCommand) {
            this.leftCommand = true;
        }
    }

    public void rightReleased() {
        if (this.rightCommand) {
            this.rightCommand = false;
        }
    }

    public void leftReleased() {
        if (this.leftCommand) {
            this.leftCommand = false;
        }
    }

    public void jump() {
        if (!this.jumpCommand) {
            this.jumpCommand = true;
        }
    }

    public void jumpRelease() {
        if (this.jumpCommand) {
            this.jumpCommand = false;
        }
    }

    public void fight(){
        if(fightMode != 0){
            if(fightMode == 1){
                fightShCommand = true;
                shabliaPunch();
            }
            else if(fightMode == 2){
                fightKCommand = true;
                knifePunch();
            }
        }
    }

    private void knifePunch() {
        /*here should be a method to fight with enemies*/
    }

    private void shabliaPunch() {
        /*here should be a method to fight with enemies*/
        TimerTask timerTask = new TimerTask() {  //тимчасовий таймер для тестування бою
            @Override
            public void run() {
                fightShCommand = false;
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 500);
    }

    public int getX() {
        return this.xCord;
    }

    public int getY() {
        return this.yCord;
    }
    
    public void setY(int y) {
        this.yMap = y;
        this.yCord = y;

    }

    public void setVelocityY(int yVel) {
        this.yVel = yVel;
    }

    public int getVelocityY() {
        return yVel;
    }

    public int getWorldX() {
        return this.xMap;
    }

    public int getWorldY() {
        return this.yMap;
    }

    public int getScreenX() {
        if (this.xMap >= SENTINEL_PLAYER_LEFT && this.xMap <= SENTINEL_PLAYER_RIGHT)
            return INITIAL_PLAYER_ABSCISSE;
        else if (this.xMap < SENTINEL_PLAYER_LEFT)
            return this.xMap;
        else
            return GameWindow.screenWidth - WORLD_WIDTH + this.xMap;
    }

    public int getScreenY() {
        return this.yMap;
    }

    public void update() {
        if (this.yMap >= GameWindow.screenHeight - 3*GameWindow.blockSize) {
            this.yVel = 0;
            this.yMap = GameWindow.screenHeight - 3*GameWindow.blockSize;
        }
        if (!onGround() || fall)
            this.yVel += GRAVITY;
        else if (this.jumpCommand)
            this.yVel = JUMP_SPEED;
        this.counter++;
        if (this.counter == 2*STATE_RATE)
            this.counter = 0;

        //якщо козак доходить до краю карти, він мусить дойти до кутка
        //В інших випадках козак знаходиться тільки в центрі екрану
        if ((this.leftCommand && this.rightCommand) || (!this.leftCommand && !this.rightCommand) || collision || fightShCommand || fightKCommand)
            this.xVel = 0;
        else if (this.leftCommand && xMap >= 0) {
            this.xVel = -HORIZONTAL_SPEED;
        }
        else if (this.rightCommand && xMap <= WORLD_WIDTH - GameWindow.blockSize) {
            this.xVel = HORIZONTAL_SPEED;
        } else {
            this.xVel = 0;
        }
        this.xMap += this.xVel;
        this.yMap += this.yVel;
        this.xCord = getScreenX();
        this.yCord = getScreenY();
    }

    private static void loadImage() {
        try {
            Cossack.left_fst = ImageIO.read(new File("heroes/CossackL_1.png"));
            Cossack.left_snd = ImageIO.read(new File("heroes/CossackL(move2).png"));
            Cossack.right_fst = ImageIO.read(new File("heroes/CossackR_1.png"));
            Cossack.right_snd = ImageIO.read(new File("heroes/Cossack(move2).png"));
            Cossack.on_place = ImageIO.read(new File("heroes/CossackS.png"));
            Cossack.jump_left = ImageIO.read(new File("heroes/CossackJL.png"));
            Cossack.jump_right = ImageIO.read(new File("heroes/CossackJR.png"));
            Cossack.dead = ImageIO.read(new File("heroes/Cossack_dead.png"));
            Cossack.sh_left = ImageIO.read(new File("heroes/CossackShL.png"));
            Cossack.sh_right = ImageIO.read(new File("heroes/CossackShR.png"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error while loading images for cossack",
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public boolean onGround() {
        return this.yVel == 0;
    }

    public boolean isRightCommand() {
        return this.rightCommand;
    }

    public boolean isLeftCommand() {
        return this.leftCommand;
    }

    public boolean isJumpCommand() {
        return this.jumpCommand;
    }

    public void setHealth(int level) {
        this.health = new Health(level);
    }

    public void setWorldWidth(int worldWidth) {
        this.WORLD_WIDTH = worldWidth;
        this.SENTINEL_PLAYER_RIGHT = worldWidth - 9*GameWindow.blockSize;
    }

    public void setDefaultCoordinates() {
        this.xMap = INITIAL_PLAYER_ABSCISSE;
        this.yMap = INITIAL_PLAYER_ORDINATE;
        this.xVel = this.yVel = 0;
        this.xCord = xMap;
        this.yCord = yMap;
    }

    public void getDamage() {
        health.getDamage();
        if(health.dead){
            die();
            return;
        }
        setDefaultCoordinates();
    }

    /**
     * Method to let cossack fight with shablia or knives
     * @param i - new mode value
     */
    public void setFightMode(int i){
        this.fightMode = i;
    }

    /**
     * To find out whether cossack can fight by shablia or/and knives
     * @return cossack's fight mode value
     */
    public int getFightMode(){
        return this.fightMode;
    }

    public BufferedImage getImage() {
        return null;
    }

    @Override
    protected int getDrawRate() {
        return STATE_RATE;
    }

    @Override
    public void leftCollision() {
        //method from Creature abstract class;
        //Does nothing because Player's collision is
        //implemented in different way
    }

    @Override
    public void rightCollision() {
        //method from Creature abstract class;
        //Does nothing because Player's collision is
        //implemented in different way
    }

    @Override
    public void upCollision() {
        //method from Creature abstract class;
        //Does nothing because Player's collision is
        //implemented in different way
    }

    @Override
    public void downCollision() {
        //method from Creature abstract class;
        //Does nothing because Player's collision is
        //implemented in different way
    }

    @Override
    protected int getVerticalSpeed() {
        return 0;
    }

    @Override
    protected int getHorizontalSpeed() {
        return HORIZONTAL_SPEED;
    }

    public void setSpeed(int i) {
        HORIZONTAL_SPEED += i*4;
    }

    public void setJump(int i) {
        JUMP_SPEED -= i*8;
    }
}