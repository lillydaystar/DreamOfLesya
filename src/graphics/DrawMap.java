package graphics;

import creatures.enemies.Creature;
import creatures.player.Cossack;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DrawMap {

    Block[] blocks;
    char[][] map;
    ArrayList<Character> marks = new ArrayList<>();
    private File mapFile;
    private Cossack cossack;
    private int level;

    public DrawMap(int level) {
        this.blocks = new Block[6];
        blocks[0] = new Block();
        blocks[0].collision = true;
        blocks[1] = new Block();
        blocks[1].collision = true;
        blocks[2] = new Block();
        blocks[2].collision = true;
        blocks[2].breakable = true;
        blocks[3] = new Block();
        blocks[3].collision = true;
        blocks[4] = new Block();
        blocks[5] = new Block();
        marks.add('A');
        marks.add('T');
        marks.add('Q');
        marks.add('B');
        marks.add('+');
        marks.add('H');
        this.level = level;
        loadMap();
    }

    public void loadMap() {
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
                    blocks[5].image = ImageIO.read(new File("images/house1.png"));
                    break;
                case 3:
                    this.mapFile = new File("worlds/map3.txt");
                    blocks[0].image = ImageIO.read(new File("images/Field.jpg"));
                    blocks[1].image = ImageIO.read(new File("images/Hay.jpg"));
                    blocks[2].image = ImageIO.read(new File("images/HayQ.png"));
                    blocks[3].image = ImageIO.read(new File("images/BookB.png"));
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

            String firstLine = br.readLine();
            String[] split = firstLine.split("");
            this.map = new char[split.length][GameWindow.rowsOnScreen];
            rows++;

            while(rows < GameWindow.rowsOnScreen) {
                String s = br.readLine();
                String[] str = s.split("");
                for(; cols < map.length; cols++) {
                    map[cols][rows] = str[cols].charAt(0);
                    configureBlockType(cols, rows);
                }
                cols = 0;
                rows++;
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

        while (col < map.length && row < GameWindow.rowsOnScreen){
            char block = map[col][row];
            int number = marks.indexOf(block);
            x = col * GameWindow.blockSize - this.cossack.getWorldX() + cossack.getX();
            //Координата кожного блоку визначається за його позицією на загальній карті.
            //Коли рухається персонаж - рухається і карта, а за нею змінюються порядок зчитування карти
            if(number != -1 && number != 5) {
                if((col * GameWindow.blockSize - GameWindow.blockSize < this.cossack.getWorldX() + this.cossack.getX() + 2 * GameWindow.blockSize||
                        col * GameWindow.blockSize - GameWindow.blockSize < 18 * GameWindow.blockSize) &&  //промальовування карти обмежене розміром вікна
                        col * GameWindow.blockSize + GameWindow.blockSize > this.cossack.getWorldX() - this.cossack.getX())     //для пришвидшення обробки інформації
                    g.drawImage(blocks[number].image, x, y, GameWindow.blockSize, GameWindow.blockSize, null);
            } else if(number == 5) {
                g.drawImage(blocks[number].image, x, y-GameWindow.blockSize*4,
                        GameWindow.blockSize*6, GameWindow.blockSize*5, null);
            }
            col++;
            if(col == map.length) {
                col = 0;
                row++;
                y += GameWindow.blockSize;
            }
        }
        checkCollision();
    }

    private final Rectangle collisionArea = new Rectangle(2, 2, 22, 45);

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
                fall(leftCol+1, leftCol, bottomRow);
            }
            else if (this.cossack.isRightCommand()) {
                changeCollision(rightCol, bottomRow, topRow);
                fall(rightCol-1, rightCol, bottomRow);
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
                            checkForCoin(rightCol, bottomRow);
                    } else if (block2 != '0' && blocks[marks.indexOf(block2)].collision) {
                        this.cossack.setVelocityY(0);
                        this.cossack.fall = true;
                        if(checkBlock(leftCol, topRow))
                            checkForCoin(leftCol,bottomRow);
                    }
                }
                if(cossack.getVelocityY() > 0) {
                    block1 = map[rightCol][bottomRow];
                    block2 = map[leftCol][bottomRow];
                    checkForCoin(rightCol,bottomRow);
                    checkForCoin(leftCol,bottomRow);
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

    private void checkForCoin(int col, int row) {
        if (map[col][row] == '+') {
            cossack.coins++;
            map[col][row] = '0';
        }
    }

    private boolean checkBlock(int col, int row) {
        if(blocks[marks.indexOf(map[col][row])].breakable){
            map[col][row] = 'B';
            /*method for bonus*/
        }
        return !blocks[marks.indexOf(map[col][row])].breakable;
    }

    private void changeCollision(int col, int bottomRow, int topRow) {
        try {
            char block1, block2;
            block1 = map[col][bottomRow-1];
            block2 = map[col][topRow];
            if (block1 != '0') {
                this.cossack.collision = blocks[marks.indexOf(block1)].collision;
                checkForCoin(col, bottomRow-1);
            }
            else if (block2 != '0') {
                this.cossack.collision = blocks[marks.indexOf(block2)].collision;
                checkForCoin(col, topRow);
            }
            else this.cossack.collision = false;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.printf("%d %d\n", col, bottomRow);
        }
    }

    private void fall(int prevCol, int col, int bottomRow){
        char block = map[col][bottomRow];
        char prevBlock = map[prevCol][bottomRow];
        if((block == '0' || !blocks[marks.indexOf(block)].collision) && !cossack.isJumpCommand()) {
            if(prevBlock == '0' || !blocks[marks.indexOf(prevBlock)].collision)
                this.cossack.fall = true;
        }
    }

    private void configureBlockType(int cols, int rows) {
        switch (map[cols][rows]) {
            case 'A': {

            }
        }
    }

    private void addCreature(Creature creature) {

    }

    public void setCossacksParams() {
        cossack.setWorldWidth(map.length*GameWindow.blockSize);
    }
}