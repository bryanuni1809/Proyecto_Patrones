package com.mycompany.GUI;

import com.mycompany.Facade.PokemonGameFacade;
import com.mycompany.Model.pokemon.Pokemon;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Pantalla de Pokédex estilo Pokémon Gen 1 (Game Boy)
 * Con fondos de pokedex y tarjetas estilo "POKéMON INFO"
 */
public class PanelPokedex extends JPanel {
    private final PokemonGameFacade facade;
    private final JPanel grilla;
    private final JTextField buscador;
    
    // Mapeo: número de Pokédex -> archivo de fondo
    // Puedes cambiar estos fondos según los que tengas disponibles
    private static final String[] FONDOS_POKEMON = {
        "sprite-1-1.png", "sprite-1-2.png", "sprite-2-1.png", "sprite-3-1.png",
        "sprite-4-1.png", "sprite-5-1.png", "sprite-6-1.png", "sprite-7-1.png",
        "sprite-8-1.png", "sprite-9-1.png", "sprite-10-1.png", "sprite-11-1.png"
    };

    public PanelPokedex(PokemonGameFacade facade) {
        this.facade = facade;
        setLayout(new BorderLayout(0, 0));
        setBackground(new Color(248, 240, 232)); // Color crema estilo Game Boy

        // ═══════════════════════════════════════════════════════════════
        // ENCABEZADO ESTILO POKEDEX
        // ═══════════════════════════════════════════════════════════════
        JPanel encabezado = new JPanel(new BorderLayout(8, 8));
        encabezado.setBackground(new Color(248, 240, 232));
        encabezado.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("📘 Pokédex Nacional");
        titulo.setFont(CargadorImagenes.getFuentePokemon(24f));
        titulo.setForeground(new Color(40, 40, 40));

        buscador = new JTextField();
        buscador.setFont(CargadorImagenes.getFuentePokemon(14f));
        buscador.putClientProperty("JTextField.placeholderText", "Buscar Pokémon...");
        buscador.setPreferredSize(new Dimension(250, 30));

        JButton btnBuscar = new JButton("Buscar");
        JButton btnLimpiar = new JButton("Ver todos");
        btnBuscar.setFont(CargadorImagenes.getFuentePokemon(12f));
        btnLimpiar.setFont(CargadorImagenes.getFuentePokemon(12f));

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelBusqueda.setOpaque(false);
        panelBusqueda.add(buscador);
        panelBusqueda.add(btnBuscar);
        panelBusqueda.add(btnLimpiar);

        encabezado.add(titulo, BorderLayout.NORTH);
        encabezado.add(panelBusqueda, BorderLayout.SOUTH);
        add(encabezado, BorderLayout.NORTH);

        // ═══════════════════════════════════════════════════════════════
        // GRILLA DE TARJETAS ESTILO "POKéMON INFO"
        // ═══════════════════════════════════════════════════════════════
        grilla = new JPanel();
        grilla.setLayout(new BoxLayout(grilla, BoxLayout.Y_AXIS));
        grilla.setBackground(new Color(248, 240, 232));
        grilla.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(grilla);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scroll, BorderLayout.CENTER);

        // Event listeners
        btnBuscar.addActionListener(e -> buscar());
        btnLimpiar.addActionListener(e -> cargarTodos());
        buscador.addActionListener(e -> buscar());

