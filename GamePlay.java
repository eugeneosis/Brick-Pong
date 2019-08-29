package brickPong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;

import static java.awt.Font.SANS_SERIF;
import static java.awt.geom.Path2D.intersects;

public class Gameplay extends JPanel implements KeyListener, ActionListener {

    private boolean play = false;

    private int score = 0;

    private int totalBricks = 21;

    private Timer timer;
    private int delay = 8;

    private int playerX = 310;

    private int ballsopX = 120;
    private int ballsopY = 350;
    private int ballXdirection = -1;
    private int ballYdirection = -2;

    private Map map;

    public Gameplay() {
        map = new Map(3, 7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g) {
        /*background*/
        g.setColor(Color.darkGray);
        g.fillRect(1, 1, 692, 592);

        /*draw map*/
        map.draw((Graphics2D) g);

        /*border around*/
        g.setColor(Color.darkGray);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        /*score*/
        g.setColor(Color.WHITE);
        g.setFont(new Font("serif", Font.TYPE1_FONT, 20)); //work with font
        g.drawString("score - " + score, 575, 30); //location

        /*platform*/
        g.setColor(Color.white);
        g.fillRect(playerX, 550, 100, 9);


        /*ball*/
        g.setColor(Color.WHITE);
        g.fillOval(ballsopX, ballsopY, 20, 20);

        if (totalBricks <= 0) {
            play = false;
            ballXdirection = 0;
            ballYdirection = 0;
            g.setColor(Color.WHITE);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You win!", 292, 318);
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 25));
            g.drawString("Enter to restart", 267, 360);
        }

        if (ballsopY > 570) {
            play = false;
            ballXdirection = 0;
            ballYdirection = 0;
            g.setColor(Color.WHITE);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over", 292, 318);
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 25));
            g.drawString("Enter to restart", 282, 360);
        }

        g.dispose();

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (play) {
            if (new Rectangle(ballsopX, ballsopY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballYdirection = -ballYdirection;
            }
            A:
            for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballsopX, ballsopY, 20, 20);
                        Rectangle brickRect = rect;

                        if (ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if (ballsopX + 1 <= brickRect.x || ballsopX + 1 >= brickRect.x + brickRect.width) {
                                ballXdirection = -ballXdirection;
                            } else {
                                ballYdirection = -ballYdirection;
                            }
                            break A;
                        }
                    }
                }
            }

            ballsopX += ballXdirection;
            ballsopY += ballYdirection;
            if (ballsopX < 0) {
                ballXdirection = -ballXdirection;
            }
            if (ballsopY < 0) {
                ballYdirection = -ballYdirection;
            }
            if (ballsopX > 670) {
                ballXdirection = -ballXdirection;
            }
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 600) {
                playerX = 600;
            } else {
                moveRight();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX < 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                play = true;
                ballsopX = 120;
                ballsopY = 350;
                ballXdirection = -1;
                ballYdirection = -2;
                playerX = 310;
                score = 0;
                totalBricks = 21;
                map = new Map(3, 7);

                repaint();
            }
        }
    }

    public void moveRight() {
        play = true;
        playerX += 20;
    }

    public void moveLeft() {
        play = true;
        playerX -= 20;
    }


    @Override
    public void keyReleased(KeyEvent e) {
    }
}
