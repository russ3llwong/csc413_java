package rainbowreefgame;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

public class Katch extends Items implements Observer {

    //objects
    private GameRun gameRun;
    private Pop pop;
    //variables/properties
    private boolean moveLeft, moveRight, shootStatus, shootFlag;
    private int respawnX, respawnY, left, right, shoot, angle, beforeCollision;
    private double speed;

    Katch(GameRun gameRun, BufferedImage img, int x, int y, int speed, int leftKey, int rightKey, int shootKey) {
        super(x, y, img, null);
        this.left = leftKey;
        this.right = rightKey;
        this.shoot = shootKey;
        this.moveLeft = false;
        this.moveRight = false;
        this.shootStatus = false;
        this.shootFlag = true;
        this.respawnX = x;
        this.respawnY = y;
        this.gameRun = gameRun;
        this.angle = 245;
        this.speed = speed;
    }

    int getLeftKey() {
        return this.left;
    }

    void left() {
        this.moveLeft = true;
    }

    void leftFalse() {
        this.moveLeft = false;
    }

    int getRightKey() {
        return this.right;
    }

    void right() {
        this.moveRight = true;
    }

    void rightFalse() {
        this.moveRight = false;
    }

    int getShootKey() {
        return this.shoot;
    }

    void shootStatus() {
        this.shootStatus = true;
    }

    void shootStatusFalse() {
        this.shootStatus = false;
    }

    boolean getShootFlag() {
        return this.shootFlag;
    }

    void shootFlag() {
        this.shootFlag = true;
    }

    void shootFlagFalse() {
        this.shootFlag = false;
    }

    void setPop(Pop pop) {
        this.pop = pop;
    }

    void respawn() {
        this.x = respawnX;
        this.y = respawnY;
    }

    void drawImage(Graphics2D g) {
        g.drawImage(image, x, y, this);
    }

    //for blocking collision with wall by reverting to its coordinates before collision
    private void blockCollision(){
        //create hitbox
        Rectangle katchHitbox = new Rectangle(x, y, width, height);
        //iterate wall items
        for (Wall wall : this.gameRun.getWallItems()) {
            //if collision occurs, coordinate/position is reset to before it
            if (katchHitbox.intersects(wall.getHitbox())) {
                this.x = beforeCollision;
            }
        }
    }

    @Override
    public void update(Observable obj, Object arg) {
        //for shooting
        if (shootStatus && shootFlag) {
            this.pop.setVx(this.pop.getSpeed() * Math.cos(Math.toRadians(angle)));
            this.pop.setVy(this.pop.getSpeed() * Math.sin(Math.toRadians(angle)));
            this.pop.active();
            shootFlagFalse();
        }
        //save coordinates before collision(wall)
        beforeCollision = this.x;
        //for moving left
        if (moveLeft) {
            x -= speed * 1.2;
        }
        //for moving right
        if (moveRight) {
            x += speed * 1.2;
        }
        //block collision if collide into wall
        blockCollision();
    }
}