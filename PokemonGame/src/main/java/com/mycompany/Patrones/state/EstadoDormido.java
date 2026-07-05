package com.mycompany.Patrones.state;

import com.mycompany.Model.pokemon.Pokemon;
import java.util.Random;

/**
 * Estado Dormido: el Pokémon no puede atacar mientras duerme.
 * Se despierta aleatoriamente entre 1 y 3 turnos.
 *
 * Patrón State → al despertar, el estado cambia a EstadoNormal automáticamente.
 */
public class EstadoDormido implements EstadoPokemon {

    private int turnosRestantes;
    private final Random random = new Random();

    public EstadoDormido() {
        // Duerme entre 1 y 3 turnos
        this.turnosRestantes = 1 + random.nextInt(3);
    }

    @Override
    public String getNombre() {
        return "Dormido";
    }

    @Override
    public boolean alIniciarTurno(Pokemon pokemon) {
        if (turnosRestantes > 0) {
            System.out.println(pokemon.getNombre() + " está dormido y no puede atacar! ("
                    + turnosRestantes + " turnos restantes)");
            turnosRestantes--;

            // Si ya se acabaron los turnos, se despierta al inicio del siguiente
            if (turnosRestantes == 0) {
                System.out.println(pokemon.getNombre() + " se ha despertado!");
                pokemon.setEstado(new EstadoNormal()); // Cambia de estado
            }
            return false; // No puede atacar
        }
        return true;
    }

    @Override
    public void alFinalTurno(Pokemon pokemon) {
        // No aplica efecto adicional al final del turno
    }
}
