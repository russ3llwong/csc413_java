package tankgame;

import static tankgame.GameRun.SCREEN_HEIGHT;
import static tankgame.GameRun.SCREEN_WIDTH;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import javax.imageio.ImageIO;

//the class that controls the game panel/world,initializes everything
public class GameWorld extends JPanel {
    //Game GameWorld Map
    private int worldWidth = 1280;
    private int worldHeight = 960;
    private BufferedImage gameWorldImage = new BufferedImage(worldWidth, worldHeight, BufferedImage.TYPE_INT_RGB);
    private Graphics2D gameWorldGraphics, graphics2D;

    //GameRun Screen
    private int SCREEN_WIDTHwithOffset = SCREEN_WIDTH - 16;
    private int SCREEN_HEIGHTwithOffset = SCREEN_HEIGHT - 39;

    //used for update method
    private Tank tank1, tank2;
    private Timer timer;

    //images
    private static BufferedImage tankImage1, tankImage2, ubWalls, bWalls, tiles, bullet1, bullet2,
            smallExplosion, largeExplosion, powerUp, lives, gameOver;

    //To store the amount of instances needed for each object
    private ArrayList<UnbreakableWall> ubWallItems = new ArrayList<>();
    private ArrayList<BreakableWall> bWallItems = new ArrayList<>();
    private ArrayList<Explosions> explosionItems = new ArrayList<>();
    private ArrayList<Bullet> bulletItems = new ArrayList<>();
    private ArrayList<PowerUp> pUpItems = new ArrayList<>();

    //flag to end game by painting game over image over screen
    private boolean flag = false;

    //constructor
    GameWorld() {
        setFocusable(true);
        setItems();
        setTanks();
        setTimer();
        timer.start();
    }

    //function to initialize items
    private void setItems() {
        //load and render all images from resources folder
        try {
            tiles = ImageIO.read(GameWorld.class.getResource("resources/bgTile.png"));
            tankImage1 = ImageIO.read(GameWorld.class.getResource("resources/cropTank1.png"));
            tankImage2 = ImageIO.read(GameWorld.class.getResource("resources/cropTank2.png"));
            bWalls = ImageIO.read(GameWorld.class.getResource("resources/Wall1.gif"));
            ubWalls = ImageIO.read(GameWorld.class.getResource("resources/Wall2.gif"));
            bullet1 = ImageIO.read(GameWorld.class.getResource("resources/bullet.gif"));
            bullet2 = ImageIO.read(GameWorld.class.getResource("resources/bullet.gif"));
            smallExplosion = ImageIO.read(GameWorld.class.getResource("resources/Explosion_small.gif"));
            largeExplosion = ImageIO.read(GameWorld.class.getResource("resources/Explosion_large.gif"));
            powerUp = ImageIO.read(GameWorld.class.getResource("resources/healthUp.png"));
            lives = ImageIO.read(GameWorld.class.getResource("resources/lives.png"));
            gameOver = ImageIO.read(GameWorld.class.getResource("resources/game_over.jpg"));
        } catch (IOException e) {
            System.out.println("One or more image(s) not found.");
        }

        //create new Map object
        Map map = new Map();
        //2D array reference
        int[][] GameMap = map.getGameMap();

        //instances of the items
        UnbreakableWall ubWall;
        BreakableWall bWall;
        PowerUp power;

        //count the amount of items needed each
        for (int y = 0; y < 30; y++) {
            for (int x = 0; x < 40; x++) {
                if (GameMap[y][x] == 1) { //1 for ubWall
                    ubWall = new UnbreakableWall(x * 32, y * 32, ubWalls);
                    ubWallItems.add(ubWall);
                } else if (GameMap[y][x] == 2) { //2 for bWall
                    bWall = new BreakableWall(x * 32, y * 32, bWalls);
                    bWallItems.add(bWall);
                } else if (GameMap[y][x] == 3) { //3 for powerUp
                    power = new PowerUp(x * 32, y * 32, powerUp);
                    pUpItems.add(power);
                }
            }
        }
    }

