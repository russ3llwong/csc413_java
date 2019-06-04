package rainbowreefgame;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import javax.swing.JComponent;

//all game items/objects extends the Items class, which is a child of JComponent
public class Items extends JComponent {

    int x, y, width, height;
    BufferedImage image;
    private Rectangle hitbox;

    //constructor for observed objects
    Items(int x, int y, BufferedImage image, ImageObserver observer) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = image.getWidth(observer);
        this.height = image.getHeight(observer);
    }

    //constructor for static objects
    Items(int x, int y, int width, int height, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
        this.hitbox = new Rectangle(x, y, this.width, this.height);
    }

    //getter for hitbox
    Rectangle getHitbox() {
        return new Rectangle(x, y, width, height);
    }

    //METHODS BELOW ARE REQUIRED TO BE OVERRIDDEN
    @Override
    public void setLocation(int newX, int newY) {
        this.x = newX;
        this.y = newY;
        this.hitbox = new Rectangle(newX, newY);
    }

    @Override
    public void setLocation(Point newLocation) {
        this.x = newLocation.x;
        this.y = newLocation.y;
        this.hitbox.setLocation(newLocation);
    }

    @Override
    public void setSize(int newWidth, int newHeight) {
        this.width = newWidth;
        this.height = newHeight;
        this.hitbox.setSize(newWidth, newHeight);
    }

    @Override
    public void setSize(Dimension dim) {
        this.hitbox.setSize(dim);
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public Point getLocation() {
        return new Point(this.x, this.y);
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public Dimension getSize() {
        return this.hitbox.getSize();
    }
}
