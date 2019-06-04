package tankgame;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Observable;

public class Bullet extends Items {
    private short angle;
    private int player;

    Bullet(int x, int y, short angle, int player, BufferedImage image) {
        super(x, y, image);
        this.angle = angle;
        this.player = player;
    }

    void move() {
        int deltaX, deltaY;
        final int r = 15;
        deltaX = (int) Math.round(r * Math.cos(Math.toRadians(angle)));
        deltaY = (int) Math.round(r * Math.sin(Math.toRadians(angle)));
        x += deltaX;
        y += deltaY;
    }

    int getPlayer() {
        return this.player;
    }

    void draw(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.getImage().getWidth() / 2, this.getImage().getHeight() / 2);
        Graphics2D graphic2D = (Graphics2D) g;
        graphic2D.drawImage(this.getImage(), rotation, null);
        graphic2D.draw(this.getHitBox());
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
