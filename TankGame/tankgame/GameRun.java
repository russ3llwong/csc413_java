package tankgame;

import javax.swing.JFrame;

//where the game is run, where the main method is in
public class GameRun extends JFrame {

    //the size of the game screen
    final static int SCREEN_WIDTH = 640;
    final static int SCREEN_HEIGHT = 480;

    private void startGame() {
        //properties of the pop-up window
        setTitle("BASIC TANK GAME");
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        add(new GameWorld());
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        //create Audio object and play background music
        Audio audio = new Audio();
        audio.backgroundMusic();
        //create a gameRun object and start it
        GameRun game = new GameRun();
        game.startGame();
    }
}
