package com.mycompany.GUI;

import com.mycompany.Facade.PokemonGameFacade;
import javax.swing.*;
import java.awt.*;

/**
 * Punto de entrada de la interfaz gráfica. Fase 1: solo Pokédex.
 * Las próximas pestañas (Equipo, Mochila, Combate) se agregan aquí sin
 * tocar PanelPokedex ni la Facade existente.
 */
public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        PokemonGameFacade facade = new PokemonGameFacade();
        setTitle("Pokemon Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 720);
        setMinimumSize(new Dimension(760, 560));
        setLocationRelativeTo(null);
        setIconImage(CargadorImagenes.cargarIconoPokebola(32).getImage());

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.setFont(CargadorImagenes.getFuentePokemon(13f));
        pestanas.setBackground(new Color(250, 244, 234));
        pestanas.addTab("Pokedex", CargadorImagenes.cargarIconoPokedex(16), new PanelPokedex(facade));
        pestanas.addTab("Batalla", CargadorImagenes.cargarIconoBatalla(16), new PanelPokedex(facade));
        add(pestanas);
    }

    public static void main(String[] args) {
        // ═══════════════════════════════════════════════════════════════
        // IMPORTANTE: Configurar renderizado de fuente ANTES de crear
        // cualquier componente Swing para que la fuente pixelada se vea
        // nítida y correcta
        // ═══════════════════════════════════════════════════════════════
        CargadorImagenes.configurarRenderizadoFuente();


        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}