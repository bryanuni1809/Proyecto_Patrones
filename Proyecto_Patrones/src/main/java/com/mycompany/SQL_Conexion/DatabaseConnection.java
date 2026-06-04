package com.mycompany.SQL_Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // 1. Datos de conexión (¡CAMBIA ESTOS VALORES POR LOS TUYOS!)
    private static final String URL = "jdbc:postgresql://localhost:5432/Pokemon_db";
    private static final String USER = "user_pokemon";
    private static final String PASSWORD = "T#9vQ!2mL@7xR$4kZ&8pN^5wC*1jY";

    // 2. Método para obtener la conexión
    public static Connection getConnection() throws SQLException {
        try {
            // Intenta establecer la conexión
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ ¡Conexión a PostgreSQL exitosa!");
            return connection;
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar a la base de datos:");
            e.printStackTrace();
            throw e;
        }
    }
    
}