package creatures;

import creatures.Creature;
import creatures.CreatureState;
import creatures.enemies.Chort;
import graphics.DrawMap;
import graphics.GameWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Viy extends Creature {

    private static final int DRAW_RATE = 20;
    private static BufferedImage closed_fst, closed_snd, opened_fst, opened_snd, a1, a2, a3, a4;
    private int healthPoints;
    private boolean takeDamage = false;
    private int countOfChorts;
    private static final ArrayList<Creature> chorts = new ArrayList<>();
    private final DrawMap dm;
    private int animation = 0;
    private ArrayList<Integer> waves = new ArrayList<>();

    static {
        loadImage();
        loadChorts();
    }

    public Viy(int x, int y, DrawMap dm) {
        super(x, y);
        super.solidArea = new Rectangle(1, 1, 4*GameWindow.blockSize - 2, 4*GameWindow.blockSize - 2);
        this.healthPoints = 100;
        this.state = CreatureState.OpenedEyes;
        this.countOfChorts = 13;
        this.dm = dm;
        this.waves.add(25);
        this.waves.add(50);
        this.waves.add(75);
    }

    /**
     * Method to put chorts on the map
     */
    public void spawnChorts() {
        this.countOfChorts = 1;
        reloadChorts();
        dm.addCreatures(chorts);
    }

    @Override
    public void update() {
        if(takeDamage){
            if(waves.contains(healthPoints)) {
                takeDamage = false;
                animation = 1;
                waves.remove((Integer) healthPoints);
            }
        }
        if(animation == 6) {
            closeEyes();
            animation = 0;
        }
        else if(animation == 5) {
            openEyes();
            animation = 0;
        }
        else if(countOfChorts == 0 && healthPoints > 0 && animation == 0 && this.state == CreatureState.OpenedEyes){
            animation = 4;
        }else if (healthPoints == 0){
            finallyDie();
        }
    }

    @Override
    public void draw(Graphics2D graph, int playerAbscissa, int playerOrdinate, int playerScreenX, int playerScreenY) {
        if(animation == 0) {
            super.draw(graph, playerAbscissa, playerOrdinate, playerScreenX, playerScreenY);
            return;
        }
        int enemyScreenAbscissa = playerScreenX + abscissa - playerAbscissa;
        int enemyScreenOrdinate = playerScreenY + ordinate - playerOrdinate;
        if(animation > 0 && animation < 5){
            if(animation == 1)
                drawAnimation(graph, enemyScreenAbscissa, enemyScreenOrdinate, a1);
            else if(animation == 2)
                drawAnimation(graph, enemyScreenAbscissa, enemyScreenOrdinate, a2);
            else if(animation == 3)
                drawAnimation(graph, enemyScreenAbscissa, enemyScreenOrdinate, a3);
            else
                drawAnimation(graph, enemyScreenAbscissa, enemyScreenOrdinate, a4);
        }
    }

    private void drawAnimation(Graphics2D graph, int enemyScreenAbscissa, int enemyScreenOrdinate, BufferedImage a) {
        graph.drawImage(a, enemyScreenAbscissa, enemyScreenOrdinate,
                getFigureWidth(), getFigureHeight(), null);
        ++draw_counter;
        if (this.draw_counter == 2 * getDrawRate()){
            this.draw_counter = 0;
            if(this.state == CreatureState.ClosedEyes)
                this.animation++;
            else if (this.state == CreatureState.OpenedEyes) {
                this.animation--;
                if(this.animation == 0) this.animation = 6;
            }
        }

    }

    private void closeEyes() {
        this.state = CreatureState.ClosedEyes;
        takeDamage = true;
    }

    @Override
    public void die() {
        if(takeDamage)
            getDamage();
    }

    @Override
    protected void wake() {

    }

    private void openEyes() {
        this.state = CreatureState.OpenedEyes;
        spawnChorts();
    }

    public void getDamage(){
        healthPoints-=5;
        if(healthPoints == 0){
            finallyDie();
        }
    }

    private void finallyDie() {
        this.state = CreatureState.Dead;
        dm.victory();
    }

    public int getHealthPoints() {
        return healthPoints;
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
        if (this.state == CreatureState.ClosedEyes) {
            if (this.draw_counter < getDrawRate())
                return Viy.closed_fst;
            else
                return Viy.closed_snd;
        } else if(this.state == CreatureState.OpenedEyes){
            if (this.draw_counter < getDrawRate())
                return Viy.opened_fst;
            else
                return Viy.opened_snd;
        }
        else return Viy.closed_fst;
    }

    public void chortDie(){
        this.countOfChorts--;
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
    protected int getSpeed() {
        return 0;
    }

    private static void loadImage() {
        try {
            Viy.closed_fst = ImageIO.read(new File("heroes/ViyC_1.png"));
            Viy.closed_snd = ImageIO.read(new File("heroes/ViyC_2.png"));
            Viy.opened_fst = ImageIO.read(new File("heroes/ViyO_5.png"));
            Viy.opened_snd = ImageIO.read(new File("heroes/ViyO_6.png"));
            Viy.a1 = ImageIO.read(new File("heroes/ViyO_1.png"));
            Viy.a2 = ImageIO.read(new File("heroes/ViyO_2.png"));
            Viy.a3 = ImageIO.read(new File("heroes/ViyO_3.png"));
            Viy.a4 = ImageIO.read(new File("heroes/ViyO_4.png"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error while loading images for viy",
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private static void reloadChorts(){
        chorts.set(0, new Chort(0,2*GameWindow.blockSize));
        chorts.set(1, new Chort(17*GameWindow.blockSize, 4*GameWindow.blockSize));
        chorts.set(2, new Chort(29*GameWindow.blockSize,3*GameWindow.blockSize));
        chorts.set(3, new Chort(5*GameWindow.blockSize, 4*GameWindow.blockSize));
        chorts.set(4, new Chort(31*GameWindow.blockSize,4*GameWindow.blockSize));
        chorts.set(5, new Chort(26*GameWindow.blockSize, 5*GameWindow.blockSize));
        chorts.set(6, new Chort(15*GameWindow.blockSize,6*GameWindow.blockSize));
        chorts.set(7, new Chort(GameWindow.blockSize, 7*GameWindow.blockSize));
        chorts.set(8, new Chort(32*GameWindow.blockSize,7*GameWindow.blockSize));
        chorts.set(9, new Chort(20*GameWindow.blockSize, 8*GameWindow.blockSize));
        chorts.set(10, new Chort(25*GameWindow.blockSize,9*GameWindow.blockSize));
        chorts.set(11, new Chort(33*GameWindow.blockSize, 10*GameWindow.blockSize));
        chorts.set(12, new Chort(17*GameWindow.blockSize,11*GameWindow.blockSize));
    }

    private static void loadChorts(){
        Chort ch1 = new Chort(0,0);

        for(int i=0; i<13; i++)
            chorts.add(ch1);

    }
}
