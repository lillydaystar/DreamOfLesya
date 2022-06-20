package graphics;

import javax.swing.*;
import java.awt.*;

class FirstPanel extends JPanel {

    FirstPanel() {
        this.setPreferredSize(new Dimension(GameWindow.screenWidth, GameWindow.screenHeight));
        GridLayout layout = new GridLayout(3, 1);
        layout.setVgap(30);
        this.setLayout(layout);

        JButton playButton = new JButton("PLAY");
        playButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        JButton bestButton = new JButton("BESTIARY");
        bestButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        JButton exitButton = new JButton("EXIT");
        exitButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));

        this.add(playButton);
        this.add(bestButton);
        this.add(exitButton);
    }
}