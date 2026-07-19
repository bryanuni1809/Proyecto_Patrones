package com.mycompany.Combate.Atk;
import com.mycompany.Model.pokemon.*;
public class Calculadora {
    private static Calculadora instancia;

    private Calculadora() {
    }

    public static Calculadora getInstancia() {
        if (instancia == null) {
            instancia = new Calculadora();
        }
        return instancia;
    }
    // Ataque físico (sin tipos)
    public int calcularDanioFisico(Pokemon atacante, Pokemon defensor, int potencia) {

        int danio = (atacante.getAtaque() * potencia) / defensor.getDefensa();

        return Math.max(danio, 1);
    }

    // Ataque especial (con tipos)
    public int calcularDanioEspecial(Pokemon atacante, Pokemon defensor, int potencia, TipoPokemon tipoAtaque, TablaTipos tabla) {

        double multiplicador = tabla.getMultiplicador(
                tipoAtaque,
                defensor.getTipo()
        );

        int danio = (int) (((atacante.getAtaque() * potencia)
                / defensor.getDefensa()) * multiplicador);

        return Math.max(danio, 1);
    }
}
