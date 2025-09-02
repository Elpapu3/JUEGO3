package Clases;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Balas_enemigo {
    int x, y, w = 10, h = 20;
    int velo = 5;
    private BufferedImage sprite; // Imagen opcional


    public Balas_enemigo(int x, int y) {
        this.x = x;
        this.y = y;
        cargarImagen("/imagen/bome.png"); // ruta de la imagen

    }

    public void update() {
    	
        y += velo;
        
    }

    public boolean Pantalla(int height) {
        return y <= height;
    }

    public void draw(Graphics2D g2) {
    	  if(sprite != null) {
    	        g2.drawImage(sprite, x, y, w, h, null);
    	    } else {
    	        g2.setColor(Color.MAGENTA);
    	        g2.fillRect(x, y, w, h);
    	    }
    }
    private void cargarImagen(String path) {
        try {
            sprite = javax.imageio.ImageIO.read(getClass().getResource(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Rectangle limites() {
        return new Rectangle(x, y, w, h);
    }
}
