package com.mycompany.Combate.Atk;

import com.mycompany.Model.entrenador.Entrenador;
import com.mycompany.Model.pokemon.Pokemon;

interface CombateCallback {
    void notificarAtaque(Pokemon atacante, Pokemon defensor, String ataque, int damage);
    void notificarPokemonDebilitado(Pokemon p);
    void notificarCambio(Pokemon viejo, Pokemon nuevo, String motivo);
    void verificarEstadoPokemon(Entrenador entrenador);
}