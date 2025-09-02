package Clases;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Clase Enemigo: nave roja o imagen personalizada con vidas variables.
 */
class Ene {
    int x, y;
    int w = 80, h = 50;
    private BufferedImage sprite; // Imagen opcional
    int vidas = 1; // cantidad de impactos que puede recibir antes de morir
    int tipo = 1; // tipo de enemigo

    // Constructor normal: enemigo básico de 1 vida
    Ene(int x, int y) {
        this.x = x;
        this.y = y;
        cargarImagen("/imagen/ene1.png"); // enemigo normal
    }

    // Constructor con tamaño, imagen personalizada y cantidad de vidas
    Ene(int x, int y, int ancho, int alto, String rutaImagen, int vidas, int tipo) {
        this.x = x;
        this.y = y;
        this.w = ancho;
        this.h = alto;
        this.vidas = vidas;
        this.tipo = tipo;
        cargarImagen(rutaImagen);
    }

    // Carga la imagen desde la carpeta /src/imagen/
    private void cargarImagen(String ruta) {
        try {
            sprite = ImageIO.read(getClass().getResource(ruta));
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("No se pudo cargar la imagen del enemigo");
            sprite = null; // si falla, no dibuja nada
        }
    }

    // Dibuja el enemigo
    void draw(Graphics2D g2) {
        int padding = 4; // espacio interno
        if (sprite != null) {
            g2.drawImage(sprite, x + padding, y + padding, w - 2 * padding, h - 2 * padding, null);
        }
        // si sprite es null, no dibuja nada
    }

    // Devuelve los límites para colisiones
    Rectangle getBounds() {
        return new Rectangle(x, y, w, h);
    }
}
