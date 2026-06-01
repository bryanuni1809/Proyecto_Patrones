/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Sqlite_Conexion;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author USER
 */
public class ConexionSQLite {
    public static Connection conectar() {
        try {
            Connection conn =
                DriverManager.getConnection("jdbc:sqlite:Pokemon_DB.db");

            System.out.println("Conectado correctamente");
            return conn;

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}
