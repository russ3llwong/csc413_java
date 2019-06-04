package tankgame;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.net.URL;

class Audio {

    void backgroundMusic(){
        AudioInputStream inputStream;
        //URL path to the file
        URL path = GameWorld.class.getResource("resources/Music.wav"); //mp3 not supported, converted to wav

        try{
            File file = new File(path.getPath());
            //create AudioInputStream object
            inputStream = AudioSystem.getAudioInputStream(file.getAbsoluteFile());
            //clip reference
            Clip clip = AudioSystem.getClip();
            //open AudioInputStream to Clip
            clip.open(inputStream);
            clip.loop(100);
            clip.start();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

}
