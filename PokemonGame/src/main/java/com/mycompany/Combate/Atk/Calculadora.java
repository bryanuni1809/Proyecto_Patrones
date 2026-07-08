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

    public int calcularDanio(Pokemon atacante, Pokemon defensor, int poderBase, TablaTipos tabla) {
        double multiplicador = tabla.getMultiplicador(atacante.getTipo(), defensor.getTipo());
        int danio = (int) ((poderBase + atacante.getAtaque() - defensor.getDefensa()) * multiplicador);
        return Math.max(danio, 0); // nunca negativo
    }
}
