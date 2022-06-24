package creatures.player;

import graphics.Block;
import graphics.GameWindow;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Health {
    private int healthPoints = 0;
    private BufferedImage[] hpImages;
    private int[] hpStatus;
    private int level;
    private boolean halfHPMode = false;

    public Health(int level){
        this.level = level;
        if (level == 5) {
            healthPoints = 5;
        } else {
            healthPoints = 3;
        }
        hpImages = new BufferedImage[healthPoints];
        hpStatus = new int[healthPoints];
        for(int i=0; i<healthPoints; i++){
            try {
                hpImages[i] = ImageIO.read(new File("images/fullHP.png"));
                hpStatus[i] = 1;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void drawHP(Graphics2D graphics2D){
        int x, y;
        for(int i=0; i<healthPoints; i++){
            x = i* GameWindow.blockSize;
            y = GameWindow.blockSize/4;
            graphics2D.drawImage(hpImages[i], x, y, GameWindow.blockSize, GameWindow.blockSize, null);
        }
    }

    public void getDamage(){
        int i;
        for(i=healthPoints-1; i>=0; i--){
            if(hpStatus[i] > 0)
                break;
        }
        if(i == -1) {
            /*cossack.die();*/
            return;
        }
        try {
            if (halfHPMode) {
                if (hpStatus[i] == 2) {
                    hpStatus[i] = 0;
                    hpImages[i] = ImageIO.read(new File("images/emptyHP.png"));
                }
                else if (hpStatus[i] == 1) {
                    hpStatus[i] = 2;
                    hpImages[i] = ImageIO.read(new File("images/halfHP.png"));
                }
            } else if (hpStatus[i] == 1) {
                hpStatus[i] = 0;
                hpImages[i] = ImageIO.read(new File("images/emptyHP.png"));
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
