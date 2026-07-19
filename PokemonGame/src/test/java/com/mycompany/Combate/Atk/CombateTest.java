package com.mycompany.Combate.Atk; 

import com.mycompany.Model.entrenador.Entrenador;
import com.mycompany.Model.pokemon.Pokemon;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

/**
 * Clase de prueba para la lógica de Combate.
 * 
 * SENTIDO PRÁCTICO: Validar que el Patrón Observer funciona correctamente 
 * cuando un Pokémon se desmaya, sin necesidad de ejecutar una batalla completa 
 * ni crear objetos reales complejos.
 */
@ExtendWith(MockitoExtension.class) // Indica a JUnit 5 que debe inicializar las anotaciones @Mock de Mockito
public class CombateTest {

    // Creamos "dobles de prueba" (mocks). No son objetos reales, sino simulaciones 
    // que podemos controlar para aislar y probar únicamente la lógica de la clase Combate.
    
    @Mock
    private Entrenador entrenador1; // Simula al jugador cuyo Pokémon se va a debilitar

    @Mock
    private Entrenador entrenador2; // Simula al oponente (necesario para el constructor, aunque no se active en esta prueba)

    @Mock
    private Pokemon pokemonDebilitado; // Simula el Pokémon que llega a 0 HP

    @Mock
    private Pokemon pokemonNuevo; // Simula el Pokémon de reserva que saldrá al campo automáticamente

    @Test
    public void testVerificarEstadoPokemon_CuandoSeDesmaya_NotificaYCambia() {
        // ---------------------------------------------------------
        // 1. ARRANGE (Preparar): Configuramos el comportamiento esperado de nuestros mocks
        // ---------------------------------------------------------
        
        // Cuando el sistema pregunte si el Pokémon está desmayado, responderá que SÍ.
        when(pokemonDebilitado.estaDesmayado()).thenReturn(true);

        // Cuando el combate pregunte cuál es el Pokémon activo del entrenador 1, devolverá el que simulamos como debilitado.
        when(entrenador1.getPokemonActivo()).thenReturn(pokemonDebilitado);

        // Simulamos que el entrenador aún tiene otros Pokémon en su equipo (no ha perdido la partida completa).
        when(entrenador1.equipoDerrotado()).thenReturn(false);

        // Simulamos que, al pedir el siguiente Pokémon, el entrenador saca exitosamente al "pokemonNuevo".
        when(entrenador1.sacarSiguientePokemon()).thenReturn(pokemonNuevo);

        // Instanciamos el objeto REAL que queremos probar (Combate).
        // Pasamos 'null' en la lista de observadores para probar que el constructor la maneja de forma segura.
        Combate combate = new Combate(entrenador1, entrenador2, null);

        // Creamos un "espía" (mock) del observador. 
        // Objetivo: Vigilar si el combate le envía las notificaciones correctas en el momento justo.
        CombateObservador observador = mock(CombateObservador.class);
        combate.registrarObservador(observador);

        // ---------------------------------------------------------
        // 2. ACT (Actuar): Ejecutamos el método específico que queremos validar
        // ---------------------------------------------------------
        
        // Llamamos al método que revisa el estado. Internamente, debería detectar
        // el desmayo y disparar las notificaciones al observador registrado.
        combate.verificarEstadoPokemon(entrenador1);

        // ---------------------------------------------------------
        // 3. ASSERT (Verificar): Comprobamos que el Patrón Observer funcionó como se esperaba
        // ---------------------------------------------------------
        
        // Verificamos que el método 'onPokemonDebilitado' se llamó EXACTAMENTE 1 vez (times(1))
        // y que recibió como argumento al Pokémon que simulamos como desmayado.
        verify(observador, times(1)).onPokemonDebilitado(pokemonDebilitado);

        // Verificamos que el método 'onPokemonCambiado' se llamó EXACTAMENTE 1 vez,
        // indicando el cambio del Pokémon viejo al nuevo, con la razón "se desmayo".
        verify(observador, times(1)).onPokemonCambiado(pokemonDebilitado, pokemonNuevo, "se desmayo");
    }

    @Test
    public void testConstructor_ConListaNula_NoLanzaExcepcion() {
        // ---------------------------------------------------------
        // ARRANGE & ACT (Preparar y Actuar)
        // ---------------------------------------------------------
        
        // Probamos un "caso borde" (edge case): ¿Qué pasa si quien crea el combate 
        // olvida pasar una lista de observadores y envía 'null'?
        Combate combate = new Combate(entrenador1, entrenador2, null);

        // ---------------------------------------------------------
        // ASSERT (Verificar)
        // ---------------------------------------------------------
        
        // En JUnit, si una prueba no lanza una excepción no esperada, se considera exitosa (barra verde).
        // Al poder registrar un observador después de crear el combate con 'null',
        // demostramos que el constructor inicializó la lista internamente de forma segura,
        // evitando un temido NullPointerException en el futuro.
        combate.registrarObservador(mock(CombateObservador.class));
    }
}