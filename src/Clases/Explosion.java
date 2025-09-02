package Clases;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Explosion {
    int x, y;
    int frame = 0;
    int maxFrames = 1; // cantidad de imágenes
    int frameDelay = 5; // cantidad de ticks antes de cambiar frame
    int tick = 0;       // contador interno
    boolean finished = false;
    private BufferedImage[] sprites;

    public Explosion(int x, int y) {
        this.x = x;
        this.y = y;

        sprites = new BufferedImage[maxFrames];
        for (int i = 0; i < maxFrames; i++) {
            try {
                sprites[i] = ImageIO.read(getClass().getResource("/imagen/explo.png"));
            } catch (IOException | IllegalArgumentException e) {
                System.out.println("No se pudo cargar sprite de explosión " + i);
                sprites[i] = null;
            }
        }
    }

    public void update() {
        if (finished) return;

        tick++;
        if (tick >= frameDelay) {
            tick = 0;
            frame++;
            if (frame >= maxFrames) {
                finished = true;
            }
        }
    }

    public void draw(Graphics g) {
        if (!finished && sprites[frame] != null) {
            g.drawImage(sprites[frame], x, y, 50, 50, null);
        }
    }
}
