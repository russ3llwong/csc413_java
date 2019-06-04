package rainbowreefgame;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JPanel;

public class GameWorld extends JPanel {
    //properties of windows
	private BufferedImage backgroundImage, screen, winImage, overImage, worldImage;
    private Graphics2D worldGraphics;
    private int worldWidth, worldHeight;
    //game stuff
    private int level = 1;
    private static Pop pop; 
    private static Katch katch;
    //for condition checking
    private boolean overFlag = false;
    private boolean winFlag = false;
    private boolean lvlFlag = false;
    //for items
    private ArrayList<Wall> wallItems = new ArrayList<>();
    private ArrayList<BlockSolid> blockSolidItems = new ArrayList<>();
    private ArrayList<Block> blockItems = new ArrayList<>();
    private ArrayList<Bigleg> biglegItems = new ArrayList<>();
    private ArrayList<PowerUp> pUpItems = new ArrayList<>();

    GameWorld(int worldWidth, int worldHeight, BufferedImage backgroundImage, BufferedImage winImage,
              BufferedImage overImage) throws IOException {
        super();
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;

        this.setSize(worldWidth, worldHeight);
        this.setPreferredSize(new Dimension(worldWidth, worldHeight));

        this.backgroundImage = backgroundImage;
        this.winImage = winImage;
        this.overImage = overImage;
    }

    //initialize arrayLists
    void setItems(ArrayList<Wall> walls,ArrayList<BlockSolid> blockSolidBlocks,ArrayList<Block> coralBlocks,
                  ArrayList<PowerUp> powerUp,ArrayList<Bigleg> bigLegs){
        this.wallItems = walls;
        this.blockSolidItems = blockSolidBlocks;
        this.blockItems = coralBlocks;
        this.pUpItems = powerUp;
        this.biglegItems = bigLegs;
    }

    void setKatch(Katch katch) {
    	GameWorld.katch = katch;
    }

    void setPop(Pop pop) {
    	GameWorld.pop = pop;
    }

    boolean getLvlFlag() {
        return lvlFlag;
    }

    void setLvlFlag(boolean bool) {
        this.lvlFlag = bool;
    }

    int getLevel() {
        return level;
    }

    void levelUp() {
        level++;
    }

    //for drawing basically everything
    private void drawMap(Graphics2D g) {
        //draw all items by iterating through arrayLists
        wallItems.forEach((current) -> {
            current.drawImage(g);
        });
        blockSolidItems.forEach((current) -> {
            current.drawImage(g);
        });
        blockItems.forEach((current) -> {
            current.drawImage(g);
        });
        pUpItems.forEach((current) -> {
            current.drawImage(g);
        });
        biglegItems.forEach((current) -> {
            current.drawImage(g);
        });
        //draw katch and pop
        katch.drawImage(g);
        pop.drawImage(this, g);
        //drawing scores and life
        int score = pop.getScore();
        int life = pop.getLife();
        g.drawString("SCORE: " + score, 150, 13);
        g.drawString("REMAINING LIVES: " + life, 20, 13);
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        //initialize worldImage and create graphics from it
        this.worldImage = new BufferedImage(worldWidth, worldHeight, BufferedImage.TYPE_INT_ARGB);
        this.worldGraphics = worldImage.createGraphics();

        //conditions to end game or level up
        if (level > 3) {
            winFlag = true;
        }
        if (biglegItems.isEmpty()) {
            lvlFlag = true;
            pop.respawn(lvlFlag);
        }
        if (pop.getLife() <= 0) {
            overFlag = true;
        }
        //draw based on conditions
        if (!overFlag && !winFlag) {
            worldGraphics.drawImage(this.backgroundImage, 0, 0, this);
            drawMap(worldGraphics);
            screen = worldImage;
        } else if (winFlag) {
            worldGraphics.drawImage(this.winImage, 0, 0, 610, 435, this);
            screen = worldImage;
        } else if (lvlFlag) {
            worldGraphics.drawImage(this.backgroundImage, 0, 0, this);
            drawMap(worldGraphics);
            lvlFlag = false;
            screen = worldImage;
        } else if (overFlag) {
            worldGraphics.drawImage(this.overImage, 0, 0, this);
            screen = worldImage;
        }
        g.drawImage(screen, 0, 0, this);
    }
}