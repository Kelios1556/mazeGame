package byow.Core;

import byow.TileEngine.*;
import java.awt.Color;
import java.io.Serializable;
import java.util.*;

public class Player implements Serializable {
    private int x;
    private int y;
    private String name = "yeah";
    private char avatar = '$';
    private Color color = Color.PINK;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setName(String nameT) {
        this.name = nameT;
    }

    public void setAvatar(char avatarT) {
        this.avatar = avatarT;
    }

    public void setColor(Color colorT) {
        this.color = colorT;
    }

    /**
     * act: 'w' or 'a' or 's' or 'd' or 'W' or 'A' or 'S' or 'D'.
     */
    public void move(TETile[][] world, char act) {
        int newX;
        int newY;
        switch (act) {
            case 'w':
            case 'W':
                newX = x;
                newY = y + 1;
                break;
            case 'a':
            case 'A':
                newX = x - 1;
                newY = y;
                break;
            case 's':
            case 'S':
                newX = x;
                newY = y - 1;
                break;
            case 'd':
            case 'D':
                newX = x + 1;
                newY = y;
                break;
            default:
                return;
        }
        //System.out.println(world[newX][newY].description());
        if (newX < 0 || newX > world.length || newY < 0 || newY > world[0].length) {
            return;
        }
        if (!(world[newX][newY].description().equals("hallway")
                || world[newX][newY].description().equals("door")
                || world[newX][newY].description().equals("room")
                || world[newX][newY].description().equals("light"))) {
            return;
        }
        //System.out.println("original x: " + x + "  current x: " + newX);
        //System.out.println("original y: " + y + "  current y: " + newY);
        this.x = newX;
        this.y = newY;
    }

    public int x() {
        return this.x;
    }

    public int y() {
        return this.y;
    }

    public String name() {
        return this.name;
    }

    public char avatar() {
        return this.avatar;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Color color() {
        return this.color;
    }
}

        
