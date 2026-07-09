package com.mycompany.Combate.Atk;

import com.mycompany.Model.pokemon.Pokemon;
import com.mycompany.Model.pokemon.TipoPokemon;

public class AtaqueFisico extends Ataque {
    
    // Recibe su constructor específico
    private AtaqueFisico(AtaqueFisicoBuilder builder) {
        super(builder);
    }

    @Override
    public void atacar(Pokemon atacante, Pokemon defensor) {

        int damage = Calculadora.getInstancia().calcularDanioFisico(atacante, defensor, potencia); //La calculadora calcula el daño

        defensor.setHpActual(defensor.getHpActual() - damage); //resta vida y le asigna a HpActual

        if (defensor.getHpActual() < 0) { //la vida no puede tener valores negativos
            defensor.setHpActual(0);
        }

        System.out.println(atacante.getNombre() + " uso " + nombre);
        System.out.println("Damage causado " + damage);
        System.out.println("HP restante de " + defensor.getNombre() + ": " + defensor.getHpActual());
    }
    
    @Override
    public Ataque clonar() {
        // El Prototype ahora puede usar el Builder para clonarse elegantemente
        return new AtaqueFisico.AtaqueFisicoBuilder(this.nombre, this.tipo)
                .potencia(this.potencia)
                .build();
    }

    // El Builder concreto para AtaqueFisico
    public static class AtaqueFisicoBuilder extends Ataque.Builder<AtaqueFisicoBuilder> {
        public AtaqueFisicoBuilder(String nombre, TipoPokemon tipo) {
            super(nombre, tipo);
        }

        @Override
        public AtaqueFisico build() {
            return new AtaqueFisico(this);
        }

        @Override
        protected AtaqueFisicoBuilder self() {
            return this;
        }
    }

}
