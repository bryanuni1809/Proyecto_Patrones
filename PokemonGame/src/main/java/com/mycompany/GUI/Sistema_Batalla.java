package com.mycompany.gui;

import com.mycompany.Combate.Atk.AtaqueEspecial;
import com.mycompany.Combate.Atk.AtaqueFisico;
import com.mycompany.Combate.Atk.BitacoraCombate;
import com.mycompany.Combate.Atk.Combate;
import com.mycompany.Model.entrenador.Entrenador;
import com.mycompany.Model.pokedex.Pokedex;
import com.mycompany.Model.pokemon.Pokemon;
import com.mycompany.Model.pokemon.TipoPokemon;
import static com.mycompany.Model.pokemon.TipoPokemon.FUEGO;
import static com.mycompany.Model.pokemon.TipoPokemon.PLANTA;
import static com.mycompany.Model.pokemon.TipoPokemon.PSIQUICO;
import com.mycompany.Patrones.composite.Pocion;
import com.mycompany.Patrones.state.EstadoDormido;
import com.mycompany.Patrones.state.EstadoParalizado;
import com.mycompany.Patrones.state.EstadoPokemon;
import com.mycompany.Patrones.state.EstadoQuemado;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

// Panel principal que gestiona el flujo de batalla usando CardLayout
public class Sistema_Batalla extends JPanel {

    // Cantidad fija de Pokemon por equipo
    private static final int TAMANO_EQUIPO = 3;

    // Identificadores para las vistas del CardLayout
    private static final String CARD_INICIO = "inicio";
    private static final String CARD_SELECCION = "seleccion";
    private static final String CARD_BATALLA = "batalla";

    // Manejador de vistas y contenedor principal
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contenedor = new JPanel(cardLayout);

    // Referencias para limpiar y reemplazar vistas anteriores
    private JPanel panelSeleccionActual;
    private JPanel panelBatallaActual;

    // Inicializa el panel y muestra la vista de inicio por defecto
    public Sistema_Batalla() {
        setLayout(new BorderLayout());
        setBackground(EstiloJuego.FONDO_APP);
        contenedor.setBackground(EstiloJuego.FONDO_APP);
        add(contenedor, BorderLayout.CENTER);
        contenedor.add(crearPanelInicio(), CARD_INICIO);
        cardLayout.show(contenedor, CARD_INICIO);
    }

    // Metodo estatico para compatibilidad con ejecucion en ventana independiente
    public static void iniciar() {
        SwingUtilities.invokeLater(Sistema_Batalla::iniciarJuego);
    }

    // Configura y lanza la ventana de seleccion de equipo en modo independiente
    public static void iniciarJuego() {
        Pokedex pokedex = Pokedex.getInstancia();
        List<Pokemon> disponibles = pokedex.getPokemones();

        if (disponibles == null || disponibles.isEmpty()) {
            System.err.println("La Pokedex esta vacia: revisa la conexion a la base de datos.");
            return;
        }

        VentanaSeleccionEquipo seleccion = new VentanaSeleccionEquipo(
                disponibles, TAMANO_EQUIPO, Sistema_Batalla::iniciarCombateEnVentana);
        seleccion.setVisible(true);
    }

    // Arma los equipos y lanza la ventana de batalla en modo independiente
    private static void iniciarCombateEnVentana(List<Pokemon> equipoElegido) {
        Pokedex pokedex = Pokedex.getInstancia();

        Entrenador jugador = new Entrenador("Ash");
        Entrenador rival = new Entrenador("Gary");

        for (Pokemon base : equipoElegido) {
            jugador.agregarPokemon(construirPokemonDeCombate(base));
        }
        for (Pokemon base : elegirRivalesAleatorios(pokedex.getPokemones(), equipoElegido.size())) {
            rival.agregarPokemon(construirPokemonDeCombate(base));
        }

        jugador.agregarItem(new Pocion("Pocion", 3, 20));
        jugador.agregarItem(new Pocion("Super Pocion", 2, 50));
        jugador.agregarItem(new Pocion("Hiper Pocion", 1, 200));

        if (jugador.getPokemonActivo() == null || rival.getPokemonActivo() == null) {
            System.err.println("No se pudo armar algun equipo.");
            return;
        }

        Combate combate = new Combate(jugador, rival, new ArrayList<>());
        combate.registrarObservador(new BitacoraCombate());

        VentanaBatalla ventana = new VentanaBatalla(jugador, rival, combate, com.mycompany.pokemongame.Main::iniciarJuego);
        ventana.setVisible(true);
        ventana.iniciarCombateEnHilo();
    }

