/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.proyecto_patrones;

import com.mycompany.Abstractas.Pokemon;
import com.mycompany.SQL_Conexion.PokemonDBQ;

/**
 *
 * @author admin
 */
public class Proyecto_Patrones {

    public static void main(String[] args) {
        // Llamamos a la función de PostgreSQL a través de nuestro método Java
        PokemonDBQ dao = new PokemonDBQ();
        Pokemon pikachu = dao.obtenerPorId(7);

        // Siempre verificamos que no sea null (por si el ID no existe)
        if (pikachu != null) {
            System.out.println("ID: " + pikachu.getId());
            System.out.println("Nombre: " + pikachu.getNombre());
            System.out.println("Tipo: " + pikachu.getTipo());
            System.out.println("Ataque: " + pikachu.getAtaque());
            System.out.println("Defensa: " + pikachu.getDefensa());
            System.out.println("Vida: " + pikachu.getVida());
        } else {
            System.out.println("No se encontró ningún Pokémon con ese ID.");
        }
    }
}
