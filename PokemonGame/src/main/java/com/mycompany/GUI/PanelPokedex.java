package com.mycompany.gui;

import com.mycompany.Facade.PokemonGameFacade;
import com.mycompany.Model.pokemon.Pokemon;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import com.mycompany.Model.pokemon.TipoPokemon;

// Panel principal de la Pokedex que usa el patron Fachada para obtener datos
public class PanelPokedex extends JPanel {

    // Define la paleta de colores personalizada para la interfaz estilo retro
    private static final Color FONDO_APP = new Color(250, 244, 234);
    private static final Color ROJO_ACENTO = new Color(200, 44, 44);
    private static final Color ROJO_ACENTO_OSC = new Color(150, 28, 28);
    private static final Color BISEL_CLARO = new Color(182, 200, 166);
    private static final Color BISEL_OSCURO = new Color(122, 142, 108);
    private static final Color BISEL_BORDE = new Color(78, 96, 66);
    private static final Color BISEL_BORDE_HOVER = new Color(255, 196, 0);
    private static final Color PANTALLA_FONDO = new Color(240, 246, 224);
    private static final Color PANTALLA_BORDE = new Color(150, 168, 122);
    private static final Color HEADER_CLARO = new Color(146, 198, 120);
    private static final Color HEADER_OSCURO = new Color(92, 150, 76);
    private static final Color SPRITE_FONDO = new Color(222, 234, 202);
    private static final Color SPRITE_BORDE = new Color(140, 160, 116);
    private static final Color TEXTO_OSCURO = new Color(44, 52, 38);
    private static final Color TEXTO_SUAVE = new Color(96, 108, 84);

    // Fachada que centraliza el acceso a la logica y datos del juego
    private final PokemonGameFacade facade;
    
    // Contenedor de la grilla que muestra las tarjetas de los Pokemon
    private final JPanel grilla;
    
    // Campo de texto para filtrar la busqueda
    private JTextField buscador;

    // Inicializa el panel, configura el diseño y carga los datos iniciales
    public PanelPokedex(PokemonGameFacade facade) {
        this.facade = facade;
        setLayout(new BorderLayout(0, 0));
        setBackground(FONDO_APP);

        add(construirEncabezado(), BorderLayout.NORTH);

        grilla = new JPanel(new GridLayout(0, 2, 22, 22));
        grilla.setBackground(FONDO_APP);
        grilla.setBorder(BorderFactory.createEmptyBorder(6, 16, 20, 16));

        // Envoltorio para evitar que la grilla se estire verticalmente
        JPanel envoltorio = new JPanel(new BorderLayout());
        envoltorio.setBackground(FONDO_APP);
        envoltorio.add(grilla, BorderLayout.NORTH);

        // Scroll que permite navegar por la lista sin ocupar todo el espacio
        JScrollPane scroll = new JScrollPane(envoltorio);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(FONDO_APP);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        add(scroll, BorderLayout.CENTER);

        cargarTodos();
    }

