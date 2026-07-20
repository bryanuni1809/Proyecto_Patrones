package com.mycompany.Combate.Atk;

import java.util.Scanner;
import com.mycompany.Model.entrenador.Entrenador;

// Implementa el patrón Observador para mostrar eventos del combate en consola
public class ConsolaObservador implements CombateObservador {
    
    // Referencia al objeto combate que se está observando
    private final Combate combate;
    
    // Nombre del jugador actual para personalizar los mensajes
    private final String nombreJugador;
    
    // Límite de veces que el jugador puede reiniciar el combate
    private int reiniciosPermitidos = 1; 

    // Inicializa el observador con la instancia del combate y el jugador
    public ConsolaObservador(Combate combate, String nombreJugador) {
        this.combate = combate;
        this.nombreJugador = nombreJugador;
    }

    // Evento: cambio de turno. Vacío porque no requiere acción en consola
    @Override
    public void onCambioTurno(int turno, com.mycompany.Model.pokemon.Pokemon p) { }

    // Evento: ataque realizado. Vacío porque no requiere acción en consola
    @Override
    public void onAtaqueRealizado(com.mycompany.Model.pokemon.Pokemon a,
                                  com.mycompany.Model.pokemon.Pokemon d,
                                  String ataque, int damage) { }

    // Evento: pokemon debilitado. Vacío porque no requiere acción en consola
    @Override
    public void onPokemonDebilitado(com.mycompany.Model.pokemon.Pokemon p) { }

    // Evento: cambio de pokemon. Vacío porque no requiere acción en consola
    @Override
    public void onPokemonCambiado(com.mycompany.Model.pokemon.Pokemon viejo,
                                  com.mycompany.Model.pokemon.Pokemon nuevo,
                                  String motivo) { }

    // Evento: el combate ha terminado. Aquí se maneja la lógica de reinicio
    @Override
    public void onCombateTerminado(String ganador) {
        // Muestra el nombre del ganador
        System.out.println("Combate terminado. Ganador: " + ganador);
        
        // Verifica si el jugador perdió y aún tiene reinicios disponibles
        if (!ganador.equalsIgnoreCase(nombreJugador) && reiniciosPermitidos > 0) {
            // Pide confirmación al jugador para reiniciar
            System.out.println("Has perdido. ¿Deseas reiniciar el combate desde el inicio? (s/n)");
            Scanner sc = new Scanner(System.in);
            
            // Lee y normaliza la respuesta del usuario
            String resp = sc.nextLine().trim().toLowerCase();
            
            // Si la respuesta es afirmativa, reinicia y reduce el contador
            if (resp.equals("s") || resp.equals("si")) {
                reiniciosPermitidos--;
                System.out.println("Reiniciando combate...");
                combate.reiniciarDesdeInicio();
            } else {
                // El jugador decide no reiniciar
                System.out.println("Fin del encuentro.");
            }
        } else if (!ganador.equalsIgnoreCase(nombreJugador)) {
            // El jugador perdió pero ya no tiene reinicios
            System.out.println("No quedan reinicios disponibles.");
        } else {
            // El jugador es el ganador
            System.out.println("¡Felicidades! Has ganado.");
        }
    }
}