package com.mycompany.Abstractas;

public abstract class Pokemon {
    protected int id;
    protected String nombre;
<<<<<<< HEAD
    protected int vida;
    protected String tipo;
    protected int vidaMax;
    protected int ataque;
    protected int defensa;
=======
    protected String tipo;
    protected int defensa;
    protected int ataque; //hola
    protected int vida; 
>>>>>>> 41a43f1d89e581335e0db3dbb03b2aa1e2d585ca

    
    
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
