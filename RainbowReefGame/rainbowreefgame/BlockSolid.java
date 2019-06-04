package rainbowreefgame;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

public class BlockSolid extends Items implements Observer {

    BlockSolid(int x, int y, int width, int height, BufferedImage img) {
        super(x, y, width, height, img);
    }

    void drawImage(Graphics2D g) {
        g.drawImage(this.image, this.x, this.y, this);
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}