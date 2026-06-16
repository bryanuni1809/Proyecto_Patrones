package com.mycompany.pokemongame;

// Faltan estas importaciones
import com.pokemon.model.pokemon.Pokemon;
import com.pokemon.model.pokemon.TipoPokemon;
import com.mycompany.SQL_Conexion.PokemonDBQ;
import com.pokemon.factory.PokemonFactory;
import com.pokemon.model.pokedex.Pokedex;

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
        PokemonDBQ db = new PokemonDBQ();
        Pokemon pikachuPruebadb = db.obtenerPorId(4);

        // Siempre verificamos que no sea null (por si el ID no existe)
        if (pikachuPruebadb != null) {
            System.out.println("ID: " + pikachuPruebadb.getNombre());
            System.out.println("Número de Pokédex: " + pikachuPruebadb.getNumeroPokedex()); 
            System.out.println("Nombre: " + pikachuPruebadb.getNombre());
            System.out.println("Tipo: " + pikachuPruebadb.getTipo());
            System.out.println("Nivel: " + pikachuPruebadb.getNivel());
            System.out.println("HP: " + pikachuPruebadb.getHp());
            System.out.println("Ataque: " + pikachuPruebadb.getAtaque());
            System.out.println("Defensa: " + pikachuPruebadb.getDefensa());
            System.out.println("Velocidad: " + pikachuPruebadb.getVelocidad());
        } else {
            System.out.println("No se encontro ningun Pokemon con ese ID.");
        }
    }
}
