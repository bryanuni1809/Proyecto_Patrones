package com.mycompany.gui;

import com.mycompany.Combate.Atk.Ataque;
import com.mycompany.Model.entrenador.Entrenador;
import com.mycompany.Model.entrenador.SelectorAtaque;
import com.mycompany.Model.pokemon.Pokemon;
import com.mycompany.Patrones.composite.ItemMochila;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Implementación de SelectorAtaque para el jugador humano en la GUI.
 *
 * Combate.iniciarBatalla() corre en un hilo aparte del EDT (ver
 * VentanaBatalla.iniciarCombateEnHilo()). Cuando le toca elegir ataque al
 * jugador, este selector muestra los botones (ataques + Mochila) y BLOQUEA
 * ese hilo de combate (nunca el EDT) hasta que el usuario:
 *   - hace clic en un ataque → se envía su índice (0, 1, 2...), o
 *   - usa un ítem desde la Mochila → se envía el sentinel -1, que
 *     Combate.ejecutarAccion() interpreta como "turno consumido, sin ataque"
 *     (ver el pequeño cambio agregado en Combate.java).
 */
public class SelectorAtaqueGUI implements SelectorAtaque {

    private static final int SIN_ATAQUE = -1;
    private static final Color ROJO_POKEBALL = new Color(204, 40, 40);
    private static final Color AZUL_MARCO = new Color(40, 60, 130);
    private static final Color CREMA = new Color(255, 249, 233);

    private final PanelBatalla panel;
    private final Entrenador jugador;
    private final BlockingQueue<Integer> seleccion = new ArrayBlockingQueue<>(1);

    public SelectorAtaqueGUI(PanelBatalla panel, Entrenador jugador) {
        this.panel = panel;
        this.jugador = jugador;
    }

    @Override
    public Ataque elegir(Pokemon activo, List<Ataque> ataques) {
        seleccion.clear();
        SwingUtilities.invokeLater(() -> {
            panel.mostrarMensaje("¿Qué hará " + activo.getNombre() + "?");
            panel.mostrarBotonesAtaque(ataques, seleccion::offer);
            panel.agregarBotonMochila(() -> abrirMochila(activo));
        });
        try {
            int idx = seleccion.take(); // Espera aquí a la decisión del jugador, sin congelar la GUI.
            return idx == SIN_ATAQUE ? null : ataques.get(idx);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ataques.get(0);
        }
    }

    private void abrirMochila(Pokemon activo) {
        JDialog dialogo = new JDialog((Frame) SwingUtilities.getWindowAncestor(panel), "Mochila", true);
        dialogo.setLayout(new BorderLayout());
        dialogo.getContentPane().setBackground(CREMA);

        // ── Banner rojo tipo "menú de ítems" ────────────────────────────
        JLabel banner = new JLabel("MOCHILA", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, ROJO_POKEBALL, 0, getHeight(), new Color(150, 24, 24)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(AZUL_MARCO);
                g2.fillRect(0, getHeight() - 3, getWidth(), 3);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        banner.setOpaque(false);
        banner.setForeground(Color.WHITE);
        banner.setFont(new Font("SansSerif", Font.BOLD, 16));
        banner.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // ── Lista de ítems, cada uno como una "tarjeta" ─────────────────
        JPanel lista = new JPanel();
        lista.setLayout(new javax.swing.BoxLayout(lista, javax.swing.BoxLayout.Y_AXIS));
        lista.setBackground(CREMA);
        lista.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        boolean hayItems = false;
        for (ItemMochila item : jugador.getMochila().getItems()) {
            if (item.getCantidad() <= 0) {
                continue;
            }
            hayItems = true;
            lista.add(crearTarjetaItem(item, activo, dialogo));
            lista.add(javax.swing.Box.createVerticalStrut(8));
        }
        if (!hayItems) {
            JLabel vacio = new JLabel("No quedan objetos en la mochila.");
            vacio.setForeground(new Color(90, 70, 40));
            vacio.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            lista.add(vacio);
        }

        JButton cancelar = new JButton("Cancelar");
        estilizarBotonSecundario(cancelar);
        cancelar.addActionListener(e -> dialogo.dispose()); // No consume el turno: los botones siguen activos.

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(CREMA);

        JPanel pie = new JPanel();
        pie.setBackground(CREMA);
        pie.add(cancelar);

        dialogo.add(banner, BorderLayout.NORTH);
        dialogo.add(scroll, BorderLayout.CENTER);
        dialogo.add(pie, BorderLayout.SOUTH);
        dialogo.setSize(320, 320);
        dialogo.setLocationRelativeTo(panel);
        dialogo.setVisible(true);
    }

    private JPanel crearTarjetaItem(ItemMochila item, Pokemon activo, JDialog dialogo) {
        JPanel tarjeta = new JPanel(new BorderLayout(10, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                g2.setColor(AZUL_MARCO);
                g2.setStroke(new java.awt.BasicStroke(1.4f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        tarjeta.setOpaque(false);
        tarjeta.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        tarjeta.setAlignmentX(JPanel.CENTER_ALIGNMENT);

        JLabel icono = new JLabel(new ImageIcon(RecursosImagenes.iconoItem(item.getNombre())));
        tarjeta.add(icono, BorderLayout.WEST);

        JLabel texto = new JLabel("<html><b>" + item.getNombre() + "</b><br><font size=2>x" + item.getCantidad() + "</font></html>");
        texto.setForeground(new Color(50, 40, 20));
        tarjeta.add(texto, BorderLayout.CENTER);

        JButton usar = new JButton("Usar");
        estilizarBotonPrimario(usar);
        usar.addActionListener(e -> {
            jugador.usarItem(item, activo); // Reutiliza tal cual la lógica ya implementada (Composite).
            panel.animarHpJugador(activo.getHpActual());
            panel.mostrarMensaje(jugador.getNombre() + " usó " + item.getNombre() + " en " + activo.getNombre() + ".");
            dialogo.dispose();
            panel.ocultarBotonesAtaque();
            seleccion.offer(SIN_ATAQUE); // Consume el turno sin atacar.
        });
        tarjeta.add(usar, BorderLayout.EAST);

        return tarjeta;
    }

    private void estilizarBotonPrimario(JButton boton) {
        boton.setFocusPainted(false);
        boton.setBackground(new Color(70, 130, 200));
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("SansSerif", Font.BOLD, 12));
        boton.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
    }

    private void estilizarBotonSecundario(JButton boton) {
        boton.setFocusPainted(false);
        boton.setBackground(new Color(230, 230, 230));
        boton.setForeground(new Color(60, 60, 60));
        boton.setFont(new Font("SansSerif", Font.BOLD, 12));
        boton.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
    }
}
