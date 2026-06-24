package com.mycompany.Model.pokedex;

import com.mycompany.Model.pokemon.Pokemon;
import com.mycompany.SQL_Conexion.PokemonDBQ;
import java.util.List;

/**
 * Gestiona el registro global de Pokémon mediante el patrón Singleton.
 */
public class Pokedex {

    private static Pokedex instancia;

    private final List<Pokemon> pokemones;
    PokemonDBQ bQ= new PokemonDBQ();

    private Pokedex() {
        pokemones = bQ.obtenerListaPokemon();
    }

    public static Pokedex getInstancia() {

        if (instancia == null) {
            instancia = new Pokedex();
        }

        return instancia;
    }

    public Pokemon buscarporNombre(String nombre) {
        for (Pokemon pk : pokemones) {
            if (pk.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                return pk;
            }
        }
        return null;
    }

    public void agregarPokemon(Pokemon pokemon) {
        pokemones.add(pokemon);
    }

    public List<Pokemon> getPokemones() {
        return pokemones;
    }
}
