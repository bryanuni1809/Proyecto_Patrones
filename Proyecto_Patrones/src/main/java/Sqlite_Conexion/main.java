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
public class main {
    public static void main(String[] args) {
         try {
            Connection conn = ConexionSQLite.conectar();

            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(
                "SELECT * FROM pokemones"
            );

            while(rs.next()) {
                System.out.println(
                    rs.getInt("numero") +
                    " - " +
                    rs.getString("nombre")
                );
            }

            conn.close();

        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
