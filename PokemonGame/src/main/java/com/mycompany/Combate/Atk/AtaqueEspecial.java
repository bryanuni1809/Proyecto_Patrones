package com.mycompany.Combate.Atk;

import com.mycompany.Model.pokemon.Pokemon;
import com.mycompany.Model.pokemon.TipoPokemon;
import com.mycompany.Patrones.state.*;
import java.util.Random;

// Representa un ataque especial con mecánica de carga y posible efecto de estado
public class AtaqueEspecial extends Ataque {
    
    // Generador de números aleatorios para determinar si se aplica el estado
    private final Random random = new Random();
    
    // Contador de turnos que el ataque lleva cargándose
    private int turnosCarga;
    
    // Cantidad fija de turnos requeridos para que el ataque esté listo
    private final int turnosNecesarios = 3;
    
    // Indica si el ataque ya completó su carga y puede ejecutarse
    private boolean cargado;
    
    // Estado alterado que el ataque puede infligir al defensor
    private EstadoPokemon efectoEstado;
    
    // Porcentaje de probabilidad de aplicar el estado alterado
    private int probabilidadEstado;

    // Constructor privado que inicializa el ataque usando el builder
    private AtaqueEspecial(AtaqueEspecialBuilder builder) {
        super(builder);
        this.efectoEstado = builder.efectoEstado;
        this.probabilidadEstado = builder.probabilidadEstado;
        this.turnosCarga = 0;
        this.cargado = false;
    }
    
    // Ejecuta la lógica del ataque, incluyendo carga, daño y efectos
    @Override
    public void atacar(Pokemon atacante, Pokemon defensor) {
        
        // Fase de carga: si no está listo, incrementa el contador y termina el turno
        if (!cargado) {
            turnosCarga++;
            System.out.println(atacante.getNombre() + " está cargando " + nombre + " (" + turnosCarga + "/" + turnosNecesarios + ")");
            if (turnosCarga >= turnosNecesarios) {
                cargado = true;
                System.out.println(nombre + " está listo para usarse!");
            }
            return; // No ejecuta daño todavía
        }
        
        // Fase de ejecución: obtiene la tabla de tipos y calcula el daño
        TablaTipos tabla = TablaTipos.getInstancia();
        int damage = Calculadora.getInstancia().calcularDanioEspecial(atacante, defensor, potencia, tipo, tabla);
        
        // Resta el daño al defensor, asegurando que la vida no baje de cero
        defensor.setHpActual(defensor.getHpActual() - damage);
        if (defensor.getHpActual() <= 0) {
            defensor.setHpActual(0);
        }
        
        System.out.println(atacante.getNombre() + " uso " + nombre);
        
        // Muestra mensajes de efectividad basados en el multiplicador de tipos
        double multiplicador = tabla.getMultiplicador(tipo, defensor.getTipo());
        if (multiplicador == 2.0) {
            System.out.println("¡Es muy efectivo!");
        } else if (multiplicador == 0.75) {
            System.out.println("No es muy efectivo");
        }

        System.out.println("Damage causado: " + damage);
        System.out.println("HP restante de " + defensor.getNombre() + ": " + defensor.getHpActual());

        // Fase de efecto: si el ataque tiene estado y el defensor sigue vivo
        if (efectoEstado != null && defensor.getHpActual() > 0) {
            // Evalúa la probabilidad y aplica el estado clonado si tiene éxito
            if (random.nextInt(100) < probabilidadEstado) {
                EstadoPokemon nuevoEstado = efectoEstado.clonar();
                if (nuevoEstado != null) {
                    defensor.setEstado(nuevoEstado);
                    System.out.println("¡" + defensor.getNombre() + " ahora está " + nuevoEstado.getNombre() + "!");
                }
            }
        }
    }

    // Implementa el patrón Prototype para crear una copia independiente del ataque
    @Override
    public Ataque clonar() {
        // Clona el estado de forma segura para evitar referencias compartidas
        EstadoPokemon estadoClonado = (this.efectoEstado != null) ? this.efectoEstado.clonar() : null;
        
        // Usa el builder para replicar el estado actual de forma limpia
        return new AtaqueEspecial.AtaqueEspecialBuilder(this.nombre, this.tipo)
                .potencia(this.potencia)
                .efectoEstado(estadoClonado, this.probabilidadEstado)
                .build();
    }

    // Builder concreto encargado de construir instancias de AtaqueEspecial
    public static class AtaqueEspecialBuilder extends Ataque.Builder<AtaqueEspecialBuilder> {
        
        // Valor por defecto para el efecto de estado
        private EstadoPokemon efectoEstado = null;
        
        // Valor por defecto para la probabilidad del estado
        private int probabilidadEstado = 0;

        public AtaqueEspecialBuilder(String nombre, TipoPokemon tipo) {
            super(nombre, tipo);
        }

        // Método fluido para configurar el estado y su probabilidad
        public AtaqueEspecialBuilder efectoEstado(EstadoPokemon efectoEstado, int probabilidadEstado) {
            this.efectoEstado = efectoEstado;
            this.probabilidadEstado = probabilidadEstado;
            return this;
        }

        // Construye y devuelve el objeto AtaqueEspecial final
        @Override
        public AtaqueEspecial build() {
            return new AtaqueEspecial(this);
        }

        // Devuelve la referencia actual para permitir el encadenamiento de métodos
        @Override
        protected AtaqueEspecialBuilder self() {
            return this;
        }
    }
}