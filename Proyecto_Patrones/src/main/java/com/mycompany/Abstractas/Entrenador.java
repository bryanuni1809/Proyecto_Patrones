package com.mycompany.Abstractas;

public abstract class Entrenador implements Prototype<Entrenador> {
    protected String nombre;

    public Entrenador(String nombre) {
        this.nombre = nombre;
    }

    //constructor de copia para prototype
    public Entrenador(Entrenador entrenador) {
        this.nombre = entrenador.nombre;
    }

    @Override
    public abstract Entrenador clonar();

    
}