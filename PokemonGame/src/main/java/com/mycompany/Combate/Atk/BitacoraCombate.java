package com.mycompany.Combate.Atk;

import com.mycompany.Model.pokemon.Pokemon;

// Implementa el patrón Observador para registrar un historial detallado de la batalla en consola
public class BitacoraCombate implements CombateObservador {
    
    // Registra el cambio de turno, indicando el número de turno y el estado del Pokémon activo
    @Override
    public void onCambioTurno(int turnoGeneral, Pokemon pokemonActivo) {
        System.out.println("\n[BITACORA] Accion en Turno General #" + turnoGeneral + 
                           " -> Es el turno de: " + pokemonActivo.getNombre() + 
                           " (Estado actual: " + pokemonActivo.getEstado().getNombre() + ")");
    }

    // Muestra los detalles del ataque, incluyendo el daño infligido y la vida restante del defensor
    @Override
    public void onAtaqueRealizado(Pokemon atacante, Pokemon defensor, String ataque, int danio) {
        System.out.println("[BITACORA] " + atacante.getNombre() + " ejercuto " + ataque + 
                           " contra " + defensor.getNombre() + ".");
        System.out.println("[BITACORA] Danio total provocado: " + danio + " HP.");
        System.out.println("[BITACORA] HP restante de " + defensor.getNombre() + ": " + defensor.getHpActual() + "/" + defensor.getHp());
    }

    // Alerta cuando un Pokémon pierde toda su vida y queda fuera de combate
    @Override
    public void onPokemonDebilitado(Pokemon pokemon) {
        System.out.println("[BITACORA] ATENCION: " + pokemon.getNombre() + " se ha quedado sin fuerzas y se desmayo.");
    }

    // Informa sobre el relevo de un Pokémon, indicando el motivo y el nuevo participante si existe
    @Override
    public void onPokemonCambiado(Pokemon viejo, Pokemon nuevo, String motivo) {
        System.out.println("[BITACORA] RELEVO FORZADO: " + viejo.getNombre() + " " + motivo + ".");
        if (nuevo != null) {
            System.out.println("[BITACORA] ¡" + nuevo.getNombre() + " entra al campo de batalla!");
        } else {
            System.out.println("[BITACORA] No quedan mas Pokemon disponibles en la reserva.");
        }
    }
    
    // Imprime un mensaje final destacado anunciando al ganador del encuentro
    @Override
    public void onCombateTerminado(String ganador) {
        System.out.println("\n=======================================================");
        System.out.println("EL COMBATE HA TERMINADO");
        System.out.println("Victoria absoluta para el entrenador: " + ganador);
        System.out.println("=======================================================");
    }
}