    // Crea la barra superior con titulo, decoracion y campos de busqueda
    private JPanel construirEncabezado() {
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(FONDO_APP);

        // Franja roja decorativa estilo Pokedex de mano
        JPanel franja = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        franja.setBackground(ROJO_ACENTO);
        franja.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, ROJO_ACENTO_OSC));
        franja.add(circuloDecorativo(18, new Color(90, 170, 230), Color.WHITE));
        franja.add(circuloDecorativo(10, new Color(230, 60, 60), new Color(140, 20, 20)));
        franja.add(circuloDecorativo(10, new Color(230, 210, 60), new Color(150, 130, 20)));
        contenedor.add(franja, BorderLayout.NORTH);

        JPanel encabezado = new JPanel(new BorderLayout(8, 8));
        encabezado.setBackground(FONDO_APP);
        encabezado.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));

        JLabel titulo = new JLabel("Pokedex");
        titulo.setFont(RecursosImagenes.getFuentePokemon(26f));
        titulo.setForeground(ROJO_ACENTO_OSC);
        titulo.setIcon(RecursosImagenes.cargarIconoPokebola(26));
        titulo.setIconTextGap(10);

        buscador = new JTextField();
        buscador.setFont(RecursosImagenes.getFuentePokemon(14f));
        buscador.putClientProperty("JTextField.placeholderText", "Buscar Pokemon...");
        buscador.setPreferredSize(new Dimension(260, 34));
        buscador.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 168, 122), 2, true),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));

        JButton btnBuscar = botonEstilizado("Buscar", new Color(180, 30, 30), Color.WHITE);
        JButton btnLimpiar = botonEstilizado("Ver todos", new Color(80, 140, 80), Color.WHITE);

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelBusqueda.setOpaque(false);
        panelBusqueda.add(buscador);
        panelBusqueda.add(btnBuscar);
        panelBusqueda.add(btnLimpiar);

        encabezado.add(titulo, BorderLayout.NORTH);
        encabezado.add(panelBusqueda, BorderLayout.SOUTH);
        contenedor.add(encabezado, BorderLayout.SOUTH);

        // Asigna las acciones a los botones y al campo de texto
        btnBuscar.addActionListener(e -> buscar());
        btnLimpiar.addActionListener(e -> cargarTodos());
        buscador.addActionListener(e -> buscar());

        return contenedor;
    }

    // Dibuja un circulo decorativo personalizado usando graficos 2D
    private JComponent circuloDecorativo(int diametro, Color relleno, Color borde) {
        return new JComponent() {
            public Dimension getPreferredSize() {
                return new Dimension(diametro, diametro);
            }

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

    // Genera un boton con colores personalizados y efectos visuales al pasar el mouse
    private JButton botonEstilizado(String texto, Color fondo, Color textoColor) {
        JButton boton = new JButton(texto);
        boton.setFont(RecursosImagenes.getFuentePokemon(12f));
        boton.setForeground(textoColor);
        boton.setBackground(fondo);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(fondo.darker(), 1, true),
                BorderFactory.createEmptyBorder(7, 16, 7, 16)
        ));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setOpaque(true);
        boton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(fondo.brighter());
            }
            public void mouseExited(MouseEvent e) {
                boton.setBackground(fondo);
            }
        });
        return boton;
    }

    // Busca un Pokemon por nombre usando la Fachada y actualiza la grilla
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
            grilla.add(Box.createGlue());
        } else {
            JLabel lblNoEncontrado = new JLabel("No se encontro: " + texto, SwingConstants.CENTER);
            lblNoEncontrado.setFont(RecursosImagenes.getFuentePokemon(16f));
            lblNoEncontrado.setForeground(TEXTO_SUAVE);
            grilla.add(lblNoEncontrado);
        }

        grilla.revalidate();
        grilla.repaint();
    }

    // Limpia la grilla y carga todos los Pokemon disponibles desde la base de datos
    private void cargarTodos() {
        buscador.setText("");
        grilla.removeAll();

        List<Pokemon> lista = facade.obtenerTodosLosPokemon();

        if (lista.isEmpty()) {
            JLabel lblVacio = new JLabel("La Pokedex esta vacia (revisa la BD)", SwingConstants.CENTER);
            lblVacio.setFont(RecursosImagenes.getFuentePokemon(14f));
            lblVacio.setForeground(TEXTO_SUAVE);
            grilla.add(lblVacio);
        } else {
            for (Pokemon p : lista) {
                grilla.add(crearTarjetaEstiloGen1(p));
            }
        }

        grilla.revalidate();
        grilla.repaint();
    }

    // Construye una tarjeta visual estilo GameBoy Advance para cada Pokemon
    private JPanel crearTarjetaEstiloGen1(Pokemon p) {
        TarjetaGBA tarjeta = new TarjetaGBA();
        tarjeta.setPreferredSize(new Dimension(430, 210));
        tarjeta.setLayout(new BorderLayout());

        // Barra superior con degradado dentro de la pantalla
        BarraDegradado header = new BarraDegradado(HEADER_CLARO, HEADER_OSCURO);
        header.setLayout(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));

        JLabel lblNombre = new JLabel(p.getNombre().toUpperCase());
        lblNombre.setFont(RecursosImagenes.getFuentePokemon(15f));
        lblNombre.setForeground(new Color(30, 40, 24));

        JLabel lblNumero = new JLabel("No." + String.format("%03d", p.getNumeroPokedex()));
        lblNumero.setFont(RecursosImagenes.getFuentePokemon(13f));
        lblNumero.setForeground(new Color(50, 62, 40));

        header.add(lblNombre, BorderLayout.WEST);
        header.add(lblNumero, BorderLayout.EAST);

        // Contenedor principal de la informacion
        JPanel contenido = new JPanel(new BorderLayout(14, 0));
        contenido.setOpaque(false);
        contenido.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Cuadro que muestra el sprite del Pokemon
        JPanel panelSprite = new JPanel(new GridBagLayout());
        panelSprite.setBackground(SPRITE_FONDO);
        panelSprite.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SPRITE_BORDE, 2, true),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)
        ));
        panelSprite.setPreferredSize(new Dimension(140, 170));

        JLabel spriteLabel = new JLabel(RecursosImagenes.spritePokemonIcon(p.getNombre(), 96, 96));
        panelSprite.add(spriteLabel);

        // Columna derecha que muestra el tipo y las estadisticas
        JPanel columnaInfo = new JPanel();
        columnaInfo.setOpaque(false);
        columnaInfo.setLayout(new BoxLayout(columnaInfo, BoxLayout.Y_AXIS));

        JPanel filaTipo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        filaTipo.setOpaque(false);
        JLabel lblTipoTag = new JLabel("TYPE    ");
        lblTipoTag.setFont(RecursosImagenes.getFuentePokemon(10f));
        lblTipoTag.setForeground(TEXTO_SUAVE);
        filaTipo.add(lblTipoTag);
        JLabel badgeTipo = new JLabel(RecursosImagenes.cargarBadgeTipo(p.getTipo().toString(), 70, 22));
        filaTipo.add(badgeTipo);
        filaTipo.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Caja que agrupa las barras de estadisticas
        JPanel statsBox = new JPanel();
        statsBox.setOpaque(false);
        statsBox.setLayout(new BoxLayout(statsBox, BoxLayout.Y_AXIS));
        statsBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(PANTALLA_BORDE, 1),
                        "STATS", 0, 0, RecursosImagenes.getFuentePokemon(9f), TEXTO_SUAVE
                ),
                BorderFactory.createEmptyBorder(4, 6, 6, 6)
        ));
        statsBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        statsBox.add(new BarraStat("HP", p.getHp(), 130, new Color(96, 190, 96)));
        statsBox.add(Box.createVerticalStrut(4));
        statsBox.add(new BarraStat("ATK", p.getAtaque(), 130, new Color(230, 130, 60)));
        statsBox.add(Box.createVerticalStrut(4));
        statsBox.add(new BarraStat("DEF", p.getDefensa(), 130, new Color(90, 150, 220)));
        statsBox.add(Box.createVerticalStrut(4));
        statsBox.add(new BarraStat("SPD", p.getVelocidad(), 130, new Color(220, 90, 150)));

        columnaInfo.add(filaTipo);
        columnaInfo.add(Box.createVerticalStrut(0));
        columnaInfo.add(statsBox);

        contenido.add(panelSprite, BorderLayout.WEST);
        contenido.add(columnaInfo, BorderLayout.CENTER);

        // Ensamblaje final de la tarjeta
        JPanel pantallaInterior = new JPanel(new BorderLayout());
        pantallaInterior.setOpaque(false);
        pantallaInterior.setBorder(new EmptyBorder(10, 10, 10, 10));
        pantallaInterior.add(header, BorderLayout.NORTH);
        pantallaInterior.add(contenido, BorderLayout.CENTER);

        tarjeta.add(pantallaInterior, BorderLayout.CENTER);
        tarjeta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Efecto visual al pasar el mouse sobre la tarjeta
        tarjeta.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                tarjeta.setHover(true);
            }
            public void mouseExited(MouseEvent e) {
                tarjeta.setHover(false);
            }
        });

        return tarjeta;
    }

    // Dibuja el marco exterior de la tarjeta con sombras y bordes redondeados
    private static class TarjetaGBA extends JPanel {
        private boolean hover = false;

        TarjetaGBA() {
            setOpaque(false);
        }

        void setHover(boolean hover) {
            this.hover = hover;
            repaint();
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int arco = 20;

            // Sombra suave
            g2.setColor(new Color(0, 0, 0, 35));
            g2.fillRoundRect(5, 7, w - 8, h - 8, arco, arco);

            // Bisel exterior con degradado
            GradientPaint bisel = new GradientPaint(0, 0, BISEL_CLARO, 0, h, BISEL_OSCURO);
            g2.setPaint(bisel);
            g2.fillRoundRect(2, 2, w - 8, h - 8, arco, arco);

            g2.setColor(hover ? BISEL_BORDE_HOVER : BISEL_BORDE);
            g2.setStroke(new BasicStroke(hover ? 3f : 2f));
            g2.drawRoundRect(3, 3, w - 10, h - 10, arco - 2, arco - 2);

            // Pantalla interior
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

    // Dibuja un fondo con degradado vertical para los encabezados
    private static class BarraDegradado extends JPanel {
        private final Color c1;
        private final Color c2;

        BarraDegradado(Color c1, Color c2) {
            this.c1 = c1;
            this.c2 = c2;
            setOpaque(false);
        }

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

    // Dibuja la etiqueta del tipo del Pokemon con su color caracteristico
    private static class EtiquetaTipo extends JComponent {
        private final TipoPokemon tipo;

        EtiquetaTipo(TipoPokemon tipo) {
            this.tipo = tipo;
            setFont(RecursosImagenes.getFuentePokemon(11f));
        }

        public Dimension getPreferredSize() {
            FontMetrics fm = getFontMetrics(getFont());
            int ancho = fm.stringWidth(tipo.toString().toUpperCase()) + 26;
            return new Dimension(Math.max(70, ancho), 22);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color fondo = RecursosImagenes.colorTipo(tipo);
            int w = getWidth();
            int h = getHeight();

            g2.setColor(fondo);
            g2.fillRoundRect(0, 0, w - 1, h - 1, h, h);
            g2.setColor(fondo.darker());
            g2.drawRoundRect(0, 0, w - 1, h - 1, h, h);

            g2.setColor(RecursosImagenes.colorTextoSobreTipo(tipo));
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            String texto = tipo.toString().toUpperCase();
            int tx = (w - fm.stringWidth(texto)) / 2;
            int ty = (h + fm.getAscent()) / 2 - 2;
            g2.drawString(texto, tx, ty);

            g2.dispose();
        }
    }

    // Dibuja una barra de progreso visual para las estadisticas del Pokemon
    private static class BarraStat extends JComponent {
        private static final int MAX_REFERENCIA_DEFECTO = 150;
        private final String etiqueta;
        private final int valor;
        private final int maximo;
        private final Color color;

        BarraStat(String etiqueta, int valor, int maximo, Color color) {
            this.etiqueta = etiqueta;
            this.valor = valor;
            this.maximo = maximo <= 0 ? MAX_REFERENCIA_DEFECTO : maximo;
            this.color = color;
            setFont(RecursosImagenes.getFuentePokemon(9f));
            setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        public Dimension getPreferredSize() {
            return new Dimension(190, 16);
        }

        public Dimension getMaximumSize() {
            return new Dimension(Integer.MAX_VALUE, 16);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            int anchoEtiqueta = 32;
            int anchoValor = 30;
            int trackX = anchoEtiqueta;
            int trackW = w - anchoEtiqueta - anchoValor;

            g2.setFont(getFont());
            g2.setColor(TEXTO_SUAVE);
            g2.drawString(etiqueta, 0, h - 4);

            // Fondo de la barra
            g2.setColor(new Color(210, 210, 200));
            g2.fillRoundRect(trackX, 3, trackW, h - 8, 6, 6);

            // Relleno proporcional al valor de la estadistica
            double proporcion = Math.max(0, Math.min(1.0, valor / (double) maximo));
            int anchoRelleno = (int) (trackW * proporcion);
            if (anchoRelleno > 2) {
                GradientPaint gp = new GradientPaint(trackX, 0, color.brighter(), trackX, h, color.darker());
                g2.setPaint(gp);
                g2.fillRoundRect(trackX, 3, anchoRelleno, h - 8, 6, 6);
            }
            g2.setColor(new Color(150, 150, 140));
            g2.drawRoundRect(trackX, 3, trackW, h - 8, 6, 6);

            g2.setColor(TEXTO_OSCURO);
            g2.drawString(String.valueOf(valor), trackX + trackW + 4, h - 4);

            g2.dispose();
        }
    }
}