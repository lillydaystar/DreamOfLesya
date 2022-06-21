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
        loadMap(1);
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

            while(rows < GameWindow.rowsOnScreen) {
                String s = br.readLine();
                String[] str = s.split("");
                for(; cols < GamePanel.mapCols; cols++) {
                    if(cols >= str.length)
                        throw new IllegalArgumentException("Неправильний формат карти");
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
                if((col * GameWindow.blockSize - GameWindow.blockSize < this.cossack.getWorldX() + this.cossack.getX() + 2 * GameWindow.blockSize||
                        col * GameWindow.blockSize - GameWindow.blockSize < 18 * GameWindow.blockSize) &&  //промальовування карти обмежене розміром вікна
                        col * GameWindow.blockSize + GameWindow.blockSize > this.cossack.getWorldX() - this.cossack.getX())     //для пришвидшення обробки інформації
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
        if(cossack.getWorldX() < GameWindow.screenWidth - GameWindow.blockSize &&
                cossack.getWorldX() >= 0 && cossack.getY() >= 0) {
            int rectLeftX = this.cossack.getWorldX() + collisionArea.x;
            int rectRightX = this.cossack.getWorldX() + this.cossack.getFigureWidth() + collisionArea.width;
            int rectTopY = this.cossack.getWorldY() + collisionArea.y;
            int rectBottomY = this.cossack.getWorldY() + this.cossack.getFigureHeight() - 2;

            int leftCol = rectLeftX/GameWindow.blockSize;
            int rightCol = rectRightX/GameWindow.blockSize;
            int topRow = rectTopY/GameWindow.blockSize;
            int bottomRow = rectBottomY/GameWindow.blockSize + 1;

            if (this.cossack.isLeftCommand()) {
                changeCollision(leftCol, bottomRow, topRow);
            }
            else if (this.cossack.isRightCommand()) {
                changeCollision(rightCol, bottomRow, topRow);
            }
            if(cossack.onGround()) {
                char block1, block2;
                if(cossack.getVelocityY() < 0) {
                    block1 = map[rightCol][topRow];
                    block2 = map[leftCol][topRow];
                    if (block1 != '0' && blocks[marks.indexOf(block1)].collision) {
                        this.cossack.setVelocityY(0);
                    } else if (block2 != '0' && blocks[marks.indexOf(block2)].collision) {
                        this.cossack.setVelocityY(0);
                    }
                }
                if(cossack.getVelocityY() > 0) {
                    block1 = map[rightCol][bottomRow];
                    block2 = map[leftCol][bottomRow];
                    if (block1 != '0' && blocks[marks.indexOf(block1)].collision) {
                        cossack.setVelocityY(0);
                        this.cossack.setY(bottomRow*GameWindow.blockSize - 2*GameWindow.blockSize);
                    }
                    else if (block2 != '0' && blocks[marks.indexOf(block2)].collision) {
                        cossack.setVelocityY(0);
                        this.cossack.setY(bottomRow*GameWindow.blockSize - 2*GameWindow.blockSize);
                    }
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
            if(block3 == '0' || !blocks[marks.indexOf(block3)].collision) {
                cossack.jump();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.printf("%d %d\n", col, bottomRow);
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
}