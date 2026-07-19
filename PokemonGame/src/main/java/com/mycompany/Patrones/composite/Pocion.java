package com.mycompany.Patrones.composite;

import com.mycompany.Model.pokemon.Pokemon;

/**
 * Hoja del Composite: ítem individual de tipo Poción.
 *
 * Restaura HP a un Pokémon. Hay tres variantes según la cantidad curada:
 * Pocion (20), SuperPocion (50), HiperPocion (200).
 *
 * Patrón Composite → nodo hoja, no tiene hijos.
 */
public class Pocion implements ItemMochila {

    private final String nombre;
    private int cantidad;
    private final int hpCurado;

    /**
     * @param nombre    nombre del ítem ("Poción", "Super Poción", "Hiper Poción")
     * @param cantidad  cuántas unidades hay en la mochila
     * @param hpCurado  cuántos HP restaura cada uso
     */
    public Pocion(String nombre, int cantidad, int hpCurado) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.hpCurado = hpCurado;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public int getCantidad() {
        return cantidad;
    }

    @Override
    public void usar(Pokemon objetivo) {
        if (cantidad <= 0) {
            System.out.println("No quedan " + nombre + "s disponibles.");
            return;
        }
        if (objetivo.estaDesmayado()) {
            System.out.println(objetivo.getNombre() + " está desmayado, no se puede usar " + nombre + ".");
            return;
        }

        int hpAntes = objetivo.getHpActual();
        int hpNuevo = Math.min(objetivo.getHp(), hpAntes + hpCurado);
        objetivo.setHpActual(hpNuevo);
        cantidad--;

        System.out.println("Usaste " + nombre + " en " + objetivo.getNombre()
                + ". HP: " + hpAntes + " → " + hpNuevo
                + " (quedan " + cantidad + " " + nombre + "s)");
    }
}
