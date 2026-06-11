
package com.pokemon.model.pokedex;

import com.pokemon.model.pokemon.Pokemon;
import java.util.ArrayList;
import java.util.List;

public class Pokedex {
    private static Pokedex instancia;

    private List<Pokemon> pokemones;

    private Pokedex() {

        pokemones = new ArrayList<>();
    }

    public static Pokedex getInstancia() {

        if (instancia == null) {
            instancia = new Pokedex();
        }

        return instancia;
    }

    public void agregarPokemon(Pokemon pokemon) {
        pokemones.add(pokemon);
    }

    public List<Pokemon> getPokemones() {
        return pokemones;
    }
}