        cargarTodos();
    }

    private void buscar() {
        String texto = buscador.getText().trim();
        if (texto.isEmpty()) {
            cargarTodos();
            return;
        }

        Pokemon encontrado = facade.buscarPokemon(texto);
        grilla.removeAll();

        if (encontrado != null) {
            grilla.add(crearTarjetaEstiloGen1(encontrado));
        } else {
            JLabel lblNoEncontrado = new JLabel(" No se encontró: " + texto, SwingConstants.CENTER);
            lblNoEncontrado.setFont(CargadorImagenes.getFuentePokemon(16f));
            grilla.add(lblNoEncontrado);
        }

        grilla.revalidate();
        grilla.repaint();
    }

    private void cargarTodos() {
        buscador.setText("");
        grilla.removeAll();

        List<Pokemon> lista = facade.obtenerTodosLosPokemon();

        if (lista.isEmpty()) {
            JLabel lblVacio = new JLabel(
                "<html><center>La Pokédex está vacía<br>" +
                "<small>(revisa la conexión a la BD)</small></center></html>",
                SwingConstants.CENTER);
            lblVacio.setFont(CargadorImagenes.getFuentePokemon(14f));
            grilla.add(lblVacio);
        } else {
            // Mostrar en grilla de 2 columnas
            JPanel fila = null;
            int contador = 0;

            for (Pokemon p : lista) {
                if (contador % 2 == 0) {
                    fila = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
                    fila.setOpaque(false);
                    fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));
                    grilla.add(fila);
                }

                fila.add(crearTarjetaEstiloGen1(p));
                contador++;
            }
        }

        grilla.revalidate();
        grilla.repaint();
    }

    /**
     * Crea una tarjeta estilo "POKéMON INFO" de Gen 1
     */
    private JPanel crearTarjetaEstiloGen1(Pokemon p) {
        // Panel principal con borde estilo Game Boy
        JPanel tarjeta = new JPanel(new BorderLayout(0, 0));
        tarjeta.setPreferredSize(new Dimension(420, 260));
        tarjeta.setMaximumSize(new Dimension(420, 260));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(120, 120, 120), 2),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        tarjeta.setBackground(new Color(248, 248, 248));
        tarjeta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // ═══════════════════════════════════════════════════════════════
        // BARRA SUPERIOR (como en la imagen: "POKéMON INFO" + No)
        // ═══════════════════════════════════════════════════════════════
        JPanel barraSuperior = new JPanel(new BorderLayout());
        barraSuperior.setBackground(new Color(168, 208, 176)); // Verde claro estilo GB
        barraSuperior.setBorder(BorderFactory.createLineBorder(new Color(88, 88, 88), 1));
        barraSuperior.setPreferredSize(new Dimension(420, 24));

        JLabel lblTitulo = new JLabel("POKéMON INFO");
        lblTitulo.setFont(CargadorImagenes.getFuentePokemon(12f));
        lblTitulo.setForeground(new Color(40, 40, 40));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));

        JLabel lblNumero = new JLabel("No " + String.format("%03d", p.getNumeroPokedex()));
        lblNumero.setFont(CargadorImagenes.getFuentePokemon(12f));
        lblNumero.setHorizontalAlignment(SwingConstants.RIGHT);
        lblNumero.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));

        barraSuperior.add(lblTitulo, BorderLayout.WEST);
        barraSuperior.add(lblNumero, BorderLayout.EAST);

        // ═══════════════════════════════════════════════════════════════
        // CONTENIDO PRINCIPAL
        // ═══════════════════════════════════════════════════════════════
        JPanel contenido = new JPanel(new BorderLayout(8, 8));
        contenido.setBackground(new Color(248, 248, 248));
        contenido.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // Panel izquierdo: Sprite del Pokémon
        JPanel panelSprite = new JPanel(new GridBagLayout());
        panelSprite.setOpaque(false);
        panelSprite.setPreferredSize(new Dimension(140, 180));
        panelSprite.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Cargar sprite del Pokémon
        JLabel spriteLabel = new JLabel(
            CargadorImagenes.cargarSpritePokemon(p.getNumeroPokedex(), 96, 96)
        );
        spriteLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panelSprite.add(spriteLabel, gbc);

        // Nivel (como en la imagen: "Lv5")
        JLabel lblNivel = new JLabel("Lv" + p.getNivel());
        lblNivel.setFont(CargadorImagenes.getFuentePokemon(10f));
        lblNivel.setForeground(new Color(80, 80, 80));
        lblNivel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridy = 1;
        gbc.weighty = 0;
        gbc.insets = new Insets(0, 5, 5, 5);
        panelSprite.add(lblNivel, gbc);

        // Panel derecho: Información detallada
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setOpaque(false);

        // Tabla de información estilo Gen 1
        JPanel infoGrid = new JPanel(new GridLayout(7, 2, 4, 2));
        infoGrid.setOpaque(false);
        infoGrid.setMaximumSize(new Dimension(240, 200));

        // Agregar filas de información
        infoGrid.add(crearLabelInfo("NAME:", p.getNombre().toUpperCase()));
        infoGrid.add(crearLabelValor(p.getNombre()));

        infoGrid.add(crearLabelInfo("TYPE:", ""));
        infoGrid.add(crearLabelTipo(p.getTipo()));

        infoGrid.add(crearLabelInfo("OT:", "RED"));
        infoGrid.add(crearLabelInfo("", ""));

        infoGrid.add(crearLabelInfo("IDNo:", String.format("%05d", p.getNumeroPokedex())));
        infoGrid.add(crearLabelInfo("", ""));

        infoGrid.add(crearLabelInfo("ITEM:", "NONE"));
        infoGrid.add(crearLabelInfo("", ""));

        infoGrid.add(crearLabelInfo("HP:", String.valueOf(p.getHp())));
        infoGrid.add(crearLabelInfo("", ""));

        infoGrid.add(crearLabelInfo("ATK:", String.valueOf(p.getAtaque())));
        infoGrid.add(crearLabelInfo("DEF:", String.valueOf(p.getDefensa())));

        panelInfo.add(infoGrid);

        contenido.add(panelSprite, BorderLayout.WEST);
        contenido.add(panelInfo, BorderLayout.CENTER);

        tarjeta.add(barraSuperior, BorderLayout.NORTH);
        tarjeta.add(contenido, BorderLayout.CENTER);

        // Click para ver detalles completos
        tarjeta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarDetalleCompleto(p);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                tarjeta.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(100, 149, 237), 3),
                    BorderFactory.createEmptyBorder(8, 8, 8, 8)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                tarjeta.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(120, 120, 120), 2),
                    BorderFactory.createEmptyBorder(8, 8, 8, 8)
                ));
            }
        });

        return tarjeta;
    }

    private JLabel crearLabelInfo(String texto, String valor) {
        JLabel label = new JLabel(texto);
        label.setFont(CargadorImagenes.getFuentePokemon(9f));
        label.setForeground(new Color(60, 60, 60));
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        return label;
    }

    private JLabel crearLabelValor(String valor) {
        JLabel label = new JLabel(valor);
        label.setFont(CargadorImagenes.getFuentePokemon(9f));
        label.setForeground(new Color(40, 40, 40));
        return label;
    }

    private JLabel crearLabelTipo(Object tipo) {
        JLabel label = new JLabel(tipo.toString());
        label.setFont(CargadorImagenes.getFuentePokemon(9f));
        label.setForeground(CargadorImagenes.getColorTipo(tipo.toString()));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    private void mostrarDetalleCompleto(Pokemon p) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body style='font-family: monospace; font-size: 14px;'>");
        sb.append("<h2>POKéMON INFO - ").append(p.getNombre()).append("</h2>");
        sb.append("<table>");
        sb.append("<tr><td>N° Pokédex:</td><td>").append(String.format("%03d", p.getNumeroPokedex())).append("</td></tr>");
        sb.append("<tr><td>Tipo:</td><td>").append(p.getTipo()).append("</td></tr>");
        sb.append("<tr><td>Nivel:</td><td>").append(p.getNivel()).append("</td></tr>");
        sb.append("<tr><td>HP:</td><td>").append(p.getHp()).append("</td></tr>");
        sb.append("<tr><td>Ataque:</td><td>").append(p.getAtaque()).append("</td></tr>");
        sb.append("<tr><td>Defensa:</td><td>").append(p.getDefensa()).append("</td></tr>");
        sb.append("<tr><td>Velocidad:</td><td>").append(p.getVelocidad()).append("</td></tr>");
        sb.append("</table></body></html>");

        JOptionPane.showMessageDialog(this, sb.toString(), 
            p.getNombre() + " - #" + String.format("%03d", p.getNumeroPokedex()),
            JOptionPane.PLAIN_MESSAGE);
    }
}