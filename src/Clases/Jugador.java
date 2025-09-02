package Clases;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Jugador {
    int x;
    private int y;
    private int w = 80, h = 40;
    private int speed = 6;

    private BufferedImage sprite; // Imagen del jugador

    public Jugador(int x, int y) {
        this.x = x;
        this.y = y;
        cargarImagen("/imagen/P1.png"); // Ruta de la imagen
    }

    /** Carga la imagen desde la carpeta "imagen" */
    private void cargarImagen(String ruta) {
        try {
            sprite = ImageIO.read(getClass().getResource(ruta));
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("No se pudo cargar la imagen del jugador");
            sprite = null; // si falla, no dibuja nada
        }
    }

    /** Movimiento manual */
    public void mover(int dx, int anchoPantalla) {
        x += dx;
        if (x < 0) x = 0;
        if (x > anchoPantalla - w) x = anchoPantalla - w;
    }

    /** Dibuja la nave */
    public void draw(Graphics2D g2) {
        if (sprite != null) {
            g2.drawImage(sprite, x, y, w, h, null);
        }
        // si sprite es null, no dibuja nada
    }

    /** Limites de colisión */
    public Rectangle getBounds() {
        return new Rectangle(x, y, w, h);
    }

    // Getters y setters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getAncho() { return w; }
    public int getAlto() { return h; }
    public void setSpeed(int speed) { this.speed = speed; }
}
