package graphics;

import creatures.params.Bonus;
import creatures.enemies.*;

import creatures.*;
import sound.Music;
import sound.Sound;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DrawMap {

    private Block[] blocks;
    private LinkedList<Bonus> bonuses = new LinkedList<>();
    private char[][] map;
    private ArrayList<Character> marks = new ArrayList<>();
    List<Creature> creatures;
    private File mapFile;
    private Cossack cossack;
    private int level;
    private GamePanel panel;
    private int cols_on_map, rows_on_map;

    DrawMap(int level, GamePanel panel, Cossack cossack) {
        this.panel = panel;
        this.blocks = new Block[10];
        blocks[0] = new Block();
        blocks[0].collision = true;
        blocks[1] = new Block();
        blocks[1].collision = true;
        blocks[2] = new Block();
        blocks[2].collision = true;
        blocks[3] = new Block();
        blocks[3].collision = true;
        blocks[4] = new Block();
        blocks[5] = new Block();
        blocks[6] = new Block();
        blocks[6].collision = true;
        marks.add('A');
        marks.add('T');
        marks.add('Q');
        marks.add('B');
        marks.add('+');
        marks.add('H');
        marks.add('W');
        this.creatures = new ArrayList<>();
        this.level = level;
        setCossack(cossack);
        loadMap();
    }

    private void loadMap() {
        try {
            blocks[4].image = ImageIO.read(new File("images/sunflower.png"));
            switch (level){
                case 1:
                    this.mapFile = new File("worlds/map1.txt");
                    blocks[0].image = ImageIO.read(new File("images/Grass.jpg"));
                    blocks[1].image = ImageIO.read(new File("images/TreeV.jpg"));
                    blocks[2].image = ImageIO.read(new File("images/TreeQ.png"));
                    blocks[3].image = ImageIO.read(new File("images/TreeB.png"));
                    blocks[5].image = ImageIO.read(new File("images/house1.png"));
                    break;
                case 2:
                    this.mapFile = new File("worlds/map2.txt");
                    blocks[0].image = ImageIO.read(new File("images/Plank.jpg"));
                    blocks[1].image = ImageIO.read(new File("images/BookShelf.jpg"));
                    blocks[2].image = ImageIO.read(new File("images/BookQ.png"));
                    blocks[3].image = ImageIO.read(new File("images/BookB.png"));
                    blocks[5].image = ImageIO.read(new File("images/Door.png"));
                    break;
                case 3:
                    this.mapFile = new File("worlds/map3.txt");
                    blocks[0].image = ImageIO.read(new File("images/Field.jpg"));
                    blocks[1].image = ImageIO.read(new File("images/Hay.jpg"));
                    blocks[2].image = ImageIO.read(new File("images/HayQ.png"));
                    blocks[3].image = ImageIO.read(new File("images/HayB.png"));
                    blocks[5].image = ImageIO.read(new File("images/Cave.png"));
                    break;
                case 4:
                    this.mapFile = new File("worlds/map4.txt");
                    blocks[0].image = ImageIO.read(new File("images/Mud.jpg"));
                    blocks[1].image = ImageIO.read(new File("images/SeaWeed.png"));
                    blocks[2].image = ImageIO.read(new File("images/SeaWeedQ.png"));
                    blocks[3].image = ImageIO.read(new File("images/SeaWeedB.png"));
                    blocks[5].image = ImageIO.read(new File("images/Cave.png"));
                    blocks[6].image = ImageIO.read(new File("images/Water.jpg"));
                    break;
                case 5:
                    this.mapFile = new File("worlds/map5.txt");
                    blocks[0].image = ImageIO.read(new File("images/DarkGrass.jpg"));
                    blocks[1].image = ImageIO.read(new File("images/DarkTree.png"));
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        configMap();
    }

    private void configMap() {
        FileReader fr;
        try {
            fr = new FileReader(mapFile);
            BufferedReader br = new BufferedReader(fr);

            int cols = 0;
            int rows = 0;

            String format = br.readLine();
            String[] characteristics = format.split(" ");
            if (characteristics.length != 3)
                throw new IllegalArgumentException("Неправильний формат карти (форматна стрічка)");
            int spriteNumber = Integer.valueOf(characteristics[2]);
            this.rows_on_map = Integer.valueOf(characteristics[0]);
            this.cols_on_map = Integer.valueOf(characteristics[1]);
            this.map = new char[cols_on_map][rows_on_map+1];
            while(rows < GameWindow.rowsOnScreen) {
                String s = br.readLine();
                String[] str = s.split("");
                for(; cols < map.length; cols++) {
                    map[cols][rows] = str[cols].charAt(0);
                }
                cols = 0;
                rows++;
            }
            for (int cnt = 0; cnt < spriteNumber; cnt++) {
                String spriteFormat = br.readLine();
                String[] spriteCharacteristics = spriteFormat.split(" ");
                if (spriteCharacteristics.length != 3)
                    throw new IllegalArgumentException("Неправильний формат карти (характеристики ворогів)");
                addCreature(spriteCharacteristics);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setCossacksParams();
    }


    private void setCossack(Cossack cossack){
        this.cossack = cossack;
        if(level > 1)
            this.cossack.health.levelConfigs(level);
    }

    int getLevel(){
        return this.level;
    }

    public void addCreatures(ArrayList<Creature> creatures){
        this.creatures.addAll(creatures);
        this.cossack.setDefaultCoordinates();
    }

    void paintMap(Graphics2D g) {
        int col = 0; //порядковий номер стовпця на карті
        int row = 0; //номер рядка
        int x;
        int y = 0;

        while (col < map.length && row < GameWindow.rowsOnScreen){
            char block = map[col][row];
            int number = marks.indexOf(block);
            x = col * GameWindow.blockSize - this.cossack.getAbscissa() + cossack.getX();
            //Координата кожного блоку визначається за його позицією на загальній карті.
            //Коли рухається персонаж - рухається і карта, а за нею змінюються порядок зчитування карти
            if(number != -1 && number != 5) {
                if((col * GameWindow.blockSize - GameWindow.blockSize < this.cossack.getAbscissa() + this.cossack.getX() + 2 * GameWindow.blockSize||
                        col * GameWindow.blockSize - GameWindow.blockSize < 18 * GameWindow.blockSize) &&  //промальовування карти обмежене розміром вікна
                        col * GameWindow.blockSize + GameWindow.blockSize > this.cossack.getAbscissa() - this.cossack.getX())     //для пришвидшення обробки інформації
                    g.drawImage(blocks[number].image, x, y, GameWindow.blockSize, GameWindow.blockSize, null);
            } else if(number == 5) {
                int hX, hY, width, height;
                if(level == 1){
                    hX = x - 5*GameWindow.blockSize/2;
                    hY = y-GameWindow.blockSize*4;
                    width = GameWindow.blockSize*6;
                    height = GameWindow.blockSize*5;
                }
                else if(level == 2){
                    hX = x - GameWindow.blockSize*2;
                    hY = y-GameWindow.blockSize*3 + GameWindow.blockSize/2;
                    width = GameWindow.blockSize*4;
                    height = GameWindow.blockSize*4;
                }else{
                    hX = x - GameWindow.blockSize;
                    hY = y-GameWindow.blockSize*4;
                    width = GameWindow.blockSize*6;
                    height = GameWindow.blockSize*5;
                }
                g.drawImage(blocks[number].image, hX, hY,
                        width, height, null);
            }
            col++;
            if(col == map.length) {
                col = 0;
                row++;
                y += GameWindow.blockSize;
            }
        }
        if (!bonuses.isEmpty()) {
            for(Bonus bonus : bonuses)
                drawBonus(g, bonus);
        }
        checkCollision();
        if(level == 5){
            bossHealthPanel(g);
        }
    }

    private void bossHealthPanel(Graphics2D g) {
        Viy v;
        if(!creatures.isEmpty() && creatures.get(0) instanceof Viy)
            v = (Viy) creatures.get(0);
        else return;
        int hp = v.getHealthPoints();
        BufferedImage healthPointsPanel = null;
        BufferedImage hp1 = null, hp2 = null, hp3 = null;
        int hpWidth = 500, hpHeight = 20;

        try{
            healthPointsPanel = ImageIO.read(new File("images/BossHp.png"));
            if(hp == 100){
                hp3 = ImageIO.read(new File("images/Bhp3.png"));
            }
            if(hp > 0){
                hp1 = ImageIO.read(new File("images/Bhp1.png"));
            }
            if(hp > 1){
                hp2 = ImageIO.read(new File("images/Bhp2.png"));
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        int width = 720;
        int height = 48;
        int x = (GameWindow.screenWidth - width)/2, y = GameWindow.screenHeight - height;
        int hpX = 119 + x, hpY = y + 16;
        g.drawImage(healthPointsPanel, x, y,
                width, height, null);
        if(hp1 != null){
            g.drawImage(hp1, hpX, hpY, hpWidth, hpHeight, null);
        }
        if(hp2 != null){
            g.drawImage(hp2, hpX+5, hpY, hpWidth*hp/100 , hpHeight, null);
        }
        if(hp3 != null){
            g.drawImage(hp3, hpX, hpY, hpWidth, hpHeight, null);
        }
        g.setFont(new Font("Karmatic Arcade", Font.BOLD, 23));
        g.drawString(String.valueOf(hp), x + width - 90, y + 30);
    }

    private void checkCollision() {
        int rectLeftX = this.cossack.getAbscissa() + this.cossack.getSolidArea().x;
        int rectRightX = this.cossack.getAbscissa() + this.cossack.getFigureWidth() - this.cossack.getSolidArea().x;
        int rectTopY = this.cossack.getOrdinate() + this.cossack.getSolidArea().y;
        double rectBottomY = this.cossack.getY() + 2*GameWindow.blockSize - 1;

        int leftCol = rectLeftX/GameWindow.blockSize;
        int rightCol = rectRightX/GameWindow.blockSize;
        int topRow = rectTopY/GameWindow.blockSize;
        int bottomRow = (int)Math.round(rectBottomY/GameWindow.blockSize);

        if (this.cossack.isLeftCommand()) {
            changeCollision(leftCol, bottomRow, topRow);
            fall(leftCol+1, leftCol, bottomRow, 'l');
        }
        else if (this.cossack.isRightCommand()) {
            changeCollision(rightCol, bottomRow, topRow);
            fall(rightCol-1, rightCol, bottomRow, 'r');
        }
        if(!cossack.onGround()) {
            char block1, block2;
            if(cossack.getVelocityY() < 0) {
                block1 = map[rightCol][topRow];
                block2 = map[leftCol][topRow];
                if(cossack.getY() <= GameWindow.blockSize){
                    this.cossack.setVelocityY(0);
                    this.cossack.fall = true;
                }
                else if (block1 != '0' && blocks[marks.indexOf(block1)].collision) {
                    this.cossack.setVelocityY(0);
                    this.cossack.fall = true;
                    if(checkBlock(rightCol, topRow))
                        checkForBonus(rightCol, bottomRow);
                } else if (block2 != '0' && blocks[marks.indexOf(block2)].collision) {
                    this.cossack.setVelocityY(0);
                    this.cossack.fall = true;
                    if(checkBlock(leftCol, topRow))
                        checkForBonus(leftCol,bottomRow);
                }
            }
            if(cossack.getVelocityY() > 0) {
                block1 = map[rightCol][bottomRow];
                block2 = map[leftCol][bottomRow];
                checkForBonus(rightCol,bottomRow);
                checkForBonus(leftCol,bottomRow);
                if (block1 != '0' && blocks[marks.indexOf(block1)].collision) {
                    cossack.setVelocityY(0);
                    this.cossack.setY(bottomRow*GameWindow.blockSize - 2*GameWindow.blockSize);
                    this.cossack.fall = false;
                    fall(rightCol-1, rightCol, bottomRow, 'r');
                }
                else if (block2 != '0' && blocks[marks.indexOf(block2)].collision) {
                    cossack.setVelocityY(0);
                    this.cossack.setY(bottomRow*GameWindow.blockSize - 2*GameWindow.blockSize);
                    this.cossack.fall = false;
                    fall(leftCol-1, leftCol, bottomRow, 'l');
                }
            }
        }
    }

    private void checkForBonus(int col, int row) {
        if (map[col][row] == '+') {
            cossack.coins++;
            map[col][row] = '0';
        }
        if(map[col][row] == '0' && !bonuses.isEmpty()){
            for (Bonus bonus : bonuses) {
                if (bonus != null && bonus.getWorldX() / GameWindow.blockSize == col
                        && bonus.getWorldY() / GameWindow.blockSize == row) {
                    bonus.activateBonus();
                    bonuses.remove(bonus);
                    break;
                }
            }
        }
    }

    private boolean checkBlock(int col, int row) {
        if(map[col][row] == 'Q'){
            map[col][row] = 'B';
            /*method for bonus*/
            throwBonus(col, row);
        }
        return map[col][row] != 'Q';
    }

    private void throwBonus(int col, int row) {
        int bonusNum = randomNumber();
        Bonus b = new Bonus(bonusNum, this.cossack, col, row);
        bonuses.add(b);
    }

    private void drawBonus(Graphics2D graphics2D, Bonus bonus){
        bonus.setWorldX(bonus.getWorldX()+bonus.getxVel());
        bonus.setWorldY(bonus.getWorldY()+bonus.getyVel());

        if(bonus.isFall()){
            bonus.setyVel(bonus.getyVel()+2);
            if(map[bonus.getWorldX()/GameWindow.blockSize + 1][bonus.getWorldY()/GameWindow.blockSize] != '0'
                    && map[bonus.getWorldX()/GameWindow.blockSize + 1][bonus.getWorldY()/GameWindow.blockSize] != '+'){
                bonus.setFall(false);
                bonus.setxVel(0);
                bonus.setyVel(0);
                bonus.setWorldY((bonus.getWorldY()/GameWindow.blockSize - 1)*GameWindow.blockSize);
            }
        }

        if((bonus.getWorldX() - GameWindow.blockSize < this.cossack.getAbscissa() + this.cossack.getX() + 2 * GameWindow.blockSize ||
                bonus.getWorldX() * GameWindow.blockSize - GameWindow.blockSize < 18 * GameWindow.blockSize) &&
                bonus.getWorldX() * GameWindow.blockSize + GameWindow.blockSize > this.cossack.getAbscissa() - this.cossack.getX()) {
            int x = bonus.getWorldX() - this.cossack.getAbscissa() + cossack.getX();
            int y = bonus.getWorldY();
            graphics2D.drawImage(bonus.getImage(), x, y,
                    GameWindow.blockSize, GameWindow.blockSize, null);
        }
    }

    private int randomNumber() {
        int random = (int) Math.floor(Math.random() * 100) + 1; // 1..100
        switch (level){
            case 1:
                if(random < 51) return 1;
                else return 2;
            case 2:
                if(random < 46) return 1;
                else if(random < 76) return 2;
                else return 3;
            case 3:
                if(random < 41) return 1;
                else if(random < 56) return 2;
                else if (random < 71) return 3;
                else if(random < 86) return 4;
                else return 5;
            case 4:
                if(random < 36) return 1;
                else if(random < 50) return 2;
                else if (random < 64) return 3;
                else if(random < 78) return 4;
                else if (random < 92) return 5;
                else return 6;
            case 5:
                if(random < 21) return 1;
                else if(random < 37) return 2;
                else if (random < 53) return 3;
                else if(random < 69) return 4;
                else if (random < 85) return 5;
                else return 6;
            default:
                return 1;
        }
    }

    private void changeCollision(int col, int bottomRow, int topRow) {
        char block1, block2;
        block1 = map[col][bottomRow-1];
        block2 = map[col][topRow];
        if (block1 == 'H' || block2 == 'H') {
            panel.changeLevel(++this.level);
        }
        else if (block1 != '0') {
            this.cossack.collision = blocks[marks.indexOf(block1)].collision;
            if (!this.cossack.collision)
                checkForBonus(col, bottomRow-1);
        }
        else if (block2 != '0') {
            this.cossack.collision = blocks[marks.indexOf(block2)].collision;
            if(!this.cossack.collision)
                checkForBonus(col, topRow);
        }
        else {
            this.cossack.collision = false;
            checkForBonus(col, bottomRow-1);
        }
    }

    private void fall(int prevCol, int col, int bottomRow, char lORr){
        char prevBlock, block;
        if(col < map.length)
            block = map[col][bottomRow];
        else block = map[prevCol][bottomRow];
        if(prevCol >=0 && prevCol < map.length)
            prevBlock = map[prevCol][bottomRow];
        else prevBlock = block;
        if ((block == '0' || !blocks[marks.indexOf(block)].collision) && !cossack.isJumpCommand()) {
            if(prevBlock == '0' || !blocks[marks.indexOf(prevBlock)].collision)
                this.cossack.fall = true;
        }
        else if(cossack.getY() >= GameWindow.screenHeight - 3*GameWindow.blockSize - 2) {
            int rectX;
            if(lORr == 'l') {
                rectX = this.cossack.getAbscissa() + GameWindow.blockSize/4;
            }
            else {
                rectX = this.cossack.getAbscissa() + this.cossack.getFigureWidth() - GameWindow.blockSize/4;
            }
            int realCol = rectX/GameWindow.blockSize;
            if(map[realCol][bottomRow] == 'W') {
                cossack.getDamage();
            }
        }
        if(cossack.getKnife() != null && cossack.getKnife().isExist()) {
            checkTile(cossack.getKnife());
        }
    }

    void checkTile(Creature creature) {
        int creatureLeftWorldX = creature.getAbscissa() + creature.getSolidArea().x;
        int creatureRightWorldX = creatureLeftWorldX + creature.getSolidArea().width;
        int creatureTopWorldY = creature.getOrdinate() + creature.getSolidArea().y;
        int creatureBottomWorldY = creatureTopWorldY + creature.getSolidArea().height;

        int creatureLeftCol = creatureLeftWorldX/GameWindow.blockSize;
        int creatureRightCol = round((float)creatureRightWorldX/GameWindow.blockSize);
        int creatureTopRow = creatureTopWorldY/GameWindow.blockSize;
        int creatureBottomRow = round((float)creatureBottomWorldY/GameWindow.blockSize);

        if (creatureLeftCol < 0 || creatureRightCol < 0)
            creature.leftCollision();
        else if (creatureRightCol >= this.cols_on_map || creatureLeftCol >= this.cols_on_map)
        creature.rightCollision();
        else if (creatureBottomRow >= this.rows_on_map || creatureTopRow >= this.rows_on_map)
            creature.downCollision();
        else if (creatureTopRow <= 0 || creatureBottomRow <= 0)
            creature.upCollision();
        else {
            char tileNum1, tileNum2;

            if (creature.getVelocityY() > 0) {
                int moveCreatureBottomRow = (creatureBottomWorldY + creature.getVelocityY()) / GameWindow.blockSize;
                if (moveCreatureBottomRow >= this.rows_on_map)
                    creature.downCollision();
                else {
                    tileNum1 = this.map[creatureLeftCol][moveCreatureBottomRow];
                    tileNum2 = this.map[creatureRightCol][moveCreatureBottomRow];
                    if ((tileNum1 != '0' && this.blocks[marks.indexOf(tileNum1)].collision) ||
                            (tileNum2 != '0' && this.blocks[marks.indexOf(tileNum2)].collision))
                        creature.downCollision();
                }
            }
            if (creature.getVelocityY() < 0) {
                int moveCreatureTopRow = (creatureTopWorldY + creature.getVelocityY()) / GameWindow.blockSize;
                if (moveCreatureTopRow <= 0)
                    creature.upCollision();
                else {
                    tileNum1 = this.map[creatureLeftCol][moveCreatureTopRow];
                    tileNum2 = this.map[creatureRightCol][moveCreatureTopRow];
                    if ((tileNum1 != '0' && this.blocks[marks.indexOf(tileNum1)].collision) ||
                            (tileNum2 != '0' && this.blocks[marks.indexOf(tileNum2)].collision))
                        creature.upCollision();
                }
            }
            if (creature.getVelocityX() > 0) {
                int moveCreatureRightCol = (creatureRightWorldX + creature.getVelocityX()) / GameWindow.blockSize;
                if (moveCreatureRightCol >= this.cols_on_map)
                    creature.rightCollision();
                else {
                    tileNum1 = this.map[moveCreatureRightCol][creatureTopRow];
                    tileNum2 = this.map[moveCreatureRightCol][creatureBottomRow];
                    if ((tileNum1 != '0' && this.blocks[marks.indexOf(tileNum1)].collision) ||
                            (tileNum2 != '0' && this.blocks[marks.indexOf(tileNum2)].collision)) {
                        creature.rightCollision();
                    }
                }
            } else if (creature.getVelocityX() < 0) {
                int moveCreatureLeftCol = (creatureLeftWorldX + creature.getVelocityX()) / GameWindow.blockSize;
                if (moveCreatureLeftCol < 0)
                    creature.leftCollision();
                else {
                    tileNum1 = this.map[moveCreatureLeftCol][creatureTopRow];
                    tileNum2 = this.map[moveCreatureLeftCol][creatureBottomRow];
                    if ((tileNum1 != '0' && this.blocks[marks.indexOf(tileNum1)].collision) ||
                            (tileNum2 != '0' && this.blocks[marks.indexOf(tileNum2)].collision)) {
                        creature.leftCollision();
                    }
                }
            }
        }
        if(!(creature instanceof Knife))
            checkPlayer(creature, creatureRightWorldX, creatureLeftWorldX, creatureBottomWorldY, creatureTopWorldY);
    }

    private void checkPlayer(Creature creature, int creatureRightWorldX,
                             int creatureLeftWorldX, int creatureBottomWorldY, int creatureTopWorldY) {
        Rectangle player = this.cossack.getSolidArea();
        Rectangle truePlayer = new Rectangle(this.cossack.getAbscissa() + player.x,
                this.cossack.getOrdinate() + player.y, player.width, player.height);

        Rectangle fightArea = null;
        if(!cossack.getMove() && cossack.isFightShCommand()){
            fightArea = new Rectangle(this.cossack.getAbscissa() + player.x + this.cossack.getFigureWidth(),
                    this.cossack.getOrdinate() + player.y, player.width*2, player.height);
        }else if(cossack.isFightShCommand()){
            fightArea = new Rectangle(this.cossack.getAbscissa() + player.x - this.cossack.getFigureWidth(),
                    this.cossack.getOrdinate() + player.y, player.width*2, player.height);
        }

        Rectangle knifeArea = null;
        if(cossack.getKnife() != null){
            knifeArea = cossack.getKnife().getSolidArea();
            knifeArea = new Rectangle(this.cossack.getKnife().getAbscissa() + knifeArea.x,
                    this.cossack.getKnife().getOrdinate() + knifeArea.y, knifeArea.width, knifeArea.height);
        }

        boolean intersectsTop, intersectsBottom, intersectsRight, intersectsLeft;
        intersectsTop = intersectsBottom = intersectsLeft = intersectsRight = false;

        if (intersects_with_horizontal_segment(truePlayer,
                creatureLeftWorldX, creatureRightWorldX, creatureTopWorldY)) {
            intersectsTop = true;
        }
        if (intersects_with_horizontal_segment(truePlayer,
                creatureLeftWorldX, creatureRightWorldX, creatureBottomWorldY)) {
            intersectsBottom = true;
        }
        if (intersects_with_vertical_segment(truePlayer,
                creatureTopWorldY, creatureBottomWorldY, creatureLeftWorldX)) {
            intersectsLeft = true;
        }
        if (intersects_with_vertical_segment(truePlayer,
                creatureTopWorldY, creatureBottomWorldY, creatureRightWorldX)) {
            intersectsRight = true;
        }

        if (cossack.getVelocityY() - creature.getVelocityY() > 0
                && intersectsTop/* && !intersectsBottom*/ && creature.isAlive()) {
            killCreature(creature, creatureRightWorldX, creatureTopWorldY);
        } else if (intersectsTop || intersectsRight || intersectsLeft || intersectsBottom) {
            if (creature.isAlive() && !cossack.isInvincible())
                this.cossack.getDamage();
            else if (creature.isAlive()) {
                killCreature(creature, creatureRightWorldX, creatureTopWorldY);
            }
        }

        if (fightArea != null && intersects_with_vertical_segment(fightArea,
                creatureTopWorldY, creatureBottomWorldY, creatureRightWorldX)) {
            if (cossack.getMove() && creature.isAlive()) {
                killCreature(creature,creatureRightWorldX,creatureTopWorldY);
            } else if (creature.isAlive()) cossack.getDamage();
        } else if (fightArea != null && intersects_with_vertical_segment(fightArea,
                creatureTopWorldY, creatureBottomWorldY, creatureLeftWorldX)) {
            if (!cossack.getMove() && creature.isAlive()) {
                killCreature(creature,creatureRightWorldX,creatureTopWorldY);
            } else if(creature.isAlive()) cossack.getDamage();
        } else if (knifeArea != null && intersects_with_vertical_segment(knifeArea,
                creatureTopWorldY, creatureBottomWorldY, creatureLeftWorldX)) {
            creature.die();
            cossack.getKnife().fallApart();
        } else if(knifeArea != null && intersects_with_vertical_segment(knifeArea,
                creatureTopWorldY, creatureBottomWorldY, creatureRightWorldX)) {
            creature.die();
            cossack.getKnife().fallApart();
        }
    }

    private void killCreature(Creature creature, int creatureRightWorldX, int creatureTopWorldY){

        /*Clip enemy_die = Sound.getClip(Music.Enemy_Death);
        if (enemy_die != null)
            enemy_die.start();*/
        if(creature instanceof Chort) {
            int creatureRightCol = round((float)creatureRightWorldX/GameWindow.blockSize);
            int creatureTopRow = creatureTopWorldY/GameWindow.blockSize;
            int chance = (int) Math.floor(Math.random() * 3) + 1;
            if(chance == 1)
                throwBonus(creatureRightCol - 1, creatureTopRow + 1);
        }
        cossack.coins+=5;
        creature.die();
    }

    private void addCreature(String[] characteristics) {
        int row = Integer.valueOf(characteristics[0]);
        if (row > this.rows_on_map || row < 0)
            throw new IllegalArgumentException("Неправильний формат карти (рядок ворога за межами)");
        int col = Integer.valueOf(characteristics[1]);
        if (col > this.cols_on_map || col < 0)
            throw new IllegalArgumentException("Неправильний формат карти (колонка ворога за межами)");
        switch (characteristics[2].charAt(0)) {
            case 'M':
                this.creatures.add(new Mavka(GameWindow.blockSize * col, GameWindow.blockSize * row));
                break;
            case 'R':
                this.creatures.add(new Rusalka(GameWindow.blockSize * col, GameWindow.blockSize * row));
                break;
            case 'Y':
                this.creatures.add(new Poludnytsia(GameWindow.blockSize * col, GameWindow.blockSize * row));
                break;
            case 'L':
                this.creatures.add(new Lisovyk(GameWindow.blockSize * col, GameWindow.blockSize * row));
                break;
            case 'K':
                this.creatures.add(new Vodianyk(GameWindow.blockSize * col, GameWindow.blockSize * row));
                break;
            case 'U':
                this.creatures.add(new Poliovyk(GameWindow.blockSize * col, GameWindow.blockSize * row));
                break;
            case 'I':
                this.creatures.add(new Potercha(GameWindow.blockSize * col, GameWindow.blockSize * row));
                break;
            case 'Z':
                this.creatures.add(new Zlyden(GameWindow.blockSize * col, GameWindow.blockSize * row));
                break;
            case 'E':
                this.creatures.add(new Perelesnyk(GameWindow.blockSize * col, GameWindow.blockSize * row));
                break;
            case 'D':
                this.creatures.add(new Domovyk(GameWindow.blockSize * col, GameWindow.blockSize * row));
                break;
            case 'C':
                this.creatures.add(new Chort(GameWindow.blockSize * col, GameWindow.blockSize * row));
                break;
            case 'H':
                this.creatures.add(new Harakternyk(GameWindow.blockSize * col, GameWindow.blockSize * row));
                break;
            case 'V':
                this.creatures.add(new Viy(GameWindow.blockSize * col, GameWindow.blockSize * row, this));
                Viy v = (Viy) creatures.get(0);
                v.spawnChorts();
                break;
            default:
                throw new IllegalArgumentException("Неправильний формат карти (невідомий ідентифікатор ворога \""
                        +characteristics[2]+"\")");
        }
    }

    private void setCossacksParams() {
        cossack.setWorldWidth(map.length*GameWindow.blockSize);
    }

    private static int round(float number) {
        if (number % 1 == 0) return (int)number - 1;
        return (int)number;
    }

    private static boolean intersects_with_horizontal_segment(Rectangle rectangle, int left_x, int right_x, int y) {
        if (right_x < rectangle.x || left_x > rectangle.x + rectangle.width)
            return false;
        return y >= rectangle.y && y <= rectangle.y + rectangle.height;
    }

    private static boolean intersects_with_vertical_segment(Rectangle rectangle, int top_y, int bottom_y, int x) {
        if (top_y > rectangle.y + rectangle.height || bottom_y < rectangle.y)
            return false;
        return x >= rectangle.x && x <= rectangle.x + rectangle.width;
    }

    public void victory() {
        this.cossack.win = true;
    }

    List<Bonus> getBonuses() {
        return this.bonuses;
    }
}