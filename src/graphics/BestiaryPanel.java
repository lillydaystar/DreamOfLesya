package graphics;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.LinkedList;

/**Клас, який відкриває бестіарій*/
public class BestiaryPanel extends JPanel {
    private final String[] description = new String[13];
    private final GameWindow window;

    /**
     * Конструктор панелі
     * @param window - задаємо вікно, щоб потім можна було повернутися до головного меню
     */
    BestiaryPanel(GameWindow window){
        super();
        this.window = window;
        this.setLayout(null);
        this.addKeyListener(new KeyToEscape()); //на кнопку ESC повертаємось до головного меню
        readBestiaryFile();
        createTable();
    }

    /**
     * Зчитує файл з описом істот
     */
    private void readBestiaryFile() {
        File f = new File("files/Bestiary.txt");
        try {
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);

            String s;
            int i = 0;
            do{
                s = br.readLine();
                description[i] = s;
                i++;
            }while (s != null && i<13);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    /**
     * Створюємо таблицю для бестіарію
     */
    private void createTable(){
        LinkedList<ImageIcon> images = new LinkedList<>();
        /*Усі зображення істот: */
        images. add(new ImageIcon("heroes/ViyC_1.png")); //here should be viy
        images. add(new ImageIcon("heroes/VodianykL.png"));
        images. add(new ImageIcon("heroes/DomovykL.png"));
        images. add(new ImageIcon("heroes/ZlydenL_1.png"));
        images. add(new ImageIcon("heroes/LisovykL.png"));
        images. add(new ImageIcon("heroes/MavkaL_1.png"));
        images. add(new ImageIcon("heroes/PerelesnykL_1.png"));
        images. add(new ImageIcon("heroes/PoludnytsiaL_1.png"));
        images. add(new ImageIcon("heroes/PoliovykL.png"));
        images. add(new ImageIcon("heroes/PoterchaL_1.png"));
        images. add(new ImageIcon("heroes/RusalkaL_1.png"));
        images. add(new ImageIcon("heroes/Harakternyk1.png"));
        images. add(new ImageIcon("heroes/ChortL_1.png"));

        /*Задаємо ширину і висоту для істот індивідуально*/
        for(int i=0; i<13; i++){
            int width, height;
            /*if(i == 0){
                width = 120;
                height = 240;
            }else */if (i==5 || i==7 || i==10){
                width = 180;
                height = 240;
            }else if(i == 1 || i == 2 || i == 4 || i == 8) {
                width = 240;
                height = 120;
            }else if(i == 11){
                width = height = 360;
            }else {
                width = height = 240;
            }
            ImageIcon icon = images.get(i);
            Image image = icon.getImage();
            Image newImg = image.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH);
            images.set(images.indexOf(icon), new ImageIcon(newImg));
        }
        //стрічка заголовків
        String[] column = {"Фото", "Опис"};
        Object[][] info = new Object[13][2];

        DefaultTableModel tableModel = new DefaultTableModel(info, column) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        //додаємо елементи:
        for (int i = 0; i < 13; i++) {
            table.setValueAt(images.get(i), i, 0);
            table.setValueAt(description[i], i, 1);
        }
        TableColumnModel colModel = table.getColumnModel();

        TableColumn col1 = colModel.getColumn(1);
        col1.setCellRenderer(new CustomRenderer()); //задаємо рендерер для другої колонки
        col1.setPreferredWidth(400);

        TableColumn col2 = colModel.getColumn(0);
        col2.setCellRenderer(new ImageRenderer()); //задаємо рендерер для першої колонки

        table.setRowHeight(440);
        table.setOpaque(false);

        JScrollBar scrollBar = new JScrollBar(JScrollBar.VERTICAL){
          @Override
          public boolean isVisible(){
              return true;
          }
        };
        scrollBar.setUnitIncrement(20);
        JScrollPane scroll = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBar(scrollBar);
        scroll.setSize(GameWindow.screenWidth, GameWindow.screenHeight);
        scroll.setLocation(0,0);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        this.add(scroll);
    }

    /**
     * Метод для промальовування фону
     */
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        BufferedImage icon = null;
        try{
            icon = ImageIO.read(new File("images/bestiary.png"));
        }catch (IOException e){
            e.printStackTrace();
        }
        g.drawImage(icon, 0, 0, getWidth(), getHeight(), this);
    }

    /**
     * Рендерер для додавання JTextArea до таблиці.
     */
    private static class CustomRenderer implements TableCellRenderer {
        JTextArea textArea;
        public CustomRenderer() {
            textArea = new JTextArea();
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setOpaque(false);
            textArea.setFont(new Font("Bookman Old Style", Font.BOLD, 21));
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            textArea.setText((String)value);
            return textArea;
        }
    }

    /**
     * Рендерер для додавання зображень без фону до таблиці
     */
    private static class ImageRenderer implements TableCellRenderer {
        JLabel label;
        public ImageRenderer() {
            label = new JLabel("");
        }
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            label.setIcon((ImageIcon) value);
            label.setVerticalAlignment(JLabel.CENTER);
            label.setHorizontalAlignment(JLabel.CENTER);
            return label;
        }
    }

    /**
     * Клас для зчитування кнопки ESC
     */
    private class KeyToEscape implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {}

        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            if(key == KeyEvent.VK_ESCAPE){
                window.drawMainMenu();
            }
        }
    }

}
