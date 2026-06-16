package com.mycompany.SQL_Conexion;

import com.pokemon.model.pokemon.Pokemon;
import com.pokemon.model.pokemon.TipoPokemon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PokemonDBQ {

    //Metodo para obtener un pokemon por su ID utilizando la función almacenada en la base de datos
    public Pokemon obtenerPorId(int id) {
        // se usa select * from y el nombre de la función almacenada, con un signo de interrogación (?) para indicar que se pasará un parámetro
        String sql = "SELECT * FROM obtener_pokemon_por_id(?)";
        //aca se inicializa un objeto Pokemon en null, que se llenará con los datos obtenidos de la base de datos
        com.pokemon.model.pokemon.Pokemon pokemon = null;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // El signo de interrogación (?) se reemplaza por el valor del ID del pokemon, el 1 busca el primer signo de interrogación en la consulta SQL y lo reemplaza con el valor de la variable id
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                // si la consulta devuelve un resultado, se crea un nuevo objeto Pokemon y se llenan sus atributos con los datos obtenidos de la base de datos
                // utiliza p_(nombre_columna) para acceder a los datos del ResultSet, que corresponden a los nombres de las columnas devueltas por la funcion almacenada en la base de datos
                if (rs.next()) {
                    pokemon = new Pokemon();
                    pokemon.setId(rs.getInt("p_id"));
                    pokemon.setNumeroPokedex(rs.getInt("p_numero_pokedex"));
                    pokemon.setNombre(rs.getString("p_nombre"));
                    pokemon.setTipo(TipoPokemon.valueOf(rs.getString("p_tipo").toUpperCase()));
                    pokemon.setNivel(rs.getInt("p_nivel"));
                    pokemon.setHp(rs.getInt("p_hp"));
                    pokemon.setAtaque(rs.getInt("p_ataque"));
                    pokemon.setDefensa(rs.getInt("p_defensa"));
                    pokemon.setVelocidad(rs.getInt("p_velocidad"));
                }
            }

            // si ocurre algun error durante la conexion a la base de datos o la ejecucion de la consulta, se captura la excepción SQLException y se imprime un mensaje de error
        } catch (SQLException e) {
            System.err.println("Error al obtener el pokemon con ID " + id + ":");
        }

        return pokemon;
    }
}
