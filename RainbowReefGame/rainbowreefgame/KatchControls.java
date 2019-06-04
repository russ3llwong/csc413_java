package rainbowreefgame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;

public class KatchControls extends Observable implements KeyListener {

    private final Katch katch;
    private final int left, right, shoot;

    //assign controls
    KatchControls(Katch katch) {
        this.katch = katch;
        this.left = katch.getLeftKey();
        this.right = katch.getRightKey();
        this.shoot = katch.getShootKey();
    }

    public void keyTyped(KeyEvent e) {}

    @Override //start moving
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == this.left) {
            this.katch.left();
        }
        if (key == this.right) {
            this.katch.right();
        }
        if (key == this.shoot) {
            this.katch.shootStatus();
        }
    }

    @Override //stop moving
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == this.left) {
            this.katch.leftFalse();
        }
        if (key == this.right) {
            this.katch.rightFalse();
        }
        if (key == this.shoot) {
            this.katch.shootStatusFalse();
        }

    }
}