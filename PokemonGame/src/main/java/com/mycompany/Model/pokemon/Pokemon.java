package com.mycompany.Model.pokemon;
/**
 * Representa un Pokémon con sus datos y estadísticas principales.
 */

public class Pokemon {
    private int id;
    private int numeroPokedex;
    private String nombre;
    private TipoPokemon tipo;
    private int nivel;
    private int hp;
    private int ataque;
    private int defensa;
    private int velocidad;

    public Pokemon() {
    }

    public Pokemon(int id,
            int numeroPokedex,
            String nombre,
            TipoPokemon tipo,
            int nivel,
            int hp,
            int ataque,
            int defensa,
            int velocidad) {

        this.id = id;
        this.numeroPokedex = numeroPokedex;
        this.nombre = nombre;
        this.tipo = tipo;
        this.nivel = nivel;
        this.hp = hp;
        this.ataque = ataque;
        this.defensa = defensa;
        this.velocidad = velocidad;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public void setNumeroPokedex(int numeroPokedex) {this.numeroPokedex = numeroPokedex;}
    public int getNumeroPokedex() {return numeroPokedex;}
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public TipoPokemon getTipo() {return tipo;}
    public void setTipo(TipoPokemon tipo) {this.tipo = tipo;}
    public int getNivel() {return nivel;}
    public void setNivel(int nivel) {this.nivel = nivel;}
    public int getHp() {return hp;}
    public void setHp(int hp) {this.hp = hp;}
    public int getAtaque() {return ataque;}
    public void setAtaque(int ataque) {this.ataque = ataque;}
    public int getDefensa() {return defensa;}
    public void setDefensa(int defensa) {this.defensa = defensa;}
    public int getVelocidad() {return velocidad;}
    public void setVelocidad(int velocidad) {this.velocidad = velocidad;}

    @Override
    public String toString() {

        return "Pokemon{" +
                "id=" + id +
                ", numeroPokedex=" + numeroPokedex +
                ", nombre='" + nombre + '\'' +
                ", tipo=" + tipo +
                ", nivel=" + nivel +
                ", hp=" + hp +
                ", ataque=" + ataque +
                ", defensa=" + defensa +
                ", velocidad=" + velocidad +
                '}';
    }
}
