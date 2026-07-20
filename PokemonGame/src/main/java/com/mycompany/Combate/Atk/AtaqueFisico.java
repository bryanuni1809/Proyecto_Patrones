package com.mycompany.Combate.Atk;

import com.mycompany.Model.pokemon.Pokemon;
import com.mycompany.Model.pokemon.TipoPokemon;

// Representa un ataque físico, integrando los patrones Builder y Prototype
public class AtaqueFisico extends Ataque {
    
    // Constructor privado que inicializa el ataque usando el builder
    private AtaqueFisico(AtaqueFisicoBuilder builder) {
        super(builder);
    }

    // Ejecuta la lógica del ataque físico contra el defensor
    @Override
    public void atacar(Pokemon atacante, Pokemon defensor) {

        // Calcula el daño usando la instancia única de la calculadora
        int damage = Calculadora.getInstancia().calcularDanioFisico(atacante, defensor, potencia);

        // Resta el daño calculado a la vida actual del defensor
        defensor.setHpActual(defensor.getHpActual() - damage);

        // Garantiza que la vida no baje de cero
        if (defensor.getHpActual() < 0) {
            defensor.setHpActual(0);
        }

        // Muestra el resultado del ataque en la consola
        System.out.println(atacante.getNombre() + " uso " + nombre);
        System.out.println("Damage causado " + damage);
        System.out.println("HP restante de " + defensor.getNombre() + ": " + defensor.getHpActual());
    }
    
    // Implementa el patrón Prototype para crear una copia independiente del ataque
    @Override
    public Ataque clonar() {
        // Usa el builder para replicar el estado actual de forma limpia
        return new AtaqueFisico.AtaqueFisicoBuilder(this.nombre, this.tipo)
                .potencia(this.potencia)
                .build();
    }

    // Builder concreto encargado de construir instancias de AtaqueFisico paso a paso
    public static class AtaqueFisicoBuilder extends Ataque.Builder<AtaqueFisicoBuilder> {
        
        public AtaqueFisicoBuilder(String nombre, TipoPokemon tipo) {
            super(nombre, tipo);
        }

        // Construye y devuelve el objeto AtaqueFisico final
        @Override
        public AtaqueFisico build() {
            return new AtaqueFisico(this);
        }

        // Devuelve la referencia actual del builder para encadenar métodos fluidamente
        @Override
        protected AtaqueFisicoBuilder self() {
            return this;
        }
    }
}