    //function to initialize tanks
    private void setTanks() {
        //Keys objects for the players' controls
        Keys player1 = new Keys(KeyEvent.VK_W, KeyEvent.VK_D, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_G);
        Keys player2 = new Keys(KeyEvent.VK_UP, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_SLASH);

        //initializing tank objects
        tank1 = new Tank(120, 800, (short) 0, 1, tankImage1, player1);
        tank2 = new Tank(1100, 120, (short) 180, 2, tankImage2, player2);

        //TankControl, KeyListener/Adapter and observers
        TankControl tankControls = new TankControl();
        addKeyListener(tankControls.getKeyAdapter());
        tankControls.addObserver(tank1);
        tankControls.addObserver(tank2);
    }

    //function to set the timer as game runs
    private void setTimer() {
        //timer to update game and check if game should end
        timer = new Timer(1000 / 144, (ActionEvent e) -> {
            //if either tank runs out of lives, flag is turned on, which will trigger game_over image to be painted
            if (tank1.getLives() == 0 || tank2.getLives() == 0) {
                flag = true;
            }
            createBullets();
            collisionDetector();
            GameWorld.this.repaint();
        });
    }

    //function to create bullets when tanks have fired
    private void createBullets() {
        //to create bullet items if tank shoots
        if (tank1.shootFlag()) {
            Bullet bullet = new Bullet(tank1.getX() + (tank1.getImageWidth() / 2), tank1.getY() + (tank1.getImageHeight() / 2), tank1.getAngle(), 1, bullet1);
            bulletItems.add(bullet);
            tank1.setShoot(false);
        }
        if (tank2.shootFlag()) {
            Bullet bullet = new Bullet(tank2.getX() + (tank2.getImageWidth() / 2), tank2.getY() + (tank2.getImageHeight() / 2), tank2.getAngle(), 2, bullet2);
            bulletItems.add(bullet);
            tank2.setShoot(false);
        }
    }

    //function to create hitBoxes and test every single type of collision by calling collisionHandler
    private void collisionDetector() {
        Rectangle t1HitBox = tank1.getHitBox();
        Rectangle t2HitBox = tank2.getHitBox();

        //tank to tank collision
        collisionHandler(t1HitBox,t2HitBox,tank1,null,null,null);
        collisionHandler(t2HitBox,t1HitBox,tank2,null,null,null);

        //check all breakableWall items
        for (BreakableWall bWall : bWallItems) {
            Rectangle bHitBox = bWall.getHitBox();
            //tank to breakableWall collision
            collisionHandler(t1HitBox, bHitBox, tank1,null,null,null);
            collisionHandler(t2HitBox, bHitBox, tank2,null,null,null);

            for (Bullet bullet : bulletItems) {
                Rectangle hitBoxBullet = bullet.getHitBox();
                //bullet to breakableWall collision
                collisionHandler(hitBoxBullet, bHitBox, null, bWall, bullet, null);
            }
        }

        //check all unbreakableWall items
        for (UnbreakableWall ubWall : ubWallItems) {
            Rectangle uHitBox = ubWall.getHitBox();
            //tank to unbreakableWall collision
            collisionHandler(t1HitBox, uHitBox, tank1,null,null,null);
            collisionHandler(t2HitBox, uHitBox, tank2,null,null,null);

            for (Bullet bullet : bulletItems) {
                Rectangle hitBoxBullet = bullet.getHitBox();
                //bullet to unbreakableWall collision
                collisionHandler(hitBoxBullet, uHitBox, null,null, bullet, null);
            }
        }

        //check all bullet items
        for (Bullet bullet : bulletItems) {
            Rectangle bltHitBox = bullet.getHitBox();
            //tank to bullet collision
            collisionHandler(t1HitBox,bltHitBox,tank1,null,bullet,null);
            collisionHandler(t2HitBox,bltHitBox,tank2,null,bullet,null);
        }

        //check all powerUp items
        for (PowerUp power : pUpItems) {
            Rectangle powerHitBox = power.getHitBox();
            //tank to powerUp collision
            collisionHandler(t1HitBox,powerHitBox,tank1,null,null,power);
            collisionHandler(t2HitBox,powerHitBox,tank2,null,null,power);
        }
    }

