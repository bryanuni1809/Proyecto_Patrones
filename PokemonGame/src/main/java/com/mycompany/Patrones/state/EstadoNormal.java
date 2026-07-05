package com.mycompany.Patrones.state;

import com.mycompany.Model.pokemon.Pokemon;

/**
 * Estado Normal: el Pokémon no tiene ninguna alteración.
 * Es el estado por defecto al crear un Pokémon.
 *
 * Patrón State → estado concreto que no interfiere con ninguna acción.
 */
public class EstadoNormal implements EstadoPokemon {

    @Override
    public String getNombre() {
        return "Normal";
    }

    @Override
    public boolean alIniciarTurno(Pokemon pokemon) {
        // Sin alteraciones: el Pokémon puede atacar con normalidad
        return true;
    }

    @Override
    public void alFinalTurno(Pokemon pokemon) {
        // Sin efectos al final del turno
    }
}
