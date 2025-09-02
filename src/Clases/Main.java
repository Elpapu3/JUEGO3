package Clases;

import javax.swing.*;

/**
 * Clase principal del juego.
 * Esta es la ventana que abre el juego y muestra el "Tablero".
 */
public class Main extends JFrame {

    public Main() {
        setTitle("Space Invader"); // Título de la ventana
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Cierra el juego al apretar la X
        Tablero panel = new Tablero(); // Creamos el panel donde está el juego
        setContentPane(panel); // Lo agregamos a la ventana
        pack(); // Ajusta el tamaño al del panel
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
        setVisible(true); // Hace visible la ventana
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
