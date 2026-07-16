package com.mycompany.gui;

import com.mycompany.Combate.Atk.Ataque;
import com.mycompany.Model.entrenador.SelectorAtaque;
import com.mycompany.Model.pokemon.Pokemon;

import java.util.List;
import java.util.Random;

/**
 * Implementación de SelectorAtaque para el entrenador rival (IA sencilla):
 * espera un momento (para dar ritmo a la batalla) y elige un ataque al azar.
 */
public class SelectorAtaqueIA implements SelectorAtaque {

    private final Random random = new Random();

    @Override
    public Ataque elegir(Pokemon activo, List<Ataque> ataques) {
        try {
            Thread.sleep(700 + random.nextInt(500));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return ataques.get(random.nextInt(ataques.size()));
    }
}
