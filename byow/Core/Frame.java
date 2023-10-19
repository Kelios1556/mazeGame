package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.*;

public class Frame {

    private int WIDTH;
    private int HEIGHT;
    private static final int TILE_SIZE = 16;
    private static final int XOFF = 3;
    private static final int YOFF = 0;

    private static final Font FRONTBIG = new Font("Monaco", Font.BOLD, 50);
    private static final Font FRONTMIDDLE = new Font("Monaco", Font.BOLD, 25);
    private static final Font FRONTSMALL = new Font("Monaco", Font.BOLD, 20);
    private static final Font FRONTMAP = new Font("Monaco", Font.BOLD, TILE_SIZE - 2);

    public Frame(int wid, int hei) {
        this.WIDTH = wid + 6;
        this.HEIGHT = hei + 4;
        StdDraw.setCanvasSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
    }

    public void welcomePage() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);

        StdDraw.setFont(FRONTBIG);
        StdDraw.text(this.WIDTH / 2, this.HEIGHT * 3 / 4, "CS61BL: THE GAME");

        StdDraw.setFont(FRONTMIDDLE);
        StdDraw.line(10, this.HEIGHT * 3 / 4 - 2, this.WIDTH - 10, this.HEIGHT * 3 / 4 - 2);

        //StdDraw.textLeft(0, this.height - 1, "Round: "  + this.round);
        StdDraw.text(this.WIDTH / 2, this.HEIGHT / 2 - 10, "New Game (N)");
        StdDraw.text(this.WIDTH / 2, this.HEIGHT / 2 - 12, "Load Game (L)");
        StdDraw.text(this.WIDTH / 2, this.HEIGHT / 2 - 14, "Quit (Q)");

        StdDraw.show();
    }

    public void winPage() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);

        StdDraw.setFont(FRONTBIG);
        StdDraw.text(this.WIDTH / 2, this.HEIGHT * 3 / 4, "You Win");
        StdDraw.circle(this.WIDTH / 2, this.HEIGHT * 3 / 4, 10);

        StdDraw.setFont(FRONTMIDDLE);
        StdDraw.text(this.WIDTH / 2, this.HEIGHT / 2 - 10, "New Game (N)");
        StdDraw.text(this.WIDTH / 2, this.HEIGHT / 2 - 12, "Quit (Q)");

        StdDraw.show();
    }

    public void losePage() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);

        StdDraw.setFont(FRONTBIG);
        StdDraw.text(this.WIDTH / 2, this.HEIGHT * 3 / 4, "You Lose");
        StdDraw.circle(this.WIDTH / 2, this.HEIGHT * 3 / 4, 10);

        StdDraw.setFont(FRONTMIDDLE);
        StdDraw.text(this.WIDTH / 2, this.HEIGHT / 2 - 10, "New Game (N)");
        StdDraw.text(this.WIDTH / 2, this.HEIGHT / 2 - 12, "Try Again (L)");
        StdDraw.text(this.WIDTH / 2, this.HEIGHT / 2 - 14, "Quit (Q)");

        StdDraw.show();
        return;
    }

    public Long seedReceiver() {
        String input = "";
        char in = ' ';

        while (!(in == 'S' || in == 's')) {
            pageHelper("please type a seed (type 'S' after finishing typing)", input);
            if (StdDraw.hasNextKeyTyped()) {
                in = StdDraw.nextKeyTyped();
                if (in >= '0' && in <= '9') {
                    input += in;
                }
            }
        }
        return Long.valueOf(input);
    }

    public void diyPage(World w, Player p) {
        String[] result = new String[]{"$", "yeah", "w"};
        String a = diyAvatar();
        if (a != null) {
            p.setAvatar(a.charAt(0));
        }
        String n = diyName();
        if (n != null) {
            p.setName(n);
        }
        char re = diyTheme();
        if (!(re == 'n' || re == 'N')) {
            w.diyWorld(re);
        }
    }

    public void pageHelper(String message, String input) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(FRONTMIDDLE);
        StdDraw.text(WIDTH / 2, HEIGHT * 3 / 4, message);
        StdDraw.line(XOFF, HEIGHT * 3 / 4 - 2, WIDTH - XOFF, HEIGHT * 3 / 4 - 2);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, input);
        StdDraw.show();
    }

    public void pageHelper(String message) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(FRONTMIDDLE);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, message);
        StdDraw.show();
    }

    public String diyAvatar() {
        pageHelper("Do you want to design your own avatar? (y/n)");
        char inp = ' ';
        while (!(inp == 'y' || inp == 'Y' || inp == 'n' || inp == 'N')) {
            if (StdDraw.hasNextKeyTyped()) {
                inp = StdDraw.nextKeyTyped();
            }
        }
        if (inp == 'n' || inp == 'N') {
            return null;
        }

        String in = "";
        pageHelper("Design a avatar for your role! (only one single char supported)", in);
        while (!StdDraw.hasNextKeyTyped()) {
            continue;
        }
        in += StdDraw.nextKeyTyped();
        pageHelper("Your avatar is being built...", in);
        StdDraw.pause(1000);
        return in;
    }

    public String diyName() {
        pageHelper("Do you want to deside your own name? (y/n)");

        char inp = ' ';
        while (!(inp == 'y' || inp == 'Y' || inp == 'n' || inp == 'N')) {
            if (StdDraw.hasNextKeyTyped()) {
                inp = StdDraw.nextKeyTyped();
            }
        }
        if (inp == 'n' || inp == 'N') {
            return null;
        }

        String in = "";
        pageHelper("Type your name now! (type ':' after finishing typing your name)", in);
        while (!StdDraw.hasNextKeyTyped()) {
            continue;
        }
        char i = StdDraw.nextKeyTyped();
        in += i;
        while (i != ':') {
            pageHelper("Type your name now! (type ':' after finishing typing your name)", in);
            if (StdDraw.hasNextKeyTyped()) {
                i = StdDraw.nextKeyTyped();
                in += i;
            }
        }
        pageHelper("Hi! " + in.substring(0, in.length() - 1));
        StdDraw.pause(1000);
        return in.substring(0, in.length() - 1);
    }

    public char diyTheme() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(FRONTMIDDLE);
        StdDraw.text(WIDTH / 2, HEIGHT / 3 * 4, "Let's pick a theme!");
        StdDraw.line(XOFF, HEIGHT * 3 / 4 - 2, WIDTH - XOFF, HEIGHT * 3 / 4 - 2);
        StdDraw.text(WIDTH / 4, HEIGHT / 2, "vigorous (r)");
        StdDraw.text(WIDTH / 4, HEIGHT / 2 - 2, "luminous (y)");
        StdDraw.text(WIDTH / 4, HEIGHT / 2 - 4, "vivid (g)");
        StdDraw.text(WIDTH * 3 / 4, HEIGHT / 2, "genuine (b)");
        StdDraw.text(WIDTH * 3 / 4, HEIGHT / 2 - 2, "romantic (p)");
        StdDraw.text(WIDTH * 3 / 4, HEIGHT / 2 - 4, "simple (w)");
        StdDraw.text(WIDTH / 2, HEIGHT / 4, "do not choose (n)");
        StdDraw.show();

        char re = ' ';
        while (!(re == 'r' || re == 'y' || re == 'g' || re == 'b' || re == 'p'
                || re == 'w' || re == 'n' || re == 'R' || re == 'Y'
                || re == 'G' || re == 'B' || re == 'p' || re == 'W' || re == 'N')) {
            if (StdDraw.hasNextKeyTyped()) {
                re = StdDraw.nextKeyTyped();
            }
        }
        return re;
    }

    public void showPage(TETile[][] world, Player player) {
        int playerX = player.x();
        int playerY = player.y();
        TETile[][] toShow = TETile.copyOf(world);
        toShow[playerX][playerY] = new TETile(player.avatar(),
                Color.black, player.color(), "player");

        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(FRONTSMALL);
        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss Z");
        String date = format.format(curDate);
        StdDraw.textLeft(XOFF, HEIGHT - 2, "Hi! " + player.name());
        StdDraw.textLeft(this.WIDTH / 2, HEIGHT - 2, date);
        int mouseX = (int) Math.floor(StdDraw.mouseX());
        int mouseY = (int) Math.floor(StdDraw.mouseY());
        if (mouseX > XOFF && mouseX < XOFF + world.length
                && mouseY > YOFF && mouseY < YOFF + world[0].length) {
            StdDraw
                    .textRight(WIDTH - XOFF, HEIGHT - 2,
                            world[mouseX - XOFF][mouseY - YOFF]
                    .description());
        }
        StdDraw.line(XOFF, HEIGHT - 3, WIDTH, HEIGHT - 3);
        StdDraw.setFont(FRONTMAP);
        for (int x = 0; x < world.length; x++) {
            for (int y = 0; y < world[0].length; y++) {
                toShow[x][y].draw(x + XOFF, y + YOFF);
            }
        }
//        System.out.println(toString(toShow));
        StdDraw.show();
    }

