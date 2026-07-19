package com.mycompany.Model.pokemon;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de prueba para la entidad Pokémon.
 * 
 * SENTIDO PRÁCTICO: 
 * 1. Validar el Patrón Prototype: asegurar que al clonar un Pokémon se crea 
 *    una nueva instancia en memoria, independiente del original.
 * 2. Validar la lógica de Estado (State): confirmar que el sistema detecta 
 *    correctamente cuando un Pokémon ha perdido toda su vida (HP).
 */
public class PokemonTest {

    @Test
    public void testClonar_PokemonClonadoEsIndependiente() {
        // ---------------------------------------------------------
        // 1. ARRANGE (Preparar): Usamos datos reales de la base de datos
        // ---------------------------------------------------------
        // Pikachu: Pokedex 25, Nivel 5, HP 35, Atq 55, Def 40, Vel 90
        // Nota: El primer parámetro '1' es el ID de la base de datos. 
        // Ajústalo si tu tabla usa otro número para este registro.
        Pokemon original = new Pokemon(1, 25, "Pikachu", TipoPokemon.ELECTRICO, 5, 35, 55, 40, 90);
        
        // Simulamos que el Pokémon original ha recibido daño en batalla y su HP bajó a 10.
        original.setHpActual(10); 

        // ---------------------------------------------------------
        // 2. ACT (Actuar): Aplicamos el Patrón Prototype
        // ---------------------------------------------------------
        // Invocamos el método de clonación. Según tu implementación, 
        // esto crea un nuevo objeto con los stats base, por lo que el HP vuelve a su máximo (35).
        Pokemon clon = original.clonar();

        // ---------------------------------------------------------
        // 3. ASSERT (Verificar): Comprobamos la independencia de la copia
        // ---------------------------------------------------------
        // 1. Deben ser objetos distintos en la memoria (no es la misma referencia).
        assertNotSame(original, clon, "El clon y el original deben ser objetos diferentes en memoria");
        
        // 2. El nombre y stats base se mantienen iguales.
        assertEquals("Pikachu", clon.getNombre(), "El clon debe conservar el nombre del original");
        
        // 3. Verificamos que el clon tiene su HP máximo (35) y no copió el daño (10) del original.
        assertEquals(35, clon.getHpActual(), "El clon debe iniciar con su HP máximo (35) según la implementación de clonar()");
        
        // 4. PRUEBA DEFINITIVA DE INDEPENDENCIA: 
        // Modificamos el original DESPUÉS de clonar. El clon NO debe verse afectado.
        original.setHpActual(5);
        assertEquals(35, clon.getHpActual(), "Cambiar el HP del original a 5 no debe afectar al clon, que sigue en 35");
    }

    @Test
    public void testEstaDesmayado_CuandoHpEsCero_ReturnsTrue() {
        // ---------------------------------------------------------
        // 1. ARRANGE (Preparar): Usamos datos reales de la base de datos
        // ---------------------------------------------------------
        // Oddish: Pokedex 43, Nivel 5, HP 45, Atq 50, Def 55, Vel 30
        // Nota: El primer parámetro '5' es el ID de la base de datos.
        Pokemon pokemon = new Pokemon(5, 43, "Oddish", TipoPokemon.PLANTA, 5, 45, 50, 55, 30);

        // ---------------------------------------------------------
        // 2. ACT (Actuar): Simulamos que recibe daño letal
        // ---------------------------------------------------------
        pokemon.setHpActual(0);

        // ---------------------------------------------------------
        // 3. ASSERT (Verificar): Validamos la lógica de estado de batalla
        // ---------------------------------------------------------
        // Verificamos que el método detecta correctamente el estado de desmayo.
        assertTrue(pokemon.estaDesmayado(), "Un Pokémon con HP 0 debe reportar que está desmayado");
        
        // Prueba adicional de robustez (Caso Borde): ¿Qué pasa con HP negativo?
        // En los videojuegos, un ataque fuerte puede bajar el HP por debajo de cero.
        pokemon.setHpActual(-5);
        assertTrue(pokemon.estaDesmayado(), "Un Pokémon con HP negativo también debe estar desmayado");
    }
}