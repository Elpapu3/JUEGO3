package Clases;

import javax.swing.*;

public class Main extends JFrame {

    public Main() {
        setTitle("Space Invader"); 
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Tablero panel = new Tablero(); 
        setContentPane(panel); 
        pack(); 
        setLocationRelativeTo(null); 
        setVisible(true); 
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
