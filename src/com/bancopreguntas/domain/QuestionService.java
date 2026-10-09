package com.bancopreguntas.domain;

import com.bancopreguntas.infra.Observer;
import com.bancopreguntas.infra.Subject;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Capa de dominio.
 * Servicio que concentra la lógica de negocio sobre Question y actúa
 * como Subject del patrón Observer: cada vez que cambia el estado de
 * una pregunta, notifica a las vistas suscritas (GUIObserver1 y
 * GUIObserver2) para que se refresquen.
 */
public class QuestionService implements Subject {

    private final QuestionRepository repository;
    private final List<Observer> observers = new ArrayList<>();

    public QuestionService(QuestionRepository repository) {
        this.repository = repository;
    }

    public List<Question> listarPreguntas() {
        return repository.findAll();
    }

    public Question buscarPorId(String id) {
        return repository.findById(id);
    }

    /**
     * Cambia el estado de una pregunta y notifica a los observadores.
     * Este es el único punto de entrada para modificar el estado,
     * de modo que ninguna vista pueda cambiarlo sin que las demás
     * se enteren.
     */
    public void cambiarEstado(String id, EstadoPregunta nuevoEstado) {
        Question pregunta = repository.findById(id);
        if (pregunta == null) {
            throw new IllegalArgumentException("No existe una pregunta con id " + id);
        }
        pregunta.setEstado(nuevoEstado);
        repository.update(pregunta);
        notifyObservers();
    }

    /** Cuenta cuántas preguntas hay en cada estado (para la vista de estadísticas). */
    public Map<EstadoPregunta, Integer> contarPorEstado() {
        Map<EstadoPregunta, Integer> conteo = new EnumMap<>(EstadoPregunta.class);
        for (EstadoPregunta estado : EstadoPregunta.values()) {
            conteo.put(estado, 0);
        }
        for (Question pregunta : repository.findAll()) {
            conteo.merge(pregunta.getEstado(), 1, Integer::sum);
        }
        return conteo;
    }

    /** Calcula el porcentaje de preguntas en cada estado (para la vista gráfica). */
    public Map<EstadoPregunta, Double> calcularPorcentajePorEstado() {
        Map<EstadoPregunta, Integer> conteo = contarPorEstado();
        int total = repository.findAll().size();
        Map<EstadoPregunta, Double> porcentajes = new EnumMap<>(EstadoPregunta.class);
        for (Map.Entry<EstadoPregunta, Integer> entrada : conteo.entrySet()) {
            double porcentaje = total == 0 ? 0.0 : (entrada.getValue() * 100.0) / total;
            porcentajes.put(entrada.getKey(), porcentaje);
        }
        return porcentajes;
    }

    // ----- Implementación de Subject -----

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
