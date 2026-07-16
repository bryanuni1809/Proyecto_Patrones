/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * Panel de la pestaña "Batalla".
 *
 * Antes este flujo abría VentanaSeleccionEquipo y VentanaBatalla, que eran
 * JFrame: ventanas del sistema operativo separadas de la ventana principal
 * (por eso "se ejecutaban fuera" de la pestaña). Ahora todo vive dentro de
 * este mismo JPanel, usando un CardLayout para alternar entre tres
 * pantallas: inicio -> selección de equipo -> batalla. Nada se abre en una
 * ventana aparte.
 */
public class Sistema_Batalla extends JPanel {

    private static final int TAMANO_EQUIPO = 3;

    private static final String CARD_INICIO = "inicio";
    private static final String CARD_SELECCION = "seleccion";
    private static final String CARD_BATALLA = "batalla";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contenedor = new JPanel(cardLayout);

    // Referencias a las "cards" reemplazables, para poder sacarlas antes de
    // montar una nueva versión (equipo elegido de nuevo, revancha, etc.).
    private JPanel panelSeleccionActual;
    private JPanel panelBatallaActual;

    public Sistema_Batalla() {
        setLayout(new BorderLayout());
        add(contenedor, BorderLayout.CENTER);
        contenedor.add(crearPanelInicio(), CARD_INICIO);
        cardLayout.show(contenedor, CARD_INICIO);
    }

    /**
     * Punto de entrada estático "clásico", conservado por compatibilidad
     * con quien lo llame fuera de la pestaña (p. ej. Main). Sigue abriendo
     * VentanaSeleccionEquipo/VentanaBatalla como ventanas aparte, tal como
     * funcionaba antes. El flujo embebido en la pestaña (arriba) es
     * independiente de este y no usa JFrame.
     */
    public static void iniciar() {
        SwingUtilities.invokeLater(Sistema_Batalla::iniciarJuego);
    }

    public static void iniciarJuego() {
        Pokedex pokedex = Pokedex.getInstancia();
        List<Pokemon> disponibles = pokedex.getPokemones();

        if (disponibles == null || disponibles.isEmpty()) {
            System.err.println("La Pokedex está vacía: revisa la conexión a la base de datos.");
            return;
        }

        VentanaSeleccionEquipo seleccion = new VentanaSeleccionEquipo(
                disponibles, TAMANO_EQUIPO, Sistema_Batalla::iniciarCombateEnVentana);
        seleccion.setVisible(true);
    }

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

        jugador.agregarItem(new Pocion("Poción", 3, 20));
        jugador.agregarItem(new Pocion("Súper Poción", 2, 50));
        jugador.agregarItem(new Pocion("Hiper Poción", 1, 200));

        if (jugador.getPokemonActivo() == null || rival.getPokemonActivo() == null) {
            System.err.println("No se pudo armar algún equipo.");
            return;
        }

        Combate combate = new Combate(jugador, rival, new ArrayList<>());
        combate.registrarObservador(new BitacoraCombate());

