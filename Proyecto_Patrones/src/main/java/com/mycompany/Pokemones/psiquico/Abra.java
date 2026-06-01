package com.mycompany.Pokemones.psiquico;
import com.mycompany.Tipos.*;
import com.mycompany.Abstractas.Pokemon;


public class Abra extends Pokemon implements ATespecial_Psiquico{

    public Abra(int id, String nombre, int vida, String tipo, int vidaMax, int ataque, int defensa) {
        super(id, nombre, vida, tipo, vidaMax, ataque, defensa);
    }


    @Override
    public void ataqueComun() {
        System.out.println("golpe psicotico");
    }

    @Override
    public void confusion() {
        System.out.println("Ataque confusion");
    }
}
