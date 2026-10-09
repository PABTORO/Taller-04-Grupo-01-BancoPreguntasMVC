package com.bancopreguntas.infra;

/**
 * Capa transversal (infra).
 * Interfaz Observer del patrón Observer.
 * Toda vista que deba enterarse de cambios en el Subject
 * (por ejemplo, cuando cambia el estado de una pregunta)
 * debe implementar esta interfaz y registrarse en el Subject.
 */
public interface Observer {

    /**
     * Método invocado por el Subject cuando su estado cambia.
     * Cada observador decide qué información volver a consultar
     * y cómo refrescar su propia representación (tabla, gráfica, etc.).
     */
    void update();
}
