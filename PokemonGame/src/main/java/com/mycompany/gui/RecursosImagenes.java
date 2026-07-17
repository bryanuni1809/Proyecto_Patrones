package com.mycompany.gui;

import com.mycompany.Model.pokemon.TipoPokemon;

import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;
import javax.swing.ImageIcon;

/**
 * Carga las imágenes del juego (fondo de batalla y sprites de cada Pokémon).
 *
 * CONVENCIÓN DE ARCHIVOS (dentro de src/main/resources): /fondo/escenario.png →
 * fondo de la batalla /sprites/<nombre>_normal.png → Pokémon en reposo
 * /sprites/<nombre>_ataque.png → Pokémon atacando /sprites/<nombre>_dano.png →
 * Pokémon recibiendo daño
 *
 * <nombre> = nombre del Pokémon en minúsculas y sin espacios/acentos (ej:
 * "pikachu_normal.png", "pikachu_ataque.png", "pikachu_dano.png").
 *
 * Mientras no exista el archivo real, esta clase genera automáticamente una
 * imagen de referencia (placeholder) para que la interfaz sea jugable de
 * inmediato. En cuanto coloques el PNG correspondiente con el nombre indicado,
 * se usará solo — no hay que tocar nada de código.
 */
public final class RecursosImagenes {

    private RecursosImagenes() {
    }

    public enum EstadoSprite {
        NORMAL("normal"),
        ATAQUE("ataque"),
        DANIO("dano");

        final String sufijo;

        EstadoSprite(String sufijo) {
            this.sufijo = sufijo;
        }
    }

    private static final Map<String, BufferedImage> CACHE = new HashMap<>();
    private static final Map<String, ImageIcon> CACHE_ICONOS = new HashMap<>();
    private static Font fuentePokemon = null;

    private static final Map<TipoPokemon, Color> COLOR_TIPO = new EnumMap<>(TipoPokemon.class);

    static {
        COLOR_TIPO.put(TipoPokemon.NORMAL, new Color(168, 168, 120));
        COLOR_TIPO.put(TipoPokemon.FUEGO, new Color(240, 128, 48));
        COLOR_TIPO.put(TipoPokemon.AGUA, new Color(104, 144, 240));
        COLOR_TIPO.put(TipoPokemon.ELECTRICO, new Color(248, 208, 48));
        COLOR_TIPO.put(TipoPokemon.PLANTA, new Color(120, 200, 80));
        COLOR_TIPO.put(TipoPokemon.HIELO, new Color(152, 216, 216));
        COLOR_TIPO.put(TipoPokemon.LUCHA, new Color(192, 48, 40));
        COLOR_TIPO.put(TipoPokemon.VENENO, new Color(160, 64, 160));
        COLOR_TIPO.put(TipoPokemon.TIERRA, new Color(224, 192, 104));
        COLOR_TIPO.put(TipoPokemon.VOLADOR, new Color(168, 144, 240));
        COLOR_TIPO.put(TipoPokemon.PSIQUICO, new Color(248, 88, 136));
        COLOR_TIPO.put(TipoPokemon.BICHO, new Color(168, 184, 32));
        COLOR_TIPO.put(TipoPokemon.ROCA, new Color(184, 160, 56));
        COLOR_TIPO.put(TipoPokemon.FANTASMA, new Color(112, 88, 152));
        COLOR_TIPO.put(TipoPokemon.DRAGON, new Color(112, 56, 248));
    }

