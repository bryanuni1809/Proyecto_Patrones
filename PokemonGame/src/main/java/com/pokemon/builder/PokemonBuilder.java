
package com.pokemon.builder;

import com.pokemon.model.pokemon.Pokemon;
import com.pokemon.model.pokemon.TipoPokemon;

public class PokemonBuilder {
    private int id;
    private int numeroPokedex;
    private String nombre;
    private TipoPokemon tipo;
    private int nivel;
    private int hp;
    private int ataque;
    private int defensa;
    private int velocidad;

    public PokemonBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public PokemonBuilder setNumeroPokedex(int numeroPokedex) {
        this.numeroPokedex = numeroPokedex;
        return this;
    }

    public PokemonBuilder setNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public PokemonBuilder setTipo(TipoPokemon tipo) {
        this.tipo = tipo;
        return this;
    }
    
    public PokemonBuilder setNivel(int nivel) {
    this.nivel = nivel;
    return this;
    }

    public PokemonBuilder setHp(int hp) {
        this.hp = hp;
        return this;
    }

    public PokemonBuilder setAtaque(int ataque) {
        this.ataque = ataque;
        return this;
    }

    public PokemonBuilder setDefensa(int defensa) {
        this.defensa = defensa;
        return this;
    }

    public PokemonBuilder setVelocidad(int velocidad) {
        this.velocidad = velocidad;
        return this;
    }

    public Pokemon build() {

        return new Pokemon(
            id,
            numeroPokedex,
            nombre,
            tipo,
            nivel,
            hp,
            ataque,
            defensa,
            velocidad
        );
    }
}
