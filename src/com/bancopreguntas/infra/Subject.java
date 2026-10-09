package com.bancopreguntas.infra;

/**
 * Capa transversal (infra).
 * Interfaz Subject (sujeto observado) del patrón Observer.
 * La implementa cualquier clase de negocio que deba avisar a varias
 * vistas cuando algo relevante cambia (en este taller, QuestionService,
 * cada vez que cambia el estado de una pregunta).
 */
public interface Subject {

    /** Suscribe un observador para que reciba notificaciones futuras. */
    void attach(Observer observer);

    /** Cancela la suscripción de un observador. */
    void detach(Observer observer);

    /** Notifica a todos los observadores suscritos que hay un cambio. */
    void notifyObservers();
}
