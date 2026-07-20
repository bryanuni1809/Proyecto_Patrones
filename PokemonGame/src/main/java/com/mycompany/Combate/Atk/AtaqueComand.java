package com.mycompany.Combate.Atk;

import com.mycompany.Model.entrenador.Entrenador;
import com.mycompany.Model.pokemon.Pokemon;

// Implementa el patron Comando para encapsular la accion de atacar
public class AtaqueComand implements Comand {
    
    // El receptor que contiene la logica real del daño (fisico o especial)
    private final Ataque ataque;
    
    // El Pokemon que realiza el movimiento
    private final Pokemon atacante;
    
    // El Pokemon que recibe el impacto
    private final Pokemon defensor;
    
    // El entrenador del defensor, necesario para revisar su estado posterior
    private final Entrenador defensorEnt;
    
    // Mecanismo para notificar eventos y delegar validaciones al combate
    private final CombateCallback callback;
    
    // Almacena la cantidad exacta de vida restada en esta ejecucion
    private int dañofinal;

    // Inicializa el comando con todos los participantes y el sistema de notificacion
    public AtaqueComand(Ataque ataque,
                         Pokemon atacante,
                         Pokemon defensor,
                         Entrenador defensorEnt,
                         CombateCallback callback) {
        this.ataque = ataque;
        this.atacante = atacante;
        this.defensor = defensor;
        this.defensorEnt = defensorEnt;
        this.callback = callback;
    }

    // Ejecuta la accion encapsulada y coordina las notificaciones
    @Override
    public void ejecutar() {
        // Guarda la vida actual antes de calcular el daño
        int hpAntes = defensor.getHpActual();

        // Ejecuta la logica especifica del ataque mediante polimorfismo
        ataque.atacar(atacante, defensor);

        // Calcula la diferencia de vida para reportarla con precision
        dañofinal = Math.max(0, hpAntes - defensor.getHpActual());

        // Informa a los observadores sobre el ataque realizado
        callback.notificarAtaque(atacante, defensor, ataque.nombre, dañofinal);
        
        // Delega al combate la revision de si el defensor se debilito
        callback.verificarEstadoPokemon(defensorEnt);
    }

    // Devuelve el daño total infligido en esta ejecucion
    public int getDañofinal() {
        return dañofinal;
    }
}