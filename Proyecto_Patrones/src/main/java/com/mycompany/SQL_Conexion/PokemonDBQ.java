package com.mycompany.SQL_Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mycompany.Abstractas.Pokemon;

public class PokemonDBQ {
    //Metodo para obtener un pokemon por su ID utilizando la función almacenada en la base de datos
    public Pokemon obtenerPorId(int id) {
        String sql = "SELECT * FROM obtener_pokemon_por_id(?)";
        Pokemon pokemon = null;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // El signo de interrogación (?) se reemplaza por el valor del ID del pokemon
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    pokemon = new Pokemon();
                    pokemon.setId(rs.getInt("p_id"));
                    pokemon.setNombre(rs.getString("p_nombre"));
                    pokemon.setTipo(rs.getString("p_tipo"));
                    pokemon.setDefensa(rs.getInt("p_defensa"));
                    pokemon.setAtaque(rs.getInt("p_ataque"));
                    pokemon.setVida(rs.getInt("p_vida"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener el pokemon con ID " + id + ":");
            e.printStackTrace();
        }

        return pokemon;
    }
}
