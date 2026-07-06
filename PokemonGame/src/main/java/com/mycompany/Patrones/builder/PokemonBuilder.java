package com.pokemon.builder;

import com.mycompany.Model.pokemon.Pokemon;
import com.mycompany.Model.pokemon.TipoPokemon;

/**
 * Construye objetos Pokemon utilizando el patron Builder.
 */

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

    // Estado alterado con el que nace el pokemon y por defecto es normal
    private EstadoPokemon estadoInicial = new EstadoNormal();

    public PokemonBuilder conId(int id) {
        this.id = id;
        return this;
    }

    public PokemonBuilder conNumeroPokedex(int numeroPokedex) {
        this.numeroPokedex = numeroPokedex;
        return this;
    }

    public PokemonBuilder conNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public PokemonBuilder conTipo(TipoPokemon tipo) {
        this.tipo = tipo;
        return this;
    }

    public PokemonBuilder conNivel(int nivel) {
        this.nivel = nivel;
        return this;
    }

    public PokemonBuilder conHp(int hp) {
        this.hp = hp;
        return this;
    }

    public PokemonBuilder conAtaque(int ataque) {
        this.ataque = ataque;
        return this;
    }

    public PokemonBuilder conDefensa(int defensa) {
        this.defensa = defensa;
        return this;
    }

    public PokemonBuilder conVelocidad(int velocidad) {
        this.velocidad = velocidad;
        return this;
    }

    /**
     * Define el estado alterado inicial (pattron State).
     * Si no se llama, el Pokémon se construye en EstadoNormal.
     */
    public PokemonBuilder conEstadoInicial(EstadoPokemon estadoInicial) {
        this.estadoInicial = estadoInicial;
        return this;
    }

    /**
     * Construye el pokemon final, ya con su estado inicial asignado.
     */
    public Pokemon build() {
        Pokemon pokemon = new Pokemon(
                id, numeroPokedex, nombre, tipo, nivel, hp, ataque, defensa, velocidad
        );
        pokemon.setEstado(estadoInicial);
        return pokemon;
    }
}
