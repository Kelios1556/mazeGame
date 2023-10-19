package byow.Core;

import java.io.Serializable;
import java.util.*;
import byow.TileEngine.*;

public class Room implements Serializable {
    private int x;
    private int y;
    private int halfWid;
    private int halfLen;
    private int doorX;
    private int doorY;


    public Room(int x, int y, int halfWid, int halfLen) {
        this.x = x;
        this.y = y;
        this.halfWid = halfWid;
        this.halfLen = halfLen;
        this.doorX = -1;
        this.doorY = -1;
    }

    public void setDoor(int doorXT, int doorYT) {
        this.doorX = doorXT;
        this.doorY = doorYT;
    }

    public int x() {
        return this.x;
    }

    public int y() {
        return this.y;
    }

    public int halfWid() {
        return this.halfWid;
    }

    public int halfLen() {
        return this.halfLen;
    }

    public int doorX() {
        return this.doorX;
    }

    public int doorY() {
        return this.doorY;
    }
}
