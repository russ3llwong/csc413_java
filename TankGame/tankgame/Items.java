package tankgame;

import java.util.Observable;
import java.util.Observer;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

//abstract superclass which is extended by all items/objects in the game
abstract class Items extends Observable implements Observer {
    private BufferedImage image;
    int x, y;
    private boolean visibility = true;

    Items(int x, int y, BufferedImage Image) {
        this.image = Image;
        this.x = x;
        this.y = y;
    }

    boolean getVisibility() {
        return this.visibility;
    }

    void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    BufferedImage getImage() {
        return this.image;
    }

    int getImageWidth() {
        return this.image.getWidth();
    }

    int getImageHeight() {
        return this.image.getHeight();
    }

    int getX() {
        return this.x;
    }

    void setX(int x) {
        this.x = x;
    }

    int getY() {
        return this.y;
    }

    void setY(int y) {
        this.y = y;
    }

    Rectangle getHitBox() {
        //construct a new Rectangle object as item's hitbox
        return new Rectangle(this.x, this.y, this.image.getWidth(), this.image.getHeight());
    }
}
