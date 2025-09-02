package Clases;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Clase Tablero: donde ocurre todo el juego.
 */
public class Tablero extends JPanel implements ActionListener {

    private int ancho = 800;
    private int alto = 600;

    private final Timer tem = new Timer(16, this);
    private final Jugador jugador = new Jugador(ancho / 2 - 20, alto - 60);

    private final ArrayList<Balas> balas = new ArrayList<>();
    private final ArrayList<Ene> ene = new ArrayList<>();
    private final ArrayList<Balas_enemigo> balas_enemigos = new ArrayList<>();

    private int eneDir = 1;
    private int eneVelo = 2;

    private enum Estado { 
    	RUNNING, WIN, GAME_OVER, ESPERANDO // LE AGREGUE ESPERANDO PAPRA TENER 5 SEGUNDOS ENTERE NIVELES 
    }
    private Estado estado = Estado.RUNNING;

    private final Font hudFont = new Font("Consolas", Font.PLAIN, 18);
    private final Font bigFont = new Font("Consolas", Font.BOLD, 36);

    private int puntos = 0;
    private int vidas = 3;

    private boolean izquierda, derecha;

    private long ultimoDisparo = 0;
    private final int tiempoRecarga = 300;

    private JButton botonReinciar;
    private JButton botonSalir;
    
    //le agregue el fonfdo 
    private BufferedImage fondo; // Imagen de fondo
    
    //esto para los niveles
    private int nivel= 1;
    private int niveles=4;
    // esto para la explcio, darle una especie de animacion
    private final ArrayList<Explosion> explosiones = new ArrayList<>();

