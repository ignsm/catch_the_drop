package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class GameWindow extends JFrame {

    private static GameWindow game_window;

    private static long lastFrameTime;

    private static Image background;
    private static Image drop;
    private static Image game_over;

    private static float drop_left = 200;
    private static float drop_top = -100;
    private static float drop_v = 200;


    private static int score = 0;
    private static boolean isGameOver = false;

//    private  static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//    private  static double sWidth = screenSize.getWidth();
//    private  static double sHeight = screenSize.getHeight();

    public static void main(String[] args) throws IOException {
        background = ImageIO.read(GameWindow.class.getResourceAsStream("bg.png"));
        drop = ImageIO.read(GameWindow.class.getResourceAsStream("drop.png"));
        game_over = ImageIO.read(GameWindow.class.getResourceAsStream("game_over.png"));

        game_window = new GameWindow();
        game_window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game_window.setLocation(200, 100);
        game_window.setSize(906,478);
        game_window.setResizable(false);

        lastFrameTime = System.nanoTime();

        GameField game_field = new GameField();

        game_field.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                float drop_right = drop_left + drop.getWidth(null);
                float drop_bottom = drop_top + drop.getHeight(null);
                boolean isDrop = x >= drop_left && x <= drop_right && y >= drop_top && y <= drop_bottom;
                if (isDrop){
                    drop_top = - 100;
                    drop_left = (int)(Math.random() * (game_field.getWidth() - drop.getWidth(null)));
                    drop_v = drop_v + 20;
                    score++;
                    game_window.setTitle("Score: " + score);
                }
            }
        });

//        game_field.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                if (e.getClickCount() == 2 && !e.isConsumed() && isGameOver == true) {
//                    e.consume();
//                    score = 0;
//                    isGameOver = false;
//                    drop_v = 200;
//                    game_window.setTitle("Score: " + score);
//                }
//            }
//        });

        game_window.add(game_field);
        game_window.setVisible(true);
    }

    private static void onRepaint(Graphics g) {
        long currentTime = System.nanoTime();
        float deltaTime = (currentTime - lastFrameTime) * 0.000000001f;
        lastFrameTime = currentTime;

        drop_top = drop_top + drop_v * deltaTime;

        g.drawImage(background, 0, 0, null);
        g.drawImage(drop, (int)drop_left, (int)drop_top, null);

        if (drop_top > game_window.getHeight()){
            g.drawImage(game_over, 313, 220, null);
            isGameOver = true;
        }

    }


    private static class GameField extends JPanel{

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            onRepaint(g);
            repaint();
        }
    }
}