package rainbowreefgame;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

public class PowerUp extends Items implements Observer {

    PowerUp(int x, int y, int width, int height, BufferedImage img) {
        super(x, y, width, height, img);
        this.height = img.getHeight();
        this.width = img.getWidth();
    }

    void drawImage(Graphics2D g) {
        g.drawImage(this.image, this.x, this.y, this);
    }

    public void update(Observable o, Object arg) {}
}