package com.mycompany.Combate.Atk;

import java.util.Scanner;
import com.mycompany.Model.entrenador.Entrenador;

public class ConsolaObservador implements CombateObservador {
    private final Combate combate;
    private final String nombreJugador;
    private int reiniciosPermitidos = 1; // opcional: limitar reinicios

    public ConsolaObservador(Combate combate, String nombreJugador) {
        this.combate = combate;
        this.nombreJugador = nombreJugador;
    }

    @Override
    public void onCambioTurno(int turno, com.mycompany.Model.pokemon.Pokemon p) { }

    @Override
    public void onAtaqueRealizado(com.mycompany.Model.pokemon.Pokemon a,
                                  com.mycompany.Model.pokemon.Pokemon d,
                                  String ataque, int damage) { }

    @Override
    public void onPokemonDebilitado(com.mycompany.Model.pokemon.Pokemon p) { }

    @Override
    public void onPokemonCambiado(com.mycompany.Model.pokemon.Pokemon viejo,
                                  com.mycompany.Model.pokemon.Pokemon nuevo,
                                  String motivo) { }

    @Override
    public void onCombateTerminado(String ganador) {
        System.out.println("Combate terminado. Ganador: " + ganador);
        if (!ganador.equalsIgnoreCase(nombreJugador) && reiniciosPermitidos > 0) {
            System.out.println("Has perdido. ¿Deseas reiniciar el combate desde el inicio? (s/n)");
            Scanner sc = new Scanner(System.in);
            String resp = sc.nextLine().trim().toLowerCase();
            if (resp.equals("s") || resp.equals("si")) {
                reiniciosPermitidos--;
                System.out.println("Reiniciando combate...");
                combate.reiniciarDesdeInicio();
            } else {
                System.out.println("Fin del encuentro.");
            }
        } else if (!ganador.equalsIgnoreCase(nombreJugador)) {
            System.out.println("No quedan reinicios disponibles.");
        } else {
            System.out.println("¡Felicidades! Has ganado.");
        }
    }
}
