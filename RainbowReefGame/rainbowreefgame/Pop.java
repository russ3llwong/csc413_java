package rainbowreefgame;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.Observable;
import java.util.Observer;

public class Pop extends Items implements Observer {

    private double vx, vy, deltaX, deltaY, speed;
    private int score, life, yLimit, respawnY, counter;
    private boolean active;
    private Katch katch;
    private GameRun world;

    Pop(GameRun world, Katch katch, BufferedImage img, double speed) {
    	
        super(katch.getX(), katch.getY(), img, null);
        this.vx = 0;
        this.vy = 0;
        this.katch = katch;
        this.world = world;
        this.speed = speed;
        this.active = false;
        this.x += this.katch.getWidth();
        this.y -= this.height + 2;
        this.respawnY = y;
        this.life = 3;
        this.yLimit = world.getMapHeight();
        this.counter = 0;
        this.deltaX = x;
        this.deltaY = y;
    }

    private void setDeltaX(double deltaX) {
        this.deltaX = deltaX;
    }

    private void setDeltaY(double deltaY) {
        this.deltaY = deltaY;
    }

    double getSpeed() {
        return this.speed;
    }
    
    void active() {
        this.active = true;
    }

    void notActive() {
        this.active = false;
    }

    void setVx(double vx) {
        this.vx = vx;
    }

    void setVy(double vy) {
        this.vy = vy;
    }

    int getScore() {
        return score;
    }

    int getLife() {
        return life;
    }

    void drawImage(ImageObserver observer, Graphics2D g) {
        g.drawImage(image, (int)Math.round(deltaX), (int)Math.round(deltaY), observer);
    }

    //detect collisions
    private void collisionDetector() {
        Rectangle hitbox = new Rectangle(this.getHitbox());
        hitbox.setLocation((int)Math.round(deltaX + vx), (int)Math.round(deltaY + vy));
        Items item = null;

        //to iterate through items and check collision
        for (Wall wall : this.world.getWallItems()) {
            if (hitbox.intersects(wall.getHitbox())) {
                item = getItemCollided(item, wall);
            }
        }
        for (BlockSolid blockSolid : this.world.getBlockSolidItems()) {
            if (hitbox.intersects(blockSolid.getHitbox())) {
                item = getItemCollided(item, blockSolid);
            }
        }
        for (Block block : this.world.getBlockItems()) {
            if (hitbox.intersects(block.getHitbox())) {
                item = getItemCollided(item, block);
            }
        }
        for (Bigleg bigleg : this.world.getBiglegItems()) {
            if (hitbox.intersects(bigleg.getHitbox())) {
                item = getItemCollided(item, bigleg);
            }
        }
        for (PowerUp pUp : this.world.getpUpItems()) {
            if (hitbox.intersects(pUp.getHitbox())) {
                item = getItemCollided(item, pUp);
            }
        }
        if (!(this.katch.getShootFlag())) {
            if (hitbox.intersects(this.katch.getHitbox())) {
                item = getItemCollided(item, this.katch);
            }
        }

        //manage collision depending on type
        if (item instanceof Katch) {
            //counter++;
            collisionKatchManager();
        } else {
            collisionManager(item);
        }
    }

    //manage katch-pop collisions
    private void collisionKatchManager() {

        this.setDeltaX(deltaX + vx);
        this.setDeltaY(deltaY + vy);
        //split katch up to set different resulting collision for each part
        int popX = (int)Math.round(deltaX) + (width / 2);
        int katchX = this.katch.getX();
        int parts = 5;
        int bounds = parts - 1;
        int katchSecWidth = this.katch.getWidth() / parts;
        int[] katch = new int[bounds];
        for (int i = 0; i < bounds; i++) {
            katch[i] = katchX + (katchSecWidth * (i + 1));
        }
        //computations for angles and bounces
        if (popX < katch[0] || popX < katch[1]) {
            if (vx > 0) {
                vx *= -1;
            } else if (vx == 0) {
                vx = -1;
            } else {
                vx -= 1;
            }
        } else if (popX < katch[2]) {
            vx = 0;
        } else if (popX < katch[3]) {
            if (vx < 0) {
                vx *= -1;
            } else if (vx == 0) {
                vx = 1;
            }
        } else {
            if (vx < 0) {
                vx *= -1;
            } else if (vx == 0) {
                vx = 1;
            } else {
                vx += 1;
            }
        }
        vy *= -1;
    }