//    public void printPage(TETile[][] world, Player player) {
//        int playerX = player.x();
//        int playerY = player.y();
//        TETile[][] toShow = TETile.copyOf(world);
//        toShow[playerX][playerY] = new TETile(player.avatar(),
//                Color.black, player.color(), "player");
////        System.out.println(player.color());
//        System.out.println(toString(toShow));
//    }

//    private String toString(TETile[][] world) {
//        int width = world.length;
//        int height = world[0].length;
//        StringBuilder sb = new StringBuilder();
//        Color c1 = new Color(81, 145, 130);
//        Color c2 = new Color(149, 200, 255);
//        Color c3 = new Color(98, 123, 246);
//        Color c4 = new Color(161, 137, 150);
//        Color c5 = new Color(255, 180, 244);
//        Color c6 = new Color(167, 92, 230);
//        Color c7 = Color.white;
//        char typeColor = '.';
//        for (int y = height - 1; y >= 0; y -= 1) {
//            for (int x = 0; x < width; x += 1) {
//                if (world[x][y] == null) {
//                    throw new IllegalArgumentException("Tile at position x=" + x + ", y=" + y
//                            + " is null.");
//                }
//                if (world[x][y].description().equals("wall")) {
//                    sb.append("#");
//                } else if (world[x][y].description().equals("sea")) {
//                    sb.append(" ");
//                } else if (world[x][y].description().equals("player")
//                        || world[x][y].description().equals("light")) {
//                    if (world[x][y].getBackgroundColor().equals(c1)) {
//                        sb.append("1");
//                    } else if (world[x][y].getBackgroundColor().equals(c2)) {
//                        sb.append("2");
//                    } else if (world[x][y].getBackgroundColor().equals(c3)) {
//                        sb.append("3");
//                    } else if (world[x][y].getBackgroundColor().equals(c4)) {
//                        sb.append("4");
//                    } else if (world[x][y].getBackgroundColor().equals(c5)) {
//                        sb.append("5");
//                    } else if (world[x][y].getBackgroundColor().equals(c6)) {
//                        sb.append("6");
//                    } else if (world[x][y].getBackgroundColor().equals(c7)) {
//                        sb.append("7");
//                    } else sb.append(world[x][y].character());
////                    sb.append(" ");
//                } else sb.append(world[x][y].character());
//            }
//            sb.append('\n');
//        }
//        return sb.toString();
//    }

    public void showPartPage(TETile[][] world, Player player) {
        int radius = 10;
        int playerX = player.x();
        int playerY = player.y();
        int boundX1 = Math.max(0, playerX - radius), boundY1 = Math.max(0, playerY - radius);
        int boundX2 = Math.min(world.length - 1, playerX + radius);
        int boundY2 = Math.min(world[0].length - 1, playerY + radius);
        TETile[][] partToShow = new TETile[radius * 2 + 1][radius * 2 + 1];
        for (int i = 0; i < radius * 2 + 1; i++) {
            for (int j = 0; j < radius * 2 + 1; j++) {
                if (i + boundX1 <= boundX2 && j + boundY1 <= boundY2) {
                    partToShow[i][j] = world[i + boundX1][j + boundY1];
                } else {
                    partToShow[i][j] = Tileset.NOTHING;
                }
            }
        }
        partToShow[playerX - boundX1][playerY - boundY1] = new TETile(player.avatar(),
                Color.black, player.color(), "player");

        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(FRONTSMALL);
        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss Z");
        String date = format.format(curDate);
        StdDraw.textLeft(XOFF, HEIGHT - 2, "Hi! " + player.name());
        StdDraw.textLeft(this.WIDTH / 2, HEIGHT - 2, date);
        int mouseX = (int) Math.floor(StdDraw.mouseX());
        int mouseY = (int) Math.floor(StdDraw.mouseY());
        if (mouseX >= boundX1 + XOFF && mouseX <= boundX2 + XOFF
                && mouseY >= YOFF + boundY1 && mouseY <= YOFF + boundY2) {
            StdDraw.textRight(WIDTH - XOFF, HEIGHT - 2,
                    world[mouseX - XOFF][mouseY - YOFF].description());
        }
        StdDraw.line(XOFF, HEIGHT - 3, WIDTH, HEIGHT - 3);
        StdDraw.setFont(FRONTMAP);
        for (int x = boundX1; x <= boundX2; x++) {
            for (int y = boundY1; y <= boundY2; y++) {
                if (!partToShow[x - boundX1][y - boundY1].equals(Tileset.NOTHING)) {
                    partToShow[x - boundX1][y - boundY1].draw(x + XOFF, y + YOFF);
                }
            }
        }
//        System.out.println(toString(partToShow));
        StdDraw.show();
    }
}

