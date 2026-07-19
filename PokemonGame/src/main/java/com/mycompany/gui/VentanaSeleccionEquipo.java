package com.mycompany.gui;

import com.mycompany.Model.pokemon.Pokemon;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
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
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Pantalla previa a la batalla, para que el jugador arme su equipo.
 *
 * Usa el mismo lenguaje visual que PanelPokedex (franja roja decorativa,
 * tarjetas "consola GBA" con bisel + pantalla, fuente pixelada, botones
 * estilizados) en vez de un diseño distinto solo para esta pantalla. Es un
 * JPanel embebible: Sistema_Batalla lo monta dentro de su propio
 * CardLayout, no abre ninguna ventana aparte.
 *
 * No conoce nada de Combate ni Entrenador: solo junta la lista elegida y
 * la entrega mediante el callback onConfirmar cuando el jugador termina.
 */
public class VentanaSeleccionEquipo extends JPanel {

    private final List<Pokemon> seleccionados = new ArrayList<>();
    private final int tamanoEquipo;
    private JLabel titulo;
    private final JButton comenzar;

    public VentanaSeleccionEquipo(List<Pokemon> disponibles, int tamanoEquipo, Consumer<List<Pokemon>> onConfirmar) {
        super(new BorderLayout());
        this.tamanoEquipo = tamanoEquipo;
        setBackground(EstiloJuego.FONDO_APP);

        add(construirEncabezado(), BorderLayout.NORTH);

        JPanel grilla = new JPanel(new GridLayout(0, 3, 18, 18));
        grilla.setBackground(EstiloJuego.FONDO_APP);
        grilla.setBorder(BorderFactory.createEmptyBorder(6, 16, 16, 16));
        for (Pokemon p : disponibles) {
            grilla.add(crearTarjetaSeleccionable(p));
        }

        // Envoltorio para que el grid no se estire para llenar todo el alto (igual que en PanelPokedex).
        JPanel envoltorio = new JPanel(new BorderLayout());
        envoltorio.setBackground(EstiloJuego.FONDO_APP);
        envoltorio.add(grilla, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(envoltorio);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(EstiloJuego.FONDO_APP);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll, BorderLayout.CENTER);

        comenzar = EstiloJuego.botonEstilizado("Comenzar batalla", EstiloJuego.ROJO_ACENTO, Color.WHITE);
        comenzar.setEnabled(false);
        comenzar.addActionListener(e -> onConfirmar.accept(new ArrayList<>(seleccionados)));

        JPanel pie = new JPanel();
        pie.setBackground(EstiloJuego.FONDO_APP);
        pie.setBorder(BorderFactory.createEmptyBorder(2, 0, 14, 0));
        pie.add(comenzar);
        add(pie, BorderLayout.SOUTH);
    }

    // ═══════════════════════════════════════════════════════════════
    // ENCABEZADO (igual estilo que PanelPokedex)
    // ═══════════════════════════════════════════════════════════════
    private JPanel construirEncabezado() {
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(EstiloJuego.FONDO_APP);
        contenedor.add(EstiloJuego.franjaDecorativa(), BorderLayout.NORTH);

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(EstiloJuego.FONDO_APP);
        encabezado.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));

        titulo = new JLabel(textoTitulo());
        titulo.setFont(RecursosImagenes.getFuentePokemon(22f));
        titulo.setForeground(EstiloJuego.ROJO_ACENTO_OSC);
        titulo.setIcon(RecursosImagenes.cargarIconoPokebola(24));
        titulo.setIconTextGap(10);

        JLabel subtitulo = new JLabel("Toca un Pokémon para agregarlo a tu equipo");
        subtitulo.setFont(RecursosImagenes.getFuentePokemon(11f));
        subtitulo.setForeground(EstiloJuego.TEXTO_SUAVE);

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        textos.add(titulo);
        textos.add(subtitulo);

        encabezado.add(textos, BorderLayout.WEST);
        contenedor.add(encabezado, BorderLayout.SOUTH);
        return contenedor;
    }

    private String textoTitulo() {
        return "Elige tu equipo  (" + seleccionados.size() + "/" + tamanoEquipo + ")";
    }

    // ═══════════════════════════════════════════════════════════════
    // TARJETA "consola GBA" seleccionable
    // ═══════════════════════════════════════════════════════════════
    private JToggleButton crearTarjetaSeleccionable(Pokemon p) {
        JToggleButton boton = new JToggleButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();
                int arco = 16;

                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(4, 6, w - 6, h - 6, arco, arco);

                GradientPaint bisel = new GradientPaint(0, 0, EstiloJuego.BISEL_CLARO, 0, h, EstiloJuego.BISEL_OSCURO);
                g2.setPaint(bisel);
                g2.fillRoundRect(1, 1, w - 6, h - 6, arco, arco);

                g2.setColor(isSelected() ? EstiloJuego.BISEL_BORDE_HOVER : EstiloJuego.BISEL_BORDE);
                g2.setStroke(new BasicStroke(isSelected() ? 3f : 2f));
                g2.drawRoundRect(2, 2, w - 8, h - 8, arco - 2, arco - 2);

                int pad = 6;
                int px = 1 + pad;
                int py = 1 + pad;
                int pw = w - 6 - pad * 2;
                int ph = h - 6 - pad * 2;
                g2.setColor(EstiloJuego.PANTALLA_FONDO);
                g2.fillRoundRect(px, py, pw, ph, 10, 10);
                g2.setColor(EstiloJuego.PANTALLA_BORDE);
                g2.setStroke(new BasicStroke(1.6f));
                g2.drawRoundRect(px, py, pw, ph, 10, 10);

                g2.dispose();
                super.paintComponent(g);
            }
        };

        boton.setLayout(new BorderLayout());
        boton.setPreferredSize(new Dimension(150, 190));
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel spriteLabel = new JLabel(RecursosImagenes.spritePokemonIcon(p.getNombre(), 76, 76));
        spriteLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        spriteLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel nombre = new JLabel(p.getNombre().toUpperCase(), SwingConstants.CENTER);
        nombre.setFont(RecursosImagenes.getFuentePokemon(11f));
        nombre.setForeground(EstiloJuego.TEXTO_OSCURO);
        nombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nivel = new JLabel("Nv." + p.getNivel(), SwingConstants.CENTER);
        nivel.setFont(RecursosImagenes.getFuentePokemon(9f));
        nivel.setForeground(EstiloJuego.TEXTO_SUAVE);
        nivel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel badgeTipo = new JLabel(RecursosImagenes.cargarBadgeTipo(p.getTipo().toString(), 64, 20));
        badgeTipo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel contenido = new JPanel();
        contenido.setOpaque(false);
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBorder(BorderFactory.createEmptyBorder(12, 4, 10, 4));
        contenido.add(spriteLabel);
        contenido.add(Box.createVerticalStrut(6));
        contenido.add(nombre);
        contenido.add(nivel);
        contenido.add(Box.createVerticalStrut(6));
        contenido.add(badgeTipo);

        boton.add(contenido, BorderLayout.CENTER);
        boton.addActionListener(e -> alternarSeleccion(p, boton));
        return boton;
    }

    private void alternarSeleccion(Pokemon p, JToggleButton boton) {
        if (boton.isSelected()) {
            if (seleccionados.size() >= tamanoEquipo) {
                boton.setSelected(false);
                JOptionPane.showMessageDialog(this, "Ya elegiste " + tamanoEquipo + " Pokémon.",
                        "Equipo completo", JOptionPane.INFORMATION_MESSAGE);
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