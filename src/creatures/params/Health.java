package creatures.params;

import graphics.GameWindow;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;

public class Health {

    private int healthPoints = 0;
    private final LinkedList<BufferedImage> hpImages;
    private final LinkedList<Integer> hpStatus;
    private boolean halfHPMode = false;
    public boolean dead = false;

    public Health(int level) {
        levelConfigs(level);
        hpImages = new LinkedList<>();
        hpStatus = new LinkedList<>();
        setFullHP();
    }

    public void levelConfigs(int level) {
        if (level == 5) {
            healthPoints = 5;
            if (hpStatus != null && hpImages != null) {
                hpStatus.clear();
                hpImages.clear();
                setFullHP();
            }
        } else if (level > 2 && hpStatus != null) {
            healthPoints = 3 + Collections.frequency(hpStatus, 3);
        } else
            healthPoints = 3;

        if (level > 2) {
            halfHPMode = true;
        }
    }

    private void setFullHP() {
        for (int i = 0; i < healthPoints; i++) {
            try {
                hpImages.add(ImageIO.read(new File("images/fullHP.png")));
                hpStatus.add(1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void drawHP(Graphics2D graphics2D) {
        int x, y;
        for (int i = 0; i < healthPoints; i++) {
            x = i * GameWindow.blockSize;
            y = GameWindow.blockSize / 4;
            graphics2D.drawImage(hpImages.get(i), x, y, GameWindow.blockSize, GameWindow.blockSize, null);
        }
    }

    public void addExtraLife() {
        if (Collections.frequency(hpStatus, 3) < 3) {
            healthPoints++;
            System.out.println("add points");
            try {
                hpImages.add(ImageIO.read(new File("images/Extra-life.png")));
                hpStatus.add(3);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addHP() {
        if (hpStatus.contains(2)) {
            int i = hpStatus.indexOf(2);
            if (hpStatus.contains(0)) {
                int j = hpStatus.indexOf(0);
                try {
                    hpStatus.set(i, 1);
                    hpImages.set(i, ImageIO.read(new File("images/fullHP.png")));
                    hpStatus.set(j, 2);
                    hpImages.set(j, ImageIO.read(new File("images/halfHP.png")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    hpStatus.set(i, 1);
                    hpImages.set(i, ImageIO.read(new File("images/fullHP.png")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (hpStatus.contains(0)) {
            int i = hpStatus.indexOf(0);
            try {
                hpStatus.set(i, 1);
                hpImages.set(i, ImageIO.read(new File("images/fullHP.png")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void getDamage() {
        int i;
        int tempStatus;
        for (i = healthPoints - 1; i >= 0; i--) {
            if (hpStatus.get(i) > 0)
                break;
        }
        if (i == -1) {
            dead = true;
            return;
        }
        try {
            tempStatus = hpStatus.get(i);
            if (halfHPMode) {
                if (tempStatus == 2) {
                    hpStatus.set(i, 0);
                    hpImages.set(i, ImageIO.read(new File("images/emptyHP.png")));
                    if (i == 0)
                        dead = true;
                } else if (tempStatus == 1) {
                    hpStatus.set(i, 2);
                    hpImages.set(i, ImageIO.read(new File("images/halfHP.png")));
                } else if (tempStatus == 3) {
                    hpStatus.remove(i);
                    hpImages.remove(i);
                    healthPoints--;
                }
            } else if (tempStatus == 1) {
                hpStatus.set(i, 0);
                hpImages.set(i, ImageIO.read(new File("images/emptyHP.png")));
                if (i == 0)
                    dead = true;
            } else if (tempStatus == 3) {
                hpStatus.remove(i);
                hpImages.remove(i);
                healthPoints--;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getHealthPoints() {
        return healthPoints;
    }
}