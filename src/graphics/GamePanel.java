package graphics;

import javax.swing.*;
import java.awt.*;

class GamePanel extends JPanel {

    GamePanel() {
        this.setPreferredSize(new Dimension(GameWindow.screenWidth, GameWindow.screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
    }

    public void paintComponent(Graphics graphics) {
        System.out.println("here paint");
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D)graphics;
        graphics2D.setColor(Color.white);
        graphics2D.dispose();
    }

    void update() {

    }
}