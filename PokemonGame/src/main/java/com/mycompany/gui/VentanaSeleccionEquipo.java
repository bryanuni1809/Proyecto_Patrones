package com.mycompany.gui;

import com.mycompany.Model.pokemon.Pokemon;
import com.mycompany.gui.RecursosImagenes.EstadoSprite;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Pantalla previa a la batalla, para que el jugador arme su equipo.
 *
 * Es la versión "embebible" de lo que antes era VentanaSeleccionEquipo
 * (un JFrame aparte). Ahora es un JPanel normal que Sistema_Batalla monta
 * dentro de su propio CardLayout, así que vive dentro de la pestaña
 * "Batalla" en vez de abrirse como una ventana flotante independiente del
 * sistema operativo.
 *
 * No conoce nada de Combate ni Entrenador: solo junta la lista elegida y
 * la entrega mediante el callback onConfirmar cuando el jugador termina.
 */
public class VentanaSeleccionEquipo extends JPanel {

    private static final Color ROJO_POKEBALL = new Color(204, 40, 40);
    private static final Color AZUL_MARCO = new Color(40, 60, 130);
    private static final Color CREMA = new Color(255, 249, 233);

    private final List<Pokemon> seleccionados = new ArrayList<>();
    private final int tamanoEquipo;
    private final JLabel titulo;
    private final JButton comenzar = new JButton("Comenzar batalla") {
        @Override
        protected void paintComponent(Graphics g) {
            g.drawImage(RecursosImagenes.botonComenzar(), 0, 0, getWidth(), getHeight(), null);
            super.paintComponent(g);
        }
    };

    public VentanaSeleccionEquipo(List<Pokemon> disponibles, int tamanoEquipo, Consumer<List<Pokemon>> onConfirmar) {
        super(new BorderLayout(8, 10));
        this.tamanoEquipo = tamanoEquipo;
        setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        // ── Banner de título tipo Pokédex ──────────────────────────────────
        titulo = new JLabel(textoTitulo(), SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint degradado = new GradientPaint(0, 0, ROJO_POKEBALL, 0, getHeight(), new Color(150, 24, 24));
                g2.setPaint(degradado);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(255, 255, 255, 60));
                g2.fillOval(-18, getHeight() / 2 - 18, 36, 36);
                g2.setColor(AZUL_MARCO);
                g2.fillRect(0, getHeight() - 4, getWidth(), 4);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        titulo.setOpaque(false);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setBorder(BorderFactory.createEmptyBorder(16, 0, 16, 0));

        JPanel grid = new JPanel(new GridLayout(0, 4, 18, 18)) {
            @Override
            protected void paintComponent(Graphics g) {
                g.drawImage(RecursosImagenes.fondoSeleccion(), 0, 0, getWidth(), getHeight(), null);
                super.paintComponent(g);
            }
        };
        grid.setOpaque(false);
        grid.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        for (Pokemon p : disponibles) {
            grid.add(crearTarjeta(p));
        }

        comenzar.setEnabled(false);
        comenzar.setFont(new Font("SansSerif", Font.BOLD, 15));
        comenzar.setForeground(Color.WHITE);
        comenzar.setContentAreaFilled(false);
        comenzar.setBorderPainted(false);
        comenzar.setFocusPainted(false);
        comenzar.setOpaque(false);
        comenzar.setPreferredSize(new Dimension(240, 50));
        comenzar.addActionListener(e -> onConfirmar.accept(new ArrayList<>(seleccionados)));

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(titulo, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        JPanel sur = new JPanel();
        sur.setOpaque(false);
        sur.add(comenzar);
        add(sur, BorderLayout.SOUTH);
    }

    /** Fondo degradado tipo "cielo Pokémon" (antes lo pintaba el JPanel raíz del JFrame). */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        GradientPaint degradado = new GradientPaint(
                0, 0, new Color(120, 170, 230),
                0, getHeight(), new Color(235, 244, 250));
        g2.setPaint(degradado);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    private String textoTitulo() {
        return "Elige " + tamanoEquipo + " Pokémon  (" + seleccionados.size() + "/" + tamanoEquipo + ")";
    }

    private JToggleButton crearTarjeta(Pokemon p) {
        BufferedImage img = RecursosImagenes.spriteRival(p.getNombre(), EstadoSprite.NORMAL);
        Image escalada = img.getScaledInstance(72, 72, Image.SCALE_SMOOTH);
        Color colorTipo = RecursosImagenes.colorTipo(p.getTipo());

        JToggleButton boton = new JToggleButton(
                "<html><center><b>" + p.getNombre() + "</b><br>Nv." + p.getNivel() + "</center></html>",
                new ImageIcon(escalada)) {
            @Override
            protected void paintComponent(Graphics g0) {
                Graphics2D g = (Graphics2D) g0.create();
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g.drawImage(RecursosImagenes.tarjetaSeleccion(isSelected()), 0, 0, getWidth(), getHeight(), null);

                // Franja de color según el tipo, arriba de la tarjeta.
                g.setColor(colorTipo);
                g.fillRoundRect(6, 6, getWidth() - 12, 8, 6, 6);

                // Etiqueta del tipo, abajo.
                String tipoTexto = p.getTipo().toString();
                g.setFont(new Font("SansSerif", Font.BOLD, 10));
                int tw = g.getFontMetrics().stringWidth(tipoTexto) + 14;
                int tx = (getWidth() - tw) / 2;
                int ty = getHeight() - 22;
                g.setColor(colorTipo);
                g.fillRoundRect(tx, ty, tw, 16, 10, 10);
                g.setColor(Color.WHITE);
                g.drawString(tipoTexto, tx + 7, ty + 12);

                if (isSelected()) {
                    g.setColor(new Color(190, 150, 20));
                    g.setStroke(new BasicStroke(2.5f));
                    g.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 18, 18);
                }

                g.dispose();
                super.paintComponent(g0);
            }
        };
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        boton.setBorderPainted(false);
        boton.setVerticalTextPosition(SwingConstants.BOTTOM);
        boton.setHorizontalTextPosition(SwingConstants.CENTER);
        boton.setFocusPainted(false);
        boton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        boton.setBorder(BorderFactory.createEmptyBorder(8, 6, 26, 6));
        boton.addActionListener(e -> alternarSeleccion(p, boton));
        return boton;
    }

    private void alternarSeleccion(Pokemon p, JToggleButton boton) {
        if (boton.isSelected()) {
            if (seleccionados.size() >= tamanoEquipo) {
                boton.setSelected(false);
                JOptionPane.showMessageDialog(this, "Ya elegiste " + tamanoEquipo + " Pokémon.");
                return;
            }
            seleccionados.add(p);
        } else {
            seleccionados.remove(p);
        }
        titulo.setText(textoTitulo());
        comenzar.setEnabled(seleccionados.size() == tamanoEquipo);
    }
}