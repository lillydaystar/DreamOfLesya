package graphics;

import creatures.Creature;
import creatures.Cossack;
import creatures.CreatureState;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/*
 * This class draws the main game panel, where the map will be displayed
 * and all the characters will be painted.
 */
public class GamePanel extends JPanel {

    private DrawMap dm;
    private BufferedImage background;
    private Cossack cossack;
    /*private int level;*/
    private List<Creature> creatures;
    public boolean game = true;

    GamePanel(int level) {
        this.setPreferredSize(new Dimension(GameWindow.screenWidth, GameWindow.screenHeight));
        /*this.level = level;*/
        this.dm = new DrawMap(level, this);
        this.cossack = new Cossack();
        this.cossack.setHealth(level);
        creatures = new LinkedList<>();
        setBackgroundImage();
        this.addKeyListener(new KeyCommander());
        this.revalidate();
        dm.setCossack(this.cossack); //для промальовування карти задаються координати козака
        this.dm.setCossacksParams();
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("files/upheavtt.ttf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Toolkit.getDefaultToolkit().sync();
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.drawImage(background, 0, 0, getWidth(), getHeight(), null);
        graphics2D.setColor(Color.darkGray);
        graphics2D.fillRect(0,0, GameWindow.screenWidth, 3*GameWindow.blockSize/2);
        graphics2D.setPaint(Color.white);
        graphics2D.setFont(new Font("Upheaval TT (BRK)", Font.BOLD, 50));
        String s = "Coins: " + cossack.coins;
        FontMetrics fm = graphics2D.getFontMetrics();
        int x = GameWindow.screenWidth - fm.stringWidth(s) - 5;
        int y = fm.getHeight();
        graphics2D.drawString(s, x, y);
        dm.paintMap(graphics2D);
        cossack.draw(graphics2D);
        for (Creature creature : this.dm.creatures) {
            creature.draw(graphics2D, cossack.getWorldX(), cossack.getWorldY(),
                    cossack.getScreenX(), cossack.getScreenY());
            if (creature.getState() == CreatureState.Dead && creature.getFigureHeight() == 0)
                this.dm.creatures.remove(creature);
        }
        graphics2D.dispose();
        this.revalidate();
    }

    void update() {
        if(!cossack.alive){
            this.cossack.setSpeed(1);
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    game = false;
                }
            };
            java.util.Timer timer = new Timer();
            timer.schedule(timerTask, 4000);

        }
        else {
            cossack.update();
            for (Creature creature : this.dm.creatures) {
                dm.chackTile(creature);
                creature.update();
            }
        }
    }

    private void setBackgroundImage() {
        try {
            int l = dm.getLevel();
            switch (l){
                case 1:
                    this.background = ImageIO.read(new File("images/background1.png"));
                    break;
                case 2:
                    this.background = ImageIO.read(new File("images/background2.jpg"));
                    break;
                case 3:
                    this.background = ImageIO.read(new File("images/background3.jpg"));
                    break;
                case 4:
                    this.background = ImageIO.read(new File("images/background4.png"));
                    break;
                case 5:
                    this.background = ImageIO.read(new File("images/background5.png"));
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeLevel(int level) {
        this.cossack.setDefaultCoordinates();
        this.dm = new DrawMap(level, this);
        setBackgroundImage();
        dm.setCossack(this.cossack);
        this.dm.setCossacksParams();
    }

    private class KeyCommander implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
                cossack.rightPressed();
            }

            if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
                cossack.leftPressed();
            }

            if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_J) {
                cossack.jump();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
                cossack.rightReleased();
            }

            if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
                cossack.leftReleased();
            }

            if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_J) {
                cossack.jumpRelease();
            }
            if(key == KeyEvent.VK_ESCAPE){
                game = false;
            }
        }
    }
}