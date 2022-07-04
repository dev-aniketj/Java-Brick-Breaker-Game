/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BB;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;
import javax.swing.JPanel;

/**
 *
 * @author Aniket Jain
 */
public class GamePlay extends JPanel implements KeyListener, ActionListener {

    public static boolean play = false;
    public static int score = 0;

    public static int totalBricks;

    private Timer timer;
    private int delay = 8;

    private int playerX = 310;

    public static int ballposX = (int) (Math.random() * (550 - 150 + 1) + 150);
    public static int ballposY = 400;
    public static int ballXdir = -1;
    public static int ballYdir = -2;

    public static MapGenerator map;

    //Constructor
    public GamePlay() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    @Override
    public void paint(Graphics g) {
        // background
        g.setColor(Main.bg_color);
        g.fillRect(1, 1, 692, 592);

        // border 
        g.setColor(Main.border_color);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        // padel / player
        g.setColor(Main.player_color);
        g.fillRect(playerX, 550, 100, 8);

        // ball
        g.setColor(Main.ball_color);
        g.fillOval(ballposX, ballposY, 20, 20);

        // blocks
        map.draw((Graphics2D) g);

        // score 
        g.setColor(Main.player_color);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("" + score, 640, 35);

        // player out
        if (ballposY > 570) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            // display score
            g.setColor(Main.player_color);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over, Score : " + score, 200, 300);
            // display restart
            g.setColor(Main.block_color);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to restart : ", 250, 350);
        }
        // player win
        if (totalBricks <= 0) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            // winner score
            g.setColor(Main.ball_color);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You Won!, Score : " + score, 200, 300);
            // display restart
            g.setColor(Main.block_color);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to restart : ", 250, 350);
        }

        //flush
        g.dispose();
    }

    public void moveLeft() {
        play = true;
        playerX -= 10;
    }

    public void moveRight() {
        play = true;
        playerX += 10;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX <= 0) {
                playerX = 0;
            } else {
                moveLeft();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 600) {
                playerX = 600;
            } else {
                moveRight();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                play = true;
                ballposX = (int) (Math.random() * (550 - 150 + 1) + 150);
                ballposY = 400;
                ballXdir = -1;
                ballYdir = -2;
                score = 0;
                if (Main.level_1.isEnabled()) {
                    totalBricks = 21;
                } else if (Main.level_2.isEnabled()) {
                    totalBricks = 28;
                } else if (Main.level_3.isEnabled()) {
                    totalBricks = 40;
                } else if (Main.level_4.isEnabled()) {
                    totalBricks = 48;
                }
                switch (Main.selected_level) {
                    case 1:
                        map = new MapGenerator(3, 7);
                        break;
                    case 2:
                        map = new MapGenerator(4, 7);
                        break;
                    case 3:
                        map = new MapGenerator(5, 8);
                        break;
                    case 4:
                        map = new MapGenerator(6, 8);
                        break;
                    default:
                        throw new AssertionError();
                }
                repaint();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();

        if (play) {
            Rectangle r1 = new Rectangle(ballposX, ballposY, 20, 30);
            Rectangle r2 = new Rectangle(playerX, 550, 100, 8);
            if (r1.intersects(r2)) {
                ballYdir = -ballYdir;
            }

            for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[i].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);

                        if (ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            //left and right touch
                            if (ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
                                ballXdir = -ballXdir;
                            } //top and bottom touch
                            else {
                                ballYdir = -ballYdir;
                            }
                        }
                    }
                }
            }

            ballposX += ballXdir;
            ballposY += ballYdir;
            // left
            if (ballposX < 0) {
                ballXdir = -ballXdir;
            }
            // top
            if (ballposY < 0) {
                ballYdir = -ballYdir;
            }
            // right
            if (ballposX > 670) {
                ballXdir = -ballXdir;
            }
//            
        }

        repaint();
    }
}