    // Construye la vista inicial con boton para comenzar
    private JPanel crearPanelInicio() {
        JPanel inicio = new JPanel(new BorderLayout());
        inicio.setBackground(EstiloJuego.FONDO_APP);
        inicio.add(construirEncabezado(), BorderLayout.NORTH);

        EstiloJuego.TarjetaGBA tarjeta = new EstiloJuego.TarjetaGBA();
        tarjeta.setPreferredSize(new Dimension(380, 260));
        tarjeta.setLayout(new BorderLayout());

        JPanel contenido = new JPanel();
        contenido.setOpaque(false);
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBorder(BorderFactory.createEmptyBorder(30, 24, 24, 24));

        JLabel icono = new JLabel(RecursosImagenes.cargarIconoBatalla(56));
        icono.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titulo = new JLabel("Modo Batalla", SwingConstants.CENTER);
        titulo.setFont(RecursosImagenes.getFuentePokemon(18f));
        titulo.setForeground(EstiloJuego.TEXTO_OSCURO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setBorder(BorderFactory.createEmptyBorder(14, 0, 8, 0));

        JLabel subtitulo = new JLabel(
                "<html><center>Arma tu equipo de 3 Pokemon<br>y enfrentate a un entrenador rival</center></html>",
                SwingConstants.CENTER);
        subtitulo.setFont(RecursosImagenes.getFuentePokemon(11f));
        subtitulo.setForeground(EstiloJuego.TEXTO_SUAVE);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JButton comenzar = EstiloJuego.botonEstilizado("Iniciar batalla", EstiloJuego.ROJO_ACENTO, Color.WHITE);
        comenzar.setAlignmentX(Component.CENTER_ALIGNMENT);
        comenzar.addActionListener(e -> mostrarSeleccionEquipo());

        contenido.add(icono);
        contenido.add(titulo);
        contenido.add(subtitulo);
        contenido.add(comenzar);
        tarjeta.add(contenido, BorderLayout.CENTER);

        JPanel centrado = new JPanel(new GridBagLayout());
        centrado.setOpaque(false);
        centrado.setBackground(EstiloJuego.FONDO_APP);
        centrado.add(tarjeta);

        inicio.add(centrado, BorderLayout.CENTER);
        return inicio;
    }

    // Construye la barra superior con el titulo de la seccion
    private JPanel construirEncabezado() {
        JPanel contenedorEnc = new JPanel(new BorderLayout());
        contenedorEnc.setBackground(EstiloJuego.FONDO_APP);
        contenedorEnc.add(EstiloJuego.franjaDecorativa(), BorderLayout.NORTH);

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(EstiloJuego.FONDO_APP);
        encabezado.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));

        JLabel titulo = new JLabel("Batalla");
        titulo.setFont(RecursosImagenes.getFuentePokemon(26f));
        titulo.setForeground(EstiloJuego.ROJO_ACENTO_OSC);
        titulo.setIcon(RecursosImagenes.cargarIconoBatalla(26));
        titulo.setIconTextGap(10);

