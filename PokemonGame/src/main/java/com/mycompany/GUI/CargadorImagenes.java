package com.mycompany.GUI;

import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Cargador de recursos gráficos para el juego Pokémon
 * Maneja: sprites, fondos, fuentes y colores de tipos
 *
 * MEJORAS:
 *  - Cache real (antes existía el mapa pero nunca se usaba)
 *  - Escalado "pixel perfect" (nearest neighbor) para sprites y badges,
 *    para que no se vean borrosos como con SCALE_SMOOTH
 *  - Icono de pokebola vectorial de respaldo si no hay imagen en el classpath
 */
public class CargadorImagenes {

    private static final Map<String, ImageIcon> cacheImagenes = new HashMap<>();
    private static Font fuentePokemon = null;

    // ═══════════════════════════════════════════════════════════════
    // FONDOS Y ELEMENTOS DE POKEDEX
    // ═══════════════════════════════════════════════════════════════

    public static ImageIcon cargarFondoPokedex(String nombreArchivo, int ancho, int alto) {
        return cargarImagen("/img/pokedex/" + nombreArchivo, ancho, alto, false);
    }

    /** Sprite de Pokémon (pixel art -> escalado nearest neighbor) */
    public static ImageIcon cargarSpritePokemon(int numeroPokedex, int ancho, int alto) {
        return cargarImagen("/img/pokemon/front/" + numeroPokedex + ".png", ancho, alto, true);
    }

    public static ImageIcon cargarSpriteEspalda(int numeroPokedex, int ancho, int alto) {
        return cargarImagen("/img/pokemon/back/" + numeroPokedex + ".png", ancho, alto, true);
    }

    /**
     * Icono de pokebola para decorar encabezados. Si no existe el archivo
     * "/img/pokedex/pokebola-icon.png" se dibuja una pokebola vectorial simple.
     */
    public static ImageIcon cargarIconoPokebola(int tamaño) {
        String clave = "pokebola-" + tamaño;
        if (cacheImagenes.containsKey(clave)) {
            return cacheImagenes.get(clave);
        }
        URL recurso = CargadorImagenes.class.getResource("/img/pokedex/pokebola-icon.png");
        ImageIcon icon;
        if (recurso != null) {
            icon = cargarImagen("/img/pokedex/pokebola-icon.png", tamaño, tamaño, true);
        } else {
            icon = new ImageIcon(dibujarPokebola(tamaño));
        }
        cacheImagenes.put(clave, icon);
        return icon;
    }

