package tankgame;

import java.awt.image.BufferedImage;
import java.util.Observable;

public class BreakableWall extends Items {

    BreakableWall(int x, int y, BufferedImage image) {
        super(x, y, image);
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}