    //function to handle all types of collisions, if any collision happens
    private void collisionHandler(Rectangle hitBox1, Rectangle hitBox2, Tank tank,
                                  BreakableWall bWall, Bullet bullet, PowerUp power){
        //first checks if the hit boxes intersect
        if(hitBox1.intersects(hitBox2)){
            //using if-else statements to check objects passed in, to determine the type of collision, then handle accordingly
            if(tank != null){
                //tank to bullet collision
                if(bullet != null){
                    if(bullet.getPlayer() != tank.getPlayer()) {
                        if (tank.getHealth() <= 10) {
                            Explosions explosion = new Explosions(tank.getX(), tank.getY(), largeExplosion);
                            explosionItems.add(explosion);
                        }
                        tank.collide(bullet);
                        Explosions explosion = new Explosions(bullet.getX(), bullet.getY(), smallExplosion);
                        explosionItems.add(explosion);
                    }
                }
                //tank to powerUp collision
                else if(power != null){
                    power.pickedUp(tank);
                }
                //tank to tank collision
                else{
                    tank.stopCollision();
                }
            }
            //tank is null
            else{
                //bullet to breakableWall collision
                if(bWall != null){
                    bWall.setVisibility(false);
                    bullet.setVisibility(false);
                    Explosions explosion = new Explosions(bWall.getX(), bWall.getY(), smallExplosion);
                    explosionItems.add(explosion);
                }
                //bullet to unbreakableWall collision
                else{
                    bullet.setVisibility(false);
                }
            }
        }
    }

    @Override //method in JComponent
    public void paintComponent(Graphics g) {
        super.printComponents(g);
        //creating a graphics object from the bufferedImage
        gameWorldGraphics = gameWorldImage.createGraphics();
        //flag checks if game is over yet(when either tank runs out of lives)
        if(!flag){
            //drawing everything to the graphics object
            drawMap(gameWorldGraphics);
            tank1.draw(gameWorldGraphics);
            tank2.draw(gameWorldGraphics);
            drawMobile(gameWorldGraphics);
            splitScreen(g);
            setMiniMap(g);
            graphics2D.dispose();
            g.dispose();
        }else {
            //draws game_over image if flag is true
            g.drawImage(gameOver,0,0,null);
        }
    }

    //function to draw everything static on the map
    private void drawMap(Graphics g) {
        //drawing the map of 30x40 tiles
        for (int x = 0; x < 40; x++) {
            for (int y = 0; y < 30; y++) {
                g.drawImage(tiles, x * 32, y * 32, this);
            }
        }

        //drawing the unbreakable walls
        for (UnbreakableWall ubWall : ubWallItems) {
            //draw a ubWall
            g.drawImage(ubWall.getImage(), ubWall.getX(), ubWall.getY(), null);
            //draw it to the map
            gameWorldGraphics.drawImage(ubWall.getImage(), ubWall.getX(), ubWall.getY(), null);
        }

        //drawing the breakable walls
        for (int i = 0; i < bWallItems.size(); i++) {
            BreakableWall bWall = bWallItems.get(i);
            //determine if a bWall object is to be drawn again or not
            if (!bWall.getVisibility()) {
                //not drawn if already got shot
                bWallItems.remove(bWall);
            } else {
                //otherwise draw
                g.drawImage(bWall.getImage(), bWall.getX(), bWall.getY(), null);
                gameWorldGraphics.drawImage(bWall.getImage(), bWall.getX(), bWall.getY(), null);
            }
        }

        //drawing the power ups
        for (int i = 0; i < pUpItems.size(); i++) {
            PowerUp pUp = pUpItems.get(i);
            if (!pUp.getVisibility()) {
                pUpItems.remove(pUp);
            } else {
                g.drawImage(pUp.getImage(), pUp.getX(), pUp.getY(), null);
            }
        }

        //drawing the explosions when necessary
        for (int i = 0; i < explosionItems.size(); i++) {
            Explosions explosion = explosionItems.get(i);
            if (!explosion.getVisibility()) {
                explosionItems.remove(explosion);
            } else {
                g.drawImage(explosion.getImage(), explosion.getX(), explosion.getY(), null);
                explosion.setVisibility(false);
            }
        }
    }

