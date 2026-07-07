package com.mycompany.SQL_Conexion;

import com.mycompany.Model.pokemon.Pokemon;
import com.mycompany.Model.pokemon.TipoPokemon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * Clase encargada de las consultas a la base de datos relacionadas con los Pokemon.
 *
 * Sigue el principio de responsabilidad única: se ocupa únicamente de traducir
 * entre los objetos del dominio (Pokemon) y las tablas de la base de datos,
 * aislando así la lógica de negocio del acceso a datos.
 *
 * Internamente utiliza el patrón Builder para construir los objetos Pokemon
 * a partir de las filas del ResultSet, evitando el uso de constructores con
 * muchos parámetros o de múltiples setters.
 */
public class PokemonDBQ {
    /**
     * Extrae los datos de la fila actual de un ResultSet y construye un objeto Pokemon
     * completamente instanciado con los datos obtenidos de la base de datos.
     *
     * Antes se creaba un Pokemon vacío y se llenaba con 9 setters.
     * Ahora se usa el patrón Builder para armar el objeto de forma fluida y legible.
     */

    private Pokemon encontrarPokemon(ResultSet rs) throws SQLException {
        /**
         * No se usara el metodo conEstadoInicial porque un pokemon se crea siempre
         * en estado normal, RECORDATORIO POR SI NUNCA LO USAMOS PARA BORRARLO!!!!
        */
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

    public List<Pokemon> obtenerListaPokemon() {
        // consulta SQL que llama a la funcion
        String sql = "SELECT * FROM obtener_todos_pokemon()";
        // Se inicializa una lista vacía que se irá llenando con los objetos Pokemon obtenidos de la BD
        List<Pokemon> lista = new ArrayList<>();

        /**
         * try-with-resources: abre la conexión, el PreparedStatement y el ResultSet,
         * y los cierra automáticamente al finalizar, evitando fugas de memoria
         */
        try (Connection conn = DatabaseConnection.getConnection(); // establece la conexion
                PreparedStatement pstmt = conn.prepareStatement(sql); //obtiene la consulta
                ResultSet rs = pstmt.executeQuery()) { //ejecuta la consulta y retorna lo obtenido (linea por linea)
            /**
             * while (en vez de if) porque se espera recorrer TODAS las filas devueltas por la consulta,
             * no solo una como en el método obtenerPorId (rs.next(): pasa a la siguiente linea)
             */
            while (rs.next()) {
                /**
                 * Se convierte cada fila del ResultSet en un objeto Pokemon usando el método auxiliar
                 * y se agrega directamente a la lista
                 */
                lista.add(encontrarPokemon(rs));
            }

            /**
             * Si ocurre algún error durante la conexión o la ejecución de la consulta,
             * se captura la excepción SQLException y se imprime un mensaje de error
             */
        } catch (SQLException e) {
            System.err.println("Error al obtener la lista de Pokémon:");
        }
        return lista;
    }

}
