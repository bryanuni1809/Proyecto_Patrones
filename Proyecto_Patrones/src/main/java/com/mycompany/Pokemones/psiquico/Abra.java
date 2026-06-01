package Pokemones.psiquico;
import Tipos.*;
import Abstractas.Pokemon;


public class Abra extends Pokemon implements ATespecial_Psiquico{

    public Abra(String nombre, int defensa, int ataque, int vida) {
        super("Abra","Psiquico", 40, 50, 100);
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
