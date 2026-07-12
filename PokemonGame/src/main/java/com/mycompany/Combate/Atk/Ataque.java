package com.mycompany.Combate.Atk;

import com.mycompany.Model.pokemon.Pokemon;
import com.mycompany.Model.pokemon.TipoPokemon;
import com.mycompany.Patrones.prototype.Prototype;

public abstract class Ataque implements Prototype<Ataque>{ // Se utiliza el patron prototype para clonar el ataque
    //Atributos
    protected String nombre;
    protected int potencia;
    protected TipoPokemon tipo;
    // El constructor ahora es protegido y recibe el Builder genérico
    protected Ataque(Builder<?> builder) {
        this.potencia = builder.potencia;
        this.tipo = builder.tipo;
        this.nombre = "Ataque especial tipo: " + tipo;
    }

    //Metodo abstracto
    public abstract void atacar(Pokemon atacante, Pokemon defensor);
    
    @Override
    public abstract Ataque clonar(); // Cada tipo de ataque sabrá cómo clonarse

    // ── EL PATRÓN BUILDER ANIDADO ───────────────────────────────────────
    public static abstract class Builder<T extends Builder<T>> {
        private String nombre;
        private int potencia;
        private TipoPokemon tipo;

        // Obligamos a que al menos pasen el nombre y el tipo al iniciar el Builder
        public Builder(String nombre, TipoPokemon tipo) {
            this.nombre = nombre;
            this.tipo = tipo;
        }

        public T potencia(int potencia) {
            this.potencia = potencia;
            return self(); // Retorna el tipo específico de Builder
        }

        public abstract Ataque build();
        protected abstract T self();
    }

    public String getNombre() {
        return nombre;
    }

}
