package com.mycompany.pokemongame;

// Faltan estas importaciones
import com.mycompany.Model.pokedex.Pokedex;
import com.mycompany.Model.pokemon.Pokemon;
import com.mycompany.Model.pokemon.TipoPokemon;
import com.mycompany.Patrones.factory.PokemonFactory;

/**
 *
 * @author admin
 */
public class Main {

    public static void main(String[] args) {
        
        Pokemon pikachu = PokemonFactory.crearPokemon(
                1,
                25,
                "Pikachu",
                TipoPokemon.ELECTRICO,
                5,
                35,
                55,
                40,
                90
        );

        Pokedex pokedex = Pokedex.getInstancia();

        pokedex.agregarPokemon(pikachu);

        pokedex.getPokemones().forEach(System.out::println);
    

        // Llamamos a la función de PostgreSQL a través de nuestro método Java
        Pokedex pk=Pokedex.getInstancia();
        Pokemon pokemon= pk.buscarporNombre("pikachu");

        // Siempre verificamos que no sea null (por si el ID no existe)
        if (pokemon != null) {
            System.out.println("ID: " + pokemon.getNombre());
            System.out.println("Número de Pokédex: " + pokemon.getNumeroPokedex()); 
            System.out.println("Nombre: " + pokemon.getNombre());
            System.out.println("Tipo: " + pokemon.getTipo());
            System.out.println("Nivel: " + pokemon.getNivel());
            System.out.println("HP: " + pokemon.getHp());
            System.out.println("Ataque: " + pokemon.getAtaque());
            System.out.println("Defensa: " + pokemon.getDefensa());
            System.out.println("Velocidad: " + pokemon.getVelocidad());
        } else {
            System.out.println("No se encontro ningun Pokemon con ese ID.");
    
        }
    }
}
