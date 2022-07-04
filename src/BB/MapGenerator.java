/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BB;

import java.awt.BasicStroke;
import java.awt.Graphics2D;

/**
 *
 * @author Aniket Jain
 */
public class MapGenerator {

    public int map[][];
    public int brickWidth;
    public static int brickHeight;

    public MapGenerator(int rows, int columns) {
        map = new int[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                map[i][j] = 1;
            }
        }
        brickWidth = 540 / columns;
    }

    public void draw(Graphics2D g) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] > 0) {
                    // fill
                    g.setColor(Main.block_color);
                    g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                    // border
                    g.setStroke(new BasicStroke(3));
                    g.setColor(Main.bg_color);
                    g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                }
            }
        }
    }
    
    public void setBrickValue(int value, int row, int column){
        map[row][column] = value;
    }

}
