package graphics;

import creatures.*;
import creatures.enemies.*;
import creatures.params.Bonus;
import sound.Music;
import sound.Sound;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
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
    private int level;
    private List<Creature> creatures;
    public boolean game = true;
    private Clip UPA;
    private Clip bestiary;

    GamePanel(int level) {
        this.setPreferredSize(new Dimension(GameWindow.screenWidth, GameWindow.screenHeight));
        this.level = level;
        this.cossack = new Cossack();
        this.cossack.setHealth(level);
        this.dm = new DrawMap(level, this, cossack);
        creatures = new LinkedList<>();
        this.UPA = Sound.getClip(Music.Background_UPA);
        this.bestiary = Sound.getClip(Music.Bestiary_Background);
        Sound.setVolume(this.bestiary, 0.15f);
        Sound.setVolume(this.UPA, 0.1f);
        if (level != 5) {
            if (this.UPA != null) {
                this.UPA.start();
                this.UPA.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } else {
            if (this.bestiary != null) {
                this.bestiary.start();
                this.bestiary.loop(Clip.LOOP_CONTINUOUSLY);
            }
        }
        setBackgroundImage();
        this.addKeyListener(new KeyCommander());
        this.revalidate();
        //dm.setCossack(this.cossack); //для промальовування карти задаються координати козака
        //this.dm.setCossacksParams();
        try {
            Font customFont1 = Font.createFont(Font.TRUETYPE_FONT, new File("files/upheavtt.ttf"));
            Font customFont2 = Font.createFont(Font.TRUETYPE_FONT, new File("files/ka1.ttf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont1);
            ge.registerFont(customFont2);
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
        if(cossack.getFightMode() != 0){
            setFightAttributes(graphics2D);
        }
        dm.paintMap(graphics2D);
        for (Creature creature : this.dm.creatures) {
            creature.draw(graphics2D, cossack.getAbscissa(), cossack.getOrdinate(),
                    cossack.getScreenX(), cossack.getScreenY());
            if (creature.getState() == CreatureState.Dead && creature.getFigureHeight() == 0)
                this.dm.creatures.remove(creature);
        }
        cossack.draw(graphics2D);
        graphics2D.dispose();
        this.revalidate();
    }

    private void setFightAttributes(Graphics2D g) {
        g.setFont(new Font("Upheaval TT (BRK)", Font.BOLD, 23));
        String st = "F";
        FontMetrics f = g.getFontMetrics();

        int length = cossack.health.getHealthPoints()*GameWindow.blockSize + GameWindow.blockSize/2;
        BufferedImage shablia = null, knife = null;
        try {
            shablia = ImageIO.read(new File("images/Shablia.png"));
            knife = ImageIO.read(new File("images/Knife.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int y = GameWindow.blockSize - 8 + f.getHeight();
        if(cossack.getFightMode() == 1 || cossack.getFightMode() == 2) {
            if(!cossack.isShabliaAvailable()){
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5F));
            }
            g.drawImage(shablia, length, 0, GameWindow.blockSize - 8, GameWindow.blockSize - 8, null);
            int x1 = length + (GameWindow.blockSize - 8 - f.stringWidth(st)) / 2;
            g.drawString(st, x1, y);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F));
        }
        if(cossack.getFightMode() > 1){
            if(cossack.getKnife() != null){
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5F));
            }
            length += GameWindow.blockSize;
            g.drawImage(knife, length, 0, GameWindow.blockSize - 8, GameWindow.blockSize - 8, null);
            st = "G";
            int x2 = length + (GameWindow.blockSize - 8 - f.stringWidth(st))/2;
            g.drawString(st, x2, y);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F));
        }
    }

    void update() {
        if(!cossack.alive || cossack.win){
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stopMusic();
            game = false;
        }
        else {
            cossack.update();
            for (int i = 0; i < this.dm.creatures.size(); i++) {
                Creature creature = dm.creatures.get(i);
                dm.checkTile(creature);
                creature.update();
                if (creature.isFullyDead()) {
                    this.dm.creatures.remove(creature);
                    if (creature instanceof Harakternyk) {
                        if (this.dm.getLevel() == 2) {
                            Bonus sh1 = new Bonus(7, this.cossack, creature.getAbscissa()/GameWindow.blockSize + 1,
                                    creature.getOrdinate()/GameWindow.blockSize + 3);
                            this.dm.getBonuses().add(sh1);
                        }
                        if (this.dm.getLevel() == 4) {
                            Bonus sh2 = new Bonus(8, this.cossack, creature.getAbscissa()/GameWindow.blockSize + 1,
                                    creature.getOrdinate()/GameWindow.blockSize + 3);
                            this.dm.getBonuses().add(sh2);
                        }
                    }
                    i--;
                    if(dm.getLevel() == 5){
                        if(creature instanceof Chort) {
                            Viy v = (Viy) dm.creatures.get(0);
                            v.chortDie();
                        }
                    }
                }
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
        this.dm = new DrawMap(level, this, this.cossack);
        setBackgroundImage();
        if (level == 5) {
            stopMusic();
            if (this.bestiary != null) {
                this.bestiary.start();
                this.bestiary.loop(Clip.LOOP_CONTINUOUSLY);
            }
        }
        //dm.setCossack(this.cossack);
        //this.dm.setCossacksParams();
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

            if(key == KeyEvent.VK_ENTER || key == KeyEvent.VK_F){
                cossack.fight();
            }

            if(key == KeyEvent.VK_G){
                cossack.throwKnife();
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
                stopMusic();
                game = false;
            }
        }
    }

    private void stopMusic() {
        if (this.UPA != null) {
            this.UPA.stop();
        }
        if (this.bestiary != null) {
            this.bestiary.stop();
        }
    }
}