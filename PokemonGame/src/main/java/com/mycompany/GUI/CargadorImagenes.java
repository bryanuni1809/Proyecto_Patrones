package com.mycompany.GUI;

import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CargadorImagenes {
    private static final Map<String, BufferedImage> cacheImagenes = new HashMap<>();
    private static Font fuentePokemon = null;
    
    // ═══════════════════════════════════════════════════════════════
    // FONDOS Y ELEMENTOS DE POKEDEX
    // ═══════════════════════════════════════════════════════════════
    
    /**
     * Carga un fondo de pokedex (sprite-X-Y.png)
     */
    public static ImageIcon cargarFondoPokedex(String nombreArchivo, int ancho, int alto) {
        return cargarImagen("/img/pokedex/" + nombreArchivo, ancho, alto);
    }
    
    /**
     * Carga un sprite de Pokémon individual desde la carpeta pokemon/front/
     * @param numeroPokedex ej: 1 para Bulbasaur, 25 para Pikachu
     */
    public static ImageIcon cargarSpritePokemon(int numeroPokedex, int ancho, int alto) {
        return cargarImagen("/img/pokemon/front/" + numeroPokedex + ".png", ancho, alto);
    }
    
    /**
     * Carga un sprite de espaldas (para el jugador en combate)
     */
    public static ImageIcon cargarSpriteEspalda(int numeroPokedex, int ancho, int alto) {
        return cargarImagen("/img/pokemon/back/" + numeroPokedex + ".png", ancho, alto);
    }
    
    // ═══════════════════════════════════════════════════════════════
    // FUENTE PIXELADA
    // ══════════════════════════════════════════════════════════════
    
    /**
     * Obtiene la fuente Pokémon pixelada (Gen 1 style)
     */
    public static Font getFuentePokemon(float tamaño) {
        if (fuentePokemon == null) {
            try (InputStream is = CargadorImagenes.class.getResourceAsStream("/fonts/pokemon-generation-1-regular.ttf")) {
                if (is != null) {
                    fuentePokemon = Font.createFont(Font.TRUETYPE_FONT, is);
                    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                    ge.registerFont(fuentePokemon);
                } else {
                    System.err.println("⚠️  Fuente TTF no encontrada");
                    fuentePokemon = new Font("Arial", Font.PLAIN, 12);
                }
            } catch (Exception e) {
                System.err.println("❌ Error cargando fuente: " + e.getMessage());
                fuentePokemon = new Font("Arial", Font.PLAIN, 12);
            }
        }
        return fuentePokemon.deriveFont(tamaño);
    }
    
    // ═══════════════════════════════════════════════════════════════
    // MÉTODOS AUXILIARES
    // ══════════════════════════════════════════════════════════════
    
    private static ImageIcon cargarImagen(String rutaClasspath, int ancho, int alto) {
        URL recurso = CargadorImagenes.class.getResource(rutaClasspath);
        if (recurso == null) {
            System.err.println("⚠️  Imagen no encontrada: " + rutaClasspath);
            return placeholder(ancho, alto);
        }
        ImageIcon icon = new ImageIcon(recurso);
        Image escalada = icon.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(escalada);
    }
    
    private static ImageIcon placeholder(int ancho, int alto) {
        BufferedImage img = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(220, 220, 220));
        g.fillRect(0, 0, ancho, alto);
        g.setColor(Color.GRAY);
        g.drawRect(0, 0, ancho - 1, alto - 1);
        g.drawString("?", ancho/2 - 5, alto/2 + 5);
        g.dispose();
        return new ImageIcon(img);
    }
    
    /**
     * Obtiene el color para cada tipo de Pokémon
     */
    public static Color getColorTipo(String tipo) {
        return switch (tipo.toLowerCase()) {
            case "fuego" -> new Color(240, 128, 48);
            case "agua" -> new Color(72, 152, 224);
            case "planta" -> new Color(120, 200, 80);
            case "electrico", "eléctrico" -> new Color(248, 208, 48);
            case "normal" -> new Color(168, 168, 120);
            case "veneno" -> new Color(160, 64, 160);
            case "tierra" -> new Color(224, 192, 104);
            case "psiquico", "psíquico" -> new Color(248, 88, 136);
            case "roca" -> new Color(184, 160, 56);
            case "bicho" -> new Color(168, 184, 32);
            case "fantasma" -> new Color(112, 88, 152);
            case "hielo" -> new Color(152, 216, 216);
            case "dragon", "dragón" -> new Color(112, 56, 248);
            case "lucha" -> new Color(192, 48, 40);
            case "volador" -> new Color(168, 144, 240);
            default -> Color.GRAY;
        };
    }
}