package com.mycompany.Patrones.state;

import com.mycompany.Model.pokemon.Pokemon;

/**
 * Estado Quemado: el Pokémon puede atacar normalmente, pero pierde 1/16 de su
 * HP máximo al final de cada turno.
 *
 * Patrón State → el daño por quemadura se aplica automáticamente sin que
 * Pokemon ni Combate contengan esta lógica.
 */
public class EstadoQuemado implements EstadoPokemon {

    @Override
    public String getNombre() {
        return "Quemado";
    }

    @Override
    public boolean alIniciarTurno(Pokemon pokemon) {
        // La quemadura no impide atacar
        return true;
    }

    @Override
    public void alFinalTurno(Pokemon pokemon) {
        // Quita 1/16 del HP máximo (mínimo 1 de daño)
        int danio = Math.max(1, pokemon.getHp() / 16);
        int hpActual = pokemon.getHpActual() - danio;
        pokemon.setHpActual(Math.max(0, hpActual));
        System.out.println(pokemon.getNombre() + " sufre " + danio
                + " puntos de daño por quemadura! HP restante: " + pokemon.getHpActual());
    }

    @Override
    public EstadoPokemon clonar() {
        return new EstadoQuemado();
    }
}
