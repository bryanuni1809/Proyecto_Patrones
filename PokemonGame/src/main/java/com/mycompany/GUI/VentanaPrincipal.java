package com.mycompany.GUI;

import com.mycompany.Facade.PokemonGameFacade;

import javax.swing.*;

/**
 * Punto de entrada de la interfaz gráfica. Fase 1: solo Pokédex.
 * Las próximas pestañas (Equipo, Mochila, Combate) se agregan aquí sin
 * tocar PanelPokedex ni la Facade existente.
 */
public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        PokemonGameFacade facade = new PokemonGameFacade();

        setTitle("Pokémon Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 700);
        setLocationRelativeTo(null);

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Pokédex", new PanelPokedex(facade));
        add(pestanas);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}
