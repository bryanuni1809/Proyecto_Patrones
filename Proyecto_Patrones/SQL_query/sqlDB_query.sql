-- 1. Tabla de tipos (Tú eliges el ID manualmente)
CREATE TABLE tipos (
    id INTEGER PRIMARY KEY,
    nombre TEXT NOT NULL UNIQUE
);

-- 2. Tabla de pokemones (Tú eliges el número manualmente)
CREATE TABLE pokemones (
    numero INTEGER PRIMARY KEY,
    nombre TEXT NOT NULL UNIQUE
);

-- 3. Tabla intermedia de relaciones
CREATE TABLE pokemon_tipos (
    pokemon_numero INTEGER,
    tipo_id INTEGER,
    PRIMARY KEY (pokemon_numero, tipo_id),
    FOREIGN KEY (pokemon_numero) REFERENCES pokemones(numero) ON DELETE CASCADE,
    FOREIGN KEY (tipo_id) REFERENCES tipos(id) ON DELETE CASCADE
);

INSERT INTO pokemones (numero, nombre) VALUES 
(7, 'Squirtle'),
(10, 'Caterpie'),
(19, 'Rattata'),
(25, 'Pikachu'),
(43, 'Oddish'),
(50, 'Diglett'),
(58, 'Growlithe'),
(63, 'Abra'),
(66, 'Machop'),
(88, 'Grimer'),
(147, 'Dratini'),
(152, 'Chikorita'),
(200, 'Misdreavus'),
(403, 'Shinx'),
(524, 'Roggenrola');

INSERT INTO tipos (id, nombre) VALUES 
(1, 'Normal'),
(2, 'Fuego'),
(3, 'Agua'),
(4, 'Planta'),
(5, 'Electrico'),
(6, 'Hielo'),
(7, 'Lucha'),
(8, 'Veneno'),
(9, 'Tierra'),
(10, 'Volador'),
(11, 'Psiquico'),
(12, 'Bicho'),
(13, 'Roca'),
(14, 'Fantasma'),
(15, 'Dragon');

INSERT INTO pokemon_tipos (pokemon_numero, tipo_id) VALUES 
(25, 5),   -- Pikachu (Eléctrico)
(7, 3),    -- Squirtle (Agua)
(43, 4),   -- Oddish (Planta)
(58, 2),   -- Growlithe (Fuego)
(19, 1),   -- Rattata (Normal)
(66, 7),   -- Machop (Lucha)
(88, 8),   -- Grimer (Veneno)
(50, 9),   -- Diglett (Tierra)
(63, 11),  -- Abra (Psíquico)
(10, 12),  -- Caterpie (Bicho)
(147, 15), -- Dratini (Dragón)
(403, 5),  -- Shinx (Eléctrico)
(152, 4),  -- Chikorita (Planta)
(200, 14), -- Misdreavus (Fantasma)
(524, 13); -- Roggenrola (Roca)


CREATE USER user_pokemon WITH PASSWORD 'T#9vQ!2mL@7xR$4kZ&8pN^5wC*1jY';

GRANT CONNECT ON DATABASE Pokemon_db TO user_pokemon;

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO user_pokemon;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO user_pokemon;

ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO user_pokemon;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO user_pokemon;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON PROCEDURES TO user_pokemon;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TRIGGER FUNCTIONS TO user_pokemon;

-- Funcion para obtener un pokemon específico por su ID
CREATE OR REPLACE FUNCTION obtener_pokemon_por_id(p_id_buscado INTEGER)
RETURNS TABLE(
    id INTEGER,
    nombre VARCHAR,
    tipo TEXT,
    defensa INTEGER,
    ataque INTEGER,
    vida INTEGER
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        pk.id,
        pk.nombre,
        t.nombre AS tipo,
        pk.defensa,
        pk.ataque,
        pk.vida
    FROM pokemones pk
    INNER JOIN tipos t ON pk.tipo_id = t.id
    WHERE pk.id = p_id_buscado;
END;
$$ LANGUAGE plpgsql;
