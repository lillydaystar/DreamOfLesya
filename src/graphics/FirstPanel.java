package graphics;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class FirstPanel extends JPanel {
    private final GameWindow window;

    FirstPanel(GameWindow window) {
        this.window = window;
        this.setPreferredSize(new Dimension(GameWindow.screenWidth, GameWindow.screenHeight));
        this.setLayout(null);

        JButton playButton = new JButton("PLAY");
        playButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        JButton bestButton = new JButton("BESTIARY");
        bestButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        JButton exitButton = new JButton("EXIT");
        exitButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        playButton.addActionListener(press -> startTheGame());
        bestButton.addActionListener(press -> openBestiary());
        exitButton.addActionListener(press -> closeTheProgram());
        playButton.setSize(220,70);
        bestButton.setSize(355,70);
        exitButton.setSize(170, 70);
        playButton.setLocation((GameWindow.screenWidth - playButton.getWidth())/2, 340);
        bestButton.setLocation((GameWindow.screenWidth - bestButton.getWidth())/2, 410);
        exitButton.setLocation((GameWindow.screenWidth - exitButton.getWidth())/2, 480);
        playButton.setMargin(new Insets(0, 0, 0, 0));
        playButton.setBorder(null);
        bestButton.setMargin(new Insets(0, 0, 0, 0));
        bestButton.setBorder(null);
        exitButton.setMargin(new Insets(0, 0, 0, 0));
        exitButton.setBorder(null);
        playButton.setOpaque(false);
        playButton.setContentAreaFilled(false);
        bestButton.setOpaque(false);
        bestButton.setContentAreaFilled(false);
        exitButton.setOpaque(false);
        exitButton.setContentAreaFilled(false);
        try {
            ImageIcon img1 = new ImageIcon("images/PlayButton.png");
            ImageIcon img2 = new ImageIcon("images/BestButton.png");
            ImageIcon img3 = new ImageIcon("images/ExitButton.png");
            playButton.setIcon(img1);
            bestButton.setIcon(img2);
            exitButton.setIcon(img3);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        this.add(playButton);
        this.add(bestButton);
        this.add(exitButton);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage background = null;
        try {
            background = ImageIO.read(new File("images/menu.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }

    private void closeTheProgram() {
        System.exit(0);
    }

    private void openBestiary() {
        window.drawBestiary();
    }

    private void startTheGame() {
        window.drawGame(1);
    }
}