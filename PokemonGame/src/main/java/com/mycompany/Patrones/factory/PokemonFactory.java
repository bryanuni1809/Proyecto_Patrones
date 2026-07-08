
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
                .conId(id)
                .conNumeroPokedex(numeroPokedex)
                .conNombre(nombre)
                .conTipo(tipo)
                .conNivel(nivel)
                .conHp(hp)
                .conAtaque(ataque)
                .conDefensa(defensa)
                .conVelocidad(velocidad)
                .build();
    }
}
