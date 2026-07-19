package com.mycompany.Model.entrenador;

import com.mycompany.Combate.Atk.Ataque;
import com.mycompany.Model.pokemon.Pokemon;
import java.util.List;

/**
 * Patrón Strategy: define CÓMO se elige el ataque de un Pokémon en su turno,
 * sin que Entrenador (ni Combate) necesiten saber si la elección viene de
 * teclado/consola, de una IA, o de un clic en la interfaz Swing.
 *
 * Se agrega esta interfaz para poder conectar la GUI sin tocar Combate.java
 * ni AtaqueComand.java: Entrenador.elegirAtaque() simplemente delega en la
 * estrategia configurada (ver Entrenador.setSelectorAtaque()).
 */
public interface SelectorAtaque {
    Ataque elegir(Pokemon activo, List<Ataque> ataques);
}
