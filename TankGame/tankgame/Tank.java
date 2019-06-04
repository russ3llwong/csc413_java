package tankgame;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

public class Tank extends Items implements Observer {
    //main properties of tank
    private int player, lives, health;
    private Set<Integer> keys;
    private Keys keyMap;

    //for positions and its calculations
    private int deltaX, deltaY, noCollisionX, noCollisionY, respawnX, respawnY;
    private final int r = 10;
    private short angle;

    //bullet-fire related
    private boolean fired;
    private long lastFire = System.currentTimeMillis();

    Tank(int x, int y, short angle, int player, BufferedImage image, Keys kmap) {
        super(x, y, image);
        keys = new HashSet();
        this.keyMap = kmap;
        this.angle = angle;
        this.fired = false;
        this.health = 100;
        this.respawnX = x;
        this.respawnY = y;
        this.lives = 3;
        this.player = player;
    }

    void stopCollision(){
        this.x = noCollisionX;
        this.y = noCollisionY;
    }

    private void blockCollision() {
        this.noCollisionX = x;
        this.noCollisionY = y;
    }

    void collide(Bullet bullet) {
        this.health -= 10;
        if (health <= 0) {
            --lives;
            respawnTank();
        }
        bullet.setVisibility(false);
    }

    private void shoot() {
        this.fired = true;
    }

    boolean shootFlag() {
        return this.fired;
    }

    void setShoot(boolean shoot) {
        this.fired = shoot;
    }

    short getAngle() {
        return this.angle;
    }

    int getLives() {
        return this.lives;
    }

    void setLives(int lives) {
        this.lives = lives;
    }

    int getHealth() {
        return this.health;
    }

    void setHealth(int health) {
        this.health = health;
    }

    int getPlayer() {
        return this.player;
    }

    private void respawnTank() {
        if (lives == 0) {
            if (this.player == 1) {
                System.out.println("Congratulations Player 2! You have beaten Player 1.");
            } else if (this.player == 2) {
                System.out.println("Congratulations Player 1! You have beaten Player 2.");
            }
        }
        //reset health and spawn it
        this.health = 100;
        this.x = respawnX;
        this.y = respawnY;
    }

    void draw(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.getImage().getWidth() / 2, this.getImage().getHeight() / 2);
        Graphics2D graphic2D = (Graphics2D) g;
        graphic2D.drawImage(this.getImage(), rotation, null);
    }

    private void forMoving() {
        final long threshold = 500;

        if (keys.contains(keyMap.getForwardKey())) {
            deltaX = (int) Math.round(r * Math.cos(Math.toRadians(angle)));
            deltaY = (int) Math.round(r * Math.sin(Math.toRadians(angle)));
            x += deltaX;
            y += deltaY;
        }
        if (keys.contains(keyMap.getRightKey())) {
            this.angle += 10;
        }
        if (keys.contains(keyMap.getLeftKey())) {
            this.angle -= 10;
        }
        if (keys.contains(keyMap.getBackwardKey())) {
            deltaX = (int) Math.round(r * Math.cos(Math.toRadians(angle)));
            deltaY = (int) Math.round(r * Math.sin(Math.toRadians(angle)));
            x -= deltaX;
            y -= deltaY;
        }
        if (keys.contains(keyMap.getShootKey())) {
            //interval between each fire
            long current = System.currentTimeMillis();
            if ((current - threshold) > lastFire) {
                this.shoot();
                lastFire = current;
            }
        }
    }

    @Override
    public void update(Observable o, Object obj) {
        tankgame.TankControl controller = (tankgame.TankControl) o;
        keys = controller.getKeys();
        blockCollision();
        forMoving();
    }
}