package graphics;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DrawMap {

    Block[] blocks;
    String[][] map;
    private int cossackWorldX;
    private int cossackX;
    private File mapFile;

    public DrawMap() {
        this.blocks = new Block[5];
        try {
            blocks[0] = new Block();
            blocks[0].image = ImageIO.read(new File("images/Grass.jpg"));
            blocks[1] = new Block();
            blocks[1].image = ImageIO.read(new File("images/TreeV.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.map = new String[GamePanel.mapCols][GameWindow.rowsOnScreen];
    }

    /**
     * Цей метод був створений для того, щоб можна було змінювати карту при переході на наступний рівень
     * @param file - текстовий файл з картою світу
     */
    public void setMapFile(File file){
        this.mapFile = file;
        configMap();
    }

    private void configMap(){
        FileReader fr;
        try {
            fr = new FileReader(mapFile);
            BufferedReader br = new BufferedReader(fr);

            int cols = 0;
            int rows = 0;

            while(rows < GameWindow.rowsOnScreen) {
                String s = br.readLine();
                String[] str = s.split("");
                for(; cols < GamePanel.mapCols; cols++){
                    if(cols >= str.length)
                        throw new IllegalArgumentException("Неправильний формат карти");
                    map[cols][rows] = str[cols];
                }
                cols = 0;
                rows++;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCossackWorldX(int x){
        this.cossackWorldX = x;
    }

    public void setCossackX(int x){
        this.cossackX = x;
    }

    void paintMap(Graphics2D g) {
        int col = 0; //порядковий номер стовпця на карті
        int row = 0; //номер рядка
        int x;
        int y = 0;

        while (col < GamePanel.mapCols && row < GameWindow.rowsOnScreen){
            String block = map[col][row];
            int number = -1;
            x = col * GameWindow.blockSize - cossackWorldX + cossackX; //координата кожного блоку визначається за його позицією на загальній карті.
                                                                      //Коли рухається персонаж - рухається і карта, а за нею змінюються порядок зчитування карти
            if(block.equals("A")){
                number = 0;
            }
            else if(block.equals("T")){
                number = 1;
            }
            if(number != -1) {
                if((col * GameWindow.blockSize - GameWindow.blockSize < cossackWorldX + cossackX + 2 * GameWindow.blockSize||
                        col * GameWindow.blockSize - GameWindow.blockSize < 18 * GameWindow.blockSize) &&  //промальовування карти обмежене розміром вікна
                        col * GameWindow.blockSize + GameWindow.blockSize > cossackWorldX - cossackX)     //для пришвидшення обробки інформації
                    g.drawImage(blocks[number].image, x, y, GameWindow.blockSize, GameWindow.blockSize, null);
            }
            col++;
            if(col == GamePanel.mapCols){
                col = 0;
                row++;
                y += GameWindow.blockSize;
            }
        }
    }
}