package com.mycompany.SQL_Conexion;

import com.mycompany.Model.pokemon.Pokemon;
import com.mycompany.Model.pokemon.TipoPokemon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PokemonDBQ {

    private Pokemon encontrarPokemon(ResultSet rs) throws SQLException {
        Pokemon pokemon = new Pokemon();
        pokemon.setId(rs.getInt("p_id"));
        pokemon.setNumeroPokedex(rs.getInt("p_numero_pokedex"));
        pokemon.setNombre(rs.getString("p_nombre"));
        pokemon.setTipo(TipoPokemon.valueOf(rs.getString("p_tipo").toUpperCase()));
        pokemon.setNivel(rs.getInt("p_nivel"));
        pokemon.setHp(rs.getInt("p_hp"));
        pokemon.setAtaque(rs.getInt("p_ataque"));
        pokemon.setDefensa(rs.getInt("p_defensa"));
        pokemon.setVelocidad(rs.getInt("p_velocidad"));
        return pokemon;
    }

    public List<Pokemon> obtenerListaPokemon() {
        // consulta SQL que llama a la funcion
        String sql = "SELECT * FROM obtener_todos_pokemon()";
        // Se inicializa una lista vacía que se irá llenando con los objetos Pokemon obtenidos de la BD
        List<Pokemon> lista = new ArrayList<>();

        // try-with-resources: abre la conexión, el PreparedStatement y el ResultSet, 
        // y los cierra automáticamente al finalizar, evitando fugas de memoria
        try (Connection conn = DatabaseConnection.getConnection(); 
                PreparedStatement pstmt = conn.prepareStatement(sql); 
                ResultSet rs = pstmt.executeQuery()) {
            // while (en vez de if) porque se espera recorrer TODAS las filas devueltas por la consulta,
            // no solo una como en el método obtenerPorId
            while (rs.next()) {
                // Se convierte cada fila del ResultSet en un objeto Pokemon usando el método auxiliar
                // y se agrega directamente a la lista
                lista.add(encontrarPokemon(rs));
            }

            // Si ocurre algún error durante la conexión o la ejecución de la consulta, 
            // se captura la excepción SQLException y se imprime un mensaje de error
        } catch (SQLException e) {
            System.err.println("Error al obtener la lista de Pokémon:");
        }

        return lista;
    }

}
