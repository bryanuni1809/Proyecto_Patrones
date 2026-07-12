package com.mycompany.Model.entrenador;

import com.mycompany.Model.pokemon.Pokemon;
import com.mycompany.Patrones.composite.*;
import com.mycompany.Combate.Atk.Ataque;
import com.mycompany.Patrones.prototype.Prototype;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Representa al entrenador Pokémon.
 *
 * Gestiona: - Su equipo de hasta 6 Pokémon. - Su mochila mediante el patrón
 * COMPOSITE.
 *
 * PATRÓN COMPOSITE → Mochila: La mochila es un MochilaGrupo raíz que puede
 * contener ítems individuales (Pocion, Pokeball) o subgrupos (MochilaGrupo
 * "Pociones", "Pokeballs", etc.). El entrenador trata ambos de forma uniforme a
 * través de ItemMochila.
 *
 * SOLID → ISP: el Entrenador no necesita conocer si un ítem es individual o
 * agrupado; solo llama a usar() y getCantidad(). SOLID → LSP: cualquier
 * ItemMochila (hoja o compuesto) es intercambiable.
 */
public class Entrenador implements Prototype<Entrenador>{

    private String nombre;
    private Pokemon[] equipo; 
    private int cantidadPokemon = 0; //cuantos pokemones tiene actualmente
    private int indiceActivo = 0; //Indica cual pokemon esta peleando 

    /**
     * Raíz del árbol Composite: la mochila completa del entrenador.
     */
    private MochilaGrupo mochila;
    //Constructor 
    public Entrenador(String nombre) {
        this.nombre = nombre;
        this.equipo = new Pokemon[6];
        this.mochila = new MochilaGrupo("Mochila de " + nombre);
    }

    // ── Getters y Setters ─────────────────────────────────────────────────
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public MochilaGrupo getMochila() {return mochila;}
    public Pokemon[] getEquipo() {return equipo;}
    
    public Ataque elegirAtaque(Pokemon activo) {
        // Mostrar lista de ataques disponibles
        System.out.println("Elige un ataque para " + activo.getNombre() + ":");
        for (int i = 0; i < activo.getAtaques().size(); i++) {
            System.out.println((i + 1) + ". " + activo.getAtaques().get(i).getNombre());
        }

        // Leer opción del jugador (ejemplo con Scanner)
        Scanner sc = new Scanner(System.in);
        int opcion = sc.nextInt() - 1;

        // Devolver el ataque elegido
        return activo.getAtaques().get(opcion);
    }

    // ── Gestión del equipo ────────────────────────────────────────────────
    public void agregarPokemon(Pokemon pokemon) {
        if (cantidadPokemon < equipo.length) {
            equipo[cantidadPokemon] = pokemon;
            cantidadPokemon++;
        } else {
            System.out.println("¡El equipo de " + nombre + " ya está lleno (máx 6)!");
        }
    }

    /**
     * Devuelve el primer Pokémon del equipo que no esté desmayado.
     * busca automáticamente el siguiente disponible.
     */
    public Pokemon getPokemonActivo() { 
        if (indiceActivo < cantidadPokemon) {
            Pokemon actual = equipo[indiceActivo];
            // CORRECCIÓN: Si el actual está debilitado, busca automáticamente el relevo
            if (actual != null && actual.estaDesmayado()) {
                return sacarSiguientePokemon();
            }
            return actual;
        }
        return null;
    }
    //recorre todo los pokemones del equipo del entrenador hasta encontrar uno que no este desmayado
    public Pokemon sacarSiguientePokemon() {
        for (int i = 0; i < cantidadPokemon; i++) {
            if (equipo[i] != null && !equipo[i].estaDesmayado()) {
                this.indiceActivo = i;
                return equipo[i];
            }
        }
        return null;
    }
    
    /**
     * Indica si todos los Pokémon del equipo están desmayados.
     *
     * @return
     */
    public boolean equipoDerrotado() {
        // Filtramos nulos por si el entrenador tiene menos de 6 Pokémon en ese momento
        return Arrays.stream(equipo)
                     .filter(p -> p != null)
                     .allMatch(Pokemon::estaDesmayado);
    }   

    @Override
    public Entrenador clonar() {
        try {
            Entrenador copia = (Entrenador) super.clone();
            // Clonar arreglo de Pokemon
            Pokemon[] equipoCopia = new Pokemon[this.equipo.length];
            for (int i = 0; i < this.equipo.length; i++) {
                equipoCopia[i] = (this.equipo[i] != null) ? this.equipo[i].clonar(): null;
            }
            copia.equipo = equipoCopia;
            // Si mochila u otros campos mutables existen, clónalos también o crea nuevas instancias
            // copia.mochila = this.mochila.clone(); // si aplica
            return copia;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
    
    // ── Gestión de la mochila (Composite) ────────────────────────────────
    /**
     * Agrega un ítem (individual o grupo) directamente a la mochila raíz.
     *
     * Para organizar por categorías, crea un MochilaGrupo primero y luego
     * agrégalo con este método.
     *
     * Ejemplo: MochilaGrupo pociones = new MochilaGrupo("Pociones");
     * pociones.agregar(new Pocion("Poción", 3, 20));
     * entrenador.agregarItem(pociones);
     *
     * @param item
     */
    public void agregarItem(ItemMochila item) { //agrega un objeto a la mochila 
        mochila.agregar(item);
    }

    /**
     * Usa un ítem específico de la mochila sobre un Pokémon objetivo. Si el
     * ítem no está en la mochila, informa al jugador.
     *
     * @param item el ítem a usar (referencia al objeto)
     * @param objetivo Pokémon que recibirá el efecto
     */
    public void usarItem(ItemMochila item, Pokemon objetivo) { //Sirve para usar un objeto sobre un Pokémon.
        // Interroga a la raíz de la mochila, la cual buscará en todas sus subcarpetas
        if (mochila.tieneItem(item)) {
            item.usar(objetivo); // Activa el efecto (ej. Curar HP)
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
     *
     * @return
     */
    public int totalItemsMochila() {
        return mochila.getCantidad();
    }
}
