package com.mycompany.gui;

import com.mycompany.Combate.Atk.Ataque;
import com.mycompany.Model.pokemon.Pokemon;
import com.mycompany.gui.RecursosImagenes.EstadoSprite;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.function.IntConsumer;

/**
 * Panel visual de la batalla.
 *
 * Responsabilidad única: dibujar el estado que se le indique (sprites, HP,
 * mensajes) y avisar cuándo el jugador hace clic en un botón de ataque.
 * No conoce reglas de combate ni el modelo Combate/AtaqueComand: todo eso
 * sigue viviendo intacto en las clases originales.
 */
public class PanelBatalla extends JPanel {

    private final Escena escena = new Escena();
    private final JTextArea mensaje = new JTextArea();
    private final JPanel panelBotones = new JPanel(new GridLayout(2, 2, 8, 8));

    private Pokemon pokemonJugador;
    private Pokemon pokemonRival;
    private EstadoSprite spriteJugador = EstadoSprite.NORMAL;
    private EstadoSprite spriteRival = EstadoSprite.NORMAL;
    private double hpJugadorMostrado;
    private double hpRivalMostrado;
    private Timer animTimer;
    
    //animaciones
    private int offsetJugadorX = 0;
    private int offsetRivalX = 0;

    private boolean flashJugador = false;
    private boolean flashRival = false;

