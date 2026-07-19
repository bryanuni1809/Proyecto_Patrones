package com.mycompany.Model.entrenador;

import com.mycompany.Model.pokemon.Pokemon;
import com.mycompany.Model.pokemon.TipoPokemon; // Asegúrate de que este Enum exista y tenga AGUA, ELECTRICO, etc.
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de prueba para la gestión del equipo de un Entrenador.
 * 
 * SENTIDO PRÁCTICO: Validar el comportamiento del Patrón Composite (el equipo como colección)
 * y la gestión de estado, asegurando que el sistema responde correctamente 
 * cuando los Pokémon pierden toda su vida (HP).
 */
public class EntrenadorTest {

    @Test
    public void testEquipoDerrotado_CuandoTodosDesmayados_ReturnsTrue() {
        // ---------------------------------------------------------
        // 1. ARRANGE (Preparar): Usamos datos reales de tu script SQL
        // ---------------------------------------------------------
        Entrenador entrenador = new Entrenador("Andrea");
        
        // Squirtle: Pokedex 7, Nivel 5, HP 44, Atq 48, Def 65, Vel 43
        // Nota: Si tu constructor de Pokemon tiene un 'id' adicional al inicio, agrégalo como primer parámetro.
        Pokemon squirtle = new Pokemon(1,7, "Squirtle", TipoPokemon.AGUA, 5, 44, 48, 65, 43);
        
        // Pikachu: Pokedex 25, Nivel 5, HP 35, Atq 55, Def 40, Vel 90
        Pokemon pikachu = new Pokemon(4, 25, "Pikachu", TipoPokemon.ELECTRICO, 5, 35, 55, 40, 90);

        // Simulamos que ambos Pokémon han sido derrotados en batalla
        squirtle.setHpActual(0);
        pikachu.setHpActual(0);

        // Agregamos los Pokémon al equipo del entrenador (Patrón Composite)
        entrenador.agregarPokemon(squirtle);
        entrenador.agregarPokemon(pikachu);

        // ---------------------------------------------------------
        // 2. ACT (Actuar): Ejecutamos la lógica a probar
        // ---------------------------------------------------------
        boolean estaDerrotado = entrenador.equipoDerrotado();

        // ---------------------------------------------------------
        // 3. ASSERT (Verificar): Comprobamos el resultado esperado
        // ---------------------------------------------------------
        assertTrue(estaDerrotado, "Si todos los Pokémon del equipo tienen HP 0, el método debe retornar true");
    }

    @Test
    public void testSacarSiguientePokemon_SaltaDesmayados() {
        // ---------------------------------------------------------
        // 1. ARRANGE (Preparar)
        // ---------------------------------------------------------
        Entrenador entrenador = new Entrenador("Andrea");
        
        // Rattata: Pokedex 19, Nivel 5, HP 30, Atq 56, Def 35, Vel 72
        Pokemon rattata = new Pokemon(3,19, "Rattata", TipoPokemon.NORMAL, 5, 30, 56, 35, 72);
        
        // Growlithe: Pokedex 58, Nivel 5, HP 55, Atq 70, Def 45, Vel 60
        Pokemon growlithe = new Pokemon(7, 58, "Growlithe", TipoPokemon.FUEGO, 5, 55, 70, 45, 60);

        // Simulamos que el primer Pokémon (Rattata) ya se desmayó
        rattata.setHpActual(0);
        
        // Agregamos al equipo en orden: primero el desmayado, luego el sano
        entrenador.agregarPokemon(rattata);
        entrenador.agregarPokemon(growlithe);

        // ---------------------------------------------------------
        // 2. ACT (Actuar)
        // ---------------------------------------------------------
        // El sistema debería ignorar a Rattata (HP 0) y devolver a Growlithe
        Pokemon pokemonActivo = entrenador.sacarSiguientePokemon();

        // ---------------------------------------------------------
        // 3. ASSERT (Verificar)
        // ---------------------------------------------------------
        assertNotNull(pokemonActivo, "Debería devolver un Pokémon válido");
        assertEquals("Growlithe", pokemonActivo.getNombre(), 
            "El sistema debe saltar al primer Pokémon desmayado y devolver el siguiente disponible");
    }
}