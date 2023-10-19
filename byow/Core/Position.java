package byow.Core;

import java.io.Serializable;

public class Position implements Serializable {
    private int x;
    private int y;
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int x() {
        return this.x;
    }

    public int y() {
        return this.y;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Position
                && this.x() == ((Position) o).x()
                && this.y() == ((Position) o).y());
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
