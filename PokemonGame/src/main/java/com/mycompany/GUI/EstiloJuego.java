package com.mycompany.gui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Paleta y componentes visuales compartidos, extraídos del diseño ya
 * existente en PanelPokedex (franja roja decorativa, tarjeta "consola GBA"
 * con bisel + pantalla, botones estilizados), para que las pantallas de
 * Batalla (Sistema_Batalla, PanelSeleccionEquipo) usen el mismo lenguaje
 * visual en vez de un estilo distinto por pantalla.
 */
public final class EstiloJuego {

    private EstiloJuego() {
    }

    // ═══════════════════════════════════════════════════════════════
    // PALETA (igual a la de PanelPokedex)
    // ═══════════════════════════════════════════════════════════════
    public static final Color FONDO_APP = new Color(250, 244, 234);
    public static final Color ROJO_ACENTO = new Color(200, 44, 44);
    public static final Color ROJO_ACENTO_OSC = new Color(150, 28, 28);

    public static final Color BISEL_CLARO = new Color(182, 200, 166);
    public static final Color BISEL_OSCURO = new Color(122, 142, 108);
    public static final Color BISEL_BORDE = new Color(78, 96, 66);
    public static final Color BISEL_BORDE_HOVER = new Color(255, 196, 0);

    public static final Color PANTALLA_FONDO = new Color(240, 246, 224);
    public static final Color PANTALLA_BORDE = new Color(150, 168, 122);

    public static final Color TEXTO_OSCURO = new Color(44, 52, 38);
    public static final Color TEXTO_SUAVE = new Color(96, 108, 84);

    // ═══════════════════════════════════════════════════════════════
    // ENCABEZADO: franja roja decorativa con "lucecitas"
    // ═══════════════════════════════════════════════════════════════
    public static JPanel franjaDecorativa() {
        JPanel franja = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        franja.setBackground(ROJO_ACENTO);
        franja.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, ROJO_ACENTO_OSC));
        franja.add(circuloDecorativo(18, new Color(90, 170, 230), Color.WHITE));
        franja.add(circuloDecorativo(10, new Color(230, 60, 60), new Color(140, 20, 20)));
        franja.add(circuloDecorativo(10, new Color(230, 210, 60), new Color(150, 130, 20)));
        return franja;
    }

    public static JComponent circuloDecorativo(int diametro, Color relleno, Color borde) {
        return new JComponent() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(diametro, diametro);
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(relleno);
                g2.fillOval(0, 0, diametro - 1, diametro - 1);
                g2.setColor(borde);
                g2.setStroke(new BasicStroke(1.6f));
                g2.drawOval(0, 0, diametro - 1, diametro - 1);
                g2.dispose();
            }
        };
    }

    // ═══════════════════════════════════════════════════════════════
    // BOTÓN estilizado (mismo look que en PanelPokedex)
    // ═══════════════════════════════════════════════════════════════
    public static JButton botonEstilizado(String texto, Color fondo, Color textoColor) {
        JButton boton = new JButton(texto);
        boton.setFont(RecursosImagenes.getFuentePokemon(13f));
        boton.setForeground(textoColor);
        boton.setBackground(fondo);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(fondo.darker(), 1, true),
                BorderFactory.createEmptyBorder(8, 18, 8, 18)
        ));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setOpaque(true);
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (boton.isEnabled()) {
                    boton.setBackground(fondo.brighter());
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(fondo);
            }
        });
        return boton;
    }

    // ═══════════════════════════════════════════════════════════════
    // Panel "consola GBA": bisel con degradado + pantalla redondeada
    // ═══════════════════════════════════════════════════════════════
    public static class TarjetaGBA extends JPanel {

        private boolean hover = false;

        public TarjetaGBA() {
            setOpaque(false);
        }

        public void setHover(boolean hover) {
            this.hover = hover;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int arco = 20;

            g2.setColor(new Color(0, 0, 0, 35));
            g2.fillRoundRect(5, 7, w - 8, h - 8, arco, arco);

            GradientPaint bisel = new GradientPaint(0, 0, BISEL_CLARO, 0, h, BISEL_OSCURO);
            g2.setPaint(bisel);
            g2.fillRoundRect(2, 2, w - 8, h - 8, arco, arco);

            g2.setColor(hover ? BISEL_BORDE_HOVER : BISEL_BORDE);
            g2.setStroke(new BasicStroke(hover ? 3f : 2f));
            g2.drawRoundRect(3, 3, w - 10, h - 10, arco - 2, arco - 2);

            int pad = 8;
            int px = 2 + pad;
            int py = 2 + pad;
            int pw = w - 8 - pad * 2;
            int ph = h - 8 - pad * 2;
            g2.setColor(PANTALLA_FONDO);
            g2.fillRoundRect(px, py, pw, ph, 12, 12);
            g2.setColor(PANTALLA_BORDE);
            g2.setStroke(new BasicStroke(1.6f));
            g2.drawRoundRect(px, py, pw, ph, 12, 12);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ═══════════════════════════════════════════════════════════════
    // Barra con degradado vertical, usada como header dentro de la pantalla
    // ═══════════════════════════════════════════════════════════════
    public static class BarraDegradado extends JPanel {

        private final Color c1;
        private final Color c2;

        public BarraDegradado(Color c1, Color c2) {
            this.c1 = c1;
            this.c2 = c2;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gp = new GradientPaint(0, 0, c1, 0, getHeight(), c2);
            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            g2.setColor(c2.darker());
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}