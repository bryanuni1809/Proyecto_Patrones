
package com.mycompany.Combate.Atk;

import com.mycompany.Model.pokemon.Pokemon;

public interface CombateObservador {
    void onCambioTurno(int turnoGeneral, Pokemon pokemonActivo);
    void onAtaqueRealizado(Pokemon atacante, Pokemon defensor, String ataque, int damage);
    void onPokemonDebilitado(Pokemon pokemon);
    void onPokemonCambiado(Pokemon viejo, Pokemon nuevo, String motivo);
    void onCombateTerminado(String ganador);
}
