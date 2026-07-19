package com.mycompany.Combate.Atk;

import com.mycompany.Model.entrenador.Entrenador;
import com.mycompany.Model.pokemon.Pokemon;

public class AtaqueComand implements Comand {
    private final Ataque ataque;                 // receiver (AtaqueFisico, AtaqueEspecial...)
    private final Pokemon atacante;
    private final Pokemon defensor;
    private final Entrenador defensorEnt;        // para verificar estado del entrenador defensor
    private final CombateCallback callback;      // para notificar y delegar verificaciones
    private int dañofinal;

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

    @Override
    public void ejecutar() {
        int hpAntes = defensor.getHpActual();

        // Llama a la implementación concreta de Ataque (polimorfismo)
        ataque.atacar(atacante, defensor);

        // Calcula daño aplicado
        dañofinal = Math.max(0, hpAntes - defensor.getHpActual());

        // Notifica el ataque y delega la verificación del estado del defensor
        callback.notificarAtaque(atacante, defensor, ataque.nombre, dañofinal);
        callback.verificarEstadoPokemon(defensorEnt);
    }

    public int getDañofinal() {
        return dañofinal;
    }
}
