package tankgame;

import java.awt.image.BufferedImage;
import java.util.Observable;

public class PowerUp extends Items {

    PowerUp(int x, int y, BufferedImage image) {
        super(x, y, image);
    }

    void pickedUp(Tank tank) {
        //tank gets full health
        tank.setHealth(100);
        //powerUp is now gone
        this.setVisibility(false);
    }

    @Override
    public void update(Observable obj, Object o) {

    }
}