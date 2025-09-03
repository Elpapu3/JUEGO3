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

    public void play() {
        if (clip != null) {
            clip.stop();
            clip.setFramePosition(0);
            clip.start();
        }
    }
    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
    
    // LE AGREGUE EL METODO ESTE PARA AJUSTAR LE VOLUMEN
    public void setVolume(float decibeles) {
        if (clip != null) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(decibeles);
        }
    }

}
