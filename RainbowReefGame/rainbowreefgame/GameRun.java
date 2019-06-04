package rainbowreefgame;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class GameRun implements Runnable {
    //for Runnable
    private Thread thread;
    private boolean running = false;
    //game panels and properties
    private JFrame frame;
    private GameWorld gameWorld;
    private final GameObservable observable;
    private int SCREEN_HEIGHT,SCREEN_WIDTH, worldHeight, worldWidth;
    //map
    private Map map = new Map();
    private int[][] gameMap;
    //game items
    private static Pop pop;
    private static Katch katch;
    private KatchControls katchControls;
    //items' images
    private BufferedImage backgroundImage, winImage, overImage, katchImage, popImage, wallImage, pUpImage, blockSolidImage,
            biglegImage, biglegSmallImage, block1i,block2i, block3i, block4i, block5i, block6i, block7i, blockDouble;
    //items' ArrayLists
    private ArrayList<Wall> wallItems;
    private ArrayList<BlockSolid> blockSolidItems;
    private ArrayList<Block> blockItems;
    private ArrayList<Bigleg> biglegItems;
    private ArrayList<PowerUp> pUpItems;

    //main method where program is ran
    public static void main(String args[]){
        //audio object to play background music
        Audio audio = new Audio();
        audio.backgroundMusic();
        //create game and start
        GameRun game = new GameRun();
        game.start();
    }

    private GameRun() {
        this.observable = new GameObservable();
    }

    private synchronized void start() {
        if (running) {
            return;
        }
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            //initialize window and world
            this.SCREEN_WIDTH = 625;
            this.SCREEN_HEIGHT = 470;
            this.worldWidth = 615;
            this.worldHeight = 470;

            //load important images
            try {
                backgroundImage = ImageIO.read(GameRun.class.getResource("resources/Background2.bmp"));
                winImage = ImageIO.read(GameRun.class.getResource("resources/Congratulation.gif"));
                overImage = ImageIO.read(GameRun.class.getResource("resources/gameOver.png"));
            } catch (IOException e) {
                System.out.println("One or more image(s) not found.");
            }

            //create gameWorld
            this.gameWorld = new GameWorld(worldWidth, worldHeight,
                    backgroundImage,
                    winImage, overImage);
            //setting up game
            setGame();

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            while (running) {
                //update
                this.observable.setChanged();
                this.observable.notifyObservers();
                this.gameWorld.repaint();
                Thread.sleep(1000 / 144);
                //level up if a level is beaten
                if (gameWorld.getLvlFlag()) {
                    gameWorld.setLvlFlag(false);
                    gameWorld.levelUp();
                    //remove all map items
                    blockItems.removeAll(blockItems);
                    blockSolidItems.removeAll(blockSolidItems);
                    pUpItems.removeAll(pUpItems);
                    //get new level map
                    this.gameMap = map.getGameMap(gameWorld.getLevel());
                    if(gameMap == null){
                        //if no new map, means game is beaten
                        //update to print congratulations
                        this.gameWorld.repaint();
                    }else {
                        //otherwise create items for new level
                        createMapItems();
                    }
                }
            }

        } catch (InterruptedException e) {
            Logger.getLogger(GameRun.class.getName()).log(Level.SEVERE, null, e);
        }

        //end if not running
        if (!running) {
            return;
        }
        //stop running
        running = false;
        //join threads in the end
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    //set katch, pop, images, map to start game
    private void setGame() {
        //load and render all images
        try {
            katchImage = ImageIO.read(GameRun.class.getResource("resources/Katch_1.gif"));
            popImage = ImageIO.read(GameRun.class.getResource("resources/Pop_1.gif"));
            wallImage = ImageIO.read(GameRun.class.getResource("resources/Wall.gif"));
            blockSolidImage = ImageIO.read(GameRun.class.getResource("resources/Block_solid.gif"));
            pUpImage = ImageIO.read(GameRun.class.getResource("resources/Block_life.gif"));
            blockDouble = ImageIO.read(GameRun.class.getResource("resources/Block_double.gif"));
            biglegImage = ImageIO.read(GameRun.class.getResource("resources/Bigleg_1.gif"));
            biglegSmallImage = ImageIO.read(GameRun.class.getResource("resources/Bigleg_small_1.gif"));
            block1i = ImageIO.read(GameRun.class.getResource("resources/Block1.gif"));
            block2i = ImageIO.read(GameRun.class.getResource("resources/Block2.gif"));
            block3i = ImageIO.read(GameRun.class.getResource("resources/Block3.gif"));
            block4i = ImageIO.read(GameRun.class.getResource("resources/Block4.gif"));
            block5i = ImageIO.read(GameRun.class.getResource("resources/Block5.gif"));
            block6i = ImageIO.read(GameRun.class.getResource("resources/Block6.gif"));
            block7i = ImageIO.read(GameRun.class.getResource("resources/Block7.gif"));
        } catch (IOException e) {
            System.out.println("One or more image(s) not found.");
        }

        //initialize katch and its controls
        GameRun.katch = new Katch(this, katchImage,
                this.worldWidth / 2 - katchImage.getWidth() / 2,
                this.worldHeight - katchImage.getHeight() - popImage.getHeight(),
                3, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_SPACE);
        //initialize pop and its speed
        GameRun.pop = new Pop(this, GameRun.katch, popImage, 1);
        GameRun.katch.setPop(GameRun.pop);
        this.katchControls = new KatchControls(katch);
        //observer
        this.observable.addObserver(katch);
        this.observable.addObserver(pop);
        //gameWorld
        this.gameWorld.setKatch(GameRun.katch);
        this.gameWorld.setPop(GameRun.pop);
        this.gameMap = map.getGameMap(gameWorld.getLevel());
        //create map
        createMapItems();
        //JFrame
        setFrame();
    }

    //mainly to initialize and populate ArrayLists
    private void createMapItems() {
        wallItems = new ArrayList<>();
        blockSolidItems = new ArrayList<>();
        blockItems = new ArrayList<>();
        biglegItems = new ArrayList<>();
        pUpItems = new ArrayList<>();

        //populate ArrayLists based on map
        int size = 19;
        for (int y = 0; y < 23; y++) {
            for (int x = 0; x < 32; x++) {
                if (gameMap[y][x] == 1) { //wall
                    wallItems.add(new Wall(x * size, y * size,
                            wallImage.getWidth(), wallImage.getHeight(), wallImage));
                }
                else if (gameMap[y][x] == 2) { //solid block
                    blockSolidItems.add(new BlockSolid(x * size, y * size,
                            blockSolidImage.getWidth(), blockSolidImage.getHeight(), blockSolidImage));
                }
                else if (gameMap[y][x] == 3) { //power up(life)
                    pUpItems.add(new PowerUp(x * size, y * size,
                            pUpImage.getWidth(), pUpImage.getHeight(), pUpImage));
                }
                else if (gameMap[y][x] == 4) { //double block (double points not implemented, unfortunately)
                    blockItems.add(new Block(x * size, y * size,
                            blockDouble.getWidth(), blockDouble.getHeight(), blockDouble));
                }
                else if (gameMap[y][x] == 5) { //big bigleg
                    biglegItems.add(new Bigleg(x * size, y * size,
                            biglegImage.getWidth(), biglegImage.getHeight(), biglegImage));
                }
                else if (gameMap[y][x] == 6) { //small bigleg
                    biglegItems.add(new Bigleg(x * size, y * size,
                            biglegSmallImage.getWidth(), biglegSmallImage.getHeight(), biglegSmallImage));
                }
                else if (gameMap[y][x] == 7) { //block 1
                    blockItems.add(new Block(x * size, y * size,
                            block1i.getWidth(), block1i.getHeight(), block1i));
                }
                else if (gameMap[y][x] == 8) { //block 2
                    blockItems.add(new Block(x * size, y * size,
                            block2i.getWidth(), block2i.getHeight(), block2i));
                }
                else if (gameMap[y][x] == 9 ) { //block 3
                    blockItems.add(new Block(x * size, y * size,
                            block3i.getWidth(), block3i.getHeight(), block3i));
                }
                else if (gameMap[y][x] == 10) { //block 4
                    blockItems.add(new Block(x * size, y * size,
                            block4i.getWidth(), block4i.getHeight(), block4i));
                }
                else if (gameMap[y][x] == 11) { //block 5
                    blockItems.add(new Block(x * size, y * size,
                            block5i.getWidth(), block5i.getHeight(), block5i));
                }
                else if (gameMap[y][x] == 12) { //block 6
                    blockItems.add(new Block(x * size, y * size,
                            block6i.getWidth(), block6i.getHeight(), block6i));
                }else if (gameMap[y][x] == 13) { //block 7
                    blockItems.add(new Block(x * size, y * size,
                            block7i.getWidth(), block7i.getHeight(), block7i));
                }
            }
        }

        //update gameWorld's ArrayLists
        this.gameWorld.setItems(wallItems,blockSolidItems,blockItems,pUpItems,biglegItems);

        wallItems.forEach((item) -> {
            this.observable.addObserver(item);
        });
        blockSolidItems.forEach((item) -> {
            this.observable.addObserver(item);
        });
        blockItems.forEach((item) -> {
            this.observable.addObserver(item);
        });
        pUpItems.forEach((item) -> {
            this.observable.addObserver(item);
        });
        biglegItems.forEach((item) -> {
            this.observable.addObserver(item);
        });

    }

    //setting up properties of JFrame
    private void setFrame(){
        frame = new JFrame("Super Rainbow Reef Game");
        this.frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.frame.setResizable(false);
        this.frame.setLocationRelativeTo(null);
        this.frame.add(this.gameWorld);
        this.frame.addKeyListener(katchControls);
        this.frame.setVisible(true);
    }

    //to set Pop's death zone
    int getMapHeight() {
        return this.worldHeight;
    }

    //BELOW ARE GETTERS FOR ITEMS' ARRAYLISTS
    ArrayList<Wall> getWallItems() {
        return this.wallItems;
    }

    ArrayList<BlockSolid> getBlockSolidItems() {
        return this.blockSolidItems;
    }

    ArrayList<Block> getBlockItems() {
        return this.blockItems;
    }

    ArrayList<Bigleg> getBiglegItems() {
        return this.biglegItems;
    }

    ArrayList<PowerUp> getpUpItems() {
        return this.pUpItems;
    }
}