        VentanaBatalla ventana = new VentanaBatalla(jugador, rival, combate, com.mycompany.pokemongame.Main::iniciarJuego);
        ventana.setVisible(true);
        ventana.iniciarCombateEnHilo();
    }

    private JPanel crearPanelInicio() {
        JPanel inicio = new JPanel(new BorderLayout());
        inicio.setBackground(new Color(235, 244, 250));

        JLabel titulo = new JLabel("Modo Batalla", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        titulo.setForeground(new Color(40, 60, 130));
        titulo.setBorder(BorderFactory.createEmptyBorder(40, 0, 10, 0));

        JLabel subtitulo = new JLabel("Arma tu equipo y enfréntate a un entrenador rival", SwingConstants.CENTER);
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(80, 80, 90));

        JPanel norte = new JPanel(new BorderLayout());
        norte.setOpaque(false);
        norte.add(titulo, BorderLayout.NORTH);
        norte.add(subtitulo, BorderLayout.CENTER);

        JButton comenzar = new JButton("Iniciar batalla");
        comenzar.setFont(new Font("SansSerif", Font.BOLD, 16));
        comenzar.setFocusPainted(false);
        comenzar.setPreferredSize(new Dimension(220, 50));
        comenzar.addActionListener(e -> mostrarSeleccionEquipo());

        JPanel centro = new JPanel();
        centro.setOpaque(false);
        centro.add(comenzar);

        inicio.add(norte, BorderLayout.NORTH);
        inicio.add(centro, BorderLayout.CENTER);
        return inicio;
    }

    /** Monta PanelSeleccionEquipo dentro del CardLayout y lo muestra. */
    private void mostrarSeleccionEquipo() {
        Pokedex pokedex = Pokedex.getInstancia();
        List<Pokemon> disponibles = pokedex.getPokemones();

        if (disponibles == null || disponibles.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "La Pokedex está vacía: revisa la conexión a la base de datos.",
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

    /** Arma jugador/rival/combate y monta PanelBatalla dentro del CardLayout. */
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

        // Pociones iniciales del jugador (patrón Composite ya implementado en Pocion/MochilaGrupo).
        jugador.agregarItem(new Pocion("Poción", 3, 20));
        jugador.agregarItem(new Pocion("Súper Poción", 2, 50));
        jugador.agregarItem(new Pocion("Hiper Poción", 1, 200));

        if (jugador.getPokemonActivo() == null || rival.getPokemonActivo() == null) {
            JOptionPane.showMessageDialog(this, "No se pudo armar algún equipo.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Combate combate = new Combate(jugador, rival, new ArrayList<>());
        combate.registrarObservador(new BitacoraCombate()); // se conserva el log por consola original

        PanelBatalla panelBatalla = new PanelBatalla();

        // Conecta el patrón Strategy: el jugador elige por clics, el rival por IA.
        jugador.setSelectorAtaque(new SelectorAtaqueGUI(panelBatalla, jugador));
        rival.setSelectorAtaque(new SelectorAtaqueIA());

        combate.registrarObservador(new CombateObservadorGUI(panelBatalla, jugador, rival,
                this::mostrarFinDeCombate));

        panelBatalla.setPokemonJugador(jugador.getPokemonActivo());
        panelBatalla.setPokemonRival(rival.getPokemonActivo());
        panelBatalla.mostrarMensaje("¡Comienza el combate entre " + jugador.getNombre()
                + " y " + rival.getNombre() + "!");

        if (panelBatallaActual != null) {
            contenedor.remove(panelBatallaActual);
        }
        panelBatallaActual = panelBatalla;
        contenedor.add(panelBatallaActual, CARD_BATALLA);
        cardLayout.show(contenedor, CARD_BATALLA);
        contenedor.revalidate();
        contenedor.repaint();

        // Combate.iniciarBatalla() es bloqueante, por eso corre en un hilo aparte
        // (igual que antes en VentanaBatalla.iniciarCombateEnHilo()).
        Thread hiloCombate = new Thread(combate::iniciarBatalla, "hilo-combate");
        hiloCombate.setDaemon(true);
        hiloCombate.start();
    }

    /** Al terminar el combate: preguntar si quiere revancha o volver al inicio de la pestaña. */
    private void mostrarFinDeCombate(String ganador) {
        SwingUtilities.invokeLater(() -> {
            int opcion = JOptionPane.showConfirmDialog(this,
                    "Ganador: " + ganador + "\n¿Deseas jugar de nuevo?",
                    "Combate terminado", JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                mostrarSeleccionEquipo();
            } else {
                cardLayout.show(contenedor, CARD_INICIO);
            }
        });
    }

    /** Elige, sin repetir, hasta "cantidad" Pokémon al azar de la Pokedex para el rival. */
    private static List<Pokemon> elegirRivalesAleatorios(List<Pokemon> disponibles, int cantidad) {
        List<Pokemon> copia = new ArrayList<>(disponibles);
        Collections.shuffle(copia);
        return copia.subList(0, Math.min(cantidad, copia.size()));
    }

    /**
     * Clona un Pokémon base de la Pokedex (Prototype, ya implementado en
     * Pokemon) y le agrega un par de ataques armados con el Builder
     * existente. La BD solo guarda los stats base; los ataques no vienen
     * de la BD, así que se arman aquí igual que lo haría cualquier otro
     * cliente del Builder.
     */
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

    /**
     * Asocia un estado alterado distinto según el tipo del Pokémon, así el
     * jugador puede toparse con Paralizado, Dormido o Quemado (los 3 ya
     * implementados en Patrones/state) en vez de usar siempre el mismo.
     */
    private static EstadoPokemon estadoParaTipo(TipoPokemon tipo) {
        return switch (tipo) {
            case FUEGO -> new EstadoQuemado();
            case PLANTA, PSIQUICO -> new EstadoDormido();
            default -> new EstadoParalizado();
        };
    }
}