package tankgame;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

class TankControl extends Observable {

    private Set<Integer> keys = new HashSet();
    private KeyAdapter kAdapter;

    // key controls for each tank
    TankControl() {
        kAdapter = new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                keys.add(key);
                TankControl.this.setChanged();
                TankControl.this.notifyObservers(TankControl.this);
            }
            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                keys.remove(key);
                TankControl.this.setChanged();
                TankControl.this.notifyObservers(TankControl.this);
            }

        };
    }

    KeyAdapter getKeyAdapter() {
        return kAdapter;
    }

    Set<Integer> getKeys() {
        return keys;
    }
}
