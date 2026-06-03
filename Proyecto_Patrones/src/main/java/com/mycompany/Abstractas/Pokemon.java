package com.mycompany.Abstractas;

public abstract class Pokemon {
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
    
    public abstract void ataqueComun();


}
