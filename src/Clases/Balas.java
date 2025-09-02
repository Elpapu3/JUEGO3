package Clases;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Balas {
    int x, y, w = 6, h = 14;
    int velo = 10;
    private BufferedImage sprite; // Imagen opcional


    public Balas(int x, int y) {
        this.x = x;
        this.y = y;
        cargarImagen("/imagen/bala.png"); // ruta de la imagen

    }
    private void cargarImagen(String path) {
    	try {
    		sprite = ImageIO.read(getClass().getResource(path));

// esto es para que la bala sea chica 
    		Image tmp = sprite.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            BufferedImage resized = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = resized.createGraphics();
            g2.drawImage(tmp, 0, 0, null);
            g2.dispose();
            sprite = resized;
    	}
    	catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            sprite = null; // fallback por si no carga
        }
    }

    public void update() {
        y -= velo;
    }

    public boolean isOnScreen() {
        return y + h >= 0;
    }

    public void draw(Graphics2D g2) {
    	if(sprite != null){
    		g2.drawImage(sprite, x, y,  null);
    	}
    	else{
    		g2.setColor(Color.CYAN);
    		g2.fillRect(x, y, w, h);
    	}
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, w, h);
    }
}
