
package com.mycompany.Patrones.factory;

import com.mycompany.Model.pokemon.Pokemon;
import com.mycompany.Model.pokemon.TipoPokemon;
import com.pokemon.builder.PokemonBuilder;

/**
 * Centraliza la creación de Pokémon mediante el patrón Factory Method.
 */

public class PokemonFactory {
    public static Pokemon crearPokemon(
            int id,
            int numeroPokedex,
            String nombre,
            TipoPokemon tipo,
            int nivel,
            int hp,
            int ataque,
            int defensa,
            int velocidad) {

        return new PokemonBuilder()
                .setId(id)
                .setNumeroPokedex(numeroPokedex)
                .setNombre(nombre)
                .setTipo(tipo)
                .setNivel(nivel)
                .setHp(hp)
                .setAtaque(ataque)
                .setDefensa(defensa)
                .setVelocidad(velocidad)
                .build();
    }
}
