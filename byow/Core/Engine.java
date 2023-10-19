package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
//import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.io.*;
//import java.nio.file.Paths;
//import java.util.ArrayList;
import java.util.Random;
//import java.util.*;

public class Engine {
    //TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    World w;
    Player player;
    TERenderer t = new TERenderer();

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        Game game = new Game();
//        game.prinStatus();
        game.start();
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, both of these calls:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

//        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
//        World world1 = new World(input);
//        world1.generateWorld();
//        for (int i = 0; i < WIDTH; i++) {
//            for (int j = 0; j < HEIGHT; j++) {
//                finalWorldFrame[i][j] = world1.world()[i][j];
//            }
//        }
//        return finalWorldFrame;
        try {
            readInput(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
//        return game.printStatus();
        TETile[][] finalWorldFrame = TETile.copyOf(w.world());
        finalWorldFrame[player.x()][player.y()] = new TETile(player.avatar(),
                Color.black, player.color(), "player");
        //t.initialize(w.world().length + 5, w.world()[0].length + 5, 2, 2);
        //t.renderFrame(finalWorldFrame);
        return finalWorldFrame;
    }

    public void readInput(String input) throws IOException, ClassNotFoundException {
        input = input.toLowerCase();
        System.out.println(input);
        if (input.charAt(0) == 'n' || input.charAt(0) == 'N') {
            String s = "";
            for (int i = 0; i < input.length(); i++) {
                if (input.charAt(i) >= '0' && input.charAt(i) <= '9') {
                    s += input.charAt(i);
                }
            }
            System.out.println(s);
            input = input.substring(1 + s.length());
            if (input.charAt(0) == 's' || input.charAt(0) == 'S') {
                System.out.println(input);
                prepareString(Long.parseLong(s));
            }
        } else if (input.charAt(0) == 'l' || input.charAt(0) == 'L') {
            load();
            input = input.substring(1);
        }
        System.out.println(input);
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == 'w' || input.charAt(i) == 's'
                    || input.charAt(i) == 'a' || input.charAt(i) == 'd') {
                player.move(w.world(), input.charAt(i));
                w.update(player);
            } else if (input.charAt(i) == ':' && input.charAt(i + 1) == 'q') {
                save();
            }
        }
    }

    public void prepareString(Long seed) {
//        System.out.println("");
        w = new World(Long.toString(seed));
        int theWid = w.world().length;
        int theLen = w.world()[0].length;
//        System.out.println("try");
//        System.out.println(seed);
        w.generateWorld();
        System.out.println("world generated");

        Random random = new Random(seed + 8888);
        int playerX = random.nextInt(theWid);
        int playerY = random.nextInt(theLen);

        while (!w.world()[playerX][playerY].description().equals("hallway")) {
            playerX = random.nextInt(theWid);
            playerY = random.nextInt(theLen);
        }
        System.out.println("playerX Y generated");

        player = new Player(playerX, playerY);
        player.setColor(w.colorSet()[w.reached()]);
        //player.setColor(Color.white);
    }

    public void load() {
        //File CWD = new File(System.getProperty("user.dir"));
        //File BIN = Paths.get(CWD.getPath(), "bin").toFile();
        //File WORLD = Paths.get(CWD.getPath(), "world.txt").toFile();
        //File PLAYER = Paths.get(CWD.getPath(), "player.txt").toFile();
        File theWorld = new File("world.txt");
        File thePlayer = new File("player.txt");

        try {
            ObjectInputStream win = new ObjectInputStream(new FileInputStream(theWorld));
            w = (World) win.readObject();
            win.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            ObjectInputStream pin = new ObjectInputStream(new FileInputStream(thePlayer));
            player = (Player) pin.readObject();
            pin.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() throws IOException {
        //File CWD = new File(System.getProperty("user.dir"));
        /*File BIN = Paths.get(CWD.getPath(), "bin").toFile();
        if (!BIN.exists()) {
            BIN.mkdir();
        }*/
        //File WORLD = Paths.get(CWD.getPath(), "world.txt").toFile();
        //File PLAYER = Paths.get(CWD.getPath(), "player.txt").toFile();
        File theWorld = new File("world.txt");
        File thePlayer = new File("player.txt");
        if (!theWorld.exists()) {
            theWorld.createNewFile();
        }
        if (!thePlayer.exists()) {
            thePlayer.createNewFile();
        }
        try {
            ObjectOutputStream wout = new ObjectOutputStream(new FileOutputStream(theWorld));
            wout.writeObject(w);
            wout.close();
        } catch (IOException e) {
            return;
        }

        try {
            ObjectOutputStream pout = new ObjectOutputStream(new FileOutputStream(thePlayer));
            pout.writeObject(player);
            pout.close();
        } catch (IOException e) {
            return;
        }
    }

    private TETile[][] printStatus() {
//        TETile[][] toReturn = w.world();
        if (w != null) {
//            frame.printPage(w.world(), player);
            return w.world();
        } else {
            return null;
        }
    }
}
