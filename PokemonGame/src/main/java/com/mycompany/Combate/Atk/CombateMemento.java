package com.mycompany.Combate.Atk;

import com.mycompany.Model.entrenador.Entrenador;
import java.util.List;

public final class CombateMemento {
    private final int turnoGeneral;
    private final List<Entrenador> entrenadoresClonados;

    public CombateMemento(int turnoGeneral, List<Entrenador> entrenadoresClonados) {
        this.turnoGeneral = turnoGeneral;
        this.entrenadoresClonados = entrenadoresClonados;
    }

    public int getTurnoGeneral() {
        return turnoGeneral;
    }

    public List<Entrenador> getEntrenadoresClonados() {
        return entrenadoresClonados;
    }
}
