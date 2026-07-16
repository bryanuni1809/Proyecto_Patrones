package com.mycompany.pokemongame;

// Faltan estas importaciones
import com.mycompany.Combate.Atk.Combate;
import com.mycompany.Combate.Atk.AtaqueEspecial;
import com.mycompany.Combate.Atk.AtaqueFisico;
import com.mycompany.Combate.Atk.BitacoraCombate;
import com.mycompany.Combate.Atk.Combate;
import com.mycompany.Model.entrenador.Entrenador;
import com.mycompany.Model.pokedex.Pokedex;
import com.mycompany.Model.pokemon.Pokemon;
import com.mycompany.Model.pokemon.TipoPokemon;
import com.mycompany.Patrones.composite.Pocion;
import com.mycompany.Patrones.state.EstadoDormido;
import com.mycompany.Patrones.state.EstadoParalizado;
import com.mycompany.Patrones.state.EstadoPokemon;
import com.mycompany.Patrones.state.EstadoQuemado;
import com.mycompany.gui.VentanaBatalla;
import com.mycompany.gui.VentanaSeleccionEquipo;

import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Punto de entrada de la aplicación.
 *
 * Flujo:
 *   1. Carga la Pokedex (BD).
 *   2. Abre VentanaSeleccionEquipo para que el jugador arme su equipo
 *      eligiendo entre los Pokémon que ya están en la base de datos.
 *   3. Con esa elección, arma Entrenador jugador (+ sus pociones) y un
 *      Entrenador rival con Pokémon aleatorios de la misma Pokedex.
 *   4. Abre VentanaBatalla como antes.
 */
public class Main {

    private static final int TAMANO_EQUIPO = 3;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::iniciarJuego);
    }

    /**
     * Público y estático para poder usarse también como acción de
     * "jugar de nuevo" desde VentanaBatalla, sin acoplar el paquete gui
     * a esta clase.
     */
    public static void iniciarJuego() {
        Pokedex pokedex = Pokedex.getInstancia();
        List<Pokemon> disponibles = pokedex.getPokemones();

        if (disponibles == null || disponibles.isEmpty()) {
            System.err.println("La Pokedex está vacía: revisa la conexión a la base de datos.");
            return;
        }

        VentanaSeleccionEquipo seleccion = new VentanaSeleccionEquipo(
                disponibles, TAMANO_EQUIPO, Main::iniciarCombate);
        seleccion.setVisible(true);
    }

    private static void iniciarCombate(List<Pokemon> equipoElegido) {
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
            System.err.println("No se pudo armar algún equipo.");
            return;
        }

        Combate combate = new Combate(jugador, rival, new ArrayList<>());
        combate.registrarObservador(new BitacoraCombate()); // se conserva el log por consola original

        VentanaBatalla ventana = new VentanaBatalla(jugador, rival, combate, Main::iniciarJuego);
        ventana.setVisible(true);
        ventana.iniciarCombateEnHilo();
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
