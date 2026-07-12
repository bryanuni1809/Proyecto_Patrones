package com.mycompany.SQL_Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase encargada de gestionar la conexión con la base de datos PostgreSQL.
 * CORREGIDO: Mejor manejo de errores y logs
 */
public class DatabaseConnection {
    
    // Datos de conexión
    private static final String URL = "jdbc:postgresql://localhost:5432/Pokemon_db";
    private static final String USER = "user_pokemon";
    private static final String PASSWORD = "T#9vQ!2mL@7xR$4kZ&8pN^5wC*1jY";
    
    /**
     * Método para obtener la conexión
     * CORREGIDO: Mejor logging de errores
     */
    public static Connection getConnection() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✓ Conexión exitosa a la base de datos");
            return connection;
        } catch (SQLException e) {
            System.err.println("✗ Error al conectar a la base de datos:");
            System.err.println("URL: " + URL);
            System.err.println("Usuario: " + USER);
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}