    //manage collisions detected
    private void collisionManager(Items item) {
        if (item != null) {
            this.setDeltaX(deltaX + vx);
            this.setDeltaY(deltaY + vy);
            //pop's limits
            int popLeft = (int)Math.round(deltaX);
            int popRight = (int)Math.round(deltaX) + width;
            int popTop = (int)Math.round(deltaY);
            int popBottom = (int)Math.round(deltaY) + height;
            //item's limits
            int left = item.getX();
            int right = item.getX() + item.getWidth();
            int top = item.getY();
            int bottom = item.getY() + item.getHeight();
            //manage scores
            scoreManager(item);
            int limit = Integer.MAX_VALUE;
            int[] collision = new int[]{limit, limit, limit, limit};
            if (popRight > left && popLeft < left) {
                collision[0] = popRight - left;
            }
            if (popLeft < right && popRight > right) {
                collision[1] = right - popLeft;
            }
            if (popTop < bottom && popBottom > bottom) {
                collision[2] = bottom - popTop;
            }
            if (popBottom > top && popTop < top) {
                collision[3] = popBottom - top;
            }
            int min = limit;
            int min_index = -1;
            for (int i = 0; i < 4; i++) {
                if (collision[i] < min) {
                    min = collision[i];
                    min_index = i;
                }
            }
            if (min_index == 0 || min_index == 1) { 
                vx *= -1;
            }
            if (min_index == 2 || min_index == 3) {
                vy *= -1;
            }
        }
    }

    //manage scores, and power ups
    private void scoreManager(Items item) {
        if (item instanceof Block) {
            score = score + 10;
            this.world.getBlockItems().remove(item);
        } else if (item instanceof PowerUp) {
            life++;
            score = score + 25;
            this.world.getpUpItems().remove(item);
        } else if (item instanceof Bigleg) {
            score = score + 50;
            this.world.getBiglegItems().remove(item);
        }

    }

    //to increase accuracy of collision
    private Items getItemCollided(Items item1, Items item2) {
    	
        if (item1 != null) {

            Rectangle hitbox = new Rectangle(this.getHitbox());
            hitbox.setLocation((int)Math.round(deltaX + vx), (int)Math.round(deltaY + vy));

            //computations to calculate closer item
            int xc = (int)Math.round(deltaX) + (width);
            int yc = (int)Math.round(deltaY) + (height);
            int xc_obj1 = item1.getX() + (item1.getWidth());
            int yc_obj1 = item1.getY() + (item1.getHeight());
            int xc_obj2 = item2.getX() + (item2.getWidth());
            int yc_obj2 = item2.getY() + (item2.getHeight());
            double distance1 = Math.sqrt(Math.pow((xc - xc_obj1), 2) + Math.pow((yc - yc_obj1), 2));
            double distance2 = Math.sqrt(Math.pow((xc - xc_obj2), 2) + Math.pow((yc - yc_obj2), 2));

            //determine which item is collided
            if (distance1 < distance2) {
                return item1;
            } else {
                return item2;
            }
        }
        return item2;
    }

    //flag is to check if it is a death-respawn or a levelUp-respawn
    void respawn(boolean flag) {
        vx = 0;
        vy = 0;
        notActive();
        //flag is true if it is a level-up respawn
        if(!flag) {
            life--;
        }
        this.katch.respawn();
        this.deltaY = respawnY;
        this.deltaX = this.katch.getX() + (20);
        this.katch.shootFlag();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (this.active) {
            if (counter != 0 ) {
                speed = speed + 0.1;
                if (vy < 0) {
                    vy--;
                } else {
                    vy++;
                }
            }
            //update based on velocity
            this.deltaX += vx;
            this.deltaY += vy;
            //checks if pop "dies"
            if (this.deltaY >= yLimit) {
                respawn(false);
            }
        }
        //position pop on top of katch before shooting
        if (!(this.active)) {
            this.deltaX = this.katch.getX() + (20);
        }
        collisionDetector();
    }
}