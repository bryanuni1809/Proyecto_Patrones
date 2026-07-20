package com.mycompany.Combate.Atk;

import com.mycompany.Model.entrenador.Entrenador;
import com.mycompany.Model.pokemon.Pokemon;
import java.util.List;
import java.util.ArrayList;

// Clase principal del combate. Actúa como sujeto del patrón Observador y origen del patrón Memento.
public class Combate implements CombateCallback {

    // Participantes del encuentro
    private Entrenador entrenador1;
    private Entrenador entrenador2;
    
    // Guarda el estado inicial para permitir reinicios seguros
    private CombateMemento mementoInicial;
    
    // Lista de objetos que escuchan y reaccionan a los eventos del combate
    private List<CombateObservador> observadores;
    
    // Contador del turno actual de la batalla
    private int turnoGeneral;

    // Inicializa el combate con los entrenadores y la lista de observadores
    public Combate(Entrenador entrenador1, Entrenador entrenador2, List<CombateObservador> observadoresIniciales) {
        this.entrenador1 = entrenador1;
        this.entrenador2 = entrenador2;
        // Aseguramos lista no nula para evitar errores al registrar observadores
        this.observadores = (observadoresIniciales != null) ? observadoresIniciales : new ArrayList<>();
        this.turnoGeneral = 1;
    }

    // Agrega un nuevo observador a la lista si no es nulo
    public void registrarObservador(CombateObservador o) {
        if (o != null) observadores.add(o);
    }

    // Avisa a todos los observadores sobre el cambio de turno
    private void notificarCambioTurno(Pokemon p) {
        for (CombateObservador o : observadores) {
            o.onCambioTurno(turnoGeneral, p);
        }
    }

    // Notifica a los observadores que se realizó un ataque
    @Override
    public void notificarAtaque(Pokemon atacante, Pokemon defensor, String ataque, int damage) {
        for (CombateObservador o : observadores) {
            o.onAtaqueRealizado(atacante, defensor, ataque, damage);
        }
    }

    // Notifica a los observadores que un Pokémon se debilitó
    @Override
    public void notificarPokemonDebilitado(Pokemon p) {
        for (CombateObservador o : observadores) {
            o.onPokemonDebilitado(p);
        }
    }

    // Notifica a los observadores sobre el cambio de Pokémon en el campo
    @Override
    public void notificarCambio(Pokemon viejo, Pokemon nuevo, String motivo) {
        for (CombateObservador o : observadores) {
            o.onPokemonCambiado(viejo, nuevo, motivo);
        }
    }

    // Notifica a los observadores que el combate ha terminado
    private void notificarFin(String ganador) {
        for (CombateObservador o : observadores) {
            o.onCombateTerminado(ganador);
        }
    }

    // Crea una copia del estado actual para poder restaurarlo después (Patrón Memento)
    public void guardarEstadoInicial() {
        List<Entrenador> copia = new ArrayList<>();
        copia.add(entrenador1.clonar());
        copia.add(entrenador2.clonar());
        mementoInicial = new CombateMemento(turnoGeneral, copia);
    }

    // Restaura el combate al estado guardado en el memento inicial
    public void restaurarEstadoInicial() {
        if (mementoInicial == null) return;
        this.turnoGeneral = mementoInicial.getTurnoGeneral();
        List<Entrenador> copia = mementoInicial.getEntrenadoresClonados();
        this.entrenador1 = copia.get(0);
        this.entrenador2 = copia.get(1);
    }

    // Restaura el estado y vuelve a lanzar el bucle de batalla
    public void reiniciarDesdeInicio() {
        restaurarEstadoInicial();
        iniciarBatalla();
    }

    // Bucle principal del combate. Se ejecuta hasta que un equipo es derrotado
    public void iniciarBatalla() {
        // Guarda el estado inicial si no se ha hecho antes
        if (mementoInicial == null) {
            guardarEstadoInicial();
        }

        System.out.println("¡Inicia el encuentro " + entrenador1.getNombre() + " vs " + entrenador2.getNombre() + "!");
        
        while (!entrenador1.equipoDerrotado() && !entrenador2.equipoDerrotado()) {
            Pokemon p1 = entrenador1.getPokemonActivo();
            Pokemon p2 = entrenador2.getPokemonActivo();

            // Determina el orden de ataque según la velocidad de los Pokémon
            if (p1.getVelocidad() >= p2.getVelocidad()) {
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

            // Aplica efectos de final de turno a los Pokémon activos
            if (!p1.estaDesmayado()) p1.finalTurno();
            if (!p2.estaDesmayado()) p2.finalTurno();

            // Verifica si algún Pokémon se debilitó y requiere cambio
            verificarEstadoPokemon(entrenador1);
            verificarEstadoPokemon(entrenador2);

            // Aumenta el contador de turnos generales
            turnoGeneral++;
        }

        // Notifica el nombre del ganador al finalizar el bucle
        if (!entrenador1.equipoDerrotado()) {
            notificarFin(entrenador1.getNombre());
        } else {
            notificarFin(entrenador2.getNombre());
        }
    }

    // Gestiona el turno de un Pokémon específico
    private void ejecutarAccion(Entrenador atacanteEnt, Pokemon atacante, Entrenador defensorEnt, Pokemon defensor) {
        notificarCambioTurno(atacante);

        if (atacante.iniciarTurno()) {
            if (!atacante.getAtaques().isEmpty()) {
                Ataque ataqueElegido = atacanteEnt.elegirAtaque(atacante);

                // Si es nulo, el entrenador usó un ítem. El turno se consume sin atacar.
                if (ataqueElegido != null) {
                    // Ejecuta la acción encapsulada en un objeto Comando
                    AtaqueComand cmd = new AtaqueComand(ataqueElegido, atacante, defensor, defensorEnt, this);
                    cmd.ejecutar();
                }
            }
        }
    }

    // Revisa si el Pokémon activo está debilitado tras un evento
    @Override
    public void verificarEstadoPokemon(Entrenador entrenador) {
        Pokemon p = entrenador.getPokemonActivo();
        if (p != null && p.estaDesmayado()) {
            notificarPokemonDebilitado(p);

            // Si hay otro Pokémon disponible, lo saca al campo automáticamente
            if (!entrenador.equipoDerrotado()) {
                Pokemon nuevo = entrenador.sacarSiguientePokemon();
                notificarCambio(p, nuevo, "se desmayo");
            }
        }
    }

    // Verifica si el entrenador tiene más de un Pokémon con vida en su equipo
    private boolean tienePokemonDeReserva(Entrenador e) {
        int vivos = 0;
        for (Pokemon p : e.getEquipo()) {
            if (p != null && !p.estaDesmayado()) vivos++;
        }
        return vivos > 1;
    }
}