    public PanelBatalla() {
        setLayout(new BorderLayout(0, 8));
        setBackground(new Color(28, 28, 38));

        add(escena, BorderLayout.CENTER);

        JPanel sur = new JPanel(new BorderLayout(6, 6)) {
            @Override
            protected void paintComponent(Graphics g) {
                g.drawImage(RecursosImagenes.fondoPanelInferior(), 0, 0, getWidth(), getHeight(), null);
                super.paintComponent(g);
            }
        };
        sur.setOpaque(false);
        sur.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 8));
        // Alto fijo: evita que el panel inferior crezca/encoja según el mensaje
        // o la cantidad de botones (ataques/mochila), lo que antes hacía que
        // el escenario (fondo + sprites) cambiara de tamaño y se desacomodara.
        sur.setPreferredSize(new Dimension(700, 160));

        mensaje.setEditable(false);
        mensaje.setLineWrap(true);
        mensaje.setWrapStyleWord(true);
        mensaje.setFont(new Font("SansSerif", Font.PLAIN, 15));
        mensaje.setBackground(new Color(250, 250, 245));
        mensaje.setRows(3);
        mensaje.setPreferredSize(new Dimension(700, 66));
        mensaje.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 70), 2, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));

        panelBotones.setOpaque(false);
        panelBotones.setPreferredSize(new Dimension(0, 90));

        sur.add(mensaje, BorderLayout.CENTER);
        sur.add(panelBotones, BorderLayout.SOUTH);
        add(sur, BorderLayout.SOUTH);
    }

    // ── API pública usada por el observador de combate ────────────────────

    public void setPokemonJugador(Pokemon p) {
        this.pokemonJugador = p;
        this.hpJugadorMostrado = p.getHpActual();
        this.spriteJugador = EstadoSprite.NORMAL;
        escena.repaint();
    }

    public void setPokemonRival(Pokemon p) {
        this.pokemonRival = p;
        this.hpRivalMostrado = p.getHpActual();
        this.spriteRival = EstadoSprite.NORMAL;
        escena.repaint();
    }

    public void setSpriteJugador(EstadoSprite estado) {
        this.spriteJugador = estado;
        escena.repaint();
    }

    public void setSpriteRival(EstadoSprite estado) {
        this.spriteRival = estado;
        escena.repaint();
    }

    public void mostrarMensaje(String texto) {
        mensaje.setText(texto);
    }

    public void animarHpJugador(int hpDestino) {
        animarHp(true, hpDestino);
    }

    public void animarHpRival(int hpDestino) {
        animarHp(false, hpDestino);
    }

    private void animarHp(boolean esJugador, int hpDestino) {
        if (animTimer != null && animTimer.isRunning()) {
            animTimer.stop();
        }
        animTimer = new Timer(15, null);
        animTimer.addActionListener(e -> {
            double actual = esJugador ? hpJugadorMostrado : hpRivalMostrado;
            double diff = hpDestino - actual;
            if (Math.abs(diff) < 0.6) {
                if (esJugador) {
                    hpJugadorMostrado = hpDestino;
                } else {
                    hpRivalMostrado = hpDestino;
                }
                animTimer.stop();
            } else {
                double nuevo = actual + diff * 0.18;
                if (esJugador) {
                    hpJugadorMostrado = nuevo;
                } else {
                    hpRivalMostrado = nuevo;
                }
            }
            escena.repaint();
        });
        animTimer.start();
    }

    /** Muestra un botón por cada ataque disponible; al pulsar uno se invoca onElegir con su índice. */
    public void mostrarBotonesAtaque(List<Ataque> ataques, IntConsumer onElegir) {
        panelBotones.removeAll();
        for (int i = 0; i < ataques.size(); i++) {
            final int idx = i;
            JButton boton = new JButton(ataques.get(i).getNombre());
            boton.setFont(new Font("SansSerif", Font.BOLD, 13));
            boton.setFocusPainted(false);
            boton.addActionListener(e -> {
                ocultarBotonesAtaque();
                onElegir.accept(idx);
            });
            panelBotones.add(boton);
        }
        panelBotones.revalidate();
        panelBotones.repaint();
    }

    public void ocultarBotonesAtaque() {
        panelBotones.removeAll();
        panelBotones.revalidate();
        panelBotones.repaint();
    }

    /** Agrega el botón de Mochila junto a los botones de ataque ya mostrados. */
    public void agregarBotonMochila(Runnable onAbrirMochila) {
        JButton boton = new JButton("Mochila");
        boton.setFont(new Font("SansSerif", Font.BOLD, 13));
        boton.setFocusPainted(false);
        boton.addActionListener(e -> onAbrirMochila.run());
        panelBotones.add(boton);
        panelBotones.revalidate();
        panelBotones.repaint();
    }

    // ── Escena dibujada a mano: fondo + sprites + barras de vida ──────────

    private class Escena extends JPanel {

        Escena() {
            setPreferredSize(new Dimension(700, 420));
            setOpaque(true);
        }

        @Override
        protected void paintComponent(Graphics g0) {
            super.paintComponent(g0);
            Graphics2D g = (Graphics2D) g0;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            BufferedImage fondo = RecursosImagenes.fondo();
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), null);

            if (pokemonRival != null) {
                g.drawImage(RecursosImagenes.plataforma(), getWidth() - 255 + offsetRivalX, 225, 190, 60, null);
                BufferedImage sr = RecursosImagenes.spriteRival(pokemonRival.getNombre(), spriteRival);
                g.drawImage(sr, getWidth() - 260+ offsetRivalX, 120, 190, 190, null);
                dibujarPanelInfo(g, 24, 24, pokemonRival, hpRivalMostrado);
                if (flashRival) {
                    g.setColor(new Color(255,0,0,90));
                    g.fillRect(
                        getWidth()-230 + offsetRivalX,
                        40,
                        150,
                        150
                    );
                }
            }

            if (pokemonJugador != null) {
                g.drawImage(RecursosImagenes.plataforma(), 15 + offsetJugadorX, getHeight() - 90, 300, 75, null);
                BufferedImage sj = RecursosImagenes.spriteJugador(pokemonJugador.getNombre(), spriteJugador);
                g.drawImage(sj, 30, getHeight() - 250+ offsetJugadorX, 300, 300, null);
                dibujarPanelInfo(g, getWidth() - 260, getHeight() - 130, pokemonJugador, hpJugadorMostrado);
                if (flashJugador) {
                    g.setColor(new Color(255,0,0,90));
                    g.fillRect(
                        80 + offsetJugadorX,
                        getHeight()-210,
                        150,
                        150
                    );
                }
            }
        }

        private void dibujarPanelInfo(Graphics2D g, int x, int y, Pokemon p, double hpMostrado) {
            int ancho = 236, alto = 76;
            g.drawImage(RecursosImagenes.marcoInfo(), x, y, ancho, alto, null);

            g.setColor(new Color(40, 40, 50));
            g.setFont(new Font("SansSerif", Font.BOLD, 16));
            String nombreNivel = p.getNombre() + "  Nv." + p.getNivel();
            g.drawString(nombreNivel, x + 14, y + 24);

            String estado = p.getEstado().getNombre();
            if (!"Normal".equals(estado)) {
                dibujarBadgeEstado(g, x, y, ancho, estado, g.getFontMetrics().stringWidth(nombreNivel));
            }

            int barX = x + 14, barY = y + 36, barW = ancho - 28, barH = 14;
            g.drawImage(RecursosImagenes.barraVidaFondo(), barX, barY, barW, barH, null);

            double pct = Math.max(0, Math.min(1.0, hpMostrado / (double) p.getHp()));
            Color colorHp = pct > 0.5 ? new Color(80, 190, 90)
                    : pct > 0.2 ? new Color(230, 200, 60)
                    : new Color(220, 70, 70);
            g.setColor(colorHp);
            g.fillRoundRect(barX + 2, barY + 2, Math.max(0, (int) (barW * pct) - 4), barH - 4, 6, 6);

            g.setFont(new Font("SansSerif", Font.PLAIN, 12));
            g.setColor(new Color(60, 60, 60));
            FontMetrics fm = g.getFontMetrics();
            String texto = Math.max(0, (int) Math.round(hpMostrado)) + " / " + p.getHp() + " HP";
            g.drawString(texto, barX, barY + barH + fm.getAscent());
        }

        private void dibujarBadgeEstado(Graphics2D g, int x, int y, int anchoPanel, String estado, int anchoNombre) {
            Color color = switch (estado) {
                case "Paralizado" -> new Color(235, 196, 52);
                case "Dormido" -> new Color(110, 130, 200);
                case "Quemado" -> new Color(230, 110, 60);
                default -> new Color(140, 140, 140);
            };

            g.setFont(new Font("SansSerif", Font.BOLD, 10));
            FontMetrics fm = g.getFontMetrics();
            int padH = 6, padV = 3;
            int bw = fm.stringWidth(estado) + padH * 2;
            int bh = fm.getHeight() - 2;

            int bx = Math.min(x + 14 + anchoNombre + 8, x + anchoPanel - bw - 10);
            int by = y + 24 - bh + 3;

            g.setColor(color);
            g.fillRoundRect(bx, by, bw, bh, bh, bh);
            g.setColor(Color.WHITE);
            g.drawString(estado, bx + padH, by + bh - padV);
        }
    }
    public void animarAtaqueJugador() {

        Timer t = new Timer(20, null);

        final int[] paso = {0};

        t.addActionListener(e -> {

            if (paso[0] < 5) {
                offsetJugadorX += 5;      // avanza
            } else if (paso[0] < 10) {
                offsetJugadorX -= 5;      // regresa
            } else {
                offsetJugadorX = 0;
                t.stop();
            }

            paso[0]++;
            escena.repaint();
        });

        t.start();
    }
    public void animarAtaqueRival() {

         Timer t = new Timer(20, null);

        final int[] paso = {0};

        t.addActionListener(e -> {

            if (paso[0] < 5) {
                offsetRivalX -= 5;      // avanza hacia la izquierda
            } else if (paso[0] < 10) {
                offsetRivalX += 5;      // regresa
            } else {
                offsetRivalX = 0;
                t.stop();
            }

            paso[0]++;
            escena.repaint();
        });

        t.start();
    }
    public void animarGolpeJugador() {

        final int[] paso = {0};

        Timer t = new Timer(30, null);

        t.addActionListener(e -> {

            switch (paso[0]) {
                case 0 -> offsetJugadorX = -6;
                case 1 -> offsetJugadorX = 6;
                case 2 -> offsetJugadorX = -6;
                case 3 -> offsetJugadorX = 6;
                case 4 -> offsetJugadorX = 0;
            }

            escena.repaint();

            paso[0]++;

            if (paso[0] > 4) {
                offsetJugadorX = 0;
                escena.repaint();
                t.stop();
            }
        });

        t.start();
    }
    public void animarGolpeRival() {

        final int[] paso = {0};

        Timer t = new Timer(30, null);

        t.addActionListener(e -> {

            switch (paso[0]) {
                case 0 -> offsetRivalX = -6;
                case 1 -> offsetRivalX = 6;
                case 2 -> offsetRivalX = -6;
                case 3 -> offsetRivalX = 6;
                case 4 -> offsetRivalX = 0;
            }

            escena.repaint();

            paso[0]++;

            if (paso[0] > 4) {
                offsetRivalX = 0;
                escena.repaint();
                t.stop();
            }
        });

        t.start();
    }
    public void flashJugador() {

        flashJugador = true;
        escena.repaint();

        new Timer(120,e->{
            flashJugador = false;
            escena.repaint();
            ((Timer)e.getSource()).stop();
        }).start();

    }
    public void flashRival() {

        flashRival = true;
        escena.repaint();

        new Timer(120, e -> {
            flashRival = false;
            escena.repaint();
            ((Timer) e.getSource()).stop();
        }).start();
    }
}
