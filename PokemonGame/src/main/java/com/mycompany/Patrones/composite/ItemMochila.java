package com.mycompany.Patrones.composite;

import com.mycompany.Model.pokemon.Pokemon;

/**
 * Interfaz del patrón Composite para la mochila del Entrenador.
 *
 * Permite tratar de forma uniforme tanto un ítem individual (Pocion, Pokeball)
 * como un grupo de ítems (MochilaGrupo), sin que el Entrenador conozca
 * la estructura interna.
 *
 * SOLID → LSP: ItemIndividual y MochilaGrupo son intercambiables.
 * SOLID → ISP: el Entrenador solo ve usar() y getNombre(), no la estructura.
 */
public interface ItemMochila {

    /**
     * Nombre del ítem o del grupo.
     */
    String getNombre();

    /**
     * Cantidad total de usos disponibles.
     * Para un ítem individual: su cantidad propia.
     * Para un grupo: la suma de todos sus hijos.
     */
    int getCantidad();

    /**
     * Usa el ítem sobre un Pokémon objetivo.
     * @param objetivo Pokémon al que se aplica el efecto
     */
    void usar(Pokemon objetivo);
}
