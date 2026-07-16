package com.mycompany.gui;

import com.mycompany.Combate.Atk.Combate;
import com.mycompany.Model.entrenador.Entrenador;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.Dimension;

/**
 * Ventana principal del combate.
 *
 * Arma PanelBatalla, conecta SelectorAtaqueGUI (jugador) / SelectorAtaqueIA
 * (rival) y CombateObservadorGUI con el objeto Combate que ya recibe
 * armado (sin modificar su lógica interna), y ejecuta
 * Combate.iniciarBatalla() en un hilo aparte, porque ese método es
 * bloqueante (bucle while + espera de Scanner/selección) y correrlo en el
 * EDT congelaría la interfaz por completo.
 */
public class VentanaBatalla extends JFrame {

    private final Combate combate;
    private final PanelBatalla panel = new PanelBatalla();

    public VentanaBatalla(Entrenador jugador, Entrenador rival, Combate combate, Runnable onNuevaPartida) {
        super("Pokémon - Combate");
        this.combate = combate;

        // Conecta el patrón Strategy: el jugador elige por clics, el rival por IA.
        jugador.setSelectorAtaque(new SelectorAtaqueGUI(panel, jugador));
        rival.setSelectorAtaque(new SelectorAtaqueIA());

        // Se agrega como un observador más, junto a los que ya existan (ej. BitacoraCombate).
        combate.registrarObservador(new CombateObservadorGUI(panel, jugador, rival,
                ganador -> mostrarFinDeCombate(ganador, onNuevaPartida)));

        panel.setPokemonJugador(jugador.getPokemonActivo());
        panel.setPokemonRival(rival.getPokemonActivo());
        panel.mostrarMensaje("¡Comienza el combate entre " + jugador.getNombre()
                + " y " + rival.getNombre() + "!");

        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setMinimumSize(new Dimension(640, 520));
        setResizable(false);
        setLocationRelativeTo(null);
    }

    /** Corre Combate.iniciarBatalla() en un hilo aparte para no congelar la interfaz. */
    public void iniciarCombateEnHilo() {
        Thread hiloCombate = new Thread(combate::iniciarBatalla, "hilo-combate");
        hiloCombate.setDaemon(true);
        hiloCombate.start();
    }

    private void mostrarFinDeCombate(String ganador, Runnable onNuevaPartida) {
        SwingUtilities.invokeLater(() -> {
            int opcion = JOptionPane.showConfirmDialog(this,
                    "Ganador: " + ganador + "\n¿Deseas jugar de nuevo?",
                    "Combate terminado", JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION && onNuevaPartida != null) {
                dispose();
                onNuevaPartida.run();
            } else {
                dispose();
                System.exit(0);
            }
        });
    }
}
