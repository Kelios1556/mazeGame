package byow.Core;

import java.nio.file.Paths;
import java.util.*;
import java.io.*;
import java.awt.Color;

//import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.StdDraw;

public class Game {
    private static final int WID = 80;
    private static final int LEN = 40;

    private static final File CWD = new File(System.getProperty("user.dir"));
    private static final File BIN = Paths.get(CWD.getPath(), "bin").toFile();
    private World w;
    private Player player;
    private Frame frame = new Frame(WID, LEN);
    private Random random;
    private int gameOver; // 0: game not over;      1: win;     2: lose;
    private Color[] colorSet;
    private Long seed;
    private String seedString;
    private boolean gameGenerated = false;


//    private void readInputHelper(String first) throws IOException, ClassNotFoundException {
//
//        if (gameOver == 0) { // if the game is not generated
//            switch (first) {
//                case "n":       // generate new game
//                    break;
//                case "s":       // start a new game
//                    start();
//                    break;
//                case "l":       // load the previously saved game
//                    load();
//                    break;
//                case "q":
//                    System.exit(0);
//                    break;
//                default:        // append next long to seed
//                    try {
//                        Long.parseLong(first);
//                        seedString += first;
//                        seed = Long.parseLong(seedString);
//                    } catch (NumberFormatException e) { // exit if input is invalid
//                        System.out.println("Invalid input given: " + first);
//                        System.exit(0);
//                    }
//                    break;
//            }
//        } else { // after game start
//            switch (first) {
//                // @Note: Add my keyboard preferences
//                case "w": //move up
//                case "a": //move left
//                case "s": //move down
//                case "d": //move right
//                    player.move(w.world(), first.charAt(0));
//                    break;
//                case "l":
//
//                case "n":
//                case "q":
////                    save();
//                    System.exit(0);
//                    break;
//                default:
//            }
//        }
//    }

    public void welcome() {
        frame.welcomePage();
    }

    public void prepare(Long theSeed) {
        frame = new Frame(WID, LEN);
        w = new World(Long.toString(theSeed));
        w.generateWorld();
//        System.out.println(seed);
        //System.out.println("world generated");

        random = new Random(theSeed + 8888);
        int playerX = random.nextInt(WID);
        int playerY = random.nextInt(LEN);

        while (!w.world()[playerX][playerY].description().equals("hallway")) {
            playerX = random.nextInt(WID);
            playerY = random.nextInt(LEN);
        }
        //System.out.println("playerX Y generated");

        player = new Player(playerX, playerY);
        player.setColor(w.colorSet()[w.reached()]);
    }

    /**
     * upon opening the game.
     */
    public void start() {
        welcome();
        char re = interact();
        while (true) {
            if (re == 'n' || re == 'N') {
                this.seed = frame.seedReceiver();
                //System.out.println("seed received");
                prepare(this.seed);
                //System.out.println("preparation done");
                frame.diyPage(w, player);
                w.worldPaint();
                process();
                end();
                return;
            } else if (re == 'l' || re == 'L') {
                load();
                process();
                end();
                return;
            } else if (re == 'q' || re == 'Q') {
                System.exit(0);
                return;
            }
            re = interact();
        }
    }

    /**
     * after finishing the game.
     */

    public void end() {
        StdDraw.pause(1000);
        if (gameOver == 1) { // success
            frame.winPage();
            //System.out.println("You win!");
            char re = interact();
            while (true) {
                if (re == 'n' || re == 'N') {
                    this.seed = frame.seedReceiver();
                    prepare(this.seed);
                    frame.diyPage(w, player);
                    w.worldPaint();
                    process();
                    end();
                    return;
                } else if (re == 'q' || re == 'Q') {
                    System.exit(0);
                    return;
                }
                re = interact();
            }
        } else {
            frame.losePage();
            //System.out.println("You lose!");
            char re = interact();
            while (true) {
                if (re == 'n' || re == 'N') {
                    this.seed = frame.seedReceiver();
                    prepare(this.seed);
                    frame.diyPage(w, player);
                    w.worldPaint();
                    process();
                    end();
                    return;
                } else if (re == 'l' || re == 'L') {
                    prepare(this.seed);
                    frame.diyPage(w, player);
                    w.worldPaint();
                    process();
                    end();
                    return;
                } else if (re == 'q' || re == 'Q') {
                    System.exit(0);
                    return;
                }
                re = interact();
            }
        }


    }

    public void load() {
        File theWorld = Paths.get(BIN.getPath(), "world.txt").toFile();
        File thePlayer = Paths.get(BIN.getPath(), "player.txt").toFile();

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

    public void save() {
        File theWorld = Paths.get(BIN.getPath(), "world.txt").toFile();
        File thePlayer = Paths.get(BIN.getPath(), "player.txt").toFile();
        if (!theWorld.exists()) {
            try {
                theWorld.createNewFile();
            } catch (IOException e) {
                return;
            }
        }
        if (!thePlayer.exists()) {
            try {
                thePlayer.createNewFile();
            } catch (IOException e) {
                return;
            }
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

    /**
     * interact with keyboard.
     */
    public char interact() {
        while (!StdDraw.hasNextKeyTyped()) {
            continue;
        }
        char in = StdDraw.nextKeyTyped();
        return in;
    }

    public char interactWhileGame(boolean showPart) {
//        frame.printPage(w.world(), player);
        while (!StdDraw.hasNextKeyTyped()) {
            if (!showPart) {
                frame.showPage(w.world(), player);
            } else {
                frame.showPartPage(w.world(), player);
            }
        }
        char in = StdDraw.nextKeyTyped();
        char afterIn = ' ';
        if (in == ':') {
            while (!StdDraw.hasNextKeyTyped()) {
                if (!showPart) {
                    frame.showPage(w.world(), player);
                } else {
                    frame.showPartPage(w.world(), player);
                }
            }
            afterIn = StdDraw.nextKeyTyped();
            if (afterIn == 'q' || afterIn == 'Q') {
                save();
                System.exit(0);
            } else {
                in = afterIn;
            }
        }
        return in;
    }

    public void process() {
        gameOver = 0;
        frame.showPage(w.world(), player);
        int part = 0, whole = 0;
        boolean showPart = true;
        while (gameOver == 0) {
            char in = interactWhileGame(showPart);
            while (!((in == 'w') || (in == 'a') || (in == 's') || (in == 'd')
                    || (in == 'W') || (in == 'A') || (in == 'S') || (in == 'D'))) {
                in = interactWhileGame(showPart);
            }

            player.move(w.world(), in);
            w.update(player);

            if (showPart) {
                frame.showPartPage(w.world(), player);
                part = (++part) % 21;
            } else {
                frame.showPage(w.world(), player);
                whole = (++whole) % 21;
            }
            if (part == 20 && whole == 0) {
                showPart = !showPart;
            }
            gameOver = w.checkGameOver(player);
            if (gameOver == 0) {
                player.setColor(w.colorSet()[w.reached()]);
            }
        }
    }


}
