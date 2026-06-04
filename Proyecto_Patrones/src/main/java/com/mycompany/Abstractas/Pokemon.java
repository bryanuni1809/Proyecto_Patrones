package com.mycompany.Abstractas;

import com.mycompany.PatronesGOF.Prototype;

public class Pokemon implements Prototype<Pokemon> {
    protected int id;
    protected String nombre;
    protected String tipo;
    protected int defensa;
    protected int ataque;
    protected int vida;


    
    public Pokemon(int id, String nombre, String tipo, int defensa, int ataque, int vida) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.defensa = defensa;
        this.ataque = ataque;
        this.vida = vida;
    }

    public Pokemon() {
        // Constructor vacío para facilitar la creación de objetos sin inicializar
    }


    //constructor de copia para prototype
    public Pokemon(Pokemon pokemon) {
        this.id = pokemon.id;
        this.nombre = pokemon.nombre;
        this.tipo = pokemon.tipo;
        this.defensa = pokemon.defensa;
        this.ataque = pokemon.ataque;
        this.vida = pokemon.vida;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getDefensa() {
        return defensa;
    }

    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }

    public int getAtaque() {
        return ataque;
    }

    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    @Override
    public Pokemon clonar() {
        return new Pokemon(this);
    }

    @Override
    public String toString() {
        return "Pokemon [id=" + id + ", nombre=" + nombre + ", tipo=" + tipo + ", defensa=" + defensa + ", ataque="
                + ataque + ", vida=" + vida + "]";
    }
    
}
