
package com.pokemon;

import com.pokemon.factory.PokemonFactory;
import com.pokemon.model.pokedex.Pokedex;
import com.pokemon.model.pokemon.Pokemon;
import com.pokemon.model.pokemon.TipoPokemon;

public class Main {
     public static void main(String[] args) {

        Pokemon pikachu = PokemonFactory.crearPokemon(
                1,
                25,
                "Pikachu",
                TipoPokemon.ELECTRICO,
                5,
                35,
                55,
                40,
                90
        );

        Pokedex pokedex = Pokedex.getInstancia();

        pokedex.agregarPokemon(pikachu);

        pokedex.getPokemones().forEach(System.out::println);
    }
}
