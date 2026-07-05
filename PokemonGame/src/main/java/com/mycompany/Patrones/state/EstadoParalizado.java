package com.mycompany.Patrones.state;

import com.mycompany.Model.pokemon.Pokemon;
import java.util.Random;

/**
 * Estado Paralizado: el Pokémon tiene 25% de probabilidad de no poder atacar
 * cada turno. Además su velocidad se reduce a la mitad mientras dure.
 *
 * Patrón State → comportamiento dinámico en tiempo de ejecución.
 * SOLID → OCP: este comportamiento no modifica la clase Pokemon.
 */
public class EstadoParalizado implements EstadoPokemon {

    private final Random random = new Random();

    @Override
    public String getNombre() {
        return "Paralizado";
    }

    @Override
    public boolean alIniciarTurno(Pokemon pokemon) {
        // 25% de probabilidad de no poder moverse
        if (random.nextInt(4) == 0) {
            System.out.println(pokemon.getNombre() + " está paralizado y no puede moverse!");
            return false; // No puede atacar
        }
        return true; // Puede atacar normalmente
    }

    @Override
    public void alFinalTurno(Pokemon pokemon) {
        // La parálisis no se cura sola en este modelo
        System.out.println(pokemon.getNombre() + " sigue paralizado.");
    }
}
