package com.mycompany.Patrones.state;

import com.mycompany.Model.pokemon.Pokemon;

/**
 * Interfaz del patrón State para estados alterados de un Pokémon.
 *
 * Define las acciones que un estado puede modificar. Cada estado concreto
 * (Normal, Paralizado, Dormido, etc.) implementa esta interfaz con su
 * comportamiento específico.
 *
 * SOLID → OCP: agregar un nuevo estado (ej. Congelado) = nueva clase,
 * sin modificar Pokemon ni los estados existentes.
 * SOLID → LSP: cualquier EstadoPokemon es intercambiable en Pokemon.
 */
public interface EstadoPokemon {

    /**
     * Nombre descriptivo del estado, para mostrar en consola o UI.
     */
    String getNombre();

    /**
     * Lógica que se ejecuta al inicio del turno del Pokémon.
     * Puede impedir atacar (parálisis, sueño) o aplicar daño (quemadura).
     *
     * @param pokemon el Pokémon que tiene este estado
     * @return true si el Pokémon PUEDE atacar este turno, false si no
     */
    boolean alIniciarTurno(Pokemon pokemon);

    /**
     * Lógica que se ejecuta al final del turno.
     * Puede reducir duración del estado o aplicar efectos adicionales.
     *
     * @param pokemon el Pokémon que tiene este estado
     */
    void alFinalTurno(Pokemon pokemon);
}
