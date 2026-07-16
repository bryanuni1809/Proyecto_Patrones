package com.mycompany.gui;

import com.mycompany.Combate.Atk.CombateObservador;
import com.mycompany.Model.entrenador.Entrenador;
import com.mycompany.Model.pokemon.Pokemon;
import com.mycompany.gui.RecursosImagenes.EstadoSprite;

import javax.swing.SwingUtilities;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Traduce los eventos del patrón Observer ya existente (CombateObservador)
 * en actualizaciones visuales sobre PanelBatalla. No contiene ninguna regla
 * de combate: solo reacciona a lo que Combate/AtaqueComand ya decidieron.
 *
 * IMPORTANTE: Combate llama a estos métodos desde el hilo de combate (no es
 * el EDT), así que cada actualización visual se despacha con
 * SwingUtilities.invokeAndWait. Esto de paso da un pequeño ritmo/pausa a la
 * batalla (efecto "animación") sin bloquear la interfaz.
 */
public class CombateObservadorGUI implements CombateObservador {

    private final PanelBatalla panel;
    private final Entrenador jugador;
    private final Entrenador rival;
    private final Consumer<String> onCombateTerminado;

    public CombateObservadorGUI(PanelBatalla panel, Entrenador jugador, Entrenador rival,
                                 Consumer<String> onCombateTerminado) {
        this.panel = panel;
        this.jugador = jugador;
        this.rival = rival;
        this.onCombateTerminado = onCombateTerminado;
    }

    private boolean esDelJugador(Pokemon p) {
        return Arrays.asList(jugador.getEquipo()).contains(p);
    }

    private void enEDT(Runnable r) {
        try {
            SwingUtilities.invokeAndWait(r);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }

    private void pausa(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void onCambioTurno(int turnoGeneral, Pokemon pokemonActivo) {
        enEDT(() -> panel.mostrarMensaje("Turno de " + pokemonActivo.getNombre() + "..."));
        pausa(300);
    }

    @Override
    public void onAtaqueRealizado(Pokemon atacante, Pokemon defensor, String ataque, int damage) {
        boolean atacanteEsJugador = esDelJugador(atacante);

        enEDT(() -> {
            panel.mostrarMensaje(atacante.getNombre() + " usó " + ataque + "!");
            if (atacanteEsJugador) {
                panel.setSpriteJugador(EstadoSprite.ATAQUE);
                panel.animarAtaqueJugador(); 
            } else {
                panel.setSpriteRival(EstadoSprite.ATAQUE);
                panel.animarAtaqueRival();
            }
        });
        pausa(400);

        enEDT(() -> {
            if (atacanteEsJugador) {
                panel.setSpriteJugador(EstadoSprite.NORMAL);
                panel.animarAtaqueJugador(); 
            } else {
                panel.setSpriteRival(EstadoSprite.NORMAL);
                panel.animarAtaqueRival();
            }

            if (damage > 0) {
                if (esDelJugador(defensor)) {
                    panel.setSpriteJugador(EstadoSprite.DANIO);
                    panel.animarHpJugador(defensor.getHpActual());
                    panel.animarGolpeJugador();
                    panel.flashJugador();
                } else {
                    panel.setSpriteRival(EstadoSprite.DANIO);
                    panel.animarHpRival(defensor.getHpActual());
                    panel.animarGolpeRival();
                    panel.flashRival();
                }
                panel.mostrarMensaje(defensor.getNombre() + " recibió " + damage + " de daño.");
            }
        });
        pausa(400);

        enEDT(() -> {
            panel.setSpriteJugador(EstadoSprite.NORMAL);
            panel.setSpriteRival(EstadoSprite.NORMAL);
        });
    }

    @Override
    public void onPokemonDebilitado(Pokemon pokemon) {
        enEDT(() -> panel.mostrarMensaje("¡" + pokemon.getNombre() + " se debilitó!"));
        pausa(600);
    }

    @Override
    public void onPokemonCambiado(Pokemon viejo, Pokemon nuevo, String motivo) {
        if (nuevo == null) {
            return;
        }
        boolean esJugador = esDelJugador(nuevo);
        enEDT(() -> {
            if (esJugador) {
                panel.setPokemonJugador(nuevo);
            } else {
                panel.setPokemonRival(nuevo);
            }
            panel.mostrarMensaje((esJugador ? jugador.getNombre() : rival.getNombre())
                    + " envía a " + nuevo.getNombre() + "!");
        });
        pausa(500);
    }

    @Override
    public void onCombateTerminado(String ganador) {
        enEDT(() -> {
            panel.ocultarBotonesAtaque();
            panel.mostrarMensaje("¡Combate terminado! Ganador: " + ganador);
        });
        if (onCombateTerminado != null) {
            onCombateTerminado.accept(ganador);
        }
    }
}
