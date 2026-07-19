package com.mycompany.SQL_Conexion;

import com.mycompany.Model.pokemon.Pokemon;
import com.mycompany.Model.pokemon.TipoPokemon;
import com.pokemon.builder.PokemonBuilder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de las consultas a la base de datos relacionadas con los Pokemon.
 * Sigue el principio de responsabilidad única: se ocupa únicamente de traducir
 * entre los objetos del dominio (Pokemon) y las tablas de la base de datos,
 * aislando así la lógica de negocio del acceso a datos.
 * Internamente utiliza el patrón Builder para construir los objetos Pokemon
 * a partir de las filas del ResultSet, evitando el uso de constructores con
 * muchos parámetros o de múltiples setters.
 */
public class PokemonDBQ {
    
    /**
     * Extrae los datos de la fila actual de un ResultSet y construye un objeto Pokemon
     * completamente instanciado con los datos obtenidos de la base de datos.
     * CORREGIDO: Se eliminaron los espacios en los nombres de columnas
     */
    private Pokemon encontrarPokemon(ResultSet rs) throws SQLException {
        return new PokemonBuilder()
            .conId(rs.getInt("p_id"))
            .conNumeroPokedex(rs.getInt("p_numero_pokedex"))
            .conNombre(rs.getString("p_nombre"))
            .conTipo(TipoPokemon.valueOf(rs.getString("p_tipo").toUpperCase()))
            .conNivel(rs.getInt("p_nivel"))
            .conHp(rs.getInt("p_hp"))
            .conAtaque(rs.getInt("p_ataque"))
            .conDefensa(rs.getInt("p_defensa"))
            .conVelocidad(rs.getInt("p_velocidad"))
            .build();
    }
    
    /**
     * Obtiene la lista completa de Pokemon desde la base de datos
     * CORREGIDO: Mejor manejo de errores con stack trace
     */
    public List<Pokemon> obtenerListaPokemon() {
        String sql = "SELECT * FROM obtener_todos_pokemon()";
        List<Pokemon> lista = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                lista.add(encontrarPokemon(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener la lista de Pokémon: " + e.getMessage());
            e.printStackTrace(); // IMPORTANTE: Esto ayuda a diagnosticar problemas
        }
        
        return lista;
    }
}