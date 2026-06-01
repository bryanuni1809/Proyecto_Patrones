/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Sqlite_Conexion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author USER
 */
public class main2 {
    public static void main(String[] args) {
     try {

            Connection conn = ConexionSQLite.conectar();

            String sql = """
                SELECT p.numero,p.nombre,t.nombre AS tipo FROM pokemones p
                JOIN pokemon_tipos pt ON p.numero = pt.pokemon_numero
                JOIN tipos t ON t.id = pt.tipo_id
                ORDER BY RANDOM()
                LIMIT 1
            """;

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {

                System.out.println("Ha aparecido un Pokemon salvaje!1!!");
                System.out.println("Numero: " + rs.getInt("numero"));
                System.out.println("Nombre: " + rs.getString("nombre"));
                System.out.println("Tipo: " + rs.getString("tipo"));

            }

            conn.close();

        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
