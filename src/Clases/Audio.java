package Clases;

import javax.sound.sampled.*;
import java.io.IOException;

public class Audio {
    private Clip clip;

    public Audio(String ruta) {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(getClass().getResource(ruta));
            clip = AudioSystem.getClip();
            clip.open(audioIn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Reproduce una vez
    public void play() {
        if (clip != null) {
            clip.stop();
            clip.setFramePosition(0);
            clip.start();
        }
    }

    // Reproduce en loop infinito
    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
    
    // Nuevo método: ajustar volumen
    // valor de - 80 (muy bajo) a 6 (muy alto)
    public void setVolume(float decibeles) {
        if (clip != null) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(decibeles);
        }
    }

}
