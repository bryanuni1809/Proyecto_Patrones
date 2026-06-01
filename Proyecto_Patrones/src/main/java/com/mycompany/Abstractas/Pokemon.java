package com.mycompany.Abstractas;

public abstract class Pokemon {
    protected int id;
    protected String nombre;
    protected int vida;
    protected String tipo;
    protected int vidaMax;
    protected int ataque;
    protected int defensa;

    
    
    public Pokemon(int id, String nombre, int vida, String tipo, int vidaMax, int ataque, int defensa) {
        this.id = id;
        this.nombre = nombre;
        this.vida = vida;
        this.tipo = tipo;
        this.vidaMax = vidaMax;
        this.ataque = ataque;
        this.defensa = defensa;
    }



    public abstract void ataqueComun();

}