    //function to draw mobile items
    private void drawMobile(Graphics g) {
        //drawing the bullets
        for (int i = 0; i < bulletItems.size(); i++) {
            Bullet bullet = bulletItems.get(i);
            if (!bullet.getVisibility()) {
                bulletItems.remove(bullet);
            } else {
                bullet.draw(g);
                bullet.move();
            }
        }

        //drawing the health bars
        g.setColor(Color.red); //tank1
        g.fillRect(tank1.getX() - 12, tank1.getY() - tank1.getImageHeight()/2 + 2, 100, 10);
        g.setColor(Color.green);
        g.fillRect(tank1.getX() - 12, tank1.getY() - tank1.getImageHeight()/2 + 2, tank1.getHealth(), 10);
        g.setColor(Color.white);
        g.drawRect(tank1.getX() - 12, tank1.getY() - tank1.getImageHeight()/2 + 2, 100, 10);
        g.setColor(Color.red); //tank2
        g.fillRect(tank2.getX() - 36, tank2.getY() - tank2.getImageHeight()/2 + 2, 100, 10);
        g.setColor(Color.green);
        g.fillRect(tank2.getX() - 36, tank2.getY() - tank2.getImageHeight()/2 + 2, tank2.getHealth(), 10);
        g.setColor(Color.white);
        g.drawRect(tank2.getX() - 36, tank2.getY() - tank2.getImageHeight()/2 + 2, 100, 10);

        //drawing the lives
        for (int i = 0; i < tank1.getLives(); i++) {
            g.drawImage(lives, tank1.getX() + (i * 20), tank1.getY() + tank1.getImageHeight() - 100, null);
        }
        for (int i = 0; i < tank2.getLives(); i++) {
            g.drawImage(lives, tank2.getX() + (i * 20), tank2.getY() + tank2.getImageHeight() - 100, null);
        }
    }

    //function to draw the miniMap on worldMap
    private void setMiniMap(Graphics g) {
        //create a subImage of the full game map
        BufferedImage miniMap = gameWorldImage.getSubimage(0, 0, worldWidth, worldHeight);
        //cast it to Graphics2D and resize it
        graphics2D = (Graphics2D) g;
        graphics2D.scale(.12, .12);
        //draw it to map
        graphics2D.drawImage(miniMap, SCREEN_WIDTH + miniMap.getWidth(), 0, null);
    }

    //function to draw 2 sub images as split screen
    private void splitScreen(Graphics g) {
        //create subImages according to tanks' locations
        //draw the subImages onto map
        BufferedImage leftScreen = gameWorldImage.getSubimage(
                viewX(tank1),
                viewY(tank1),
                SCREEN_WIDTHwithOffset / 2,
                SCREEN_HEIGHTwithOffset);
        g.drawImage(leftScreen, 0, 0, null);

        BufferedImage rightScreen = gameWorldImage.getSubimage(
                viewX(tank2),
                viewY(tank2),
                SCREEN_WIDTHwithOffset / 2,
                SCREEN_HEIGHTwithOffset);
        g.drawImage(rightScreen, SCREEN_WIDTHwithOffset / 2, 0, null);
    }

    //function to determine the camera view of tank in terms of x
    private int viewX(Tank player) {
        int xOffset = (player.getX() + player.getImageWidth() / 2 - SCREEN_WIDTHwithOffset / 2);
        int relative_position = player.getX() + xOffset;

        if (relative_position <= 0) { //cannot exceed far left
            return 0;
        } else if (relative_position < worldWidth + SCREEN_WIDTHwithOffset) {
            return (relative_position) / 2;
        } else if (relative_position >= worldWidth + SCREEN_WIDTHwithOffset / 4) { //cannot exceed far right
            return worldWidth - SCREEN_WIDTHwithOffset / 2;
        }
        return 0;
    }

    //function to determine the camera view of tank in terms of y
    private int viewY(Tank player) {
        int yOffset = (player.getY() + (player.getImageHeight() / 2) - SCREEN_HEIGHTwithOffset);
        int relative_position = player.getY() + yOffset;

        if (relative_position <= 0) { //cannot exceed top
            return 0;
        } else if (relative_position < worldHeight - SCREEN_HEIGHTwithOffset) {
            return relative_position;
        } else if (relative_position >= worldHeight - SCREEN_HEIGHTwithOffset) { //cannot exceed bottom
            return worldHeight - SCREEN_HEIGHTwithOffset;
        }
        return 0;
    }
}