package com.mycompany.Combate.Atk;

import com.mycompany.Model.pokemon.Pokemon;

// Interfaz del patrón Observador para reaccionar a eventos del combate
public interface CombateObservador {
    
    // Notifica cuando cambia el turno y qué Pokémon está activo
    void onCambioTurno(int turnoGeneral, Pokemon pokemonActivo);
    
    // Notifica cuando se ejecuta un ataque, indicando daño y participantes
    void onAtaqueRealizado(Pokemon atacante, Pokemon defensor, String ataque, int damage);
    
    // Notifica cuando un Pokémon pierde toda su salud y queda fuera de combate
    void onPokemonDebilitado(Pokemon pokemon);
    
    // Notifica cuando un Pokémon es reemplazado por otro en el campo
    void onPokemonCambiado(Pokemon viejo, Pokemon nuevo, String motivo);
    
    // Notifica el final del combate e indica el nombre del ganador
    void onCombateTerminado(String ganador);
}