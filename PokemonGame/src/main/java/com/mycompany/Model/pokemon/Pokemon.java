package com.mycompany.Model.pokemon;

import com.mycompany.Patrones.prototype.Prototype;
import com.mycompany.Patrones.state.EstadoNormal;
import com.mycompany.Patrones.state.EstadoPokemon;

/**
 * Representa un Pokémon con sus atributos y estado actual en batalla.
 *
 * Implementa dos patrones adicionales:
 *
 * → PROTOTYPE: permite clonar un Pokémon existente para crear copias
 *   con los mismos stats base (útil en combates o cuando la Pokédex
 *   entrega instancias a los entrenadores).
 *
 * → STATE: el comportamiento en batalla varía según el estado alterado
 *   (Normal, Paralizado, Dormido, Quemado...). El Pokémon delega la
 *   lógica de turno al objeto EstadoPokemon actual, sin condiciones if/else.
 *
 * SOLID → OCP: agregar un nuevo estado no modifica esta clase.
 * SOLID → LSP: clonar() devuelve un Pokemon equivalente e intercambiable.
 */
public class Pokemon implements Prototype<Pokemon> {

    // ── Atributos base ────────────────────────────────────────────────────
    private int id;
    private int numeroPokedex;
    private String nombre;
    private TipoPokemon tipo;
    private int nivel;
    private int hp;       // HP máximo (stat base)
    private int ataque;
    private int defensa;
    private int velocidad;

    // ── Atributos de batalla ──────────────────────────────────────────────
    private int hpActual; // HP actual durante el combate

    /** Patrón State: estado alterado actual del Pokémon. Por defecto: Normal. */
    private EstadoPokemon estado;

    // ── Constructores ─────────────────────────────────────────────────────

    public Pokemon() {
        this.estado = new EstadoNormal();
    }

    public Pokemon(int id,
                   int numeroPokedex,
                   String nombre,
                   TipoPokemon tipo,
                   int nivel,
                   int hp,
                   int ataque,
                   int defensa,
                   int velocidad) {

        this.id = id;
        this.numeroPokedex = numeroPokedex;
        this.nombre = nombre;
        this.tipo = tipo;
        this.nivel = nivel;
        this.hp = hp;
        this.hpActual = hp; // Al crearse, el HP actual = HP máximo
        this.ataque = ataque;
        this.defensa = defensa;
        this.velocidad = velocidad;
        this.estado = new EstadoNormal(); // Estado inicial: sin alteraciones
    }

    // ── Patrón Prototype ──────────────────────────────────────────────────

    /**
     * Clona este Pokémon creando una nueva instancia con los mismos stats.
     * El clon tiene su propio HP actual y estado (Normal), de forma que
     * modificar el clon no afecta al original.
     *
     * Uso típico: la Pokédex almacena prototipos y entrega clones a los
     * entrenadores, evitando que compartan el mismo objeto.
     */
    @Override
    public Pokemon clonar() {
        Pokemon clon = new Pokemon(
                this.id,
                this.numeroPokedex,
                this.nombre,
                this.tipo,
                this.nivel,
                this.hp,
                this.ataque,
                this.defensa,
                this.velocidad
        );
        // El clon empieza con HP completo y sin estado alterado
        clon.hpActual = this.hp;
        clon.estado = new EstadoNormal();
        return clon;
    }

    // ── Patrón State: delegación al estado actual ─────────────────────────

    /**
     * Lógica de inicio de turno: delega al estado actual.
     * @return true si el Pokémon puede atacar este turno
     */
    public boolean iniciarTurno() {
        return estado.alIniciarTurno(this);
    }

    /**
     * Lógica de final de turno: delega al estado actual.
     * Aplica efectos como daño por quemadura.
     */
    public void finalTurno() {
        estado.alFinalTurno(this);
    }

    /**
     * Cambia el estado alterado del Pokémon en tiempo de ejecución.
     * (Usado por Combate cuando aplica un efecto de estado.)
     */
    public void setEstado(EstadoPokemon nuevoEstado) {
        System.out.println(nombre + " ahora está: " + nuevoEstado.getNombre());
        this.estado = nuevoEstado;
    }

    public EstadoPokemon getEstado() {
        return estado;
    }

    // ── Comprobaciones de batalla ─────────────────────────────────────────

    /**
     * Indica si el Pokémon está debilitado (HP actual en 0).
     */
    public boolean estaDesmayado() {
        return hpActual <= 0;
    }

    // ── Getters y Setters ─────────────────────────────────────────────────

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getNumeroPokedex() { return numeroPokedex; }
    public void setNumeroPokedex(int numeroPokedex) { this.numeroPokedex = numeroPokedex; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public TipoPokemon getTipo() { return tipo; }
    public void setTipo(TipoPokemon tipo) { this.tipo = tipo; }

    public int getNivel() { return nivel; }
    public void setNivel(int nivel) { this.nivel = nivel; }

    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = hp; }

    public int getHpActual() { return hpActual; }
    public void setHpActual(int hpActual) { this.hpActual = hpActual; }

    public int getAtaque() { return ataque; }
    public void setAtaque(int ataque) { this.ataque = ataque; }

    public int getDefensa() { return defensa; }
    public void setDefensa(int defensa) { this.defensa = defensa; }

    public int getVelocidad() { return velocidad; }
    public void setVelocidad(int velocidad) { this.velocidad = velocidad; }

    @Override
    public String toString() {
        return "Pokemon{" +
                "id=" + id +
                ", numeroPokedex=" + numeroPokedex +
                ", nombre='" + nombre + '\'' +
                ", tipo=" + tipo +
                ", nivel=" + nivel +
                ", hp=" + hp +
                ", hpActual=" + hpActual +
                ", ataque=" + ataque +
                ", defensa=" + defensa +
                ", velocidad=" + velocidad +
                ", estado=" + estado.getNombre() +
                '}';
    }
}
