package com.mycompany.Patrones.composite;

import com.mycompany.Model.pokemon.Pokemon;
import java.util.ArrayList;
import java.util.List;

/**
 * Nodo compuesto del Composite: agrupa varios ItemMochila bajo un nombre.
 *
 * Permite organizar la mochila por categorías:
 *   "Pociones" → [Pocion x3, SuperPocion x1]
 *   "Mochila completa" → [MochilaGrupo "Pociones", MochilaGrupo "Pokeballs"]
 *
 * Llamar a usar() en el grupo usa el PRIMER ítem disponible con usos restantes.
 * Llamar a getCantidad() devuelve la suma total de todos sus hijos.
 *
 * SOLID → LSP: MochilaGrupo es intercambiable con cualquier ItemMochila.
 * SOLID → OCP: se pueden agregar nuevos tipos de ítems sin modificar esta clase.
 */
public class MochilaGrupo implements ItemMochila {

    private final String nombre;
    private final List<ItemMochila> items = new ArrayList<>();

    public MochilaGrupo(String nombre) {
        this.nombre = nombre;
    }

    /** Agrega un ítem (hoja o grupo) a esta colección. */
    public void agregar(ItemMochila item) {
        items.add(item);
    }

    /** Elimina un ítem de la colección. */
    public void eliminar(ItemMochila item) {
        items.remove(item);
    }

    /** Vista de los ítems contenidos (solo lectura). */
    public List<ItemMochila> getItems() {
        return List.copyOf(items);
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    /** Suma la cantidad total de todos los ítems del grupo (recursivo). */
    @Override
    public int getCantidad() {
        return items.stream().mapToInt(ItemMochila::getCantidad).sum();
    }

    /**
     * Usa el primer ítem del grupo que tenga cantidad > 0.
     * Si ninguno tiene usos, informa al jugador.
     */
    @Override
    public void usar(Pokemon objetivo) {
        for (ItemMochila item : items) {
            if (item.getCantidad() > 0) {
                item.usar(objetivo);
                return;
            }
        }
        System.out.println("No hay ítems disponibles en el grupo: " + nombre);
    }

    /** Muestra el contenido del grupo con formato de árbol. */
    public void mostrar(String prefijo) {
        System.out.println(prefijo + "[" + nombre + "] (total: " + getCantidad() + " usos)");
        for (ItemMochila item : items) {
            if (item instanceof MochilaGrupo) {
                ((MochilaGrupo) item).mostrar(prefijo + "  ");
            } else {
                System.out.println(prefijo + "  - " + item.getNombre()
                        + " x" + item.getCantidad());
            }
        }
    }
    /**
     * Busca de forma recursiva si un ítem exacto existe 
     * dentro de este grupo o en cualquiera de sus subgrupos hijos.
     * Requerido por el método usarItem de la clase Entrenador.
     */
    public boolean tieneItem(ItemMochila itemBuscado) { // Pregunta ¿Este objeto existe en alguna parte de la mochila?
        if (this.items.contains(itemBuscado)) {
            return true;
        }

        // 2. Si no, buscamos hacia abajo en cada subgrupo de manera recursiva
        for (ItemMochila elemento : this.items) {
            if (elemento instanceof MochilaGrupo) {
                MochilaGrupo subGrupo = (MochilaGrupo) elemento;
                if (subGrupo.tieneItem(itemBuscado)) {
                    return true; // Encontrado en un subgrupo profundo
                }
            }
        }

        // 3. Si no apareció por ningún lado
        return false;
    }
}

