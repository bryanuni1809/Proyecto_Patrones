package com.mycompany.Abstractas;

public abstract class Pokemon implements Prototype<Pokemon> {
    protected String nombre;
    protected String tipo;
    protected int defensa;
    protected int ataque;
    protected int vida;

    public Pokemon(String nombre, String tipo, int defensa, int ataque, int vida) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.defensa = defensa;
        this.ataque = ataque;
        this.vida = vida;
    }
    
    //constructor de copia para prototype
    public Pokemon(Pokemon pokemon) {
        this.nombre = pokemon.nombre;
        this.tipo = pokemon.tipo;
        this.defensa = pokemon.defensa;
        this.ataque = pokemon.ataque;
        this.vida = pokemon.vida;
    }

    @Override
    public abstract Pokemon clonar();
}
