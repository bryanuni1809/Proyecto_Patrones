package com.mycompany.Combate.Atk;

import com.mycompany.Model.pokemon.Pokemon;
import com.mycompany.Model.pokemon.TipoPokemon;
import com.mycompany.Patrones.state.*;
import java.util.Random;

public class AtaqueEspecial extends Ataque {
    //Atributos
    private final Random random = new Random();
    private int turnosCarga;   // cuántos turnos lleva cargando
    private final int turnosNecesarios = 3; // constante: 3 turnos
    private boolean cargado;
    private EstadoPokemon efectoEstado; //Guarda el estado que puede producir el ataque
    private int probabilidadEstado;

    //Constructor
    private AtaqueEspecial(AtaqueEspecialBuilder builder) {
        super(builder);
        this.efectoEstado = builder.efectoEstado;
        this.probabilidadEstado = builder.probabilidadEstado;
        this.turnosCarga = 0;
        this.cargado = false;
    }
    
    
    @Override
    public void atacar(Pokemon atacante, Pokemon defensor) {
        
        if (!cargado) {
            turnosCarga++;
            System.out.println(atacante.getNombre() + " está cargando " + nombre + " (" + turnosCarga + "/" + turnosNecesarios + ")");
            if (turnosCarga >= turnosNecesarios) {
                cargado = true;
                System.out.println(nombre + " está listo para usarse!");
            }
            return; // no ejecuta daño todavía
        }
        
        TablaTipos tabla = TablaTipos.getInstancia(); //obtiene la tabla tipos
        
        int damage = Calculadora.getInstancia().calcularDanioEspecial( atacante, defensor, potencia, tipo, tabla);
        
        defensor.setHpActual(defensor.getHpActual() - damage);

        if (defensor.getHpActual() <= 0) { //la vida no puede tener valores negativos
            defensor.setHpActual(0);
        }
        System.out.println(atacante.getNombre() + "uso" + nombre);
        
        double multiplicador = tabla.getMultiplicador( tipo, defensor.getTipo() ); //revisa efectividad 
        if (multiplicador == 2.0) {
            System.out.println("¡Es muy efectivo!");
        } else if (multiplicador == 0.75) {
            System.out.println("No es muy efectivo");
        }

        System.out.println("Damage causado: " + damage);
        System.out.println("HP restante de " + defensor.getNombre() + ": " + defensor.getHpActual());

        if (efectoEstado != null && defensor.getHpActual() > 0) { //¿Este ataque tiene estado? ¿El Pokémon sigue vivo?
            if (random.nextInt(100) < probabilidadEstado) { //genera un numero aleatorio
                EstadoPokemon nuevoEstado = efectoEstado.clonar(); //clona el estado del pokemon
                if (nuevoEstado != null) {
                    defensor.setEstado(nuevoEstado);
                    System.out.println("¡" + defensor.getNombre() + "Ahora esta" + nuevoEstado.getNombre() + "!");
                }
            }
        }
    }

    @Override
    public Ataque clonar() {
        EstadoPokemon estadoClonado = (this.efectoEstado != null) ? this.efectoEstado.clonar() : null;
        
        // El prototipo ahora se clona usando el Builder de forma súper limpia
        return new AtaqueEspecial.AtaqueEspecialBuilder(this.nombre, this.tipo)
                .potencia(this.potencia)
                .efectoEstado(estadoClonado, this.probabilidadEstado)
                .build();
    }

    // ── EL BUILDER ESPECÍFICO PARA ATAQUE ESPECIAL ───────────────────────
    public static class AtaqueEspecialBuilder extends Ataque.Builder<AtaqueEspecialBuilder> {
        private EstadoPokemon efectoEstado = null;       // Valor por defecto
        private int probabilidadEstado = 0;            // Valor por defecto

        public AtaqueEspecialBuilder(String nombre, TipoPokemon tipo) {
            super(nombre, tipo);
        }

        // Método para configurar el estado opcionalmente
        public AtaqueEspecialBuilder efectoEstado(EstadoPokemon efectoEstado, int probabilidadEstado) {
            this.efectoEstado = efectoEstado;
            this.probabilidadEstado = probabilidadEstado;
            return this;
        }

        @Override
        public AtaqueEspecial build() {
            return new AtaqueEspecial(this);
        }

        @Override
        protected AtaqueEspecialBuilder self() {
            return this;
        }
    }

}
