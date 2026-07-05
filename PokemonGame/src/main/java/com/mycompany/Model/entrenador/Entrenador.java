
package com.mycompany.Model.entrenador;

import com.mycompany.Model.pokemon.Pokemon;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un entrenador que puede tener un equipo de hasta 6 Pokémon.
 */

public class Entrenador {
   private String nombre;
    private Pokemon[] equipo;

    public Entrenador(String nombre) {

        this.nombre = nombre;
        this.equipo = new Pokemon[3];
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Pokemon[] getEquipo() {
        return equipo;
    }

    public void agregarPokemon(Pokemon pokemon) {

        for (Pokemon pokemon1 : equipo) {
            pokemon1 = pokemon;
        }
    } 
}
