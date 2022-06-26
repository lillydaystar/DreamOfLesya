package graphics;

import creatures.Creature;
import creatures.Cossack;
import creatures.enemies.Lisovyk;
import creatures.enemies.Mavka;
import creatures.enemies.Rusalka;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class DrawMap {

    Block[] blocks;
    char[][] map;
    ArrayList<Character> marks = new ArrayList<>();
    LinkedList<Creature> creatures;
    private File mapFile;
    private Cossack cossack;
    private int level;
    private int cols_on_map, rows_on_map;

    public DrawMap() {
        this.map = new char[GamePanel.mapCols][GameWindow.rowsOnScreen];
        this.blocks = new Block[6];
        blocks[0] = new Block();
        blocks[0].collision = true;
        blocks[1] = new Block();
        blocks[1].collision = true;
        blocks[2] = new Block();
        blocks[2].collision = true;
        blocks[3] = new Block();
        blocks[5] = new Block();
        marks.add('A');
        marks.add('T');
        marks.add('Q');
        marks.add('+');
        marks.add('P');
        marks.add('H');
        level = 1;
        this.creatures = new LinkedList<>();
        loadMap(level);
    }

    public void loadMap(int level) {
        try {
            blocks[3].image = ImageIO.read(new File("images/sunflower.png"));
            switch (level){
                case 1:
                    this.mapFile = new File("worlds/map1.txt");
                    blocks[0].image = ImageIO.read(new File("images/Grass.jpg"));
                    blocks[1].image = ImageIO.read(new File("images/TreeV.jpg"));
                    blocks[2].image = ImageIO.read(new File("images/TreeQ.png"));
                    blocks[5].image = ImageIO.read(new File("images/house1.png"));
                    break;
                case 2:
                    this.mapFile = new File("worlds/map2.txt");
                    blocks[0].image = ImageIO.read(new File("images/Plank.jpg"));
                    blocks[1].image = ImageIO.read(new File("images/BookShelf.jpg"));
                    blocks[2].image = ImageIO.read(new File("images/BookQ.png"));
                    blocks[5].image = ImageIO.read(new File("images/house1.png"));
                    break;
                case 3:
                    this.mapFile = new File("worlds/map3.txt");
                    blocks[0].image = ImageIO.read(new File("images/Field.jpg"));
                    blocks[1].image = ImageIO.read(new File("images/Hay.jpg"));
                    blocks[2].image = ImageIO.read(new File("images/HayQ.png"));
                    blocks[5].image = ImageIO.read(new File("images/house1.png"));
                    break;
                case 4:
                    this.mapFile = new File("worlds/map4.txt");
                    break;
                case 5:
                    this.map = new char[GameWindow.columnsOnScreen][GameWindow.rowsOnScreen];
                    this.mapFile = new File("worlds/map5.txt");
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
            while(rows < GameWindow.rowsOnScreen) {
                String s = br.readLine();
                String[] str = s.split("");
                for(; cols < GamePanel.mapCols; cols++) {
                    if(cols >= str.length)
                        throw new IllegalArgumentException("Неправильний формат карти (опис блоків)");
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
    }


    public void setCossack(Cossack cossack){
        this.cossack = cossack;
    }

    public int getLevel(){
        return this.level;
    }

    void paintMap(Graphics2D g) {
        int col = 0; //порядковий номер стовпця на карті
        int row = 0; //номер рядка
        int x;
        int y = 0;

        while (col < GamePanel.mapCols && row < GameWindow.rowsOnScreen){
            char block = map[col][row];
            int number = marks.indexOf(block);
            x = col * GameWindow.blockSize - this.cossack.getWorldX() + cossack.getX();
            //координата кожного блоку визначається за його позицією на загальній карті.
            //Коли рухається персонаж - рухається і карта, а за нею змінюються порядок зчитування карти
            if(number != -1 && number != 5) {
                if((col * GameWindow.blockSize - GameWindow.blockSize < this.cossack.getWorldX()
                        + this.cossack.getX() + 2 * GameWindow.blockSize ||
                        col * GameWindow.blockSize - GameWindow.blockSize < 18 * GameWindow.blockSize) &&
                        //промальовування карти обмежене розміром вікна
                        col * GameWindow.blockSize + GameWindow.blockSize >
                                this.cossack.getWorldX() - this.cossack.getX())
                    //для пришвидшення обробки інформації
                    g.drawImage(blocks[number].image, x, y, GameWindow.blockSize, GameWindow.blockSize, null);
            } else if(number == 5) {
                g.drawImage(blocks[number].image, x, y-GameWindow.blockSize*4,
                        GameWindow.blockSize*6, GameWindow.blockSize*5, null);
            }
            col++;
            if(col == GamePanel.mapCols) {
                col = 0;
                row++;
                y += GameWindow.blockSize;
            }
        }
        checkCollision();
    }

    private Rectangle collisionArea = new Rectangle(2, 2, 22, 45);

    private void checkCollision() {
        int rectLeftX = this.cossack.getWorldX() + collisionArea.x;
        int rectRightX = this.cossack.getWorldX() + this.cossack.getFigureWidth() - collisionArea.x;
        int rectTopY = this.cossack.getWorldY() + collisionArea.y;
        double rectBottomY = this.cossack.getY() + 2*GameWindow.blockSize - 1;

        int leftCol = rectLeftX/GameWindow.blockSize;
        int rightCol = rectRightX/GameWindow.blockSize;
        int topRow = rectTopY/GameWindow.blockSize;
        int bottomRow = (int)Math.round(rectBottomY/GameWindow.blockSize);

        if (this.cossack.isLeftCommand()) {
            changeCollision(leftCol, bottomRow, topRow);
        }
        else if (this.cossack.isRightCommand()) {
            changeCollision(rightCol, bottomRow, topRow);
        }
        if(!cossack.onGround()) {
            char block1, block2;
            if(cossack.getVelocityY() < 0) {
                block1 = map[rightCol][topRow];
                block2 = map[leftCol][topRow];
                if (block1 != '0' && blocks[marks.indexOf(block1)].collision) {
                    this.cossack.setVelocityY(0);
                    this.cossack.fall = true;
                } else if (block2 != '0' && blocks[marks.indexOf(block2)].collision) {
                    this.cossack.setVelocityY(0);
                    this.cossack.fall = true;
                }
            }
            if(cossack.getVelocityY() > 0) {
                block1 = map[rightCol][bottomRow];
                block2 = map[leftCol][bottomRow];
                if (block1 != '0' && blocks[marks.indexOf(block1)].collision) {
                    cossack.setVelocityY(0);
                    this.cossack.setY(bottomRow*GameWindow.blockSize - 2*GameWindow.blockSize);
                    this.cossack.fall = false;
                }
                else if (block2 != '0' && blocks[marks.indexOf(block2)].collision) {
                    cossack.setVelocityY(0);
                    this.cossack.setY(bottomRow*GameWindow.blockSize - 2*GameWindow.blockSize);
                    this.cossack.fall = false;
                }
            }
        }
    }

    private void changeCollision(int col, int bottomRow, int topRow) {
        try {
            char block1, block2, block3;
            block1 = map[col][bottomRow-1];
            block2 = map[col][topRow];
            if (block1 != '0')
                this.cossack.collision = blocks[marks.indexOf(block1)].collision;
            else if (block2 != '0')
                this.cossack.collision = blocks[marks.indexOf(block2)].collision;
            else this.cossack.collision = false;
            block3 = map[col][bottomRow];
            if(block3 == '0' || !blocks[marks.indexOf(block3)].collision && !cossack.isJumpCommand()) {
//                System.out.println("Fall");
                //this.cossack.setVelocityY(1);
                this.cossack.fall = true;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
//            System.out.printf("%d %d\n", col, bottomRow);
        }
    }

    public void chackTile(Creature creature) {
        int creatureLeftWorldX = creature.getAbscissa() + creature.getSolidArea().x;
        int creatureRightWorldX = creatureLeftWorldX + creature.getSolidArea().width;
        int creatureTopWorldY = creature.getOrdinate() + creature.getSolidArea().y;
        int creatureBottomWorldY = creatureTopWorldY + creature.getSolidArea().height;

        int creatureLeftCol = creatureLeftWorldX/GameWindow.blockSize;
        int creatureRightCol = creatureRightWorldX/GameWindow.blockSize;
        int creatureTopRow = creatureTopWorldY/GameWindow.blockSize;
        int creatureBottomRow = creatureBottomWorldY/GameWindow.blockSize;

        char tileNum1, tileNum2;

        if (creature.getVelocityY() > 0) {
            creatureBottomRow = (creatureBottomWorldY + creature.getVelocityY())/GameWindow.blockSize;
            tileNum1 = this.map[creatureLeftCol][creatureBottomRow];
            tileNum2 = this.map[creatureRightCol][creatureBottomRow];
            if ((tileNum1 != '0' && this.blocks[marks.indexOf(tileNum1)].collision) ||
                    (tileNum2 != '0' && this.blocks[marks.indexOf(tileNum2)].collision))
                creature.downCollision();
        }
        if (creature.getVelocityY() < 0) {
            creatureTopRow = (creatureTopWorldY + creature.getVelocityY())/GameWindow.blockSize;
            tileNum1 = this.map[creatureLeftCol][creatureTopRow];
            tileNum2 = this.map[creatureRightCol][creatureTopRow];
            if ((tileNum1 != '0' && this.blocks[marks.indexOf(tileNum1)].collision) ||
                    (tileNum2 != '0' && this.blocks[marks.indexOf(tileNum2)].collision))
                creature.upCollision();
        }
        if (creature.getVelocityX() > 0) {
            creatureRightCol = (creatureRightWorldX + creature.getVelocityX())/GameWindow.blockSize;
            tileNum1 = this.map[creatureRightCol][creatureTopRow];
            tileNum2 = this.map[creatureRightCol][creatureBottomRow];
            if ((tileNum1 != '0' && this.blocks[marks.indexOf(tileNum1)].collision) ||
                    (tileNum2 != '0' && this.blocks[marks.indexOf(tileNum2)].collision))
                creature.rightCollision();
        }
        if (creature.getVelocityX() < 0) {
            creatureLeftCol = (creatureLeftWorldX + creature.getVelocityX())/GameWindow.blockSize;
            tileNum1 = this.map[creatureLeftCol][creatureTopRow];
            tileNum2 = this.map[creatureLeftCol][creatureBottomRow];
            if ((tileNum1 != '0' && this.blocks[marks.indexOf(tileNum1)].collision) ||
                    (tileNum2 != '0' && this.blocks[marks.indexOf(tileNum2)].collision))
                creature.leftCollision();
        }
    }

    private void addCreature(String[] characteristics) {
        int row = Integer.valueOf(characteristics[0]);
        if (row > this.rows_on_map || row < 0)
            throw new IllegalArgumentException("Неправильний формат карти (рядок ворога за межами)");
        int col = Integer.valueOf(characteristics[1]);
        if (col > this.cols_on_map || col < 0)
            throw new IllegalArgumentException("Неправильний формат карти (колонка ворога за межами)");
        switch (characteristics[2]) {
            case "M":
                this.creatures.add(new Mavka(GameWindow.blockSize * col, GameWindow.blockSize * row));
                break;
            case "L":
                this.creatures.add(new Lisovyk(GameWindow.blockSize * col, GameWindow.blockSize * row));
                break;
            case "R":
                this.creatures.add(new Rusalka(GameWindow.blockSize * col, GameWindow.blockSize * row));
                break;
            default:
                throw new IllegalArgumentException("Неправильний формат карти (невідомий ідентифікатор ворога \""
                        +characteristics[2]+"\")");
        }
    }
}