package byow.Core;

import byow.TileEngine.*;

import java.awt.Color;
import java.io.Serializable;
import java.util.*;


public class World implements Serializable {
    public static final int LEN = 40; // num of row
    public static final int WID = 80; // num of col
    private static final int ROOMXOFF = 6;
    private static final int ROOMYOFF = 6;

    private int roomNum;
    private ArrayList<Room> rooms = new ArrayList<>();
    //private Player player = null;
    private Color[] colorSet;
    //private TERenderer terenderer = new TERenderer();

    private TETile[][] world;
    private Long seed;
    private Random random;
    private HashMap<Color, Position> lights = new HashMap<>();

    private char theme = 'w';
    //private Player player2 = null;

    private int reached;

    private boolean showRoom = false;
    private boolean showMap = false;


    public World(String seed) {
        String s = "";
        for (int i = 0; i < seed.length(); i++) {
            if (seed.charAt(i) >= '0' && seed.charAt(i) <= '9') {
                s += seed.charAt(i);
            }
        }
        this.seed = Long.valueOf(s);
        //this.terenderer.initialize(WID + 5, LEN + 5, 2, 2);
        this.world = new TETile[WID][LEN];
        this.random = new Random(this.seed);
        for (int i = 0; i < WID; i++) {
            for (int j = 0; j < LEN; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }
        reached = 0;
    }

    public void diyWorld(char themeT) {
        //System.out.println(themeT);
        this.theme = themeT;
        //System.out.println("this theme: " + this.theme);
    }
    
    public void generateWorld() {
        for (int x = 0; x < WID; x++) {
            world[x][0] = generateTile("wall"); 
            world[x][LEN - 1] = generateTile("wall"); 
        }
        for (int y = 0; y < LEN; y++) {
            world[0][y] = generateTile("wall"); 
            world[WID - 1][y] = generateTile("wall"); 
        }
//        System.out.println("try");
        generateBlank();
        generateRoom();
        //terenderer.renderFrame(world);
        generateHallway();
        //terenderer.renderFrame(world);
        //StdDraw.pause(1000);
        generateRoomDoor();
        //terenderer.renderFrame(world);
        //System.out.println("try");
        generateLight();
        //System.out.println("tryyyyyyyyyyy");
        //terenderer.renderFrame(world);

//        System.out.println(toString(world));
        //show(world);
    }

    /*private String toString(TETile[][] world) {
        int width = world.length;
        int height = world[0].length;
        StringBuilder sb = new StringBuilder();

        for (int y = height - 1; y >= 0; y -= 1) {
            for (int x = 0; x < width; x += 1) {
                if (world[x][y] == null) {
                    throw new IllegalArgumentException("Tile at position x=" + x + ", y=" + y
                            + " is null.");
                }
                if (world[x][y].description().equals("wall")) {
                    sb.append("#");
                }
                else if (world[x][y].description().equals("sea")) {
                    sb.append(" ");
                }
                else if (world[x][y].description().equals("nothing")) {
                    sb.append('a');
                }
                else sb.append(world[x][y].character());
            }
            sb.append('\n');
        }
        return sb.toString();
    }*/

    /** Using Prim's Algorithm to generate a maze, reference to @BoL0150. */
    public void generateHallway() {
        int startX = random.nextInt(world.length - 2);
        int startY = random.nextInt(world[0].length - 2);
        while (!(world[startX][startY].equals(Tileset.WALL)
                || world[startX][startY].equals(Tileset.NOTHING))) {
            startX = random.nextInt(world.length - 2);
            startY = random.nextInt(world[0].length - 2);
        }
        if (startX % 2 != 1) {
            startX++;
        }
        if (startY % 2 != 1) {
            startY++;
        }
        Position start = new Position(startX, startY);

        for (int x = 0; x < WID; x += 1) {
            for (int y = 0; y < LEN; y += 1) {
                if (world[x][y].equals(Tileset.NOTHING)) {
                    if (x % 2 == 1 && y % 2 == 1) {
                        continue;
                    }
                    world[x][y] = Tileset.WALL;
                }
            }
        }

        LinkedList<Position> positions = new LinkedList<>();
        positions.add(start);
        //show(world);
        
        while (!positions.isEmpty()) {
            int currIndex = random.nextInt(positions.size());
            Position curr = positions.get(currIndex);
            world[curr.x()][curr.y()] = Tileset.FLOOR;
            generateHallwayHelper(curr, positions);
            positions.remove(currIndex);
        }
    }

    private void generateHallwayHelper(Position start, LinkedList<Position> positions) {
        random = new Random(seed + 234567890);
        int[][] nextStep = {{2, 0}, {0, -2}, {-2, 0}, {0, 2}};
        boolean[] visited = new boolean[4];
        boolean connected = false;
        while (!visited[0] || !visited[1] || !visited[2] || !visited[3]) {
            //random a direction
            int next = random.nextInt(4);

            //if the direction has been chosen, choose another direction
            if (visited[next]) {
                continue;
            }

            //move to the new position
            visited[next] = true;
            int nextX = start.x() + nextStep[next][0];
            int nextY = start.y() + nextStep[next][1];

            //check if the position is out of bound
            if (nextX < 0 || nextY < 0) {
                continue;
            }
            if (nextX >= WID || nextY >= LEN) {
                continue;
            }
            if (!(world[nextX][nextY].equals(Tileset.NOTHING)
                    || world[nextX][nextY].equals(Tileset.WALL)
                    || world[nextX][nextY].equals(Tileset.FLOOR))) {
                continue;
            }

            //add the new position into the list
            if (world[nextX][nextY].equals(Tileset.NOTHING)) {
                positions.add(new Position(nextX, nextY));
            }

            //remove the wall
            if (world[nextX][nextY].equals(Tileset.FLOOR) && !connected) {
                world[(start.x() + nextX) / 2][(start.y() + nextY) / 2] = Tileset.FLOOR;
                connected = true;
            }
        }
    }

    public void generateRoom() {
        this.roomNum = random.nextInt(2) + 2;
        for (int i = 0; i < roomNum; i++) {
            int x = 0, y = 0;
            int halfRoomWid = 0, halfRoomLen = 0;
            //System.out.println(toString(world));
            ArrayList<Position> candPoints = new ArrayList<>();
            for (int ii = ROOMXOFF; ii < WID - ROOMXOFF; ii++) {
                for (int j = ROOMYOFF; j < LEN - ROOMYOFF; j++) {
                    if (!world[ii][j].description().equals("nothing")) {
                        continue;
                    }
                    boolean flag = true;
                    for (Room roomOrz : rooms) {
                        if (Math.abs(ii - roomOrz.x()) < roomOrz.halfWid() + 4
                                || Math.abs(j - roomOrz.y()) < roomOrz.halfLen() + 4) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        candPoints.add(new Position(ii, j));
                    }
                }
            }
            //System.out.println("candPoint: " + candPoints.size());
            int d = random.nextInt(candPoints.size());
            x = candPoints.get(d).x();
            y = candPoints.get(d).y();
            //System.out.println(x + " " + y);
            halfRoomWid = random.nextInt(2) + 1;
            halfRoomLen = random.nextInt(2) + 1;
            //System.out.println("??????");
            for (int j = 1; j <= halfRoomWid + 1; j++) {
                if (!world[x + j][y].description().equals("nothing")
                        || !world[x - j][y].description().equals("nothing")) {
                    halfRoomWid = Math.max(0, j - 4);
                    break;
                }
            }
            for (int k = 1; k <= halfRoomLen + 1; k++) {
                if (!world[x][y + k].description().equals("nothing")
                        || !world[x][y - k].description().equals("nothing")) {
                    halfRoomLen = Math.max(0, k - 4);
                    break;
                }
            }

            //System.out.println("!!!!!!!");
            Room newRoom = new Room(x, y, halfRoomWid, halfRoomLen);
            rooms.add(newRoom);
            //System.out.println("@@@@@@@");

            for (int j = x - halfRoomWid; j <= x + halfRoomWid + 1; j++) {
                for (int k = y - halfRoomLen; k <= y + halfRoomLen + 1; k++) {
                    world[j][k] = generateTile("room");
                }
            }
            for (int j = x - halfRoomWid - 1; j <= x + halfRoomWid + 2; j++) {
                world[j][y - halfRoomLen - 1] = generateTile("wall"); 
                world[j][y + halfRoomLen + 2] = generateTile("wall"); 
            }
            for (int k = y - halfRoomLen - 1; k <= y + halfRoomLen + 2; k++) {
                world[x - halfRoomWid - 1][k] = generateTile("wall"); 
                world[x + halfRoomWid + 2][k] = generateTile("wall"); 
            }
        }
    }

    public void generateRoomDoor() {
        for (int i = 0; i < roomNum; i++) {
            int x = rooms.get(i).x(), y = rooms.get(i).y();
            int halfRoomWid = rooms.get(i).halfWid();
            int halfRoomLen = rooms.get(i).halfLen();
            int doorX;
            int doorY;

            int side1 = halfRoomLen * 2 + 1;
            int side2 = (side1 + 1) + (halfRoomWid * 2) + 1;
            int side3 = (side2 + 1) + (halfRoomLen * 2) + 1;
            int side4 = (side3 + 1) + (halfRoomWid * 2) + 1;

            for (int ii = 0; ii < WID; ii++) {
                for (int j = 0; j < LEN; j++) {
                    if (world[ii][j].equals(Tileset.WALL)) {
                        if (world[ii - 1][j].description().equals("wall")
                                    && !world[ii - 1][j].equals(Tileset.WALL)
                                || world[ii + 1][j].description().equals("wall")
                                    && !world[ii + 1][j].equals(Tileset.WALL)
                                || world[ii][j + 1].description().equals("wall")
                                    && !world[ii][j + 1].equals(Tileset.WALL)
                                || world[ii][j - 1].description().equals("wall")
                                    && !world[ii][j - 1].equals(Tileset.WALL)) {
                            world[ii][j] = Tileset.FLOOR;
                        }
                    }
                }
            }

            for (int ii = 0; ii < WID; ii++) {
                for (int j = 0; j < LEN; j++) {
                    if (world[ii][j].equals(Tileset.WALL)) {
                        world[ii][j] = generateTile("wall");
                    } else if (world[ii][j].equals(Tileset.FLOOR)) {
                        world[ii][j] = generateTile("hallway");
                    }
                }
            }

            int tt = random.nextInt((halfRoomLen + halfRoomWid + 2) * 4);
            while (true) {
                if (tt <= side1) {
                    doorX = x - halfRoomWid - 1;
                    doorY = y - halfRoomLen + tt;
                    if (world[doorX - 1][doorY].description().equals("hallway")) {
                        break;
                    }
                } else if (tt > side1 && tt <= side2) {
                    doorX = x - halfRoomWid + tt - side1 - 1;
                    doorY = y + halfRoomLen + 2;
                    if (world[doorX][doorY + 1].description().equals("hallway")) {
                        break;
                    }
                } else if (tt > side2 && tt <= side3) {
                    doorX = x + halfRoomWid + 2;
                    doorY = y + halfRoomLen + 1 - (tt - side2 - 1);
                    if (world[doorX + 1][doorY].description().equals("hallway")) {
                        break;
                    }
                } else {
                    doorX = x + halfRoomWid + 1 - (tt - side3 - 1);
                    doorY = y - halfRoomLen - 1;
                    if (world[doorX][doorY - 1].description().equals("hallway")) {
                        break;
                    }
                }
                tt = random.nextInt((halfRoomLen + halfRoomWid + 2) * 4);
            }
            rooms.get(i).setDoor(doorX, doorY);
            world[doorX][doorY] = generateTile("door");
        }
    }

    public void worldPaint() {
        for (int i = 0; i < WID; i++) {
            for (int j = 0; j < LEN; j++) {
                if (world[i][j].description().equals("wall")) {
                    world[i][j] = generateTile("wall");
                } else if (world[i][j].description().equals("hallway")) {
                    world[i][j] = generateTile("hallway");
                }
            }
        }
    }

    public void generateBlank() {
        Long temp = Long.valueOf(seed + 123456789);
        Random tempRan = new Random(temp);
        int sizeMax = (int) (Math.floor(WID * LEN * 0.95));
        int sizeMin = (int) (Math.floor(WID * LEN * 0.92) + 1);
        int size = tempRan.nextInt(sizeMax - sizeMin) + (int) Math.floor(WID * LEN * 0.92);
        int leftUpper = tempRan.nextInt((LEN * WID - size) / 3
                - (LEN * WID - size) / 4 + 1) + (LEN * WID - size) / 4;
        int rightUpper = tempRan.nextInt((LEN * WID - size - leftUpper) / 3
                - (LEN * WID - size - leftUpper) / 4 + 1) + (LEN * WID - size - leftUpper) / 4;
        int leftBottom = tempRan.nextInt((LEN * WID - size - leftUpper - rightUpper)
                - (LEN * WID - size - leftUpper - rightUpper) / 2 + 1)
                + (LEN * WID - size - leftUpper - rightUpper) / 2;
        int rightBottom = LEN * WID - size - leftUpper - rightUpper - leftBottom;

        Random seaRand = new Random(seed + 2333);
        leftUpperBlank(leftUpper, seaRand);
        rightUpperBlank(rightUpper, seaRand);
        leftBottomBlank(leftBottom, seaRand);
        rightBottomBlank(rightBottom, seaRand);
    }

    public void leftUpperBlank(int leftUpper, Random seaRand) {
        char[] ssea = new char[]{' ', '≈'};
        int boundX = 0;
        int boundY = LEN - 1;
        while (leftUpper >= 4) {
            int length = Math.min(Math.min(WID - 5, LEN - 5), leftUpper / 4);
            for (int i = boundX + 0; i <= Math.min(boundX + length, WID - 2); i++) {
                int k = seaRand.nextInt(2);
                TETile sea = new TETile(ssea[k], generateColor("sea")[0],
                        generateColor("sea")[1], "sea");
                world[i][boundY] = sea;
                world[i + 1][boundY - 1] = generateTile("wall");
            }
            for (int j = Math.max(boundY - length, 1); j <= boundY; j++) {
                int k = seaRand.nextInt(2);
                TETile sea = new TETile(ssea[k], generateColor("sea")[0],
                        generateColor("sea")[1], "sea");
                world[boundX][j] = sea;
                world[boundX + 1][j - 1] = generateTile("wall");
            }
            world[boundX][Math.max(boundY - length - 1, 0)] = generateTile("wall");
            world[Math.min(boundX + length + 1, WID)][boundY] = generateTile("wall");
            leftUpper -= length * 2;
            boundX++;
            boundY--;
        }
    }

    public void rightUpperBlank(int rightUpper, Random seaRand) {
        char[] ssea = new char[]{' ', '≈'};
        int boundX = WID - 1;
        int boundY = LEN - 1;
        while (rightUpper >= 4) {
            int length = Math.min(Math.min(WID - 5, LEN - 5), rightUpper / 4);
            for (int i = Math.max(boundX - length, 1); i <= boundX; i++) {
                int k = seaRand.nextInt(2);
                TETile sea = new TETile(ssea[k], generateColor("sea")[0],
                        generateColor("sea")[1], "sea");
                world[i][boundY] = sea;
                world[i - 1][boundY - 1] = generateTile("wall");
            }
            for (int j = Math.max(boundY - length, 1); j <= boundY; j++) {
                int k = seaRand.nextInt(2);
                TETile sea = new TETile(ssea[k], generateColor("sea")[0],
                        generateColor("sea")[1], "sea");
                world[boundX][j] = sea;
                world[boundX - 1][j - 1] = generateTile("wall");
            }
            world[boundX][Math.max(boundY - length - 1, 0)] = generateTile("wall");
            world[Math.max(boundX - length - 1, 0)][boundY] = generateTile("wall");
            rightUpper -= length * 2;
            boundX--;
            boundY--;
        }
    }

    public void leftBottomBlank(int leftBottom, Random seaRand) {
        char[] ssea = new char[]{' ', '≈'};
        int boundX = 0;
        int boundY = 0;
        while (leftBottom >= 4) {
            int length = Math.min(Math.min(WID - 5, LEN - 5), leftBottom / 4);
            for (int i = boundX; i <= Math.min(boundX + length, WID - 2); i++) {
                int k = seaRand.nextInt(2);
                TETile sea = new TETile(ssea[k], generateColor("sea")[0],
                        generateColor("sea")[1], "sea");
                world[i][boundY] = sea;
                if (!world[i + 1][boundY + 1].description().equals("sea")) {
                    world[i + 1][boundY + 1] = generateTile("wall");
                }
            }
            for (int j = boundY; j <= Math.min(boundY + length, LEN - 2); j++) {
                int k = seaRand.nextInt(2);
                TETile sea = new TETile(ssea[k], generateColor("sea")[0],
                        generateColor("sea")[1], "sea");
                world[boundX][j] = sea;
                if (!world[boundX + 1][j + 1].description().equals("sea")) {
                    world[boundX + 1][j + 1] = generateTile("wall");
                }
            }
            int idxTemp = Math.min(boundY + length + 1, LEN - 1);
            if (!world[boundX][idxTemp].description().equals("sea")) {
                world[boundX][idxTemp] = generateTile("wall");
            }
            idxTemp = Math.min(boundX + length + 1, WID - 1);
            if (!world[idxTemp][boundY].description().equals("sea")) {
                world[idxTemp][boundY] = generateTile("wall");
            }
            leftBottom -= length * 2;
            boundX++;
            boundY++;
        }
    }

    public void rightBottomBlank(int rightBottom, Random seaRand) {
        char[] ssea = new char[]{' ', '≈'};
        int boundX = WID - 1;
        int boundY = 0;
        while (rightBottom >= 4) {
            int length = Math.min(Math.min(WID - 5, LEN - 5), rightBottom / 4);
            for (int i = Math.max(boundX - length, 1); i <= boundX; i++) {
                int k = seaRand.nextInt(2);
                TETile sea = new TETile(ssea[k], generateColor("sea")[0],
                        generateColor("sea")[1], "sea");
                world[i][boundY] = sea;
                if (!world[i - 1][boundY + 1].description().equals("sea")) {
                    world[i - 1][boundY + 1] = generateTile("wall");
                }
            }
            for (int j = boundY; j <= Math.min(boundY + length, LEN - 2); j++) {
                int k = seaRand.nextInt(2);
                TETile sea = new TETile(ssea[k], generateColor("sea")[0],
                        generateColor("sea")[1], "sea");
                world[boundX][j] = sea;
                if (!world[boundX - 1][j + 1].description().equals("sea")) {
                    world[boundX - 1][j + 1] = generateTile("wall");
                }
            }
            int idxTemp = Math.min(boundY + length + 1, LEN - 1);
            if (!world[boundX][idxTemp].description().equals("sea")) {
                world[boundX][idxTemp] = generateTile("wall");
            }
            if (!world[Math.max(boundX - length - 1, 0)][boundY].description().equals("sea")) {
                world[Math.max(boundX - length - 1, 0)][boundY] = generateTile("wall");
            }
            rightBottom -= length * 2;
            boundX--;
            boundY++;
        }
    }

    public void generateLight() {
        Color c1 = new Color(81, 145, 130);
        Color c2 = new Color(149, 200, 255);
        Color c3 = new Color(98, 123, 246);
        Color c4 = new Color(161, 137, 150);
        Color c5 = new Color(255, 180, 244);
        Color c6 = new Color(167, 92, 230);
        Color c7 = Color.white;
        Color[] colorSetTT = new Color[]{c1, c2, c3, c4, c5, c6, c7};
        Random lightRan = new Random(seed + 999);
        int[] visited = new int[]{0, 0, 0, 0, 0, 0, 0};
        for (int i = 0; i < roomNum; i++) {
            int t = lightRan.nextInt(7);
            while (visited[t] != 0) {
                t = lightRan.nextInt(7);
            }
            //System.out.println("aaaaaaaaaaaaa123456789");
            visited[t] = 1;
            Room r = rooms.get(i);
            int xx = r.x() - Math.abs(r.x() - r.doorX()) + 2;
            int yy = r.y() - Math.abs(r.y() - r.doorY()) + 2;
            lights.put(colorSetTT[t], new Position(xx, yy));
            for (int j = xx - 10; j <= xx + 10; j++) {
                for (int k = yy - 10; k <= yy + 10; k++) {
                    if (j < r.x() - r.halfWid() || j > r.x() + r.halfWid() + 1
                            || k < r.y() - r.halfLen() || k > r.y() + r.halfLen() + 1) {
                        continue;
                    }
                    if (world[j][k].description().equals("room")) {
                        int rr = Math.max(0, colorSetTT[t].getRed()
                                - Math.abs(xx - j) * 20 - Math.abs(yy - k) * 20);
                        int gg = Math.max(0, colorSetTT[t].getGreen()
                                - Math.abs(xx - j) * 20 - Math.abs(yy - k) * 20);
                        int bb = Math.max(0, colorSetTT[t].getBlue()
                                - Math.abs(xx - j) * 20 - Math.abs(yy - k) * 20);
                        world[j][k] = new TETile('.', Color.white,
                                new Color(rr, gg, bb), "light");
                    }
                }
            }
        }
        ArrayList<Color> d = new ArrayList<>(lights().keySet());
        Color[] dd = new Color[d.size()];
        int idx = 0;
        int[] visitedColor = new int[]{0, 0, 0, 0, 0, 0, 0};
        //System.out.println("d.size() + " + d.size());
        while (idx < d.size()) {
            //System.out.println("idx: " + idx);
            int ttt = random.nextInt(d.size());
            while (visitedColor[ttt] > 0) {
                ttt = random.nextInt(d.size());
                //System.out.println(ttt);
            }
            visitedColor[ttt]++;
            dd[idx] = d.get(ttt);
            idx++;

        }
        //System.out.println("cccccccccc123456789");
        this.colorSet = dd;

    }

    public Color[] generateColor(String type) {
        Color[] result = new Color[2];
        switch (type) {
            case "room":
                result[0] = Color.white;
                result[1] = Color.black;
                break;
            case "wall":
                //result[0] = new Color(240, 200, 200);
                result[0] = Color.black;
//                result[1] = new Color(92, 92, 92);
                //System.out.println(theme);
                if (theme == 'w') {
                    result[1] = new Color(92, 92, 92);
                } else if (theme == 'b') {
                    result[1] = new Color(102, 51, 0);
                } else if (theme == 'y') {
                    result[1] = new Color(255, 204, 51);
                } else if (theme == 'g') {
                    result[1] = new Color(101, 210, 39);
                } else if (theme == 'r') {
                    result[1] = new Color(150, 19, 19);
                } else if (theme == 'p') {
                    result[1] = new Color(233, 82, 160);
                }
                break;
            case "door":
                result[0] = Color.black;
                result[1] = Color.black;
                break;
            case "hallway":
                result[0] = new Color(195, 195, 195);
                result[1] = Color.black;
                break;
            case "sea":
                result[0] = new Color(25, 160, 186);
                result[1] = new Color(9, 33, 45);
                break;
            default:
                result[0] = Color.black;
                result[1] = Color.black;
        }
        return result;
    }

    public TETile generateTile(String type) {
        Color[] c = generateColor(type);
        switch (type) {
            case "wall":
                return new TETile(' ', c[0], c[1], type);
                //break;
            case "door":
                return new TETile(' ', c[0], c[1], type);
                //break;
            case "hallway":
            case "room":
                return new TETile('·', c[0], c[1], type);
                //break;

            default:
                return Tileset.NOTHING;
        }
    }

    /**
     * check if the game is over:
     * 0: not over
     * 1: win
     * 2: lose
     */
    public int checkGameOver(Player player) {
        int playerX = player.x();
        int playerY = player.y();
        for (Color c : lights.keySet()) {
            if (lights.get(c).equals(new Position(playerX, playerY))
                    && !(c.equals(player.color()))) {
                return 2;
            }
        }
        if (reached == roomNum) {
            return 1;
        }
        return 0;
    }

    /*public void show(TETile[][] worldT) {
        if (worldT == null) {
            terenderer.renderFrame(world);
        } else {
            terenderer.renderFrame(worldT);
        }
    }*/

    public void update(Player player) {
        int playerX = player.x();
        int playerY = player.y();
        if (lights.get(player.color()).equals(new Position(playerX, playerY))) {
            reached += 1;
        }
    }

    public int roomNum() {
        return this.roomNum;
    }

    public List<Room> rooms() {
        return this.rooms;
    }

    public TETile[][] world() {
        return this.world;
    }

    public HashMap<Color, Position> lights() {
        return this.lights;
    }

    public int reached() {
        return this.reached;
    }

    public Color[] colorSet() {
        return this.colorSet;
    }

}


