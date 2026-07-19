package com.mycompany.Patrones.prototype;

/**
 * Interfaz del patrón Prototype.
 * Permite clonar objetos sin depender de su clase concreta.
 *
 * SOLID → LSP: cualquier clase que implemente Prototype puede sustituirse
 * por otra sin romper el sistema.
 */
public interface Prototype<T> { //se define para clonar en las demas clases
    T clonar();
}