    //para la musica de fondo 
    private Audio musfon;
    private Audio dispa;
    public Tablero() {
        setPreferredSize(new Dimension(ancho, alto));
        setBackground(Color.BLACK);
        setFocusable(true);
        
        initBotones();
        initEne();
        initKeys();
        tem.start();
        //cargar musica de fondo
        musfon = new Audio("/Audio/mus.wav");
        musfon.setVolume(-5f); // menos negativo = más fuerte
        musfon.loop(); // Reproduce en loop mientras dure el juego
        
        //audio disparo se lo agrego solo 1 porque alta paja la verdad
        dispa = new Audio("/Audio/dispa.wav");
        dispa.setVolume(-20f); // más negativo = más bajo
        // Cargar fondo
        try {
            fondo = ImageIO.read(getClass().getResource("/imagen/fondo.png"));
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("No se pudo cargar el fondo");
            fondo = null; // fallback al fondo negro
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocus();
    }
    
    private void initBotones() {
        setLayout(null);
        
        botonReinciar = new JButton("Volver a Jugar");
        botonReinciar.addActionListener(e -> reiniciarJuego());
        add(botonReinciar);
        botonReinciar.setVisible(false);

        botonSalir = new JButton("Salir");
        botonSalir.addActionListener(e -> System.exit(0));
        add(botonSalir);
        botonSalir.setVisible(false);
    }
    
    private void reiniciarJuego() {
        puntos = 0;
        vidas = 3;
        estado = Estado.RUNNING;
        
        balas.clear();
        ene.clear();
        balas_enemigos.clear();
        
        initEne();
        jugador.x = ancho / 2 - 20;
        
        botonReinciar.setVisible(false);
        botonSalir.setVisible(false);
        
        requestFocusInWindow();
    }

    private void initEne() {
        ene.clear();
        int filas = 2 + nivel;
        int columnas = 6 + nivel*2;
        int sepX = 60, sepY = 50;
        int startX = (ancho - (columnas * sepX)) / 2; // Calcula la posición inicial basada en el ancho de la ventana
        int startY = 80;

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                int posx = startX + j * sepX;
                int posY = startY  + i * sepY;
                
                if (nivel == 2 && Math.random()< 0.2) {
                	ene.add(new Ene(posx, posY, 80, 50, "/imagen/ene2.png", 2, 2));
                }else if (nivel == 3 && Math.random()< 0.2){
                	ene.add(new Ene(posx, posY, 80, 50, "/imagen/kami.png", 1,3));

                }else if (nivel == 4 && Math.random()< 0.2){
                	ene.add(new Ene(posx, posY, 80, 50, "/imagen/kami2.png", 1,4));

                }else {
                	ene.add(new Ene(posx, posY));
                }
            }
        }
        //velocidad del enemigo 
        eneVelo = 2 + nivel;
    }
        

    private void initKeys() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> izquierda = true;
                    case KeyEvent.VK_RIGHT -> derecha = true;
                    case KeyEvent.VK_SPACE -> disparar();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> izquierda = false;
                    case KeyEvent.VK_RIGHT -> derecha = false;
                }
            }
        });
    }

    private void disparar() {
        long ahora = System.currentTimeMillis();
        if (ahora - ultimoDisparo >= tiempoRecarga) {
            balas.add(new Balas(jugador.getX() + jugador.getAncho() / 2 - 2, jugador.getY()));
            ultimoDisparo = ahora;
            
            // ESTO ESTA RE CHADGEPETEADO GRACIA CHAT
            // Cada disparo tiene su propio clip y volumen bajo
            new Thread(() -> {
                Audio s = new Audio("/Audio/dispa.wav"); // crea un clip nuevo
                s.setVolume(-35f); // volumen bajo para disparos
                s.play(); // reproduce
            }).start();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ancho = getWidth();
        alto = getHeight();

        Graphics2D g2 = (Graphics2D) g;

        // Dibujar fondo
        if (fondo != null) {
            g2.drawImage(fondo, 0, 0, ancho, alto, null); // escala al tamaño del tablero
        } else {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, ancho, alto);
        }

        jugador.draw(g2);
        for (Balas b : balas) b.draw(g2);
        for (Ene e : ene) e.draw(g2);
        for (Balas_enemigo b : balas_enemigos) b.draw(g2);
        for (Explosion exp : explosiones) exp.draw(g2);

        
        g2.setColor(Color.WHITE);
        g2.setFont(hudFont);
        g2.drawString("Puntaje: " + puntos + " | Vidas: " + vidas, 10, 20);

        if (estado == Estado.WIN) {
            drawEndScreen(g2, "¡GANASTE!", "Puntaje final: " + puntos);
        }else if (estado == Estado.ESPERANDO) {
        	//ESTO PARA QUE ME TIRE E TEXTO DE ESPERA 
        	g.setColor(Color.white);
        	g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("MUY BIEN CRACk", getWidth() / 2 - 100, getHeight() / 2);

        	
        }else if (estado == Estado.GAME_OVER) {
            drawEndScreen(g2, "GAME OVER", "Puntaje final: " + puntos);
        }
        
        
    }

    private void drawEndScreen(Graphics2D g2, String title, String scoreText) {
        // Fondo semi-transparente
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, ancho, alto);

        // Mensaje de título
        g2.setColor(Color.WHITE);
        g2.setFont(bigFont);
        int w1 = g2.getFontMetrics().stringWidth(title);
        g2.drawString(title, (ancho - w1) / 2, alto / 2 - 60);

        // Mensaje de puntaje
        g2.setFont(hudFont);
        int w2 = g2.getFontMetrics().stringWidth(scoreText);
        g2.drawString(scoreText, (ancho - w2) / 2, alto / 2 - 10);
        
        // Reposiciona los botones
        botonReinciar.setBounds(ancho / 2 - 90, alto / 2 + 80, 180, 40);
        botonSalir.setBounds(ancho / 2 - 90, alto / 2 + 140, 180, 40);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        ancho = getWidth();
        alto = getHeight();

        if (estado != Estado.RUNNING) {
            botonReinciar.setVisible(true);
            botonSalir.setVisible(true);
            return;
        }

        // Movimiento jugador
        if (izquierda) jugador.mover(-5, ancho);
        if (derecha) jugador.mover(5, ancho);

        // Actualizar balas jugador
        ArrayList<Balas> balasParaEliminar = new ArrayList<>();
        for (Balas b : balas) {
            b.update();
            if (!b.isOnScreen()) balasParaEliminar.add(b);
        }
        balas.removeAll(balasParaEliminar);

        // Actualizar enemigos
        if (!ene.isEmpty()) {
            int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
            for (Ene en : ene) {
                minX = Math.min(minX, en.x);
                maxX = Math.max(maxX, en.x + en.w);
            }
            if (minX <= 10 || maxX >= ancho - 10) {
                eneDir *= -1;
            }
        }

        ArrayList<Ene> enemigosGolpeados = new ArrayList<>();

        for (Ene en : ene) {
            // Movimiento enemigos
            if (en.tipo == 3) { // kamikaze
                if (en.x < jugador.x) en.x += eneVelo + 1;
                else if (en.x > jugador.x) en.x -= eneVelo + 1;
                en.y += eneVelo + 1;

                if (en.getBounds().intersects(jugador.getBounds())) {
                    vidas--;
                    enemigosGolpeados.add(en);
                    explosiones.add(new Explosion(en.x, en.y));
                    if (vidas <= 0) estado = Estado.GAME_OVER;
                }

            } else if (en.tipo == 4) { // kamikaze mortal
                if (en.x < jugador.x) en.x += eneVelo + 1;
                else if (en.x > jugador.x) en.x -= eneVelo + 1;
                en.y += eneVelo + 1;

                if (en.getBounds().intersects(jugador.getBounds())) {
                    vidas = 0;
                    enemigosGolpeados.add(en);
                    explosiones.add(new Explosion(en.x, en.y));
                    estado = Estado.GAME_OVER;
                }

            } else { // enemigos normales
                en.x += eneDir * eneVelo;
            }

            // Disparo enemigo aleatorio
            if (Math.random() < 0.01 && balas_enemigos.size() < 10)
                balas_enemigos.add(new Balas_enemigo(en.x + en.w / 2, en.y + en.h));
        }

        // Colisión balas jugador con enemigos
        ArrayList<Balas> balasGolpeadas = new ArrayList<>();
        for (Balas b : balas) {
            for (Ene en : ene) {
                if (b.getBounds().intersects(en.getBounds())) {
                    balasGolpeadas.add(b);
                    en.vidas--;
                    if (en.vidas <= 0) {
                        enemigosGolpeados.add(en);
                        puntos += 10;
                        explosiones.add(new Explosion(en.x, en.y));
                    }
                    break;
                }
            }
        }
        balas.removeAll(balasGolpeadas);
        ene.removeAll(enemigosGolpeados);

        // Actualizar balas enemigos
        ArrayList<Balas_enemigo> balasEnemParaEliminar = new ArrayList<>();
        for (Balas_enemigo b : balas_enemigos) {
            b.update();
            if (!b.Pantalla(alto)) balasEnemParaEliminar.add(b);
            else if (b.limites().intersects(jugador.getBounds())) {
                vidas--;
                balasEnemParaEliminar.add(b);
                if (vidas <= 0) estado = Estado.GAME_OVER;
            }
        }
        balas_enemigos.removeAll(balasEnemParaEliminar);

        // Actualizar explosiones
        ArrayList<Explosion> expParaEliminar = new ArrayList<>();
        for (Explosion exp : explosiones) {
            exp.update();
            if (exp.finished) expParaEliminar.add(exp);
        }
        explosiones.removeAll(expParaEliminar);

        // Revisar si se terminó el nivel
        if (ene.isEmpty()) {
            if (nivel < niveles) {
                nivel++;
                estado = Estado.ESPERANDO;

                javax.swing.Timer timer = new javax.swing.Timer(5000, ev -> {
                    initEne();
                    estado = Estado.RUNNING;
                });
                timer.setRepeats(false);
                timer.start();
            } else {
                estado = Estado.WIN;
            }
        }

        repaint();
    }


}