package com.mycompany.Combate.Atk;

import com.mycompany.Model.entrenador.Entrenador;
import com.mycompany.Model.pokemon.Pokemon;

// Interfaz de callbacks internos para notificar eventos durante el combate
interface CombateCallback {
    
    // Notifica cuando se ejecuta un ataque entre dos Pokémon
    void notificarAtaque(Pokemon atacante, Pokemon defensor, String ataque, int damage);
    
    // Notifica cuando un Pokémon queda debilitado
    void notificarPokemonDebilitado(Pokemon p);
    
    // Notifica cuando se realiza un cambio de Pokémon en el campo
    void notificarCambio(Pokemon viejo, Pokemon nuevo, String motivo);
    
    // Verifica el estado de los Pokémon del entrenador después de un evento
    void verificarEstadoPokemon(Entrenador entrenador);
}