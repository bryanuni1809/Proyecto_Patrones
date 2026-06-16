create database Pokemon_db;
use Pokemon_db;
-- Tabla para los tipos de Pokémon (ej: Fuego, Agua, Planta)
CREATE TABLE tipos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

-- Tabla principal de Pokémon
CREATE TABLE pokemones (
    id SERIAL PRIMARY KEY,
    numero_pokedex INT NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    tipo_id INT NOT NULL,
    nivel INT DEFAULT 1 CHECK (nivel > 0),
    hp INT CHECK (hp >= 0),
    ataque INT CHECK (ataque >= 0),
    defensa INT CHECK (defensa >= 0),
    velocidad INT CHECK (velocidad >= 0),
    
    -- Relación con la tabla tipo_pokemon
    CONSTRAINT fk_tipo_pokemon 
        FOREIGN KEY (tipo_id) 
        REFERENCES tipos(id) 
        ON DELETE RESTRICT
);

-- Tipos 
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

INSERT INTO pokemones (numero_pokedex, nombre, tipo_id, nivel, hp, ataque, defensa, velocidad) VALUES 
(7,   'Squirtle',   3,  5, 44, 48, 65, 43),  -- Agua
(10,  'Caterpie',   12, 5, 45, 30, 35, 45),  -- Bicho
(19,  'Rattata',    1,  5, 30, 56, 35, 72),  -- Normal
(25,  'Pikachu',    5,  5, 35, 55, 40, 90),  -- Eléctrico
(43,  'Oddish',     4,  5, 45, 50, 55, 30),  -- Planta
(50,  'Diglett',    9,  5, 10, 55, 25, 95),  -- Tierra
(58,  'Growlithe',  2,  5, 55, 70, 45, 60),  -- Fuego
(63,  'Abra',       11, 5, 25, 20, 15, 90),  -- Psíquico
(66,  'Machop',     7,  5, 70, 80, 50, 35),  -- Lucha
(88,  'Grimer',     8,  5, 80, 80, 50, 25),  -- Veneno
(147, 'Dratini',    15, 5, 41, 64, 45, 50),  -- Dragón
(152, 'Chikorita',  4,  5, 45, 49, 65, 45),  -- Planta
(200, 'Misdreavus', 14, 5, 60, 60, 60, 85),  -- Fantasma
(403, 'Shinx',      5,  5, 45, 65, 34, 45),  -- Eléctrico
(524, 'Roggenrola', 13, 5, 55, 75, 85, 15);   -- Roca


--Usuario para la Base de Datos y permisos
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
    p_id INTEGER,
    p_numero_pokedex INTEGER,
    p_nombre VARCHAR,
    p_tipo VARCHAR,  
    p_nivel INTEGER,
    p_hp INTEGER,
    p_ataque INTEGER,
    p_defensa INTEGER,
    p_velocidad INTEGER
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        pk.id,
        pk.numero_pokedex,
        pk.nombre,
        t.nombre AS tipo,
        pk.nivel,
        pk.hp,
        pk.ataque,
        pk.defensa,
        pk.velocidad
    FROM pokemones pk
    INNER JOIN tipos t ON pk.tipo_id = t.id
    WHERE pk.id = p_id_buscado;
END;
$$ LANGUAGE plpgsql;
