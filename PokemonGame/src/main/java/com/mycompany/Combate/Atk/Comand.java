package com.mycompany.Combate.Atk;

// Interfaz base del patrón Comando para encapsular una acción o petición
interface Comand {
    
    // Ejecuta la acción específica que implemente cada comando concreto
    void ejecutar();
}