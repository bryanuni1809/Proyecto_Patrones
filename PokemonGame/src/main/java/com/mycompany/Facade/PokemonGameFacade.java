package com.mycompany.Facade;

import com.mycompany.Model.pokedex.Pokedex;
import com.mycompany.Model.pokemon.Pokemon;

import java.util.List;

/**
 * FACHADA (Facade) del juego.
 *
 * La GUI nunca debe hablar directamente con Pokedex, Entrenador, Combate,
 * etc. Todo pasa por esta clase. Por ahora solo expone lo necesario para la
 * pantalla de Pokédex; en las siguientes fases se le van agregando métodos
 * de equipo, mochila y combate sin que la GUI existente se entere.
 */
public class PokemonGameFacade {

    private final Pokedex pokedex;

    public PokemonGameFacade() {
        this.pokedex = Pokedex.getInstancia();
    }

    public List<Pokemon> obtenerTodosLosPokemon() {
        return pokedex.getPokemones();
    }

    public Pokemon buscarPokemon(String nombre) {
        return pokedex.buscarporNombre(nombre);
    }
}
