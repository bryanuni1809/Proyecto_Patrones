package com.mycompany.Combate.Atk;

import com.mycompany.Model.entrenador.Entrenador;
import java.util.List;

// Clase inmutable que guarda el estado del combate para el patrón Memento
public final class CombateMemento {
    
    // Almacena el número del turno general en el momento del guardado
    private final int turnoGeneral;
    
    // Guarda copias de los entrenadores para restaurar el estado de forma segura
    private final List<Entrenador> entrenadoresClonados;

    // Crea el memento capturando el estado actual del combate
    public CombateMemento(int turnoGeneral, List<Entrenador> entrenadoresClonados) {
        this.turnoGeneral = turnoGeneral;
        this.entrenadoresClonados = entrenadoresClonados;
    }

    // Devuelve el turno general guardado
    public int getTurnoGeneral() {
        return turnoGeneral;
    }

    // Devuelve la lista de entrenadores clonados para su posterior restauración
    public List<Entrenador> getEntrenadoresClonados() {
        return entrenadoresClonados;
    }
}