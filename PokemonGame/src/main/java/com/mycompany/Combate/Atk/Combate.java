package com.mycompany.Combate.Atk;

import com.mycompany.Model.entrenador.Entrenador;
import com.mycompany.Model.pokemon.Pokemon;
import java.util.List;
import java.util.ArrayList;

public class Combate {

    //Atributos
    private Entrenador entrenador1;
    private Entrenador entrenador2;
    private List<CombateObservador> observadores;
    private int turnoGeneral;

    public Combate(Entrenador entrenador1, Entrenador entrenador2, List<CombateObservador> observadoresIniciales) {
        this.entrenador1 = entrenador1;
        this.entrenador2 = entrenador2;
        this.observadores = observadoresIniciales;
        this.turnoGeneral = 1;
    }

    public void registrarObservador(CombateObservador o) { //permite agregar nuevos observadores
        observadores.add(o);
    }

    private void notificarCambioTurno(Pokemon p) {
        for (CombateObservador o : observadores) {
            o.onCambioTurno(turnoGeneral, p);
        }
    }

    private void notificarAtaque(Pokemon atacante, Pokemon defensor, String ataque, int damage) {
        for (CombateObservador o : observadores) {
            o.onAtaqueRealizado(atacante, defensor, ataque, damage);
        }
    }

    private void notificarPokemonDebilitado(Pokemon p) {
        for (CombateObservador o : observadores) {
            o.onPokemonDebilitado(p);
        }
    }

    private void notificarCambio(Pokemon viejo, Pokemon nuevo, String motivo) {
        for (CombateObservador o : observadores) {
            o.onPokemonCambiado(viejo, nuevo, motivo);
        }
    }

    private void notificarFin(String ganador) {
        for (CombateObservador o : observadores) {
            o.onCombateTerminado(ganador);
        }
    }

    public void iniciarBatalla() {
        System.out.println("¡Inicia el encuentro " + entrenador1.getNombre() + " vs " + entrenador2.getNombre() + "!");
        while (!entrenador1.equipoDerrotado() && !entrenador2.equipoDerrotado()) { //Mientras ninguno haya perdido 
            Pokemon p1 = entrenador1.getPokemonActivo(); //Obtiene los pokemons activos 
            Pokemon p2 = entrenador2.getPokemonActivo();

            if (p1.getVelocidad() >= p2.getVelocidad()) { // Decide quien ataca primero
                ejecutarAccion(entrenador1, p1, entrenador2, p2);
                if (!p2.estaDesmayado() && !p1.estaDesmayado()) {
                    ejecutarAccion(entrenador2, p2, entrenador1, p1);
                }
            } else {
                ejecutarAccion(entrenador2, p2, entrenador1, p1);
                if (!p1.estaDesmayado() && !p2.estaDesmayado()) {
                    ejecutarAccion(entrenador1, p1, entrenador2, p2);
                }
            }
            if (!p1.estaDesmayado()) { // Efectos de final de turno (ej. Daño por quemadura)
                p1.finalTurno();
            }
            if (!p2.estaDesmayado()) {
                p2.finalTurno();
            }
            verificarEstadoPokemon(entrenador1); //Verificar desmayos acumulados tras los efectos del final del turno
            verificarEstadoPokemon(entrenador2);

            turnoGeneral++; //Aumenta turno general 
        }
        if (!entrenador1.equipoDerrotado()) {
            notificarFin(entrenador1.getNombre());
        } else {
            notificarFin(entrenador2.getNombre());
        }
    }

    private void ejecutarAccion(Entrenador atacanteEnt, Pokemon atacante, Entrenador defensorEnt, Pokemon defensor) {
        notificarCambioTurno(atacante);

        if (atacante.iniciarTurno()) {
            if (!atacante.getAtaques().isEmpty()) {
                Ataque ataque = atacante.getAtaques().get(0); // Por ahora usa el primer ataque disponible del listado

                int hpPrevio = defensor.getHpActual(); //Guarda el hp antes del ataque 
                ataque.atacar(atacante, defensor);
                int danioRecibido = hpPrevio - defensor.getHpActual();

                notificarAtaque(atacante, defensor, ataque.nombre, danioRecibido);//notifica al observador
                verificarEstadoPokemon(defensorEnt);  // Verificar si el defensor murió por el impacto inmediato del ataque
            }
        }
    }

    private void verificarEstadoPokemon(Entrenador entrenador) {
        Pokemon p = entrenador.getPokemonActivo();
        if (p != null && p.estaDesmayado()) {
            notificarPokemonDebilitado(p);
            
            if (!entrenador.equipoDerrotado()) {
                Pokemon nuevo = entrenador.sacarSiguientePokemon();
                notificarCambio(p, nuevo, "se desmayo");
            }
        }
    }
        
    private boolean tienePokemonDeReserva(Entrenador e) {
        int vivos = 0;
        for (Pokemon p : e.getEquipo()) {
            if (p != null && !p.estaDesmayado()) {
                vivos++;
            }
        }
        return vivos > 1;
    }

}
