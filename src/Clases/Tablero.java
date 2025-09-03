package Clases;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;


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
        RUNNING, WIN, GAME_OVER, ESPERANDO, FIN
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

    private BufferedImage fondo; 
    private int nivel = 1;
    private int niveles = 4;
    private final ArrayList<Explosion> explosiones = new ArrayList<>();

    private javax.swing.Timer nivelTimer;

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

        musfon = new Audio("/Audio/mus.wav");
        musfon.setVolume(-5f);
        musfon.loop();

        dispa = new Audio("/Audio/dispa.wav");
        dispa.setVolume(-20f);

        try {
            fondo = ImageIO.read(getClass().getResource("/imagen/fondo.png"));
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("No se pudo cargar el fondo");
            fondo = null;
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
        nivel = 1;
        estado = Estado.RUNNING;

        balas.clear();
        ene.clear();
        balas_enemigos.clear();
        explosiones.clear();

        if (nivelTimer != null) {
            nivelTimer.stop();
            nivelTimer = null;
        }

        initEne();
        jugador.x = ancho / 2 - 20;

        botonReinciar.setVisible(false);
        botonSalir.setVisible(false);

        requestFocusInWindow();
    }

    private void initEne() {
        ene.clear();
        int filas = 2 + nivel;
        int columnas = 6 + nivel * 2;
        int sepX = 60, sepY = 50;
        int startX = (ancho - (columnas * sepX)) / 2;
        int startY = 80;

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                int posx = startX + j * sepX;
                int posY = startY + i * sepY;

                if (nivel == 1 && Math.random() < 0.2) {
                    ene.add(new Ene(posx, posY, 80, 50, "/imagen/ene2.png", 2, 2));
                } else if (nivel == 2 && Math.random() < 0.2) {
                    ene.add(new Ene(posx, posY, 80, 50, "/imagen/ene2.png", 2, 2));
                } else if (nivel == 2 && Math.random() < 0.1) {
                    ene.add(new Ene(posx, posY, 80, 50, "/imagen/kami.png", 1, 3));
                } else if (nivel == 3 && Math.random() < 0.1) {
                    ene.add(new Ene(posx, posY, 80, 50, "/imagen/kami.png", 1, 3));
                } else if (nivel == 3 && Math.random() < 0.2) {
                    ene.add(new Ene(posx, posY, 80, 50, "/imagen/ene2.png", 2, 2));
                } else if (nivel == 3 && Math.random() < 0.1) {
                    ene.add(new Ene(posx, posY, 80, 50, "/imagen/kami2.png", 1, 4));
                } else {
                    ene.add(new Ene(posx, posY));
                }
            }
        }
        eneVelo = nivel;
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

            new Thread(() -> {
                Audio s = new Audio("/Audio/dispa.wav");
                s.setVolume(-35f);
                s.play();
            }).start();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ancho = getWidth();
        alto = getHeight();

        Graphics2D g2 = (Graphics2D) g;

        if (fondo != null) {
            g2.drawImage(fondo, 0, 0, ancho, alto, null);
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
        } else if (estado == Estado.ESPERANDO) {
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("MUY BIEN CRACK", getWidth() / 2 - 100, getHeight() / 2);
        } else if (estado == Estado.GAME_OVER) {
            drawEndScreen(g2, "GAME OVER", "Puntaje final: " + puntos);
        } else if (estado == Estado.FIN) {
            drawEndScreen(g2, "¡FIN DEL JUEGO!", "Puntaje total: " + puntos);
        }
    }

    private void drawEndScreen(Graphics2D g2, String title, String scoreText) {
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, ancho, alto);

        g2.setColor(Color.WHITE);
        g2.setFont(bigFont);
        int w1 = g2.getFontMetrics().stringWidth(title);
        g2.drawString(title, (ancho - w1) / 2, alto / 2 - 60);

        g2.setFont(hudFont);
        int w2 = g2.getFontMetrics().stringWidth(scoreText);
        g2.drawString(scoreText, (ancho - w2) / 2, alto / 2 - 10);

        botonReinciar.setBounds(ancho / 2 - 90, alto / 2 + 80, 180, 40);
        botonSalir.setBounds(ancho / 2 - 90, alto / 2 + 140, 180, 40);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ancho = getWidth();
        alto = getHeight();

        if (estado == Estado.GAME_OVER || estado == Estado.WIN || estado == Estado.FIN) {
            botonReinciar.setVisible(true);
            botonSalir.setVisible(true);
            return;
        } else {
            botonReinciar.setVisible(false);
            botonSalir.setVisible(false);
        }

        if (estado != Estado.RUNNING) {
            return;
        }

        if (izquierda) jugador.mover(-5, ancho);
        if (derecha) jugador.mover(5, ancho);

        ArrayList<Balas> balasParaEliminar = new ArrayList<>();
        for (Balas b : balas) {
            b.update();
            if (!b.isOnScreen()) balasParaEliminar.add(b);
        }
        balas.removeAll(balasParaEliminar);

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
            if (en.tipo == 3) {
                if (en.x < jugador.x) en.x += eneVelo + 1;
                else if (en.x > jugador.x) en.x -= eneVelo + 1;
                en.y += eneVelo + 1;

                if (en.getBounds().intersects(jugador.getBounds())) {
                    vidas--;
                    enemigosGolpeados.add(en);
                    explosiones.add(new Explosion(en.x, en.y));
                    if (vidas <= 0) estado = Estado.GAME_OVER;
                }

            } else if (en.tipo == 4) {
                if (en.x < jugador.x) en.x += eneVelo + 1;
                else if (en.x > jugador.x) en.x -= eneVelo + 1;
                en.y += eneVelo + 1;

                if (en.getBounds().intersects(jugador.getBounds())) {
                    vidas = 0;
                    enemigosGolpeados.add(en);
                    explosiones.add(new Explosion(en.x, en.y));
                    estado = Estado.GAME_OVER;
                }

            } else {
                en.x += eneDir * eneVelo;
            }

            if (Math.random() < 0.01 && balas_enemigos.size() < 10)
                balas_enemigos.add(new Balas_enemigo(en.x + en.w / 2, en.y + en.h));
        }

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

        ArrayList<Explosion> expParaEliminar = new ArrayList<>();
        for (Explosion exp : explosiones) {
            exp.update();
            if (exp.finished) expParaEliminar.add(exp);
        }
        explosiones.removeAll(expParaEliminar);

        if (ene.isEmpty()) {
            if (nivel < niveles) {
                nivel++;
                estado = Estado.ESPERANDO;

                if (nivelTimer != null) {
                    nivelTimer.stop();
                }

                nivelTimer = new javax.swing.Timer(5000, ev -> {
                    initEne();
                    estado = Estado.RUNNING;
                });
                nivelTimer.setRepeats(false);
                nivelTimer.start();
            } else {
                estado = Estado.FIN; //FNin del juego definitivo
            }
        }

        repaint();
    }
}
