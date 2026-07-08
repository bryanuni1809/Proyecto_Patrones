
package com.mycompany.Model.entrenador;

import com.mycompany.Model.pokemon.Pokemon;
import com.mycompany.Patrones.composite.ItemMochila;
import com.mycompany.Patrones.composite.MochilaGrupo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Representa al entrenador Pokémon.
 *
 * Gestiona:
 * - Su equipo de hasta 6 Pokémon.
 * - Su mochila mediante el patrón COMPOSITE.
 *
 * PATRÓN COMPOSITE → Mochila:
 * La mochila es un MochilaGrupo raíz que puede contener ítems individuales
 * (Pocion, Pokeball) o subgrupos (MochilaGrupo "Pociones", "Pokeballs", etc.).
 * El entrenador trata ambos de forma uniforme a través de ItemMochila.
 *
 * SOLID → ISP: el Entrenador no necesita conocer si un ítem es individual
 *              o agrupado; solo llama a usar() y getCantidad().
 * SOLID → LSP: cualquier ItemMochila (hoja o compuesto) es intercambiable.
 */

public class Entrenador {
   private String nombre;
    private Pokemon[] equipo;

    /** Raíz del árbol Composite: la mochila completa del entrenador. */
    private MochilaGrupo mochila;

    public Entrenador(String nombre) {
        this.nombre = nombre;
        this.equipo = new Pokemon[3];
        this.mochila = new MochilaGrupo("Mochila de " + nombre);
    }

    // ── Getters y Setters ─────────────────────────────────────────────────

    public String getNombre() { return nombre; }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public MochilaGrupo getMochila() { return mochila; }


    public Pokemon[] getEquipo() {
        return equipo;
    }
    
    // ── Gestión del equipo ────────────────────────────────────────────────
    public void agregarPokemon(Pokemon pokemon) {

        for (Pokemon pokemon1 : equipo) {
            pokemon1 = pokemon;
        }
    }

    /**
     * Devuelve el primer Pokémon del equipo que no esté desmayado.
     * @return Pokémon activo, o null si todos están desmayados
     */
    public Pokemon getPokemonActivo() {
        for (Pokemon p : equipo) {
            if (!p.estaDesmayado()) {
                return p;
            }
        }
        return null;
    }

    /**
     * Indica si todos los Pokémon del equipo están desmayados.
     * @return 
     */
    public boolean equipoDerrotado() {
        return Arrays.stream(equipo).allMatch(Pokemon::estaDesmayado);
        
    }

    // ── Gestión de la mochila (Composite) ────────────────────────────────

    /**
     * Agrega un ítem (individual o grupo) directamente a la mochila raíz.
     *
     * Para organizar por categorías, crea un MochilaGrupo primero y
     * luego agrégalo con este método.
     *
     * Ejemplo:
     *   MochilaGrupo pociones = new MochilaGrupo("Pociones");
     *   pociones.agregar(new Pocion("Poción", 3, 20));
     *   entrenador.agregarItem(pociones);
     * @param item
     */
    public void agregarItem(ItemMochila item) {
        mochila.agregar(item);
    }

    /**
     * Usa un ítem específico de la mochila sobre un Pokémon objetivo.
     * Si el ítem no está en la mochila, informa al jugador.
     *
     * @param item     el ítem a usar (referencia al objeto)
     * @param objetivo Pokémon que recibirá el efecto
     */
    public void usarItem(ItemMochila item, Pokemon objetivo) {
        if (mochila.getItems().contains(item)) {
            item.usar(objetivo);
        } else {
            System.out.println(nombre + " no tiene ese ítem en su mochila.");
        }
    }

    /**
     * Muestra el contenido completo de la mochila en formato árbol.
     */
    public void mostrarMochila() {
        mochila.mostrar("");
    }

    /**
     * Devuelve el total de usos disponibles en toda la mochila.
     * @return 
     */
    public int totalItemsMochila() {
        return mochila.getCantidad();
    }
}
