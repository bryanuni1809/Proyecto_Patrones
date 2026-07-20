package com.mycompany.gui;

import com.mycompany.Facade.PokemonGameFacade;
import javax.swing.*;
import java.awt.*;

// Ventana principal que organiza las vistas del juego usando un sistema de pestanas
public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        // Objeto Fachada que centraliza y simplifica el acceso a la logica del juego
        PokemonGameFacade facade = new PokemonGameFacade();
        
        // Define las propiedades basicas de la ventana como tamaño, posicion e icono
        setTitle("Pokemon Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 720);
        setMinimumSize(new Dimension(760, 560));
        setLocationRelativeTo(null);
        setIconImage(RecursosImagenes.cargarIconoPokebola(32).getImage());

        // Panel de pestanas que permite navegar entre los diferentes modulos
        JTabbedPane pestanas = new JTabbedPane();
        pestanas.setFont(RecursosImagenes.getFuentePokemon(13f));
        pestanas.setBackground(new Color(250, 244, 234));
        
        // Agrega la vista de la Pokedex y la vista del sistema de batalla
        pestanas.addTab("Pokedex", RecursosImagenes.cargarIconoPokedex(16), new PanelPokedex(facade));
        pestanas.addTab("Batalla", RecursosImagenes.cargarIconoBatalla(16), new Sistema_Batalla());

        // Añade el panel de pestanas al marco principal de la ventana
        add(pestanas);
    }

    public static void main(String[] args) {
        // Configura la renderizacion de fuentes para asegurar una visualizacion nitida
        RecursosImagenes.configurarRenderizadoFuente();

        // Lanza la interfaz en el hilo seguro de Swing para evitar bloqueos en la aplicacion
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}