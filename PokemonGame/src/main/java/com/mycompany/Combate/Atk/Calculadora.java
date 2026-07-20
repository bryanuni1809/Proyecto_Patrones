package com.mycompany.Combate.Atk;

import com.mycompany.Model.pokemon.*;

// Implementa el patrón Singleton para centralizar los cálculos de daño
public class Calculadora {
    
    // Instancia única de la clase, inicializada bajo demanda
    private static Calculadora instancia;

    // Constructor privado para evitar que se creen nuevas instancias desde fuera
    private Calculadora() {
    }

    // Devuelve la única instancia disponible, creándola si aún no existe
    public static Calculadora getInstancia() {
        if (instancia == null) {
            instancia = new Calculadora();
        }
        return instancia;
    }
    
    // Calcula el daño físico base usando ataque y defensa, sin considerar tipos
    public int calcularDanioFisico(Pokemon atacante, Pokemon defensor, int potencia) {
        int danio = (atacante.getAtaque() * potencia) / defensor.getDefensa();
        return Math.max(danio, 1); // Garantiza un daño mínimo de 1
    }

    // Calcula el daño especial aplicando el multiplicador de efectividad de tipos
    public int calcularDanioEspecial(Pokemon atacante, Pokemon defensor, int potencia, TipoPokemon tipoAtaque, TablaTipos tabla) {
        // Obtiene la efectividad del ataque contra el tipo del defensor
        double multiplicador = tabla.getMultiplicador(tipoAtaque, defensor.getTipo());
        
        // Aplica el multiplicador a la fórmula base de daño
        int danio = (int) (((atacante.getAtaque() * potencia) / defensor.getDefensa()) * multiplicador);
        
        return Math.max(danio, 1); // Garantiza un daño mínimo de 1
    }
}