        encabezado.add(titulo, BorderLayout.WEST);
        contenedorEnc.add(encabezado, BorderLayout.SOUTH);
        return contenedorEnc;
    }

    // Cambia la vista actual al panel de seleccion de equipo
    private void mostrarSeleccionEquipo() {
        Pokedex pokedex = Pokedex.getInstancia();
        List<Pokemon> disponibles = pokedex.getPokemones();

        if (disponibles == null || disponibles.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "La Pokedex esta vacia: revisa la conexion a la base de datos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (panelSeleccionActual != null) {
            contenedor.remove(panelSeleccionActual);
        }
        panelSeleccionActual = new VentanaSeleccionEquipo(disponibles, TAMANO_EQUIPO, this::iniciarCombate);
        contenedor.add(panelSeleccionActual, CARD_SELECCION);
        cardLayout.show(contenedor, CARD_SELECCION);
        contenedor.revalidate();
        contenedor.repaint();
    }

    // Configura entrenadores, aplica patron Strategy e inicia el hilo de combate
    private void iniciarCombate(List<Pokemon> equipoElegido) {
        Pokedex pokedex = Pokedex.getInstancia();

        Entrenador jugador = new Entrenador("Ash");
        Entrenador rival = new Entrenador("Gary");

        for (Pokemon base : equipoElegido) {
            jugador.agregarPokemon(construirPokemonDeCombate(base));
        }
        for (Pokemon base : elegirRivalesAleatorios(pokedex.getPokemones(), equipoElegido.size())) {
            rival.agregarPokemon(construirPokemonDeCombate(base));
        }

        jugador.agregarItem(new Pocion("Pocion", 3, 20));
        jugador.agregarItem(new Pocion("Super Pocion", 2, 50));
        jugador.agregarItem(new Pocion("Hiper Pocion", 1, 200));

        if (jugador.getPokemonActivo() == null || rival.getPokemonActivo() == null) {
            JOptionPane.showMessageDialog(this, "No se pudo armar algun equipo.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Combate combate = new Combate(jugador, rival, new ArrayList<>());
        combate.registrarObservador(new BitacoraCombate());

        PanelBatalla panelBatalla = new PanelBatalla();

        // Strategy: el jugador usa la interfaz grafica, el rival usa inteligencia artificial
        jugador.setSelectorAtaque(new SelectorAtaqueGUI(panelBatalla, jugador));
        rival.setSelectorAtaque(new SelectorAtaqueIA());

        combate.registrarObservador(new CombateObservadorGUI(panelBatalla, jugador, rival,
                this::mostrarFinDeCombate));

        panelBatalla.setPokemonJugador(jugador.getPokemonActivo());
        panelBatalla.setPokemonRival(rival.getPokemonActivo());
        panelBatalla.mostrarMensaje("Comienza el combate entre " + jugador.getNombre()
                + " y " + rival.getNombre() + "!");

        if (panelBatallaActual != null) {
            contenedor.remove(panelBatallaActual);
        }
        panelBatallaActual = panelBatalla;
        contenedor.add(panelBatallaActual, CARD_BATALLA);
        cardLayout.show(contenedor, CARD_BATALLA);
        contenedor.revalidate();
        contenedor.repaint();

        // El bucle de batalla es bloqueante, por eso se ejecuta en un hilo separado
        Thread hiloCombate = new Thread(combate::iniciarBatalla, "hilo-combate");
        hiloCombate.setDaemon(true);
        hiloCombate.start();
    }

    // Muestra dialogo de revancha o regresa a la vista de inicio
    private void mostrarFinDeCombate(String ganador) {
        SwingUtilities.invokeLater(() -> {
            int opcion = JOptionPane.showConfirmDialog(this,
                    "Ganador: " + ganador + "\nDeseas jugar de nuevo?",
                    "Combate terminado", JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                mostrarSeleccionEquipo();
            } else {
                cardLayout.show(contenedor, CARD_INICIO);
            }
        });
    }

    // Selecciona rivales al azar sin repeticion de la lista disponible
    private static List<Pokemon> elegirRivalesAleatorios(List<Pokemon> disponibles, int cantidad) {
        List<Pokemon> copia = new ArrayList<>(disponibles);
        Collections.shuffle(copia);
        return copia.subList(0, Math.min(cantidad, copia.size()));
    }

    // Usa Prototype para clonar y Builder para agregar ataques base al Pokemon
    private static Pokemon construirPokemonDeCombate(Pokemon base) {
        Pokemon pokemon = base.clonar();
        pokemon.agregarAtaque(
                new AtaqueFisico.AtaqueFisicoBuilder("Placaje", TipoPokemon.NORMAL)
                        .potencia(40)
                        .build());
        pokemon.agregarAtaque(
                new AtaqueEspecial.AtaqueEspecialBuilder("Golpe " + pokemon.getTipo(), pokemon.getTipo())
                        .potencia(55)
                        .efectoEstado(estadoParaTipo(pokemon.getTipo()), 20)
                        .build());
        return pokemon;
    }

    // Asigna un estado alterado segun el tipo, aplicando el patron State
    private static EstadoPokemon estadoParaTipo(TipoPokemon tipo) {
        return switch (tipo) {
            case FUEGO -> new EstadoQuemado();
            case PLANTA, PSIQUICO -> new EstadoDormido();
            default -> new EstadoParalizado();
        };
    }
}