# ⚡ PokemonGame — Batallas por turnos con Patrones de Diseño

> Un simulador de batallas Pokémon hecho en Java puro, construido como laboratorio vivo de los patrones de diseño GoF. Cada mecánica del juego (ataques, estados, mochila, pokedex, combate) es la excusa para aplicar un patrón real, no de juguete.

![Java](https://img.shields.io/badge/Java-25-orange?logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/DB-PostgreSQL-4169E1?logo=postgresql&logoColor=white)
![Swing](https://img.shields.io/badge/UI-Java%20Swing-blue)

---

## 🎮 ¿Qué es esto?

Eliges tu equipo de Pokémon desde una Pokédex respaldada en base de datos, entras a una arena por turnos contra un rival, atacas, aplicas estados alterados, curas con pociones y sigues el combate en vivo con una bitácora que reacciona a cada evento. Por debajo, cada pieza del sistema está resuelta con un patrón de diseño distinto, documentado en el propio código.

## 🧩 Patrones implementados

| Patrón | Dónde vive | Para qué se usa |
|---|---|---|
| **Singleton** | `Model/pokedex/Pokedex.java` | Un único registro global de Pokémon cargado desde la base de datos. |
| **Builder** | `Patrones/builder/PokemonBuilder.java`, `AtaqueFisico`, `AtaqueEspecial` | Construcción paso a paso de Pokémon y ataques con parámetros opcionales (potencia, efecto de estado, etc). |
| **Prototype** | `Model/pokemon/Pokemon.java` (`clonar()`) | Clona un Pokémon base de la Pokédex para armar la versión de combate sin mutar el original. |
| **Composite** | `Patrones/composite/` (`ItemMochila`, `MochilaGrupo`, `Pocion`) | La mochila del entrenador organiza pociones sueltas o agrupadas bajo la misma interfaz. |
| **State** | `Patrones/state/` (`EstadoPokemon`, `EstadoDormido`, `EstadoParalizado`, `EstadoQuemado`) | Estados alterados que cambian el comportamiento del Pokémon en batalla. |
| **Command** | `Combate/Atk/` (`Comand`, `AtaqueComand`) | Cada ataque se encapsula como un comando ejecutable, desacoplando quién ordena atacar de quién resuelve el daño. |
| **Observer** | `Combate/Atk/` (`CombateObservador`, `ConsolaObservador`, `BitacoraCombate`) | La UI y la consola se enteran de cada ataque, cambio de turno o Pokémon debilitado sin que `Combate` las conozca. |
| **Memento** | `Combate/Atk/CombateMemento.java` | Guarda el estado inicial del combate para poder restaurarlo. |
| **Facade** | `Facade/PokemonGameFacade.java` |  Actúa como el único punto de entrada seguro entre la GUI y el núcleo del sistema. |

## 🗂️ Estructura del proyecto

```
PokemonGame/
├── src/main/java/com/mycompany/
│   ├── Combate/Atk/        # Motor de combate: Command, Observer, Memento
│   ├── Facade/              # Punto único de entrada para la GUI
│   ├── GUI/ & gui/          # Ventanas y paneles Swing
│   ├── Model/               # Pokémon, Entrenador, Pokédex
│   ├── Patrones/            # Builder, Composite, DAO, Prototype, State
│   └── pokemongame/Main.java
├── src/main/resources/      # Sprites, íconos y fondos del juego
├── SQL_query/                # Script de base de datos
└── pom.xml
```

## 🚀 Cómo correrlo

**Requisitos:** JDK 25, Maven y una base de datos PostgreSQL.

```bash
# 1. Clona el repo y entra a la rama de trabajo
git clone https://github.com/bryanuni1809/Proyecto_Patrones.git
cd Proyecto_Patrones
git checkout feature/operaciones-andrea
cd PokemonGame

# 2. Crea la base de datos y carga el esquema
psql -U tu_usuario -d tu_bd -f SQL_query/sqlDB_query.sql

# 3. Configura la conexión en DatabaseConnection.java (usuario/clave/URL)

# 4. Compila y ejecuta
mvn compile exec:java -Dexec.mainClass="com.mycompany.gui.VentanaPrincipal"
```

Al abrir, se carga la Pokédex desde la BD → eliges tu equipo de 3 → el juego arma un rival aleatorio → entras a la ventana de batalla.

## 🕹️ Flujo de una batalla

1. **Selección de equipo** — eliges tus Pokémon desde los disponibles en la Pokédex.
2. **Preparación** — cada Pokémon se clona (*Prototype*) y se le arman ataques (*Builder*).
3. **Turnos** — cada ataque es un comando (*Command*) que calcula daño, aplica tipo y puede inducir un estado alterado (*State*).
4. **Reacción en vivo** — la bitácora y la interfaz se actualizan mediante notificaciones (*Observer*) sin acoplarse al motor de combate.
5. **Recursos** — usas pociones organizadas en tu mochila (*Composite*) para mantener a tu equipo en pie.

## 🌱 Sobre esta rama

`feature/operaciones-andrea` corresponde al desarrollo de las operaciones de combate (ataques, daño, estados y estructura de comandos) dentro del proyecto colaborativo.

##  🟡 Contribuciones

Proyecto académico de práctica de patrones de diseño en equipo. Cada rama `feature/*` corresponde a un integrante o módulo del sistema — revisa el historial de ramas para ver el resto del desarrollo.

---

<p align="center">Hecho con ❤️, café y demasiados <code>switch</code> sobre <code>TipoPokemon</code>.</p>
