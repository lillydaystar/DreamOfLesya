package creatures;

import creatures.enemies.Knife;
import creatures.params.Health;
import graphics.*;
import sound.Music;
import sound.Sound;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
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

    private boolean collisionRight, collisionLeft, collisionAbove, collisionBelow;
    private boolean standLeft, standRight, leftCommand, rightCommand, jumpCommand, fightShCommand, fightKCommand;
    private boolean shabliaIsAvailable = false;
    private static BufferedImage left_fst, left_snd, right_fst, right_snd, on_place_right, on_place_left, jump_left, jump_right, dead, sh_left, sh_right;

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
    static final int INITIAL_PLAYER_ABSCISSE = 8 * GameWindow.blockSize;
    static final int INITIAL_PLAYER_ORDINATE = GameWindow.screenHeight - 3*GameWindow.blockSize;
    private static final int FIGURE_HEIGHT = 2*GameWindow.blockSize;
    private static final int FIGURE_WIDTH = GameWindow.blockSize;
    public boolean collision = false;
    public boolean fall = false;
    public int coins = 0;
    public boolean win = false;
    private boolean invincible = false;

    private Knife knife = null;
    public Health health;
    private int fightMode = 0;

    static {
        loadImage();
    }

    public Cossack() {
        setDefaultCoordinates();
        this.alive = true;
        this.solidArea = new Rectangle(2, 0, 20, 90);
        this.standRight = true;
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
        else if ((leftCommand && rightCommand) || (!leftCommand && !rightCommand && standRight)) {
            image = Cossack.on_place_right;
            width = GameWindow.blockSize;
            if(!onGround() || fall){
                image = Cossack.jump_right;
                width = 7*GameWindow.blockSize/4;
            }
            else if(fightShCommand){
                image = Cossack.sh_right;
                width = 7*GameWindow.blockSize/4;
            }
        } else if (!leftCommand && !rightCommand &&standLeft) {
            image = Cossack.on_place_left;
            width = GameWindow.blockSize;
            if(!onGround() || fall){
                image = Cossack.jump_left;
                width = 7*GameWindow.blockSize/4;
            }
            else if(fightShCommand){
                image = Cossack.sh_left;
                width = 7*GameWindow.blockSize/4;
                x -= GameWindow.blockSize;
            }
        }else if (leftCommand) {
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
        if(invincible){
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6F));
        }
        graphics2D.drawImage(image, x, y,
                width, height, null);
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F));
        health.drawHP(graphics2D);
        if(knife != null && knife.isExist()){
            knife.draw(graphics2D, this.getAbscissa(), this.getOrdinate(),
                    this.getScreenX(), this.getScreenY());
        }
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
        if(win){
            BufferedImage img = null;
            try{
                img = ImageIO.read(new File("images/Victory.png"));
            }catch (IOException e){
                e.printStackTrace();
            }
            graphics2D.drawImage(img, (GameWindow.screenWidth-750)/2, (GameWindow.screenHeight-250)/2, 750, 250, null);
        }
    }

    @Override
    public void die() {
        alive = false;
        Clip die_klip = Sound.getClip(Music.Cossack_Death);
        if (die_klip != null) {
            Sound.setVolume(die_klip, .1f);
            die_klip.start();
        }
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
        this.standLeft = false;
        this.standRight = false;
        if (!this.rightCommand) {
            this.rightCommand = true;
        }
    }

    public void leftPressed() {
        this.standLeft = false;
        this.standRight = false;
        if (!this.leftCommand) {
            this.leftCommand = true;
        }
    }

    public void rightReleased() {
        if (this.rightCommand) {
            this.rightCommand = false;
            this.standRight = true;
        }
    }

    public void leftReleased() {
        if (this.leftCommand) {
            this.leftCommand = false;
            this.standLeft = true;
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
        if((fightMode == 1 || fightMode == 2) && shabliaIsAvailable){
            fightShCommand = true;
            shabliaPunch();
        }
    }

    public void throwKnife() {
        if((fightMode == 2 || fightMode == 3) && this.knife == null){
            fightKCommand = true;
            knifePunch();
        }
    }

    private void knifePunch() {
        /*here should be a method to fight with enemies*/
        int path = 1;
        if(getMove()) path = -1;
        Clip knife_clip = Sound.getClip(Music.Knife_Sound);
        if (knife_clip != null) {
            knife_clip.start();
            knife_clip.loop(0);
        }
        this.knife = new Knife(this.xMap, this.yMap + GameWindow.blockSize/2, path);

        TimerTask timerTask = new TimerTask() {  //тимчасовий таймер для тестування бою
            @Override
            public void run() {
                fightKCommand = false;
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 500);
    }

    private void shabliaPunch() {
        /*here should be a method to fight with enemies*/
        Clip sabre_clip = Sound.getClip(Music.Sabre_Sound);
        if (sabre_clip != null) {
            sabre_clip.start();
            sabre_clip.loop(0);
        }
        shabliaIsAvailable = false;
        TimerTask timerTask = new TimerTask() {  //тимчасовий таймер для тестування бою
            @Override
            public void run() {
                fightShCommand = false;
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 320);

        TimerTask timerTask2 = new TimerTask() {  //тимчасовий таймер для тестування бою
            @Override
            public void run() {
                shabliaIsAvailable = true;
            }
        };
        Timer timer2 = new Timer();
        timer2.schedule(timerTask2, 2000);
    }

    /**
     *
     * @return if cossack moves to left return true, else return false
     */
    public boolean getMove(){
        return (leftCommand || standLeft) && !rightCommand;
    }

    public boolean isFightShCommand() {
        return fightShCommand;
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

    public int getAbscissa() {
        return this.xMap;
    }

    public int getOrdinate() {
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
        if ((this.leftCommand && this.rightCommand) || (!this.leftCommand && !this.rightCommand) || collision
                || fightShCommand || fightKCommand)
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

        if(knife != null && knife.isExist()){
            knife.update();
        }
        else if(knife != null && !knife.isExist()){
            knife = null;
        }
    }

    public boolean isInvincible() {
        return invincible;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    private static void loadImage() {
        try {
            Cossack.left_fst = ImageIO.read(new File("heroes/CossackL_1.png"));
            Cossack.left_snd = ImageIO.read(new File("heroes/CossackL(move2).png"));
            Cossack.right_fst = ImageIO.read(new File("heroes/CossackR_1.png"));
            Cossack.right_snd = ImageIO.read(new File("heroes/Cossack(move2).png"));
            Cossack.on_place_right = ImageIO.read(new File("heroes/CossackS.png"));
            Cossack.on_place_left = ImageIO.read(new File("heroes/CossackSL.png"));
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
        this.invincible = true;
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                invincible = false;
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 1000);
    }

    public void getDamage() {
        if(!invincible) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Interrupted exception!");
            }
            knife = null;
            shabliaIsAvailable = true;
            health.getDamage();
            if (health.dead) {
                die();
                return;
            }
            setDefaultCoordinates();
        }
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

    public Knife getKnife(){
        return knife;
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
    public Rectangle getSolidArea() {
        return this.solidArea;
    }

    @Override
    protected int getSpeed() {
        return HORIZONTAL_SPEED;
    }

    public void setSpeed(int i) {
        HORIZONTAL_SPEED += i*4;
    }

    public void setJump(int i) {
        JUMP_SPEED -= i*8;
    }

    protected void wake() {}

    public boolean isShabliaAvailable() {
        return shabliaIsAvailable;
    }

    public void setShabliaIsAvailable(boolean shabliaIsAvailable) {
        this.shabliaIsAvailable = shabliaIsAvailable;
    }
}