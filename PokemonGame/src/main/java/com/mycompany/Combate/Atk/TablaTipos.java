package com.mycompany.Combate.Atk;
import com.mycompany.Model.pokemon.*;
import java.util.*;

public class TablaTipos {
    private Map<TipoPokemon, TipoInfo> tabla = new HashMap<>();

    public TablaTipos() {
        // Definimos reglas de ejemplo
        tabla.put(TipoPokemon.FUEGO, new TipoInfo(
            Arrays.asList(TipoPokemon.PLANTA, TipoPokemon.HIELO, TipoPokemon.BICHO),
            Arrays.asList(TipoPokemon.AGUA, TipoPokemon.ROCA, TipoPokemon.TIERRA)
        ));

        tabla.put(TipoPokemon.AGUA, new TipoInfo(
            Arrays.asList(TipoPokemon.FUEGO, TipoPokemon.ROCA, TipoPokemon.TIERRA),
            Arrays.asList(TipoPokemon.PLANTA, TipoPokemon.ELECTRICO)
        ));

        tabla.put(TipoPokemon.PLANTA, new TipoInfo(
            Arrays.asList(TipoPokemon.AGUA, TipoPokemon.TIERRA, TipoPokemon.ROCA),
            Arrays.asList(TipoPokemon.FUEGO, TipoPokemon.VOLADOR, TipoPokemon.BICHO, TipoPokemon.VENENO, TipoPokemon.HIELO)
        ));

        tabla.put(TipoPokemon.ELECTRICO, new TipoInfo(
            Arrays.asList(TipoPokemon.AGUA, TipoPokemon.VOLADOR),
            Arrays.asList(TipoPokemon.TIERRA)
        ));
        
        tabla.put(TipoPokemon.NORMAL, new TipoInfo(
            Arrays.asList(),
            Arrays.asList(TipoPokemon.LUCHA)
        ));
        
        tabla.put(TipoPokemon.HIELO, new TipoInfo(
            Arrays.asList(TipoPokemon.VOLADOR, TipoPokemon.DRAGON, TipoPokemon.TIERRA, TipoPokemon.PLANTA),
            Arrays.asList(TipoPokemon.LUCHA, TipoPokemon.ROCA, TipoPokemon.FUEGO)
        ));
        
        tabla.put(TipoPokemon.LUCHA, new TipoInfo(
            Arrays.asList(TipoPokemon.ROCA, TipoPokemon.HIELO, TipoPokemon.NORMAL),
            Arrays.asList(TipoPokemon.PSIQUICO, TipoPokemon.VOLADOR)
        ));
        
        tabla.put(TipoPokemon.TIERRA, new TipoInfo(
            Arrays.asList(TipoPokemon.ELECTRICO, TipoPokemon.VENENO, TipoPokemon.FUEGO, TipoPokemon.ROCA),
            Arrays.asList(TipoPokemon.PLANTA, TipoPokemon.AGUA, TipoPokemon.HIELO)
        ));
        
        tabla.put(TipoPokemon.VENENO, new TipoInfo(
            Arrays.asList(TipoPokemon.PLANTA),
            Arrays.asList(TipoPokemon.PSIQUICO, TipoPokemon.TIERRA)
        ));
        
        tabla.put(TipoPokemon.BICHO, new TipoInfo(
            Arrays.asList(TipoPokemon.PSIQUICO, TipoPokemon.PLANTA),
            Arrays.asList(TipoPokemon.ROCA, TipoPokemon.FUEGO, TipoPokemon.VOLADOR)
        ));
        
        tabla.put(TipoPokemon.ROCA, new TipoInfo(
            Arrays.asList(TipoPokemon.BICHO, TipoPokemon.VOLADOR, TipoPokemon.HIELO, TipoPokemon.FUEGO),
            Arrays.asList(TipoPokemon.LUCHA, TipoPokemon.PLANTA, TipoPokemon.TIERRA, TipoPokemon.AGUA)
        ));
        
        tabla.put(TipoPokemon.FANTASMA, new TipoInfo(
            Arrays.asList(TipoPokemon.PSIQUICO, TipoPokemon.FANTASMA),
            Arrays.asList(TipoPokemon.FANTASMA, TipoPokemon.NORMAL)
        ));
        
        tabla.put(TipoPokemon.DRAGON, new TipoInfo(
            Arrays.asList(TipoPokemon.DRAGON),
            Arrays.asList(TipoPokemon.DRAGON, TipoPokemon.HIELO)
        ));
        
    }

    public double getMultiplicador(TipoPokemon atacante, TipoPokemon defensor) {
        TipoInfo info = tabla.get(atacante);
        if (info == null) return 1.0; // neutral si no hay reglas

        if (info.efectivos.contains(defensor)) return 2.0;
        if (info.inefectivos.contains(defensor)) return 0.75;
        return 1.0; // neutral
    }

    // Clase interna para guardar listas
    private static class TipoInfo {
        List<TipoPokemon> efectivos;
        List<TipoPokemon> inefectivos;

        TipoInfo(List<TipoPokemon> efectivos, List<TipoPokemon> inefectivos) {
            this.efectivos = efectivos;
            this.inefectivos = inefectivos;
        }
    }
}
