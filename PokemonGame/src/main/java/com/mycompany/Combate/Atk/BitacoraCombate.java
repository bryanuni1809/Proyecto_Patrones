
package com.mycompany.Combate.Atk;

import com.mycompany.Model.pokemon.Pokemon;

public class BitacoraCombate implements CombateObservador {
    @Override
    public void onCambioTurno(int turnoGeneral, Pokemon pokemonActivo) {
        System.out.println("\n[BITACORA] Accion en Turno General #" + turnoGeneral + 
                           " -> Es el turno de: " + pokemonActivo.getNombre() + 
                           " (Estado actual: " + pokemonActivo.getEstado().getNombre() + ")");
    }

    @Override
    public void onAtaqueRealizado(Pokemon atacante, Pokemon defensor, String ataque, int danio) {
        System.out.println("[BITACORA] " + atacante.getNombre() + " ejercuto " + ataque + 
                           " contra " + defensor.getNombre() + ".");
        System.out.println("[BITACORA] Danio total provocado: " + danio + " HP.");
        System.out.println("[BITACORA] HP restante de " + defensor.getNombre() + ": " + defensor.getHpActual() + "/" + defensor.getHp());
    }

    @Override
    public void onPokemonDebilitado(Pokemon pokemon) {
        System.out.println("[BITACORA] ATENCION: " + pokemon.getNombre() + " se ha quedado sin fuerzas y se desmayo.");
    }

    @Override
    public void onPokemonCambiado(Pokemon viejo, Pokemon nuevo, String motivo) {
        System.out.println("[BITACORA] RELEVO FORZADO: " + viejo.getNombre() + " " + motivo + ".");
        if (nuevo != null) {
            System.out.println("[BITACORA] ¡" + nuevo.getNombre() + " entra al campo de batalla!");
        } else {
            System.out.println("[BITACORA] No quedan mas Pokemon disponibles en la reserva.");
        }
    }
    @Override
    public void onCombateTerminado(String ganador) {
        System.out.println("\n=======================================================");
        System.out.println("EL COMBATE HA TERMINADO");
        System.out.println("Victoria absoluta para el entrenador: " + ganador);
        System.out.println("=======================================================");
    }

}