    private static BufferedImage dibujarPokebola(int tamaño) {
        BufferedImage img = new BufferedImage(tamaño, tamaño, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int pad = Math.max(1, tamaño / 12);
        int d = tamaño - pad * 2;

        g.setColor(new Color(216, 48, 48));
        g.fillArc(pad, pad, d, d, 0, 180);
        g.setColor(Color.WHITE);
        g.fillArc(pad, pad, d, d, 180, 180);
        g.setColor(new Color(30, 30, 30));
        g.setStroke(new BasicStroke(Math.max(1f, tamaño / 16f)));
        g.drawOval(pad, pad, d, d);
        g.drawLine(pad, tamaño / 2, tamaño - pad, tamaño / 2);

        int botonD = Math.max(3, tamaño / 3);
        int bx = (tamaño - botonD) / 2;
        int by = (tamaño - botonD) / 2;
        g.setColor(Color.WHITE);
        g.fillOval(bx, by, botonD, botonD);
        g.setColor(new Color(30, 30, 30));
        g.drawOval(bx, by, botonD, botonD);
        int nucleo = Math.max(2, botonD / 2);
        g.setColor(new Color(230, 230, 230));
        g.fillOval(bx + (botonD - nucleo) / 2, by + (botonD - nucleo) / 2, nucleo, nucleo);

        g.dispose();
        return img;
    }

    // ═══════════════════════════════════════════════════════════════
    // FUENTE PIXELADA ESTILO GAME BOY
    // ═══════════════════════════════════════════════════════════════

    public static Font getFuentePokemon(float tamaño) {
        if (fuentePokemon == null) {
            try (InputStream is = CargadorImagenes.class.getResourceAsStream("/fonts/pokemon-generation-1-regular.ttf")) {
                if (is != null) {
                    fuentePokemon = Font.createFont(Font.TRUETYPE_FONT, is);
                    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                    ge.registerFont(fuentePokemon);
                } else {
                    System.err.println("Fuente TTF no encontrada, usando Arial");
                    fuentePokemon = new Font("Arial", Font.PLAIN, 12);
                }
            } catch (Exception e) {
                System.err.println("Error cargando fuente: " + e.getMessage());
                fuentePokemon = new Font("Arial", Font.PLAIN, 12);
            }
        }
        return fuentePokemon.deriveFont(tamaño);
    }

    public static void configurarRenderizadoFuente() {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
    }

    // ═══════════════════════════════════════════════════════════════
    // MÉTODOS AUXILIARES
    // ═══════════════════════════════════════════════════════════════

    /**
     * @param pixelArt true = usa nearest-neighbor (sprites/badges, nítido y
     *                 "crocante"); false = usa un escalado suave (fondos/fotos).
     */
    private static ImageIcon cargarImagen(String rutaClasspath, int ancho, int alto, boolean pixelArt) {
        String clave = rutaClasspath + "|" + ancho + "x" + alto + "|" + pixelArt;
        ImageIcon cacheada = cacheImagenes.get(clave);
        if (cacheada != null) {
            return cacheada;
        }

        URL recurso = CargadorImagenes.class.getResource(rutaClasspath);
        if (recurso == null) {
            System.err.println("Imagen no encontrada: " + rutaClasspath);
            ImageIcon ph = placeholder(ancho, alto);
            cacheImagenes.put(clave, ph);
            return ph;
        }

        ImageIcon original = new ImageIcon(recurso);
        BufferedImage escalada = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = escalada.createGraphics();

        if (pixelArt) {
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        } else {
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        }

        g2.drawImage(original.getImage(), 0, 0, ancho, alto, null);
        g2.dispose();

        ImageIcon resultado = new ImageIcon(escalada);
        cacheImagenes.put(clave, resultado);
        return resultado;
    }

    private static ImageIcon placeholder(int ancho, int alto) {
        BufferedImage img = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(new Color(224, 224, 216));
        g.fillRoundRect(0, 0, ancho, alto, 8, 8);
        g.setColor(new Color(150, 150, 140));
        g.drawRoundRect(0, 0, ancho - 1, alto - 1, 8, 8);
        g.setFont(new Font("Arial", Font.BOLD, Math.max(10, ancho / 6)));
        FontMetrics fm = g.getFontMetrics();
        String txt = "?";
        int tx = (ancho - fm.stringWidth(txt)) / 2;
        int ty = (alto + fm.getAscent()) / 2 - 4;
        g.setColor(new Color(120, 120, 110));
        g.drawString(txt, tx, ty);
        g.dispose();

        return new ImageIcon(img);
    }

    /**
     * Colores oficiales por tipo (paleta Gen 1)
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

    /** Color de texto legible sobre el color de tipo (blanco u oscuro según el brillo) */
    public static Color getColorTextoSobreTipo(String tipo) {
        Color c = getColorTipo(tipo);
        double luminancia = (0.299 * c.getRed() + 0.587 * c.getGreen() + 0.114 * c.getBlue()) / 255.0;
        return luminancia > 0.6 ? new Color(40, 40, 40) : Color.WHITE;
    }

    public static ImageIcon cargarBadgeTipo(String tipo, int ancho, int alto) {
        String archivo = "/img/pokedex/" + tipo.toLowerCase() + "-icon.png";
        return cargarImagen(archivo, ancho, alto, true);
    }
}