    /**
     * Obtiene la fuente pixelada estilo Pokémon.
     */
    public static Font getFuentePokemon(float tamaño) {
        if (fuentePokemon == null) {
            try (InputStream is = RecursosImagenes.class.getResourceAsStream("/fonts/pokemon-generation-1-regular.ttf")) {
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

    /**
     * Color clásico asociado a cada tipo de Pokémon (para bordes/etiquetas).
     */
    public static Color colorTipo(TipoPokemon tipo) {
        return COLOR_TIPO.getOrDefault(tipo, new Color(120, 120, 120));
    }
    private static final int LADO_SPRITE = 150;

    /**
     * Color de texto legible sobre el color de tipo. NUEVO: Adaptado de
     * CargadorImagenes pero usando el enum
     */
    public static Color colorTextoSobreTipo(TipoPokemon tipo) {
        Color c = colorTipo(tipo);
        double luminancia = (0.299 * c.getRed() + 0.587 * c.getGreen() + 0.114 * c.getBlue()) / 255.0;
        return luminancia > 0.6 ? new Color(40, 40, 40) : Color.WHITE;
    }

    public static BufferedImage fondo() {
        return cargar("/fondo/escenario.png", RecursosImagenes::generarFondoPlaceholder);
    }

    public static BufferedImage fondoSeleccion() {
        return cargar("/fondo/seleccion.png", RecursosImagenes::generarFondoPlaceholder);
    }

    public static BufferedImage fondoPanelInferior() {
        return cargar("/fondo/panel_inferior.png", RecursosImagenes::generarFondoPanelInferiorPlaceholder);
    }

    /**
     * Marco/fondo de cada tarjeta de Pokémon en la pantalla de selección. Poner
     * en resources: /fondo/tarjeta_normal.png y /fondo/tarjeta_seleccionada.png
     * (se estiran al tamaño de la tarjeta, así que conviene un PNG con
     * proporción vertical, ej. 200x260, y fondo transparente si se quiere ver
     * el fondo de la pantalla alrededor).
     */
    public static BufferedImage tarjetaSeleccion(boolean seleccionada) {
        String archivo = seleccionada ? "/fondo/tarjeta_seleccionada.png" : "/fondo/tarjeta_normal.png";
        return cargar(archivo, () -> generarTarjetaPlaceholder(seleccionada));
    }

    /**
     * Fondo del botón "Comenzar batalla". Poner en resources:
     * /fondo/boton_comenzar.png (se estira al tamaño del botón).
     */
    public static BufferedImage botonComenzar() {
        return cargar("/fondo/boton_comenzar.png", RecursosImagenes::generarBotonPlaceholder);
    }

    /**
     * Icono de un ítem de la mochila (pociones, etc). Poner en resources:
     * /iconos/<nombre>.png (mismo normalizado que los sprites: minúsculas, sin
     * espacios ni acentos). Ej: para "Súper Poción" el archivo sería
     * /iconos/superpocion.png.
     */
    public static BufferedImage iconoItem(String nombreItem) {
        String archivo = "/iconos/" + normalizar(nombreItem) + ".png";
        return cargar(archivo, () -> generarIconoItemPlaceholder(nombreItem));
    }

    /**
     * Placa/fondo detrás del nombre y la barra de HP de cada Pokémon en
     * batalla. Poner en resources: /fondo/marco_info.png (se estira al tamaño
     * de la placa, sugerido ~236x76 o proporción similar).
     */
    public static BufferedImage marcoInfo() {
        return cargar("/fondo/marco_info.png", RecursosImagenes::generarMarcoInfoPlaceholder);
    }

    /**
     * Fondo decorativo de la barra de HP (se dibuja detrás del relleno de color
     * verde/amarillo/rojo). Poner en resources: /fondo/barra_vida.png
     */
    public static BufferedImage barraVidaFondo() {
        return cargar("/fondo/barra_vida.png", RecursosImagenes::generarBarraVidaPlaceholder);
    }

    /**
     * Plataforma/sombra que se dibuja bajo el sprite de cada Pokémon. Poner en
     * resources: /fondo/plataforma.png
     */
    public static BufferedImage plataforma() {
        return cargar("/fondo/plataforma.png", RecursosImagenes::generarPlataformaPlaceholder);
    }

    /**
     * Carga sprite frontal de Pokémon para Pokedex Usa:
     * /pokemon/{nombre}.front.png
     */
    public static BufferedImage spritePokemon(String nombrePokemon, boolean frontal) {
        String n = normalizar(nombrePokemon);
        String archivo = "/sprites/rival/" + n + "/" + n + "_normal.png";
        return cargar(archivo, () -> generarSpritePlaceholder(nombrePokemon));
    }

    public static BufferedImage spriteJugador(String nombrePokemon, EstadoSprite estado) {
        String n = normalizar(nombrePokemon);
        String archivo = "/sprites/jugador/" + n + "/" + n + "_" + estado.sufijo + ".png";
        return cargar(archivo, () -> generarSpritePlaceholder(nombrePokemon));
    }

    public static BufferedImage spriteRival(String nombrePokemon, EstadoSprite estado) {
        String n = normalizar(nombrePokemon);
        String archivo = "/sprites/rival/" + n + "/" + n + "_" + estado.sufijo + ".png";
        return cargar(archivo, () -> generarSpritePlaceholder(nombrePokemon));
    }

    // ═══════════════════════════════════════════════════════════════
    // ICONOS (de CargadorImagenes 
    // ═══════════════════════════════════════════════════════════════
    /**
     * Icono de pokebola.
     */
    /**
     * Carga y escala el sprite del Pokémon a un ImageIcon del tamaño
     * especificado. Ideal para usar directamente en JLabels de la Pokedex.
     */
    public static ImageIcon spritePokemonIcon(String nombrePokemon, int ancho, int alto) {
        BufferedImage img = spritePokemon(nombrePokemon, true);
        if (img == null) {
            return null;
        }
        BufferedImage escalada = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = escalada.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2.drawImage(img, 0, 0, ancho, alto, null);
        g2.dispose();
        return new ImageIcon(escalada);
    }

    public static ImageIcon cargarIconoPokebola(int tamaño) {
        String clave = "pokebola-" + tamaño;
        return CACHE_ICONOS.computeIfAbsent(clave, k -> {
            ImageIcon icon = cargarImagen("/pokedex/pokebola-icon.png", tamaño, tamaño);
            return icon;
        });
    }

    public static ImageIcon cargarIconoBatalla(int tamaño) {
        String clave = "batalla-" + tamaño;
        return CACHE_ICONOS.computeIfAbsent(clave, k -> {
            ImageIcon icon = cargarImagen("/pokedex/battle-icon.png", tamaño, tamaño);
            return icon;
        });
    }

    public static ImageIcon cargarIconoPokedex(int tamaño) {
        String clave = "pokedex-" + tamaño;
        return CACHE_ICONOS.computeIfAbsent(clave, k -> {
            ImageIcon icon = cargarImagen("/pokedex/game-icon.png", tamaño, tamaño);
            return icon;
        });
    }

    /**
     * Badge de tipo.
     */
    public static ImageIcon cargarBadgeTipo(String tipo, int ancho, int alto) {
        String clave = "badge-" + tipo + "-" + ancho + "x" + alto;
        return CACHE_ICONOS.computeIfAbsent(clave, k -> {
            String archivo = "/pokedex/" + tipo.toLowerCase() + "-icon.png";
            ImageIcon icon = cargarImagen(archivo, ancho, alto);
            return icon;
        });
    }

    private static String normalizar(String nombre) {
        String sinAcentos = java.text.Normalizer.normalize(nombre, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", ""); // quita los acentos (tildes, diéresis) dejando la letra base
        return sinAcentos.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
    }

    private static BufferedImage cargar(String ruta, Supplier<BufferedImage> respaldo) {
        return CACHE.computeIfAbsent(ruta, r -> {
            try (InputStream in = RecursosImagenes.class.getResourceAsStream(r)) {
                if (in != null) {
                    BufferedImage img = ImageIO.read(in);
                    if (img != null) {
                        return img;
                    }
                }
            } catch (IOException ignored) {
                // Si el archivo no existe o está corrupto, se usa el placeholder.
            }
            return respaldo.get();
        });
    }

    private static ImageIcon cargarImagen(String ruta, int ancho, int alto) {
        try (InputStream in = RecursosImagenes.class.getResourceAsStream(ruta)) {
            if (in != null) {
                BufferedImage original = ImageIO.read(in);
                if (original != null) {
                    BufferedImage escalada = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2 = escalada.createGraphics();
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                    g2.drawImage(original, 0, 0, ancho, alto, null);
                    g2.dispose();
                    return new ImageIcon(escalada);
                }
            }
        } catch (IOException ignored) {
        }
        return null;
    }

    // ── Generadores de imágenes de referencia (placeholders) ──────────────
    private static BufferedImage generarFondoPlaceholder() {
        int w = 700, h = 420;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        GradientPaint cielo = new GradientPaint(
                0, 0, new Color(120, 190, 235),
                0, h * 0.65f, new Color(205, 232, 246));
        g.setPaint(cielo);
        g.fillRect(0, 0, w, (int) (h * 0.65));

        g.setColor(new Color(150, 205, 130));
        g.fillRect(0, (int) (h * 0.62), w, (int) (h * 0.38));
        g.setColor(new Color(128, 190, 112));
        g.fillOval(-60, (int) (h * 0.60), 380, 90);
        g.fillOval(w - 340, (int) (h * 0.66), 420, 100);

        g.dispose();
        return img;
    }

    private static BufferedImage generarFondoPanelInferiorPlaceholder() {
        int w = 700, h = 160;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        GradientPaint degradado = new GradientPaint(
                0, 0, new Color(40, 40, 55),
                0, h, new Color(22, 22, 32));
        g.setPaint(degradado);
        g.fillRect(0, 0, w, h);

        g.dispose();
        return img;
    }

    private static BufferedImage generarTarjetaPlaceholder(boolean seleccionada) {
        int w = 200, h = 260;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color relleno = seleccionada ? new Color(255, 221, 89, 235) : new Color(255, 255, 255, 165);
        Color borde = seleccionada ? new Color(190, 150, 20) : new Color(80, 80, 90, 160);

        g.setColor(relleno);
        g.fillRoundRect(1, 1, w - 2, h - 2, 20, 20);
        g.setColor(borde);
        g.setStroke(new BasicStroke(seleccionada ? 3f : 1.5f));
        g.drawRoundRect(1, 1, w - 3, h - 3, 20, 20);

        g.dispose();
        return img;
    }

    private static BufferedImage generarBotonPlaceholder() {
        int w = 260, h = 54;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        GradientPaint degradado = new GradientPaint(0, 0, new Color(90, 170, 90), 0, h, new Color(60, 130, 60));
        g.setPaint(degradado);
        g.fillRoundRect(1, 1, w - 2, h - 2, 16, 16);
        g.setColor(new Color(40, 90, 40));
        g.setStroke(new BasicStroke(2f));
        g.drawRoundRect(1, 1, w - 3, h - 3, 16, 16);

        g.dispose();
        return img;
    }

    private static BufferedImage generarIconoItemPlaceholder(String nombre) {
        int lado = 40;
        BufferedImage img = new BufferedImage(lado, lado, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Cuerpo de "frasco" simple, coloreado según el nombre del ítem.
        Color color = colorParaNombre(nombre);
        g.setColor(color);
        g.fillRoundRect(8, 12, 24, 24, 10, 10);
        g.setColor(color.darker());
        g.setStroke(new BasicStroke(2f));
        g.drawRoundRect(8, 12, 24, 24, 10, 10);
        g.fillRect(16, 4, 8, 10);
        g.setColor(Color.WHITE);
        g.fillRect(11, 20, 18, 4);

        g.dispose();
        return img;
    }

    private static BufferedImage generarMarcoInfoPlaceholder() {
        int w = 236, h = 76;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(new Color(255, 255, 255, 230));
        g.fillRoundRect(0, 0, w - 1, h - 1, 16, 16);
        g.setColor(new Color(60, 60, 70));
        g.setStroke(new BasicStroke(2f));
        g.drawRoundRect(0, 0, w - 1, h - 1, 16, 16);

        g.dispose();
        return img;
    }

    private static BufferedImage generarBarraVidaPlaceholder() {
        int w = 208, h = 14;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(new Color(210, 210, 210));
        g.fillRoundRect(0, 0, w - 1, h - 1, 8, 8);
        g.setColor(new Color(90, 90, 90));
        g.drawRoundRect(0, 0, w - 1, h - 1, 8, 8);

        g.dispose();
        return img;
    }

    private static BufferedImage generarPlataformaPlaceholder() {
        int w = 180, h = 50;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(new Color(0, 0, 0, 55));
        g.fillOval(0, 10, w, h - 15);

        g.dispose();
        return img;
    }

    private static BufferedImage generarSpritePlaceholder(String nombre) {
        int lado = 96;
        BufferedImage img = new BufferedImage(lado, lado, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color base = colorParaNombre(nombre);
        int diam = 70;
        int x = (lado - diam) / 2;
        int y = (lado - diam) / 2;

        g.setColor(new Color(0, 0, 0, 40));
        g.fill(new Ellipse2D.Double(x - 6, y + diam - 14, diam + 12, 26));
        g.setColor(base);
        g.fill(new Ellipse2D.Double(x, y, diam, diam));
        g.setColor(base.darker());
        g.setStroke(new BasicStroke(3f));
        g.draw(new Ellipse2D.Double(x, y, diam, diam));

        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 32));
        String letra = nombre.isEmpty() ? "?" : nombre.substring(0, 1).toUpperCase(Locale.ROOT);
        FontMetrics fm = g.getFontMetrics();
        int tx = x + (diam - fm.stringWidth(letra)) / 2;
        int ty = y + (diam + fm.getAscent()) / 2 - 6;
        g.drawString(letra, tx, ty);

        g.dispose();
        return img;
    }

    private static Color colorParaNombre(String nombre) {
        int hash = Math.abs(nombre.toLowerCase(Locale.ROOT).hashCode());
        float hue = (hash % 360) / 360f;
        return Color.getHSBColor(hue, 0.55f, 0.85f);
    }
}
